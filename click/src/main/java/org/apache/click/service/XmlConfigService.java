package org.apache.click.service;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Splitter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.click.Context;
import org.apache.click.Page;
import org.apache.click.util.Bindable;
import org.apache.click.util.ClickUtils;
import org.apache.click.util.Format;
import org.apache.click.util.HtmlStringBuffer;
import org.apache.click.util.MessagesMap;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Element;

import javax.annotation.Nullable;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.apache.click.util.ClickUtils.trim;

/**
 * Provides a Click XML configuration service class.
 * <p/>
 * This class reads Click configuration information from a file named
 * <tt>click.xml</tt>. The service will first lookup the <tt>click.xml</tt>
 * under the applications <tt>WEB-INF</tt> directory, and if not found
 * attempt to load the configuration file from the classpath root.
 * <p/>
 * Configuring Click through the <tt>click.xml</tt> file is the most common
 * technique.
 * <p/>
 * However, you can instruct Click to use a different service implementation.
 * Please see {@link ConfigService} for more details.
 */
@SuppressWarnings("SameParameterValue")
@Slf4j
public class XmlConfigService implements ConfigService {
  /** The click deployment directory path: &nbsp; "/click". */
  static final String CLICK_PATH = "/click";

  /** The default common page headers. */
  static final Map<String, Object> DEFAULT_HEADERS;

  private static final Object PAGE_LOAD_LOCK = new Object();

  static {// Initialize the default headers.
    DEFAULT_HEADERS = new HashMap<>();
    DEFAULT_HEADERS.put("Pragma", "no-cache");
    DEFAULT_HEADERS.put("Cache-Control", "no-store, no-cache, must-revalidate, post-check=0, pre-check=0");
    DEFAULT_HEADERS.put("Expires", new Date(1L));
  }

  /** The Map of global page headers. */
  Map<String,Object> commonHeaders;

  /** The page automapping override page class for path list. */
  final List<ExcludesElm> excludesList = new ArrayList<>();

  /** The map of ClickApp.PageElm keyed on path. */
  final Map<String,PageElm> pageByPathMap = new HashMap<>();

  /** The map of ClickApp.PageElm keyed on class. */
  final Map<Class<? extends Page>,Object> pageByClassMap = new HashMap<>();

  /** The list of page packages. */
  @Getter final List<String> pagePackages = new ArrayList<>();

  /**
	 The automatically bind controls, request parameters and models flag.
	 @see ConfigService#getAutoBindingMode()
	 */
	@Getter(onMethod_=@Override) private AutoBinding autoBindingMode;

  /**
	 The Commons FileUpload service class.
	 @see ConfigService#getFileUploadService()
	 */
	@Getter(onMethod_=@Override) private FileUploadService fileUploadService;

  /** The format class. */
	@Getter private Class<? extends Format> formatClass;

  /**
	 The character encoding of this application.
	 @see org.apache.click.service.ConfigService#getCharset
	 */
	@Getter(onMethod_=@Override) @Setter private String charset = "UTF-8";

  /**
	 The default application locale.
	 @see org.apache.click.service.ConfigService#getLocale
	 @see org.springframework.context.i18n.LocaleContextHolder#getLocale()
	 */
  @Getter(onMethod_=@Override) private Locale locale;

  /** The application log service.
   (Set default logService early to log errors when services fail)

   @see ConfigService#getLogService
   @see Slf4jLogService
   */
  @Getter(onMethod_={@Override}) private final LogService logService = new Slf4jLogService();

  /**
   The application mode:
   [ PRODUCTION | PROFILE | DEVELOPMENT | DEBUG | TRACE ].
	 @see ConfigService#getApplicationMode()
	 @see ConfigService#isProductionMode()
	 @see ConfigService#isProfileMode()
   */
	@Getter(onMethod_=@Override) private Mode applicationMode = Mode.PRODUCTION;

  /**
	 The ServletContext instance.
	 @see javax.servlet.ServletContext
	 @see javax.servlet.ServletConfig
	 */
	@Getter(onMethod_=@Override) private ServletContext servletContext;
	@Getter private ServletConfig servletConfig;

  /**
	 The application PropertyService. Default {@link MVELPropertyService}
	 @see ConfigService#getPropertyService()
	 */
	@Getter(onMethod_=@Override) private PropertyService propertyService = new MVELPropertyService();

  /**
	 The application ResourceService.
	 @see ConfigService#getResourceService()
	 */
	@Getter(onMethod_=@Override) private ResourceService resourceService;

  /**
	 The application TemplateService.
	 @see ConfigService#getTemplateService()
	 */
	@Getter(onMethod_=@Override) private TemplateService templateService;

  /** Flag indicating whether Click is running on Google App Engine. */
	@Getter private boolean onGoogleAppEngine = false;

  /**
   * @see ConfigService#onInit(ServletContext)
   *
   * @param servletContext the application servlet context
   * @throws Exception if an error occurs initializing the application
   */
  @Override
	public void onInit (@NonNull ServletContext servletContext, @NonNull ServletConfig servletConfig) throws Exception {
    this.servletContext = servletContext;
		this.servletConfig = servletConfig;
    onGoogleAppEngine = servletContext.getServerInfo().startsWith("Google App Engine");

		// InputStream inputStream = ClickUtils.getClickConfig(servletContext); try
		logService.onInit(getServletContext());// Load the log service

		// Load the application mode and set the logger levels
		loadMode();

		logService.info("***  Initializing Click " + ClickUtils.getClickVersion()+ " in " + getApplicationMode() + " mode  *** LogService: "+logService.getClass().getName());

		// Deploy click resources
		deployFiles();

		// Load the format class
		loadFormatClass();

		// Load the common headers
		loadHeaders();

		// Load the pages
		loadPages();

		// Load the error and not-found pages
		loadDefaultPages();

		// Load the locale
		loadLocale();

		// Load the Property service
		loadPropertyService();

		// Load the File Upload service
		loadFileUploadService();

		// Load the Templating service
		loadTemplateService();

		// Load the Resource service
		loadResourceService();
  }

  /** @see ConfigService#onDestroy() */
  @Override
	public void onDestroy() {
    if (getFileUploadService() != null){
      getFileUploadService().onDestroy();
    }
    if (getPropertyService() != null){
      getPropertyService().onDestroy();
    }
    if (getTemplateService() != null){
      getTemplateService().onDestroy();
    }
    if (getResourceService() != null){
      getResourceService().onDestroy();
    }
  }

