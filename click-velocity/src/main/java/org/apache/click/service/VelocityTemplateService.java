package org.apache.click.service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.click.Context;
import org.apache.click.Page;
import org.apache.click.util.ClickUtils;
import org.apache.click.util.ErrorReport;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.exception.TemplateInitException;
import org.apache.velocity.io.VelocityWriter;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.apache.velocity.tools.view.WebappResourceLoader;
import org.apache.velocity.util.SimplePool;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import static org.apache.click.util.ClickUtils.trim;
import static org.apache.velocity.runtime.RuntimeConstants.RESOURCE_LOADER;
import static org.apache.velocity.runtime.RuntimeConstants.RESOURCE_LOADER_CHECK_INTERVAL;

/**
 * Provides a <a target="_blank" href="http://velocity.apache.org//">Velocity</a> TemplateService class.
 * <p/>
 * Velocity provides a simple to use, but powerful and performant templating engine
 * for the Click Framework. The Velocity templating engine is configured and accessed
 * by this VelocityTemplateService class.
 * Velocity is the default templating engine used by Click and the Velocity class
 * dependencies are included in the standard Click JAR file.
 * <p/>
 * You can also instruct Click to use a different template service implementation.
 * Please see {@link TemplateService} for more details.
 * <p/>
 * To see how to use the Velocity templating language please see the
 * <a target="blank" href="../../../../../velocity/VelocityUsersGuide.pdf">Velocity Users Guide</a>.
 *
 * <h3>Velocity Configuration</h3>
 * The VelocityTemplateService is the default template service used by Click,
 * so it does not require any specific configuration.
 * However, if you wanted to configure this service specifically in your
 * <tt>click.xml</tt> configuration file you would add the following XML element.
 *
 * <pre class="codeConfig">
 * &lt;<span class="red">template-service</span> classname="<span class="blue">org.apache.click.service.VelocityTemplateService</span>"/&gt; </pre>
 *
 * <h4>Velocity Properties</h4>
 *
 * The Velocity runtime engine is configured through a series of properties when the
 * VelocityTemplateService is initialized.  The default Velocity properties set are:
 *
 * <pre class="codeConfig">
 * resource.loader=<span class="blue">webapp</span>, <span class="red">class</span>
 *
 * <span class="blue">webapp</span>.resource.loader.class=org.apache.velocity.tools.view.WebappResourceLoader
 * <span class="blue">webapp</span>.resource.loader.cache=[true|false] &nbsp; <span class="green">#depending on application mode</span>
 * <span class="blue">webapp</span>.resource.loader.modificationCheckInterval=0 <span class="green">#depending on application mode</span>
 *
 * <span class="red">class.resource</span>.loader.class=org.apache.velocity.runtime.loader.ClasspathResourceLoader
 * <span class="red">class.resource</span>.loader.cache=[true|false] &nbsp; <span class="green">#depending on application mode</span>
 * <span class="red">class.resource</span>.loader.modificationCheckInterval=0 <span class="green">#depending on application mode</span>
 *
 * velocimacro.library.autoreload=[true|false] <span class="green">#depending on application mode</span>
 * velocimacro.library=click/VM_global_library.vm
 * </pre>
 *
 * This service uses the Velocity Tools WebappResourceLoader for loading templates.
 * This avoids issues associate with using the Velocity FileResourceLoader on JEE
 * application servers.
 * <p/>
 * See the Velocity
 * <a target="topic" href="../../../../../velocity/developer-guide.html#Velocity Configuration Keys and Values">Developer Guide</a>
 * for details about these properties. Note when the application is in <tt>trace</tt> mode
 * the Velocity properties used will be logged on startup.
 * <p/>
 * If you want to add some of your own Velocity properties, or replace Click's
 * properties, add a <span class="blue"><tt>velocity.properties</tt></span> file in the <tt>WEB-INF</tt>
 * directory. Click will automatically pick up this file and load these properties.
 * <p/>
 * As an example say we have our own Velocity macro library called
 * <tt>mycorp.vm</tt> we can override the default <tt>velocimacro.library</tt>
 * property by adding a <tt>WEB-INF/velocity.properties</tt> file to our web
 * application. In this file we would then define the property as:
 *
 * <pre class="codeConfig">
 * velocimacro.library=<span class="blue">mycorp.vm</span> </pre>
 *
 * Note do not place Velocity macros under the WEB-INF directory as the Velocity
 * ResourceManager will not be able to load them.
 * <p/>
 * The simplest way to set your own macro file is to add a file named <span class="blue"><tt>macro.vm</tt></span>
 * under your web application's root directory. At startup Click will first check to see
 * if this file exists, and if it does it will use it instead of <tt>click/VM_global_library.vm</tt>.
 *
 * <h3>Application Modes and Caching</h3>
 *
 * <h4>Production and Profile Mode</h4>
 *
 * When the Click application is in <tt>production</tt> or <tt>profile</tt> mode Velocity caching
 * is enabled. With caching enables page templates and macro files are loaded and
 * parsed once and then are cached for use with later requests. When in
 * <tt>production</tt> or <tt>profile</tt> mode the following Velocity runtime
 * properties are set:
 *
 * <pre class="codeConfig">
 * webapp.resource.loader.cache=true
 * webapp.resource.loader.modificationCheckInterval=0
 *
 * class.resource.loader.cache=true
 * class.resource.loader.modificationCheckInterval=0
 *
 * velocimacro.library.autoreload=false </pre>
 *
 * When running in these modes the {@link Slf4jLogService} will be configured to use
 *
 * <h4>Development and Debug Modes</h4>
 *
 * When the Click application is in <tt>development</tt>, <tt>debug</tt> or <tt>trace</tt>
 * modes Velocity caching is disabled. When caching is disabled page templates
 * and macro files are reloaded and parsed when ever they changed. With caching
 * disabled the following Velocity
 * runtime properties are set:
 *
 * <pre class="codeConfig">
 * webapp.resource.loader.cache=false
 *
 * class.resource.loader.cache=false
 *
 * velocimacro.library.autoreload=true </pre>
 *
 * Disabling caching is useful for application development where you can edit page
 * templates on a running application server and see the changes immediately.
 * <p/>
 * <b>Please Note</b> Velocity caching should be used for production as Velocity
 * template reloading is much, much slower and the process of parsing and
 * introspecting templates and macros can use a lot of memory.
 *
 * <h3>Velocity Logging</h3>
 * Velocity logging is very verbose at the best of times, so this service
 * keeps the logging level at <tt>ERROR</tt> in all modes except <tt>trace</tt>
 * mode where the Velocity logging level is set to <tt>WARN</tt>.
 * <p/>
 * If you are having issues with some Velocity page templates or macros please
 * switch the application mode into <tt>trace</tt> so you can see the warning
 * messages provided.
 * <p/>
 * To support the use of Click <tt>LogService</tt> classes inside the Velocity
 * runtime a {@link LogChuteAdapter} class is provided. This class wraps the
 * Click LogService with a Velocity <tt>LogChute</tt> so the Velocity runtime can
 * use it for logging messages to.
 * <p/>
 * If you are using LogServices other than ConsoleLogService you will
 * probably configure that service to filter out Velocity's verbose <tt>INFO</tt>
 * level messages.
 */
