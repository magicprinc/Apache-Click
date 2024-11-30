package net.sf.clickclick.reload;

import org.apache.click.service.ConfigService;
import org.apache.click.service.XmlConfigService;
import org.apache.click.util.ClickUtils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * ReloadClassFilter allows changes to class and resource bundles to be picked up
 * without restarting the web application.
 * <p/>
 * <b>Please note:</b> this filter is only applied to the following modes:
 * <ul>
 * <li>Development</li>
 * <li>Debug</li>
 * <li>Trace</li>
 * </ul>
 *
 * This feature is made possible by replacing the context class loader
 * with an instance of {@link ReloadClassLoader} for each incoming request.
 * <p/>
 * <h3>Configuration</h3>
 * By default ReloadClassLoader only reloads classes inside packages specified
 * by the Page packages in click.xml.
 * <p/>
 * You can include extra packages by specifying a comma separated list of
 * packages and classes to be loaded at initialization time.
 * <p/>
 * By specifying the initialization parameter 'includes', you can provide a list
 * of packages and classes that will be added to the ReloadClassLoader.
 * <p/>
 * You can also specify packages and classes to be excluded using the
 * initialization parameter 'excludes'.
 * <p/>
 * <b>Please note</b> that excludes will override includes, so if you both
 * exclude and include the class com.mycorp.page.MyPage, it will be excluded.
 * <p/>
 * Here is an example web.xml showing how to configure the filter:
 * <pre class="prettyprint">
 * &lt;?xml version="1.0" encoding="UTF-8"?&gt;
 * &lt;web-app version="2.4" xmlns="http://java.sun.com/xml/ns/j2ee" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://java.sun.com/xml/ns/j2ee http://java.sun.com/xml/ns/j2ee/web-app_2_4.xsd"&gt;
 * &lt;!--
 * ReloadClassFilter depends on ClickClickConfigService instead of the default
 * XmlConfigService. The reason for this is that XmlConfigService cache Page
 * classes even in development mode while ClickClickConfigService only caches
 * in production modes. By not caching the classes allows ReloadClassFilter
 * to reload them.
 * --&gt;
 *     &lt;context-param&gt;
 *         &lt;param-name&gt;config-service-class&lt;/param-name&gt;
 *         &lt;param-value&gt;org.apache.click.service.ClickClickConfigService&lt;/param-value&gt;
 *     &lt;/context-param&gt;
 *
 * &lt;!--
 * Setup the reload class filter. This filter will only reload classes
 * in development modes.
 * --&gt;
 *   &lt;filter&gt;
 *       &lt;filter-name&gt;reload-filter&lt;/filter-name&gt;
 *       &lt;filter-class&gt;net.sf.clickclick.reload.ReloadClassFilter&lt;/filter-class&gt;
 *       &lt;init-param&gt;
 *           &lt;param-name&gt;
 *               includes
 *            &lt;/param-name&gt;
 *           &lt;param-value&gt;
 *               com.mycorp.page, com.mycorp.controls.MyForm
 *           &lt;/param-value&gt;
 *       &lt;/init-param&gt;
 *       &lt;init-param&gt;
 *           &lt;param-name&gt;
 *               excludes
 *           &lt;/param-name&gt;
 *           &lt;param-value&gt;
 *               com.mycorp.page.account, com.mycorp.page.MyStatefulPage
 *           &lt;/param-value&gt;
 *       &lt;/init-param&gt;
 *     &lt;/filter&gt;
 *
 *     &lt;filter-mapping&gt;
 *       &lt;filter-name&gt;reload-filter&lt;/filter-name&gt;
 *       &lt;servlet-name&gt;click-servlet&lt;/servlet-name&gt;
 *     &lt;/filter-mapping&gt;
 *
 *     ....
 *
 * &lt;/web-app&gt;
 * </pre>
 *
 * The snippet above will setup the filter to reload classes containing the
 * package 'com.mycorp.page'. The filter will also reload the class
 * 'com.mycorp.controls.MyForm'.
 * <p/>
 * The filter will <b>not</b> reload class contained in the package
 * 'com.mycorp.page.account'. The class 'com.mycorp.page.MyStatefulPage' will
 * also excluded from reloading.
 *
 * <h3>Integration</h3>
 * Certain servlet containers have the ability to track changes to classes
 * and jars, and reload the entire web application when changes occur. You
 * should probably disable this feature in your container if you want automatic
 * class reloading to work. Otherwise instead of only reloading the changed
 * class, the servlet container will restart the entire application.
 * <p/>
 * For Tomcat you can disable this feature by adding the attribute
 * <tt>reloadable="false"</tt> to your context.xml file for example:
 * <pre class="prettyprint">
 * &lt;Context path="/click-test" reloadable="false" antiJARLocking="true"/&gt;
 * </pre>
 * IDE's such as Netbeans will also reload the entire web application when you
 * click the "Run" button. So instead of hitting "Run" just refresh the browser
 * to reload classes.
 *
 * <h3>Caveats</h3>
 * TODO
 */