  /**
   * @see ConfigService#createFormat()
   *
   * @return a new format object
   */
  @Override @SneakyThrows
	public Format createFormat () {
		return formatClass.newInstance();
  }

  @Override
	public boolean isProductionMode() {
    return applicationMode == Mode.PRODUCTION || applicationMode == null;
  }

  @Override
	public boolean isProfileMode() {
    return applicationMode == Mode.PROFILE;
  }

  /**
   * @see ConfigService#isJspPage(String)
   *
   * @param path the Page ".htm" path
   * @return true if JSP exists for the given ".htm" path
   */
  @Override
	public boolean isJspPage(String path) {
    val buffer = new HtmlStringBuffer();
    int index = StringUtils.lastIndexOf(path, ".");
    if (index > 0) {
      buffer.append(path.substring(0, index));
    } else {
      buffer.append(path);
    }
    buffer.append(".jsp");
    return pageByPathMap.containsKey(buffer.toString());
  }

  /**
   * Return true if the given path is a Page class template, false
   * otherwise. By default this method returns true if the path has a
   * <tt>.htm</tt> or <tt>.jsp</tt> extension.
   * <p/>
   * If you want to map alternative templates besides <tt>.htm</tt> and
   * <tt>.jsp</tt> files you can override this method and provide extra
   * checks against the given path whether it should be added as a
   * template or not.
   * <p/>
   * Below is an example showing how to allow <tt>.xml</tt> paths to
   * be recognized as Page class templates.
   *
   * <pre class="prettyprint">
   * public class MyConfigService extends XmlConfigService {
   *
   *     protected boolean isTemplate(String path) {
   *         // invoke default implementation
   *         boolean isTemplate = super.isTemplate(path);
   *
   *         if (!isTemplate) {
   *             // If path has an .xml extension, mark it as a template
   *             isTemplate = path.endsWith(".xml");
   *         }
   *         return isTemplate;
   *     }
   * } </pre>
   *
   * Here is an example <tt>web.xml</tt> showing how to configure a custom
   * ConfigService through the context parameter <tt>config-service-class</tt>.
   * We also map <tt>*.xml</tt> requests to be routed through ClickServlet:
   *
   * <pre class="prettyprint">
   * &lt;web-app xmlns="http://java.sun.com/xml/ns/j2ee"
   *   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
   *   xsi:schemaLocation="http://java.sun.com/xml/ns/j2ee http://java.sun.com/xml/ns/j2ee/web-app_2_4.xsd"
   *   version="2.4"&gt;
   *
   *   &lt;!-- Specify a custom ConfigService through the context param 'config-service-class' --&gt;
   *   &lt;context-param&gt;
   *     &lt;param-name&gt;config-service-class&lt;/param-name&gt;
   *     &lt;param-value&gt;com.mycorp.service.MyConfigSerivce&lt;/param-value&gt;
   *   &lt;/context-param&gt;
   *
   *   &lt;servlet&gt;
   *     &lt;servlet-name&gt;ClickServlet&lt;/servlet-name&gt;
   *     &lt;servlet-class&gt;org.apache.click.ClickServlet&lt;/servlet-class&gt;
   *     &lt;load-on-startup&gt;0&lt;/load-on-startup&gt;
   *   &lt;/servlet&gt;
   *
   *   &lt;!-- NOTE: we still map the .htm extension --&gt;
   *   &lt;servlet-mapping&gt;
   *     &lt;servlet-name&gt;ClickServlet&lt;/servlet-name&gt;
   *     &lt;url-pattern&gt;*.htm&lt;/url-pattern&gt;
   *   &lt;/servlet-mapping&gt;
   *
   *   &lt;!-- NOTE: we also map .xml extension in order to route xml requests to the ClickServlet --&gt;
   *   &lt;servlet-mapping&gt;
   *     &lt;servlet-name&gt;ClickServlet&lt;/servlet-name&gt;
   *     &lt;url-pattern&gt;*.xml&lt;/url-pattern&gt;
   *   &lt;/servlet-mapping&gt;
   *
   *   ...
   *
   * &lt;/web-app&gt; </pre>
   *
   * <b>Please note</b>: even though you can add extra template mappings by
   * overriding this method, it is still recommended to keep the default
   * <tt>.htm</tt> mapping by invoking <tt>super.isTemplate(String)</tt>.
   * The reason being that Click ships with some default templates such as
   * {@link ConfigService#ERROR_PATH} and {@link ConfigService#NOT_FOUND_PATH}
   * that must be mapped as <tt>.htm</tt>.
   * <p/>
   * Please see the ConfigService <a href="#config">javadoc</a> for details
   * on how to configure a custom ConfigService implementation.
   *
   * @see ConfigService#isTemplate(String)
   *
   * @param path the path to check if it is a Page class template or not
   * @return true if the path is a Page class template, false otherwise
   */
  @Override
	public boolean isTemplate (String path) {
    return path.endsWith(".htm") || path.endsWith(".jsp");
  }

  /**
   * @see ConfigService#getPageClass(String)
   *
   * @param pagePath the page path
   * @return the page class for the given path or null if no class is found
   */
  @Override
	public Class<? extends Page> getPageClass (String pagePath) {
    // If in production or profile mode.
    if (applicationMode == Mode.PROFILE || applicationMode == Mode.PRODUCTION || applicationMode == null){
      PageElm page = pageByPathMap.get(pagePath);
      if (page == null){
        String jspPath = StringUtils.replace(pagePath, ".htm", ".jsp");
        page = pageByPathMap.get(jspPath);
      }

      if (page != null){
        return page.getPageClass();
      } else {
        return null;
      }

    // Else in development, debug or trace mode
    } else {
      synchronized (PAGE_LOAD_LOCK){
        PageElm page = pageByPathMap.get(pagePath);
        if (page == null){
          String jspPath = StringUtils.replace(pagePath, ".htm", ".jsp");
          page = pageByPathMap.get(jspPath);
        }

        if (page != null){
          return page.getPageClass();
        }

        Class<? extends Page> pageClass = null;
        try {
          URL resource = servletContext.getResource(pagePath);
          if (resource != null){
            for (String pagePackage : pagePackages){
              pageClass = getPageClass(pagePath, pagePackage);
              if (pageClass != null){
                page = new PageElm(pagePath, pageClass, commonHeaders, autoBindingMode);
                pageByPathMap.put(page.getPath(), page);
                addToClassMap(page);
                logService.debug("getPageClass: {} → {}", pagePath, pageClass.getName());
                break;
              }
            }
          }
        } catch (MalformedURLException ignore){
        }
        return pageClass;
      }
    }
  }