@Slf4j
public class VelocityTemplateService implements TemplateService {
  /** The default velocity properties filename: "<tt>/WEB-INF/velocity.properties</tt>". */
  protected static final String DEFAULT_TEMPLATE_PROPS = "/WEB-INF/velocity.properties";

  /** The click error page template path. */
  protected static final String ERROR_PAGE_PATH = "/click/error.htm";

  /** The user supplied macro file name: "<tt>macro.vm</tt>". */
  protected static final String MACRO_VM_FILE_NAME = "macro.vm";

  /** The click not found page template path. */
  protected static final String NOT_FOUND_PAGE_PATH = "/click/not-found.htm";

  /** The global Velocity macro file path: "<tt>/click/VM_global_library.vm</tt>" */
  protected static final String VM_FILE_PATH = "/click/VM_global_library.vm";

  /** The Velocity writer buffer size. */
  protected static final int WRITER_BUFFER_SIZE = 32 * 1024;

  /** The application configuration service. */
  protected ConfigService configService;

  /** The /click/error.htm page template has been deployed. */
  protected boolean deployedErrorTemplate;

  /** The /click/not-found.htm page template has been deployed. */
  protected boolean deployedNotFoundTemplate;

  /** The VelocityEngine instance. */
  protected VelocityEngine velocityEngine = new VelocityEngine();

  /** Cache of velocity writers. */
  protected SimplePool writerPool = new SimplePool(40);