public class ReloadClassFilter implements Filter {

    // -------------------------------------------------------- Constants

    private static final String INCLUDES = "includes";

    private static final String EXCLUDES = "excludes";

    private static final String CLASSPATH = "classpath";

    // -------------------------------------------------------- Variables

    /** The application configuration service. */
    protected XmlConfigService clickClickConfigService;

    private ClassLoader reloadClassLoader = null;

    private URL[] classpath = null;

    private final List<String> includeList = new ArrayList();

    private List excludeList = new ArrayList();

    private List initialClasspath = new ArrayList();

    /** The filter has been configured flag. */
    private boolean configured = false;

    /**
     * The filter configuration object we are associated with.  If this value
     * is null, this filter instance is not currently configured.
     */
    protected FilterConfig filterConfig = null;

    // --------------------------------------------------------- Public Methods

    public void doFilter(ServletRequest request, ServletResponse response,
        FilterChain chain) throws IOException, ServletException {
        if (!configured) {
            loadConfiguration();
        }

        if (clickClickConfigService.isProductionMode() ||
            clickClickConfigService.isProfileMode()) {

            // In production modes skip processing
            chain.doFilter(request, response);
        } else {
            // In developments modes use custom request handler
            handleRequest(request, response, chain);
        }
    }

    /**
     * Take this filter out of service.
     *
     * @see Filter#destroy()
     */
    public void destroy() {
        this.filterConfig = null;
    }

    /**
     * Initialize the filter.
     *
     * @see Filter#init(FilterConfig)
     *
     * @param filterConfig The filter configuration object
     */
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;

        // Extract specified includedList
        String includes = filterConfig.getInitParameter(INCLUDES);
        if (includes != null) {
            StringTokenizer tokens = new StringTokenizer(includes, ", \n\t");
            while (tokens.hasMoreTokens()) {
                String token = tokens.nextToken();
                includeList.add(token);
            }
        }

        // Extract specified excludes
        String excludes = filterConfig.getInitParameter(EXCLUDES);
        if (excludes != null) {
            StringTokenizer tokens = new StringTokenizer(excludes, ", \n\t");
            while (tokens.hasMoreTokens()) {
                String token = tokens.nextToken();
                excludeList.add(token);
            }
        }