  /**
   * @see ConfigService#getPagePath(Class)
   *
   * @param pageClass the page class
   * @return path the page path or null if no path is found
   * @throws IllegalArgumentException if the Page Class is not configured
   * with a unique path
   */
  @Override
	public String getPagePath (Class<? extends Page> pageClass) {
    Object object = pageByClassMap.get(pageClass);

    if (object instanceof PageElm page){
      return page.getPath();

    } else if (object instanceof List list){
      val buffer = new HtmlStringBuffer();
      buffer.append("Page class resolves to multiple paths: ");
      buffer.append(pageClass.getName());
      buffer.append(" -> [");
      for (var it = list.iterator(); it.hasNext();) {
        PageElm pageElm = (PageElm) it.next();
        buffer.append(pageElm.getPath());
        if (it.hasNext()) {
          buffer.append(", ");
        }
      }
      buffer.append("]\nUse Context.createPage(String), or Context.getPageClass(String) instead.");
      throw new IllegalArgumentException(buffer.toString());

    } else {
      return null;
    }
  }

  /**
   * @see ConfigService#getPageClassList()
   *
   * @return the list of configured page classes
   */
  @Override public List<Class<? extends Page>> getPageClassList() {
    List<Class<? extends Page>> classList = new ArrayList<>(pageByClassMap.size());

    classList.addAll(ClickUtils.castUnsafe(pageByClassMap.keySet()));

    return classList;
  }

  /**
   * @see ConfigService#getPageHeaders(String)
   *
   * @param path the path of the page
   * @return a Map of headers for the given page path
   */
  @Override
	public Map<String, Object> getPageHeaders (String path) {
    PageElm page = pageByPathMap.get(path);
    if (page == null){
      String jspPath = StringUtils.replace(path, ".htm", ".jsp");
      page = pageByPathMap.get(jspPath);
    }

    if (page != null) {
      return page.getHeaders();
    } else {
      return null;
    }
  }

  /**
   * @see ConfigService#getNotFoundPageClass()
   *
   * @return the page not found <tt>Page</tt> <tt>Class</tt>
   */
  @Override
	public Class<? extends Page> getNotFoundPageClass () {
    PageElm page = pageByPathMap.get(NOT_FOUND_PATH);

    if (page != null) {
      return page.getPageClass();

    } else {
      return org.apache.click.Page.class;
    }
  }

  /**
   * @see ConfigService#getErrorPageClass()
   *
   * @return the error handling page <tt>Page</tt> <tt>Class</tt>
   */
  @Override
	public Class<? extends Page> getErrorPageClass() {
    PageElm page = pageByPathMap.get(ERROR_PATH);

    if (page != null) {
      return page.getPageClass();

    } else {
      return org.apache.click.util.ErrorPage.class;
    }
  }

  /**
   * @see ConfigService#getPageField(Class, String)
   *
   * @param pageClass the page class
   * @param fieldName the name of the field
   * @return the public field of the pageClass with the given name or null
   */
  @Override
	public Field getPageField (Class<? extends Page> pageClass, String fieldName) {
    return getPageFields(pageClass).get(fieldName);
  }

  /**
   * @see ConfigService#getPageFieldArray(Class)
   *
   * @param pageClass the page class
   * @return an array public fields for the given page class
   */
  @Override public Field[] getPageFieldArray (Class<? extends Page> pageClass) {
    Object object = pageByClassMap.get(pageClass);

    if (object instanceof PageElm page) {
      return page.getFieldArray();

    } else if (object instanceof List list) {
      PageElm page = (PageElm) list.get(0);
      return page.getFieldArray();

    } else {
      return null;
    }
  }

  /**
   * @see ConfigService#getPageFields(Class)
   *
   * @param pageClass the page class
   * @return a Map of public fields for the given page class
   */
  @Override public Map<String, Field> getPageFields (Class<? extends Page> pageClass) {
    Object object = pageByClassMap.get(pageClass);

    if (object instanceof PageElm page) {
      return page.getFields();

    } else if (object instanceof List list) {
      PageElm page = (PageElm) list.get(0);
      return page.getFields();

    } else {
      return Collections.emptyMap();
    }
  }

  /**
   * Find and return the page class for the specified pagePath and
   * pagesPackage.
   * <p/>
   * For example if the pagePath is <tt>'/edit-customer.htm'</tt> and
   * package is <tt>'com.mycorp'</tt>, the matching page class will be:
   * <tt>com.mycorp.EditCustomer</tt> or <tt>com.mycorp.EditCustomerPage</tt>.
   * <p/>
   * If the page path is <tt>'/admin/add-customer.htm'</tt> and package is
   * <tt>'com.mycorp'</tt>, the matching page class will be:
   * <tt>com.mycorp.admin.AddCustomer</tt> or
   * <tt>com.mycorp.admin.AddCustomerPage</tt>.
   *
   * @param pagePath the path used for matching against a page class name
   * @param pagesPackage the package of the page class
   * @return the page class for the specified pagePath and pagesPackage
   */
  protected Class<? extends  Page> getPageClass (String pagePath, String pagesPackage) {
    // To understand this method lets walk through an example as the code plays out.
    // Imagine this method is called with the arguments:
    // pagePath     = '/pages/edit-customer.htm'
    // pagesPackage = 'org.apache.click'

    String packageName = "";
    if (StringUtils.isNotBlank(pagesPackage)) {
      // Append period (.) after package:  packageName = 'org.apache.click.'
      packageName = pagesPackage + ".";
    }
    String className = "";

    // Strip off extension:  path = '/pages/edit-customer'
		int dotIndex = pagePath.lastIndexOf('.');
		String path = dotIndex >= 0
				? pagePath.substring(0, dotIndex)// todo improve getExtension
		    : pagePath;
    // If page is excluded return the excluded class
    Class<? extends  Page> excludePageClass = getExcludesPageClass(path);
    if (excludePageClass != null){
      return excludePageClass;
    }
    // Build complete packageName:
    // packageName = 'org.apache.click.pages.'
    // className   = 'edit-customer'
    if (path.contains("/")) {
      val tokenizer = new StringTokenizer(path, "/");
      while (tokenizer.hasMoreTokens()) {
        String token = tokenizer.nextToken();
        if (tokenizer.hasMoreTokens()) {
          packageName = packageName + token + ".";
        } else {
          className = token;
        }
      }
    } else {
      className = path;
    }

    // CamelCase className.
    // className = 'EditCustomer'
    StringTokenizer tokenizer = new StringTokenizer(className, "_-");
    className = "";
    while (tokenizer.hasMoreTokens()) {
      String token = ClickUtils.toPropertyName("", tokenizer.nextToken());// fooBar → FooBar
      className += token;
    }

    // className = 'org.apache.click.pages.EditCustomer'
    className = packageName + className;

    Class<? extends Page> pageClass = null;
    try {
      // Attempt to load class.
      pageClass = ClickUtils.classForName(className);

      if (!Page.class.isAssignableFrom(pageClass)) {
        throw new RuntimeException("Automapped page class " + className
            + " is not a subclass of org.apache.click.Page");
      }
    } catch (ClassNotFoundException cnfe) {
      boolean classFound = false;

      // Append "Page" to className and attempt to load class again.
      // className = 'org.apache.click.pages.EditCustomerPage'
      if (!className.endsWith("Page")) {
        String classNameWithPage = className + "Page";
        try {
          // Attempt to load class.
          pageClass = ClickUtils.classForName(classNameWithPage);

          if (!Page.class.isAssignableFrom(pageClass)) {
            throw new RuntimeException("Automapped page class " + classNameWithPage
                + " is not a subclass of org.apache.click.Page");
          }
          classFound = true;

        } catch (ClassNotFoundException ignore) {// cnfe2
        }
      }
      if (classFound) {
        logService.trace("getPageClass: {} -> found {}", pagePath, className);
      } else {
        logService.debug("getPageClass: {} → CLASS NOT FOUND {}", pagePath, className);
      }
    }
    return pageClass;
  }

