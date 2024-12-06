package org.apache.click.extras.service;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.cache.WebappTemplateLoader;
import freemarker.log.Logger;
import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.click.Page;
import org.apache.click.service.ConfigService;
import org.apache.click.service.TemplateException;
import org.apache.click.service.TemplateService;
import org.apache.click.util.ClickUtils;

import javax.servlet.ServletContext;
import java.io.IOError;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

/**
 * Provides a <a target="_blank" href="http://www.freemarker.org/">Freemarker</a> TemplateService class.
 *
 * <h3>Configuration</h3>
 * To configure the Freemarker TemplateService add the following element to your
 * <tt>click.xml</tt> configuration file.
 *
 * <pre class="prettyprint">
 * &lt;?xml version="1.0" encoding="UTF-8" standalone="yes"?&gt;
 * &lt;click-app charset="UTF-8"&gt;
 *
 *     &lt;pages package="org.apache.click.examples.page"/&gt;
 *
 *     &lt;template-service classname="org.apache.click.extras.service.FreemarkerTemplateService"/&gt;
 *
 * &lt;/click-app&gt; </pre>
 *
 * <b>Please note</b> that Click ships with a default <em>error.htm</em> that
 * is tailored for Velocity.
 * <p/>
 * If you switch to Freemarker replace the default <em>error.htm</em> with the
 * one shown below.
 * <p/>
 * To ensure Click uses your template instead of the default one, copy/paste
 * the template below to the web application path <em>/click/error.htm</em>.
 * Click won't override your custom template.
 *
 * <pre class="prettyprint">
 * &lt;html&gt;
 *   &lt;head&gt;
 *     &lt;title&gt;Error Page&lt;/title&gt;
 *     &lt;style  type='text/css'&gt;
 *       body, table, td {
 *       font-family: arial, sans-serif;
 *       font-size: 12px;
 *     }
 *     td.header {
 *       color: white;
 *       background: navy;
 *     }
 *     .errorReport {
 *       display: none;
 *     }
 *     a {
 *       color: blue;
 *     }
 *     &lt;/style&gt;
 *     &lt;script type='text/javascript'&gt;
 *       function displayError() {
 *         errorReport.style.display = 'block';
 *       }
 *     &lt;/script&gt;
 *   &lt;/head&gt;
 *
 *   &lt;body&gt;
 *     &lt;h1&gt;Error Page&lt;/h1&gt;
 *
 *     &lt;#if errorReport??&gt;
 *       The application encountered an unexpected error.
 *       &lt;p/&gt;
 *       To return to the application click &lt;a href="${context}"&gt;here&lt;/a&gt;.
 *       &lt;p/&gt;
 *
 *       &lt;#if mode != "production"&gt;
 *         To view the error details click &lt;a href="#" onclick="displayError();"&gt;here&lt;/a&gt;.
 *         &lt;p/&gt;
 *         ${errorReport}
 *         &lt;p/&gt;
 *       &lt;/#if&gt;
 *     &lt;/#if&gt;
 *
 *   &lt;/body&gt;
 * &lt;/html&gt;
 * </pre>
 */
@Slf4j
public class FreemarkerTemplateService implements TemplateService {
	static {
		try {
			Logger.selectLoggerLibrary(Logger.LIBRARY_SLF4J);
		} catch (ClassNotFoundException e){
			e.printStackTrace();
			throw new IOError(e);
		}
	}
  /** The click error page template path. */
  protected static final String ERROR_PAGE_PATH = "/click/error.htm";
  /** The click not found page template path. */
  protected static final String NOT_FOUND_PAGE_PATH = "/click/not-found.htm";

  /** The Freemarker engine configuration. */
  protected Configuration configuration;

  /**
   * The production/profile mode cache duration in seconds. The default value is 24 hours.
   *
   * cacheDuration the template cache duration in seconds to use when the application is in "production" or "profile" mode.
   */
  @Getter @Setter protected int cacheDuration = 60 * 60 * 24;