  /**
   * @see TemplateService#onInit(ServletContext)
   *
   * @param servletContext the application servlet velocityContext
   * @throws Exception if an error occurs initializing the Template Service
   */
  @Override
  public void onInit (@NonNull ServletContext servletContext) throws Exception {
    this.configService = ClickUtils.getConfigService(servletContext);

    // Set ConfigService instance for LogChuteAdapter
    velocityEngine.setApplicationAttribute(ConfigService.class.getName(), configService);
    // Set ServletContext instance for WebappResourceLoader
    velocityEngine.setApplicationAttribute(ServletContext.class.getName(), servletContext);

    // Load velocity properties
    Properties properties = getInitProperties();
    velocityEngine.init(properties);// Initialize VelocityEngine

    // Attempt to load click error page and not found templates from the web click directory
    try {
      velocityEngine.getTemplate(ERROR_PAGE_PATH);
      deployedErrorTemplate = true;
    } catch (ResourceNotFoundException ignore){// rnfe
    }
    try {
      velocityEngine.getTemplate(NOT_FOUND_PAGE_PATH);
      deployedNotFoundTemplate = true;
    } catch (ResourceNotFoundException ignore){// rnfe
    }
  }

  /** @see TemplateService#onDestroy */
  @Override
  public void onDestroy() {
    velocityEngine = null;// Dereference any allocated objects
    writerPool = null;  configService = null;
  }

  /**
   * @see TemplateService#renderTemplate(Page, Map, Writer)
   *
   * @param page the page template to render
   * @param model the model to merge with the template and render
   * @param writer the writer to send the merged template and model data to
   * @throws IOException if an IO error occurs
   * @throws TemplateException if template error occurs
   */
  @Override
  public void renderTemplate (Page page, Map<String,Object> model, Writer writer) throws IOException, TemplateException {
    String templatePath = page.getTemplate();

    if (!deployedErrorTemplate && templatePath.equals(ERROR_PAGE_PATH)){
      templatePath = "META-INF/resources" + ERROR_PAGE_PATH;
    }
    if (!deployedErrorTemplate && templatePath.equals(NOT_FOUND_PAGE_PATH)){
      templatePath = "META-INF/resources" + NOT_FOUND_PAGE_PATH;
    }

    internalRenderTemplate(templatePath, page, model, writer);
  }

  /**
   * @see TemplateService#renderTemplate(String, Map, Writer)
   *
   * @param templatePath the path of the template to render
   * @param model the model to merge with the template and render
   * @param writer the writer to send the merged template and model data to
   * @throws IOException if an IO error occurs
   * @throws TemplateException if an error occurs
   */
  @Override
  public void renderTemplate(String templatePath, Map<String,Object> model, Writer writer) throws IOException, TemplateException {
    internalRenderTemplate(templatePath, null, model, writer);
  }

  /**
   * Return the Velocity Engine initialization properties.
   *
   * @return the Velocity Engine initialization properties
   * @throws MalformedURLException if a resource cannot be loaded
   */
  protected Properties getInitProperties () throws MalformedURLException {
    val velProps = new Properties();
    // Set default velocity runtime properties.
    velProps.setProperty(RuntimeConstants.RESOURCE_LOADERS, "webapp,class");// ,file
    velProps.setProperty(RESOURCE_LOADER+".webapp.class", WebappResourceLoader.class.getName());
    velProps.setProperty(RESOURCE_LOADER+".class.class", ClasspathResourceLoader.class.getName());
    //velProps.setProperty(RESOURCE_LOADER+"file.class", FileResourceLoader.class.getName());

    if (configService.isProductionMode() || configService.isProfileMode()){
      velProps.setProperty(RESOURCE_LOADER+".webapp.cache", "true");
			velProps.setProperty(RESOURCE_LOADER+".class.cache", "true");
			velProps.setProperty(RESOURCE_LOADER+".webapp."+RESOURCE_LOADER_CHECK_INTERVAL, "3600");
			velProps.setProperty(RESOURCE_LOADER+".class."+RESOURCE_LOADER_CHECK_INTERVAL, "3600");
      velProps.setProperty(RuntimeConstants.VM_LIBRARY_AUTORELOAD, "false");
    } else {
			velProps.setProperty(RESOURCE_LOADER+".webapp.cache", "false");
			velProps.setProperty(RESOURCE_LOADER+".class.cache", "false");
      velProps.setProperty(RuntimeConstants.VM_LIBRARY_AUTORELOAD, "true");
    }
    // Use 'macro.vm' exists set it as default VM library
    ServletContext servletContext = configService.getServletContext();
    URL macroURL = servletContext.getResource('/'+ MACRO_VM_FILE_NAME);
    if (macroURL != null){
      velProps.setProperty(RuntimeConstants.VM_LIBRARY, '/'+ MACRO_VM_FILE_NAME);
    } else {// Else use '/click/VM_global_library.vm' if available.
      URL globalMacroURL = servletContext.getResource(VM_FILE_PATH);
      if (globalMacroURL != null){
        velProps.setProperty(RuntimeConstants.VM_LIBRARY, VM_FILE_PATH);
      } else {// Else use '/WEB-INF/classes/macro.vm' if available ×→ classpath
        String webInfMacroPath = '/'+ MACRO_VM_FILE_NAME;// /WEB-INF/classes/macro.vm
        URL webInfMacroURL = getClass().getResource(webInfMacroPath);
        if (webInfMacroURL != null){
          velProps.setProperty(RuntimeConstants.VM_LIBRARY, webInfMacroURL.toString());
        }
      }
    }
    // Load user velocity properties.
    Properties userProperties = new Properties();
    String filename = DEFAULT_TEMPLATE_PROPS;
    InputStream inputStream = servletContext.getResourceAsStream(filename);
		if (inputStream == null){
			inputStream = ClickUtils.getResourceAsStream("velocity.properties", getClass());
		}
    if (inputStream != null){
      try {
        userProperties.load(inputStream);
      } catch (IOException ioe){
        log.error("error loading velocity properties file: {} @ {}", filename, inputStream, ioe);
      } finally {
        ClickUtils.close(inputStream);
      }
    }
    // Add user properties.
    for (Map.Entry<Object,Object> entry : userProperties.entrySet()){
      Object pop = velProps.setProperty(trim(entry.getKey()), trim(entry.getValue()));
      if (pop != null && log.isDebugEnabled()){
				log.debug("user defined velocity property '{}={}' replaced default property '{}={}'", entry.getKey(), entry.getValue(), entry.getKey(), pop);
      }
    }
    if (log.isTraceEnabled()){
      log.trace("velocity properties: {}", new TreeMap<>(velProps));
    }
    return velProps;
  }