  /**
   * Returns true if Click resources (JavaScript, CSS, images etc) packaged
   * in jars can be deployed to the root directory of the webapp, false
   * otherwise.
   * <p/>
   * By default this method will return false in restricted environments where
   * write access to the underlying file system is disallowed. Example
   * environments where write access is not allowed include the WebLogic JEE
   * server and Google App Engine. (Note: WebLogic provides the property
   * <tt>"Archived Real Path Enabled"</tt> that controls whether web
   * applications can access the file system or not. See the Click user manual
   * for details).
   *
   * @return true if resources can be deployed, false otherwise
   */
  protected boolean isResourcesDeployable() {
    // Only deploy if writes are allowed
    if (onGoogleAppEngine) {
      return false;// Google doesn't allow writes
    }
    return ClickUtils.isResourcesDeployable(servletContext);
  }

  /**
   * Loads all Click Pages defined in the <tt>click.xml</tt> file, including
   * manually defined Pages, auto mapped Pages and excluded Pages.
   *
   * @param rootElm the root xml element containing the configuration
   * @throws java.lang.ClassNotFoundException if the specified Page class can
   * not be found on the classpath
   */
  void loadPages () throws ClassNotFoundException {
    List<String> pagesList = Splitter.on(',').omitEmptyStrings().trimResults().splitToList(opt("pages"));

    if (pagesList.isEmpty()){
      throw new RuntimeException("required configuration 'pages' element missing.");
    }
    List<String> templates = getTemplateFiles();

    for (String page : pagesList) {
      // Determine whether to use automapping
      boolean automap = true;// pagesElm.getAttribute("automapping") "true"

      // Determine whether to use autobinding.
      // String autobindingStr = pagesElm.getAttribute("autobinding"); "annotation" "public"
      autoBindingMode = AutoBinding.DEFAULT;

      // TODO: if autobinding is set to false an there are multiple pages how should this be handled
      // Perhaps autobinding should be moved to <click-app> and be a application wide setting?
      // However the way its implemented above is probably fine for backward compatibility
      // purposes, meaning the last defined autobinding wins
      // String pagesPackage = pagesElm.getAttribute("package"); if (StringUtils.isBlank(pagesPackage)){ pagesPackage = ""; }

      String pagesPackage = trim(page);
      if (pagesPackage.endsWith(".") && pagesPackage.length() > 1){
        pagesPackage = pagesPackage.substring(0, pagesPackage.length() - 2);
      }

      // Add the pages package to the list of page packages
      pagePackages.add(pagesPackage);

      if (automap) {
        buildAutoPageMapping(pagesPackage, templates);
      }
    }//f
		buildManualPageMapping();

		buildClassMap();
  }

  /**
   * Add manually defined Pages to the {@link #pageByPathMap}.
   *
   * @param pagesElm the xml element containing manually defined Pages
   * @param pagesPackage the pages package prefix
	 */
	@SneakyThrows
  void buildManualPageMapping () {
		var pages = Splitter.on(';').trimResults().omitEmptyStrings().withKeyValueSeparator('=').split(opt("page"));

		if (!pages.isEmpty()){
      logService.debug("Individual pages:");
    }

    for (var o : pages.entrySet()){
			val page = new PageElm(o.getKey(), o.getValue(), commonHeaders);
			pageByPathMap.put(page.getPath(), page);
			logService.debug("buildManualPageMapping: {} → {}", page.getPath(), page.getPageClass().getName());
		//} catch (ClassNotFoundException e){	log.warn("buildManualPageMapping: failed {} → {}", o.getKey(), o.getValue(), e);
		}
  }

  /**
   * Build the {@link #pageByPathMap} by associating template files with
   * matching Java classes found on the classpath.
   * <p/>
   * This method also rebuilds the {@link #excludesList}. This list contains
   * URL paths that should not be auto-mapped.
   *
   * @param pagesElm the xml element containing the excluded URL paths
   * @param pagesPackage the pages package prefix
   * @param templates the list of templates to map to Page classes
   */
  void buildAutoPageMapping (String pagesPackage, List<String> templates) throws ClassNotFoundException {
    excludesList.clear();// Build list of automap path page class overrides
		List<String> excludesStr = Splitter.on(';').omitEmptyStrings().trimResults().splitToList(opt("excludes"));
		for (String pattern : excludesStr){
      excludesList.add(new ExcludesElm(pattern));
    }
    logService.debug("buildAutoPageMapping: automapped pages:");

    for (String pagePath : templates){
      if (!pageByPathMap.containsKey(pagePath)){

        Class<? extends Page> pageClass = getPageClass(pagePath, pagesPackage);
        if (pageClass != null){
          PageElm page = new PageElm(pagePath, pageClass, commonHeaders, autoBindingMode);

          pageByPathMap.put(page.getPath(), page);
          logService.debug("buildAutoPageMapping: {} → {}", pagePath, pageClass.getName());
        }
      }
    }//f
  }