  /** The application configuration service. */
  protected ConfigService configService;

  /** The /click/error.htm page template has been deployed. */
  protected boolean deployedErrorTemplate;

  /** The /click/not-found.htm page template has been deployed. */
  protected boolean deployedNotFoundTemplate;

  /**
   * @see TemplateService#onInit(javax.servlet.ServletContext)
   *
   * @param servletContext the application servlet context
   * @throws ClassNotFoundException if an error occurs initializing the Template Service
   */
  @Override
	public void onInit (@NonNull ServletContext servletContext) throws ClassNotFoundException {
    configService = ClickUtils.getConfigService(servletContext);
    configuration = new Configuration();
    // Templates are stored in the / directory of the Web app.
    val webLoader = new WebappTemplateLoader(servletContext);
    // Templates are stored in the root of the classpath.
    val classLoader0 = new ClassTemplateLoader(getClass(), "/");
    val classLoader1 = new ClassTemplateLoader(getClass(), "/static");

    val multiLoader = new MultiTemplateLoader(new TemplateLoader[]{webLoader, classLoader0, classLoader1});
    configuration.setTemplateLoader(multiLoader);

    // Set the template cache duration in seconds
    if (configService.isProductionMode() || configService.isProfileMode())
      configuration.setTemplateUpdateDelay(getCacheDuration());
    else
      configuration.setTemplateUpdateDelay(1);

    // Set an error handler that prints errors, so they are readable with an HTML browser
    configuration.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);

    // Use beans wrapper (recommended for most applications)
    configuration.setObjectWrapper(ObjectWrapper.BEANS_WRAPPER);

    String charset = configService.getCharset();
    if (charset != null){
      // Set the default charset of the template files
      configuration.setDefaultEncoding(charset);

      // Set the charset of the output. This is actually just a hint, that
      // templates may require for URL encoding and for generating META element
      // that uses http-equiv="Content-type".
      configuration.setOutputEncoding(charset);
    }

    // Set the default locale
    if (configService.getLocale() != null){
      configuration.setLocale(configService.getLocale());
    }

    // Attempt to load click error page and not found templates from the web click directory
    try {
      configuration.getTemplate(ERROR_PAGE_PATH);
      deployedErrorTemplate = true;
    } catch (IOException ignore){}
    try {
      configuration.getTemplate(NOT_FOUND_PAGE_PATH);
      deployedNotFoundTemplate = true;
    } catch (IOException ignore){}
  }

  /** @see TemplateService#onDestroy */
  @Override public void onDestroy (){}

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
	public void renderTemplate (Page page, Map<String, ?> model, Writer writer) throws IOException, TemplateException {
    String templatePath = page.getTemplate();

    if (!deployedErrorTemplate && templatePath.equals(ERROR_PAGE_PATH)){
      templatePath = "META-INF/resources"+ ERROR_PAGE_PATH;
    }
    if (!deployedErrorTemplate && templatePath.equals(NOT_FOUND_PAGE_PATH)){
      templatePath = "META-INF/resources"+ NOT_FOUND_PAGE_PATH;
    }

    // Get the template object
    Template template = configuration.getTemplate(templatePath);

    // Merge the data-model and the template
    try {
      template.process(model, writer);
    } catch (freemarker.template.TemplateException fmte){
      throw new TemplateException(fmte);
    }
  }

  /**
   * @see TemplateService#renderTemplate(String, Map, Writer)
   *
   * @param templatePath the path of the template to render
   * @param model the model to merge with the template and render
   * @param writer the writer to send the merged template and model data to
   * @throws IOException if an IO error occurs
   * @throws TemplateException if template error occurs
   */
  @Override
	public void renderTemplate (String templatePath, Map<String, ?> model, Writer writer) throws IOException, TemplateException {
    // Get the template object
    Template template = configuration.getTemplate(templatePath);

    // Merge the data-model and the template
    try {
      template.process(model, writer);
    } catch (freemarker.template.TemplateException fmte){
      throw new TemplateException(fmte);
    }
  }
}