        // Extract specified classpaths
        String classpathParams = filterConfig.getInitParameter(CLASSPATH);
        if (classpathParams != null) {
            StringTokenizer tokens = new StringTokenizer(classpathParams, ", \n\t");
            while (tokens.hasMoreTokens()) {
                String token = tokens.nextToken();
                initialClasspath.add(token);
            }
        }
    }

    /**
     * Set filter configuration. This function is equivalent to init and is
     * required by Weblogic 6.1.
     *
     * @param filterConfig the filter configuration object
     */
    public void setFilterConfig(FilterConfig filterConfig) {
        init(filterConfig);
    }

    /**
     * Return filter config. This is required by Weblogic 6.1
     *
     * @return the filter configuration
     */
    public FilterConfig getFilterConfig() {
        return filterConfig;
    }

    // -------------------------------------------------------- Protected Methods

    /**
     * Return the application configuration service.
     *
     * @return the application configuration service
     */
    protected XmlConfigService getConfigService() {
        return clickClickConfigService;
    }

    /**
     * Load the filters configuration and set the configured flat to true.
     */
    protected void loadConfiguration() {
        ServletContext servletContext = getFilterConfig().getServletContext();
        ConfigService configService = ClickUtils.getConfigService(servletContext);
        if (!(configService instanceof XmlConfigService x)){
            throw new IllegalStateException(
                "ReloadClassFilter can only be used " +
                "in conjunction with ClickClickConfigService. Please see " +
                "ReloadClassFilter JavaDoc on how to setup the ClickClickConfigService.");
        }
        clickClickConfigService = x;

        // Add default package to the package list
        includeList.addAll(clickClickConfigService.getPagePackages());
        configured = true;

        String message = "ReloadClassFilter initialized with: includes="
            + includeList + " and excludes=" + excludeList;

        getConfigService().getLogService().info(message);
    }

    /**
     * Handles the request in development modes.
     * <p/>
     * This method uses the ReloadClassLoader as returned by
     * {@link #createReloadClassLoader()}.
     *
     * @param request
     * @param response
     * @param chain
     */
    protected void handleRequest(ServletRequest request, ServletResponse response,
        FilterChain chain) throws IOException, ServletException {

        // TODO should createReloadClassLoader be synchronized
        //synchronized (lock) {
        //    if(reloadClassLoader == null) {
        reloadClassLoader = createReloadClassLoader();
        //     }
        //}

        // Grab hold of the current context class loader
        ClassLoader orig = Thread.currentThread().getContextClassLoader();
        try {
            // Set the new context class loader
            Thread.currentThread().setContextClassLoader(reloadClassLoader);
            chain.doFilter(request, response);
        /*} catch (Throwable t) {
            while (t instanceof ServletException) {
                t = ((ServletException) t).getRootCause();
            }
            clickClickConfigService.getLogService().error(
                "Could not handle request", t);
            throw t;*/
        } finally {
            // Restore the context class loader
            Thread.currentThread().setContextClassLoader(orig);
        }
    }

    /**
     * Create and return a new ReloadClassLoader instance.
     *
     * @return a newly instantiated ReloadClassLoader
     */
    protected ReloadClassLoader createReloadClassLoader() {
        ClassLoader parent = Thread.currentThread().getContextClassLoader();
        classpath = getClasspath();
        ReloadClassLoader loader = new ReloadClassLoader(classpath, parent, clickClickConfigService);

        // Add includes to class loader
        for (var it = includeList.iterator(); it.hasNext();) {
            String include = (String) it.next();
            loader.addInclude(include);
        }

        // Add excludes to class loader
        for (var it = excludeList.iterator(); it.hasNext();) {
            String exclude = (String) it.next();
            loader.addExclude(exclude);
        }
        return loader;
    }

    /**
     * Add the path to the specified classpath entries.
     *
     * @param path the path to add to the classpath
     * @param classpath the Set of classpath entries
     */
    protected void addToClasspath(String path, Set classpath) {
        try {
            File f = new File(path);
            if (f.exists()) {
                classpath.add(f.getCanonicalFile().toURL());
            } else if (path.endsWith(".jar")) {
                // Check for jar under the WEB-INF/lib dir
                if (!path.startsWith("/")) {
                    path = "/" + path;
                }
                URL url = filterConfig.getServletContext().getResource("/WEB-INF/lib" +
                    path);
                if (url != null) {
                    classpath.add(url);
                }
            }
        } catch (Exception ex) {
        }
    }

    // -------------------------------------------------------- Private Methods

    private URL[] getClasspath() {
        Set classpathSet = new LinkedHashSet();
        for (Iterator it = initialClasspath.iterator(); it.hasNext();) {
            String path = (String) it.next();
            addToClasspath(path, classpathSet);
        }
        classpathSet.addAll(extractUrlList(Thread.currentThread().
            getContextClassLoader()));
        return (URL[]) classpathSet.toArray(new URL[]{null});
    }

    private List extractUrlList(ClassLoader cl) {
        List urlList = new ArrayList();
        try {
            Enumeration en = cl.getResources("");
            while (en.hasMoreElements()) {
                Object url = en.nextElement();
                urlList.add(url);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return urlList;
    }
}