  /**
   * Build the {@link #pageByClassMap} from the {@link #pageByPathMap} and
   * delegate to {@link #addToClassMap(PageElm)}.
   */
  void buildClassMap () {
    // Build pages by class map
    for (PageElm page : pageByPathMap.values()){
      addToClassMap(page);
    }
  }

  /**
   * Add the specified page to the {@link #pageByClassMap} where the Map's key
   * holds the Page class and value holds the {@link PageElm}.
   *
   * @param page the PageElm containing metadata about a specific page
   */
  void addToClassMap (PageElm page) {
    Object value = pageByClassMap.get(page.pageClass);
    if (value == null){
      pageByClassMap.put(page.pageClass, page);

    } else if (value instanceof List list){
      list.add(page);

    } else if (value instanceof PageElm p){
      val list = new ArrayList<PageElm>();
      list.add(p);
      list.add(page);
      pageByClassMap.put(page.pageClass, list);

    } else {// should never occur
      throw new IllegalStateException("addToClassMap: "+page);
    }
  }

  /**
   * Load the Page headers from the specified xml element.
   *
   * @param parentElm the element to load the headers from
   * @return the map of Page headers
   */
  Map<String,Object> loadHeadersMap (String headers) {
		val tmp = Splitter.on(';').trimResults().omitEmptyStrings().withKeyValueSeparator('=').split(headers);
		val headersMap = new HashMap<String,Object>();
    for (var header : tmp.entrySet()){
      String name = trim(header.getKey());
      String type;
      String propertyValue = trim(header.getValue());
			int typeIdx = propertyValue.indexOf(':');
			if (typeIdx > 0){
				type = trim(propertyValue.substring(typeIdx+1));// cut after :
				propertyValue = trim(propertyValue.substring(0, typeIdx));// cut left till :
			} else {
				type = "";
			}
      Object value;

      if ("".equals(type) || "String".equalsIgnoreCase(type)){
        value = propertyValue;
      } else if ("Integer".equalsIgnoreCase(type) || "int".equalsIgnoreCase(type)){
        value = Integer.valueOf(propertyValue);
      } else if ("Date".equalsIgnoreCase(type)){
        value = new Date(Long.parseLong(propertyValue));
      } else {
        throw new IllegalArgumentException("Invalid property type [String|Integer|Date]: "+ type);
      }
      headersMap.put(name, value);
    }
    return headersMap;
  }

  /**
   * Deploy files from jars and Controls.
   *
   * @param rootElm the click.xml configuration DOM element
   * @throws java.lang.Exception if files cannot be deployed
   */
  private void deployFiles () throws Exception {
    boolean isResourcesDeployable = isResourcesDeployable();
		if ("disable".equalsIgnoreCase(opt("deployFiles"))){
			log.info("deployFiles is disabled. isResourcesDeployable={}", isResourcesDeployable);
			return;
		}

    if (isResourcesDeployable){
      if (getLogService().isTraceEnabled()){
        getLogService().trace("resource deploy folder: {}", servletContext.getRealPath("/"));
      }
//      deployControls(getResourceRootElement("/click-controls.xml"));
//      deployControls(getResourceRootElement("/extras-controls.xml"));
//      deployControls();
//      deployControlSets();
      deployResourcesOnClasspath();
    }

    if (!isResourcesDeployable){
      val buffer = new HtmlStringBuffer();
      if (onGoogleAppEngine) {
        buffer.append("Google App Engine does not support deploying");
        buffer.append(" resources to the 'click' web folder.\n");

      } else {
        buffer.append("could not deploy Click resources to the 'click'");
        buffer.append(" web folder.\nThis can occur if the call to");
        buffer.append(" ServletContext.getRealPath(\"/\") returns null, which means");
        buffer.append(" the web application cannot determine the file system path");
        buffer.append(" to deploy files to. This issue also occurs if the web");
        buffer.append(" application is not allowed to write to the file");
        buffer.append(" system.\n");
      }

      buffer.append("To resolve this issue please see the Click user-guide:");
      buffer.append(" http://click.apache.org/docs/user-guide/html/ch05s03.html#deploying-restricted-env");
      buffer.append(" \nIgnore this warning once you have settled on a");
      buffer.append(" deployment strategy");
      logService.warn(buffer.toString());
    }
  }

  /**
   * Deploy from the classpath all resources found under the directory
   * 'META-INF/resources/'. For backwards compatibility resources under the
   * directory 'META-INF/web/' are also deployed.
   * <p/>
   * Only jars and folders available on the classpath are scanned.
   *
   * @throws IOException if the resources cannot be deployed
   */
  private void deployResourcesOnClasspath() throws IOException {
    long startTime = System.currentTimeMillis();

    // Find all jars and directories on the classpath that contains the
    // directory "META-INF/resources/", and deploy those resources
    String resourceDirectory = "META-INF/resources";

    List<String> resources = new DeployUtils(logService).findResources(resourceDirectory).getResources();
    for (String resource : resources) {
      deployFile(resource, resourceDirectory);
    }

    logService.trace("deployResourcesOnClasspath: deployed files from jars and folders - {}  ms", System.currentTimeMillis()-startTime);
  }

  /**
   * Deploy the specified file.
   *
   * @param file the file to deploy
   * @param prefix the file prefix that must be removed when the file is
   * deployed
   */
  private void deployFile(String file, String prefix) {
    // Only deploy resources containing the prefix
    int pathIndex = file.indexOf(prefix);
    if (pathIndex == 0) {
      pathIndex += prefix.length();

      // By default deploy to the web root dir
      String targetDir = "";

      // resourceName example -> click/table.css
      String resourceName = file.substring(pathIndex);
      int index = resourceName.lastIndexOf('/');

      if (index != -1) {
        // targetDir example -> click
        targetDir = resourceName.substring(0, index);
      }

      // Copy resources to web folder
      ClickUtils.deployFile(servletContext,
          file,
          targetDir);
    }
  }

  private void loadMode () {
		String modeValue = opt("mode");
		if (modeValue.isEmpty()){
			modeValue = "development";
		}
    modeValue = ClickUtils.sysEnv("click.mode", modeValue);

    if ("production".equalsIgnoreCase(modeValue)){
      applicationMode = Mode.PRODUCTION;
    } else if ("profile".equalsIgnoreCase(modeValue)){
			applicationMode = Mode.PROFILE;
    } else if ("development".equalsIgnoreCase(modeValue)){
			applicationMode = Mode.DEVELOPMENT;
    } else if ("debug".equalsIgnoreCase(modeValue) || "trace".equalsIgnoreCase(modeValue)){
			applicationMode = Mode.DEBUG;
    } else {
      logService.error("invalid application mode: '"+ modeValue +"' - defaulted to 'DEBUG'");
			applicationMode = Mode.DEBUG;
    }
  }