  /**
   * Provides the underlying Velocity template rendering.
   *
   * @param templatePath the template path to render
   * @param page the page template to render
   * @param model the model to merge with the template and render
   * @param writer the writer to send the merged template and model data to
   * @throws IOException if an IO error occurs
   * @throws TemplateException if a template error occurs
   */
  protected void internalRenderTemplate (String templatePath, Page page, Map<String,Object> model, Writer writer) throws IOException, TemplateException {
    val velocityContext = new VelocityContext(model);
    // May throw parsing error if a template could not be obtained
    Template template;  VelocityWriter velocityWriter = null;
    try {
      String charset = configService.getCharset();
			template = charset != null
					? velocityEngine.getTemplate(templatePath, charset)
					: velocityEngine.getTemplate(templatePath);
      velocityWriter = (VelocityWriter) writerPool.get();

      if (velocityWriter == null)
        velocityWriter = new VelocityWriter(writer, WRITER_BUFFER_SIZE, true);
      else
        velocityWriter.recycle(writer);

      template.merge(velocityContext, velocityWriter);

    } catch (ParseErrorException e){
      printErrorAndThrow(page, writer, velocityWriter, new TemplateException(e, e.getTemplateName(), e.getLineNumber(), e.getColumnNumber()));
    } catch (TemplateInitException e){
      printErrorAndThrow(page, writer, velocityWriter, new TemplateException(e, e.getTemplateName(), e.getLineNumber(), e.getColumnNumber()));
    } catch (Exception error){
      printErrorAndThrow(page, writer, velocityWriter, new TemplateException(error));
    } finally {
      if (velocityWriter != null){
        velocityWriter.flush();// flush and put back into the pool don't close to allow us to play nicely with others
        // Clear the VelocityWriter's reference to its internal Write to allow the latter to be GC'd while vw is pooled
        velocityWriter.recycle(null);
        writerPool.put(velocityWriter);
      }
      writer.flush();
      writer.close();
    }
  }

  /** Exception occurred merging template and model.
   It is possible that - some output has already been written,
   so we will append the error report to the previous output */
  private void printErrorAndThrow (Page page, Writer writer, VelocityWriter velocityWriter, TemplateException te) throws IOException, TemplateException {
    val errorReport = new ErrorReport(te,
        ((page != null) ? page.getClass() : null),
        configService.isProductionMode(),
        Context.getThreadLocalContext().getRequest(),
        configService.getServletContext()
		);
    if (velocityWriter == null){
      velocityWriter = new VelocityWriter(writer, WRITER_BUFFER_SIZE, true);
    }
    velocityWriter.write(errorReport.toString());
    throw te;
  }
}