  private void loadDefaultPages() throws ClassNotFoundException {
    if (!pageByPathMap.containsKey(ERROR_PATH)) {
      PageElm page = new PageElm("org.apache.click.util.ErrorPage", ERROR_PATH);

      pageByPathMap.put(ERROR_PATH, page);
    }

    if (!pageByPathMap.containsKey(NOT_FOUND_PATH)) {
      PageElm page = new PageElm("org.apache.click.Page", NOT_FOUND_PATH);

      pageByPathMap.put(NOT_FOUND_PATH, page);
    }
  }

  private void loadHeaders () {
    String headers = opt("headers");
    if (!headers.isEmpty()){
      commonHeaders = Collections.unmodifiableMap(loadHeadersMap(headers));
    } else {
      commonHeaders = Collections.unmodifiableMap(DEFAULT_HEADERS);
    }
  }

  private void loadFormatClass () throws ClassNotFoundException {
		String classname = opt("format.classname");
		formatClass = classname.isEmpty()
				? Format.class
				: ClickUtils.castUnsafe(ClickUtils.classForName(classname));
  }

  private void loadFileUploadService () throws Exception {
		String classname = opt("file-upload-service.classname");
    if (!classname.isEmpty()){
      val fileUploadServiceClass = ClickUtils.classForName(classname);
      fileUploadService = (FileUploadService) fileUploadServiceClass.newInstance();
//      Map<String,String> propertyMap = loadPropertyMap(fileUploadServiceElm);
//      for (var kv : propertyMap.entrySet()) {
//        getPropertyService().setValue(fileUploadService, kv.getKey(), kv.getValue());
//      }
    } else {
      fileUploadService = new CommonsFileUploadService();
    }
    log.debug("loadFileUploadService: initializing FileLoadService: {}", fileUploadService.getClass().getName());

    fileUploadService.onInit(servletContext);
  }

	private String opt (String initParamName) {
		String s = trim(servletConfig.getInitParameter(initParamName));// <servlet> <init-param> <param-name>/<param-value>
		if (!s.isEmpty()){
			return s;
		}
		return trim(servletContext.getInitParameter(initParamName));// <context-param> <param-name>/<param-value>
	}

  private void loadResourceService () throws Exception {
    String resourceServiceElm = opt("resource-service");

    if (!resourceServiceElm.isEmpty()){
			Class<?> resourceServiceClass = ClickUtils.classForName(resourceServiceElm);

      resourceService = (ResourceService) resourceServiceClass.newInstance();

//      Map<String, String> propertyMap = loadPropertyMap(resourceServiceElm);
//      for (String name : propertyMap.keySet()) {
//        getPropertyService().setValue(resourceService, name, propertyMap.get(name));
//      }
    } else {
      resourceService = new ClickResourceService();
    }
    logService.debug("initializing ResourceService: {}", resourceService.getClass().getName());
    resourceService.onInit(servletContext);
  }

  private void loadPropertyService () throws Exception {
    String classname = opt("property-service");

    if (StringUtils.isNotBlank(classname)){
			Class<? extends PropertyService> propertyServiceClass = ClickUtils.classForName(classname);
			if (propertyServiceClass != MVELPropertyService.class){
				propertyService = propertyServiceClass.newInstance();
			}//i don't re-create
    }
    logService.debug("initializing PropertyService: {}", propertyService.getClass().getName());
    propertyService.onInit(servletContext);
  }

//  private String getElementAttributeValue (@Nullable Element el, String nodeName, String attrName){
//    String value = el != null
//				?  el.getAttribute(attrName) // "classname" → "org.apache.click.service.VelocityTemplateService"
//          : null;
//
//    return trim(sysEnv("click."+nodeName+'.'+attrName, value));// click.template-service.classname
//  }

  private void loadTemplateService () throws Exception {
		String className = opt("template-service");// getElementAttributeValue(el, "template-service", "classname");

    if ( StringUtils.isNotBlank(className) ){
      // to do MVELTemplateService.class as default... | ClassCastException is not found!
      templateService = ClickUtils.newClassForName(className);
      //loadPropertyMapInto(el);
    } else {
      templateService = loadServiceClass(TemplateService.class);
    }

    if (templateService == null){
      log.warn("Implementation of TemplateService in 'template-service' NOT found. Fallback to Velocity");
      templateService = ClickUtils.newClassForName("org.apache.click.service.VelocityTemplateService");
    } else {
      log.debug("initializing TemplateService: ({}) {}", templateService.getClass().getTypeName(), templateService);
    }
    templateService.onInit(servletContext);
  }
//  private void loadPropertyMapInto (Element el) {
//    Map<String,String> propertyMap = loadPropertyMap(el);
//    for (String name : propertyMap.keySet()){
//      getPropertyService().setValue(templateService, name, propertyMap.get(name));
//    }
//  }

  private static <T> T loadServiceClass (Class<T> serviceClass){
    var o = loadServiceClass(serviceClass, Thread.currentThread().getContextClassLoader());
    if (o == null){
      o = loadServiceClass(serviceClass, ClickUtils.class.getClassLoader());
    }
    return o;
  }

  private static <T> T loadServiceClass (Class<T> serviceClass, ClassLoader loader){
    List<T> factories = new ArrayList<>();
    for (T factory : ServiceLoader.load(serviceClass, loader)){
      factories.add(factory);
    }
    if (factories.isEmpty()){
      return null;
    }
    return factories.get(0);// 1st one
  }

  private static Map<String, String> loadPropertyMap(Element parentElm) {
    Map<String, String> propertyMap = new HashMap<>();

    for (Element property : ClickUtils.getChildren(parentElm, "property")) {
      String name = property.getAttribute("name");
      String value = property.getAttribute("value");

      propertyMap.put(name, value);
    }

    return propertyMap;
  }

  private void loadLocale () {
    String value = opt("locale");
    if (!value.isEmpty()){
      val tokenizer = new StringTokenizer(value, "_");
      if (tokenizer.countTokens() == 1){
        String language = tokenizer.nextToken();
        locale = new Locale(language);

      } else if (tokenizer.countTokens() == 2){
        String language = tokenizer.nextToken();
        String country = tokenizer.nextToken();
        locale = new Locale(language, country);

      } else if (tokenizer.countTokens() == 3){
				String language = tokenizer.nextToken();
				String country = tokenizer.nextToken();
				String variant = tokenizer.nextToken();
				locale = new Locale(language, country, variant);
			}
    }
  }

  /**
   * Return the list of templates within the web application.
   *
   * @return list of all templates within the web application
   */
  private List<String> getTemplateFiles () {
    List<String> fileList = new ArrayList<>();

    Set<String> resources = servletContext.getResourcePaths("/");
    if (onGoogleAppEngine){
      // resources could be immutable so create copy
      Set<String> tempResources = new LinkedHashSet<>();

      // Load the two GAE preconfigured automapped folders
      tempResources.addAll(servletContext.getResourcePaths("/page"));
      tempResources.addAll(servletContext.getResourcePaths("/pages"));
      tempResources.addAll(resources);
      // assign copy to resources
      resources = Collections.unmodifiableSet(tempResources);
    }

    // Add all resources within web application
    for (String resource : resources){
      if (isTemplate(resource)){// .htm or .jsp
        fileList.add(resource);

      } else if (resource.endsWith("/")){
        if (!"/WEB-INF/".equalsIgnoreCase(resource)){
          processDirectory(resource, fileList);
        }
      }
    }
    Collections.sort(fileList);

    return fileList;
  }

  private void processDirectory (String dirPath, List<String> fileList) {
    Set<String> resources = servletContext.getResourcePaths(dirPath);

    if (resources != null){
      for (String resource : resources){
        if (isTemplate(resource)){
          fileList.add(resource);

        } else if (resource.endsWith("/")){
          processDirectory(resource, fileList);
        }
      }
    }
  }

	@Nullable
  private Class<? extends Page> getExcludesPageClass(String path) {
    for (ExcludesElm override : excludesList) {
      if (override.isMatch(path)){
        return override.getPageClass();
      }
    }
    return null;
  }

  /**
   * Return an array of bindable fields for the given page class based on
   * the binding mode.
   *
   * @param pageClass the page class
   * @param mode the binding mode
   * @return the field array of bindable fields
   */
  private static Field[] getBindablePageFields (Class<? extends Page> pageClass, AutoBinding mode) {
    if (mode == AutoBinding.DEFAULT){
      // Get @Bindable fields
      Map<String,Field> fieldMap = getAnnotatedBindableFields(pageClass);

      // Add public fields
      Field[] publicFields = pageClass.getFields();
      for (Field field : publicFields) {
        fieldMap.put(field.getName(), field);
      }
      // Copy the field map values into a field list
      Field[] fieldArray = new Field[fieldMap.size()];

      int i = 0;
      for (Field field : fieldMap.values()) {
        fieldArray[i++] = field;
      }
      return fieldArray;

    } else if (mode == AutoBinding.ANNOTATION) {
      Map<String, Field> fieldMap = getAnnotatedBindableFields(pageClass);

      // Copy the field map values into a field list
      Field[] fieldArray = new Field[fieldMap.size()];

      int i = 0;
      for (Field field : fieldMap.values()) {
        fieldArray[i++] = field;
      }
      return fieldArray;

    } else {// NONE
      return EMPTY_FIELD_ARRAY;
    }
  }

  /**
   * Return the fields annotated with the Bindable annotation.
   *
   * @param pageClass the page class
   * @return the map of bindable fields
   */
  private static Map<String,Field> getAnnotatedBindableFields(Class<? extends Page> pageClass) {
    List<Class<? extends Page>> pageClassList = new ArrayList<>();
    pageClassList.add(pageClass);

    Class<?> parentClass = pageClass.getSuperclass();
    while (parentClass != null) {
      // Include parent classes up to but excluding Page.class
      if (parentClass.isAssignableFrom(Page.class)) {
        break;
      }
      pageClassList.add(ClickUtils.castUnsafe(parentClass));
      parentClass = parentClass.getSuperclass();
    }
    // Reverse class list so parents are processed first, with the
    // actual page class fields processed last. This will enable the
    // page classes fields to override parent class fields
    Collections.reverse(pageClassList);

    Map<String,Field> fieldMap = new TreeMap<>();

    for (Class<? extends Page> aPageClass : pageClassList) {

      for (Field field : aPageClass.getDeclaredFields()) {
        if (field.getAnnotation(Bindable.class) != null) {
          fieldMap.put(field.getName(), field);

          // If field is not public set accessibility true
          if (!Modifier.isPublic(field.getModifiers())) {
            field.setAccessible(true);
          }
        }
      }
    }
    return fieldMap;
  }

  /**
   * Provide an Excluded Page class.
   * <p/>
   * <b>PLEASE NOTE</b> this class is <b>not</b> for public use, and can be
   * ignored.
   */
  public static class ExcludePage extends Page {
    static final Map<String,Object> HEADERS = new HashMap<>();
    static {
      HEADERS.put("Cache-Control", "max-age=3600, public");
    }

    /**
     * @see Page#getHeaders()
     *
     * @return the map of HTTP header to be set in the HttpServletResponse
     */
    @Override
    public Map<String,Object> getHeaders (){ return HEADERS; }
  }

	public static final Field[] EMPTY_FIELD_ARRAY = new Field[0];

  static final class PageElm {
		@Getter final Map<String, Field> fields;
		@Getter final Field[] fieldArray;
    @Getter final Map<String,Object> headers;
		@Getter final Class<? extends Page> pageClass;
		@Getter final String path;

    private PageElm(String path, Class<? extends Page> pageClass, Map<String,Object> commonHeaders, AutoBinding mode){
      headers = Collections.unmodifiableMap(commonHeaders);
      this.pageClass = pageClass;
      this.path = path;

      fieldArray = getBindablePageFields(pageClass, mode);

      fields = new HashMap<>();
      for (Field field : fieldArray) {
        fields.put(field.getName(), field);
      }
    }

		private PageElm (String path, String classname, Map<String,Object> commonHeaders) throws ClassNotFoundException {
			headers = Collections.unmodifiableMap(commonHeaders);
			this.path = path;
			pageClass = ClickUtils.castUnsafe(ClickUtils.classForName(classname));

			fieldArray = getBindablePageFields(pageClass, AutoBinding.DEFAULT);
			fields = new HashMap<>();
			for (Field field : fieldArray){
				fields.put(field.getName(), field);
			}
		}

    public PageElm (String classname, String path) throws ClassNotFoundException {
      this.fieldArray = EMPTY_FIELD_ARRAY;
      this.fields = Collections.emptyMap();
      this.headers = Collections.emptyMap();
      pageClass = ClickUtils.castUnsafe(ClickUtils.classForName(classname));
      this.path = path;
    }//new
  }

	@EqualsAndHashCode
  static final class ExcludesElm {
    final Set<String> pathSet = new HashSet<>();
    final Set<String> fileSet = new HashSet<>();

    public ExcludesElm (String pattern) {
      if (StringUtils.isNotBlank(pattern)){
        val tokenizer = new StringTokenizer(pattern, ", ");
        while (tokenizer.hasMoreTokens()){
          String token = tokenizer.nextToken();

          if (token.charAt(0) != '/') {
            token = "/" + token;
          }

          int index = token.lastIndexOf('.');
          if (index != -1) {
            token = token.substring(0, index);
            fileSet.add(token);

          } else {
            index = token.indexOf('*');
            if (index != -1) {
              token = token.substring(0, index);
            }
            pathSet.add(token);
          }
        }
      }
    }

    public Class<? extends Page> getPageClass () {
      return ExcludePage.class;
    }

    public boolean isMatch (String resourcePath) {
      if (fileSet.contains(resourcePath)) {
        return true;
      }

      for (String path : pathSet) {
        if (resourcePath.startsWith(path)) {
          return true;
        }
      }
      return false;
    }

    @Override
    public String toString() {
      return getClass().getName() + "[fileSet=" + fileSet + ",pathSet=" + pathSet + "]";
    }
  }

  /** Cache of resource bundle and locales which were not found, with support for multiple class loaders. */
  static final ConcurrentHashMap<String,Boolean> NOT_FOUND_MESSAGE_MAP_CACHE = new ConcurrentHashMap<>();
  /** Provides a synchronized cache of get value reflection methods, with support for multiple class loaders. */
  static final ConcurrentMap<String, MessagesMap> MESSAGE_MAP_CACHE = new ConcurrentHashMap<>();

  @Override
	public Map<String, String> createMessagesMap (@NonNull Class<?> baseClass, String globalResource, @Nullable Locale locale){
    val context = Context.getThreadLocalContext();

    if (locale == null){
      if (context != null){
        locale = context.getLocale();// 4 variants

      } else {//context==null: in test
        locale = getLocale();//this ConfigService.getLocale()

        if (locale == null){
          locale = Locale.getDefault();
        }
      }
    }
    // new MessagesMap(baseClass, globalResource, locale)
    val resourceKey = baseClass.getName()+'|'+globalResource+'_'+locale;

    val mm = MESSAGE_MAP_CACHE.get(resourceKey);

    if (mm != null){
      return mm;
    }

    Map<String,String> messages = new HashMap<>();

    loadResourceValuesIntoMap(globalResource, locale, messages);

    List<String> classnameList = new ArrayList<>();

    // Build a class list
    Class<?> aClass = baseClass;
    while ( !(aClass == null || Objects.equals(aClass , Object.class)) ){
      classnameList.add(aClass.getName());
      aClass = aClass.getSuperclass();
    }

    // Load messages from parent to child order, so that child class messages override parent messages
    for (int i = classnameList.size() - 1; i >= 0; i--){
      String className = classnameList.get(i);
      loadResourceValuesIntoMap(className, locale, messages);
    }

    messages = Collections.unmodifiableMap(messages);
    val messagesMap = new MessagesMap(resourceKey, messages);

    if ( isProductionMode() || isProfileMode() ){
      MESSAGE_MAP_CACHE.putIfAbsent(resourceKey, messagesMap);
    }

    return messagesMap;
  }


  /**
   * Return the ResourceBundle for the given resource name and locale.
   * By default: create a ResourceBundle using the standard JDK method:
   * {@link java.util.ResourceBundle#getBundle(java.lang.String, java.util.Locale, java.lang.ClassLoader)}.
   * <p/>
   * You can create your own custom ResourceBundle by overriding this method.
   * <p/>
   * In order for Click to use your custom MessagesMap implementation, you
   * need to provide your own {@link org.apache.click.service.MessagesMapService}
   * or extend {@link org.apache.click.service.DefaultMessagesMapService}.
   * <p/>
   * The method {@link org.apache.click.service.MessagesMapService#createMessagesMap(java.lang.Class, java.lang.String, java.util.Locale)  createMessagesMap},
   * can be implemented to return your custom MessagesMap instances.
   *
   * @param resourceName the resource bundle name
   * @param locale the resource bundle locale.
   *
   * @return the ResourceBundle for the given resource name and locale
   */
  protected ResourceBundle createResourceBundle (String resourceName, Locale locale) {
    return ClickUtils.getBundle(resourceName, locale);
  }

  /**
   * Load the values of the given resourceBundleName into the map.
   *
   * @param resourceBundleName the resource bundle name
   * @param toMap the map to load resource values into
   */
  protected void loadResourceValuesIntoMap (String resourceBundleName, Locale locale, Map<String,String> toMap){
    if (resourceBundleName == null || resourceBundleName.length() == 0){
      return;
    }
    val resourceKey = resourceBundleName+'_'+ locale;// this is just a key in the map

    if (!NOT_FOUND_MESSAGE_MAP_CACHE.containsKey(resourceKey)){
      try {
        ResourceBundle resources = createResourceBundle(resourceBundleName, locale);
        Enumeration<String> e = resources.getKeys();

        try {
          while (e.hasMoreElements()){
            String name = e.nextElement();
            String value = resources.getString(name);
            toMap.put(name, value);
          }
        } catch (Exception ignore){}

      } catch (MissingResourceException mre){//it's normal, thus without stacktrace
        log.debug("loadResourceValuesIntoMap: NOT FOUND {}: {}", resourceKey, mre.toString());
        NOT_FOUND_MESSAGE_MAP_CACHE.put(resourceKey,Boolean.TRUE);
      }
    }
  }

  @VisibleForTesting
  public static void clearMessagesMapCache (){
    MESSAGE_MAP_CACHE.clear();
    NOT_FOUND_MESSAGE_MAP_CACHE.clear();
    ResourceBundle.clearCache();
    ResourceBundle.clearCache(Thread.currentThread().getContextClassLoader());
    ResourceBundle.clearCache(XmlConfigService.class.getClassLoader());
  }
}