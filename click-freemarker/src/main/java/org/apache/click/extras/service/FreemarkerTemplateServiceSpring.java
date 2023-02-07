package org.apache.click.extras.service;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.cache.WebappTemplateLoader;
import freemarker.ext.beans.BeansWrapper;
import freemarker.log.Logger;
import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateModel;
import lombok.NonNull;
import org.apache.click.service.ConfigService;
import org.apache.click.service.TemplateService;
import org.apache.click.util.ClickUtils;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationEvent;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.util.Map;

/**
 * Улучшенный {@link org.apache.click.extras.service.FreemarkerTemplateService}:
 * * slf4j
 * + дополнительный путь поиска (моих) ftl: /WEB-INF/ftl/
 * + опц.путь ../conf/ftl/ - можно заменять ftl на сервере
 * + Class, Enum
 * + Spring и настроенные в нем TemplateModel (Spring, Sql)
 * + анонс себя в Spring
 * + сохранение ссылки на себя в servletContext
 *
 * @author Andrey Fink [aprpda at gmail.com]
 */
public class FreemarkerTemplateServiceSpring extends FreemarkerTemplateService {
	static {
		try {
			Logger.selectLoggerLibrary(Logger.LIBRARY_SLF4J);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new IOError(e);
		}//t
	}
  /**
   * @see TemplateService#onInit(javax.servlet.ServletContext)
   *
   * @param servletContext the application servlet context
   * @throws Exception if an error occurs initializing the Template Service
   * @see org.apache.click.service.TemplateService#onInit(javax.servlet.ServletContext)
   */
  @Override public void onInit (@NonNull ServletContext servletContext) throws ClassNotFoundException {
    configService = ClickUtils.getConfigService(servletContext);

    configuration = new Configuration();

    // Templates are stored in the / (root) directory of the Web app.
    final TemplateLoader webloaderRoot = new WebappTemplateLoader(servletContext);
    // Templates are stored in the / (root) of the classpath.
    final TemplateLoader classLoaderRoot = new ClassTemplateLoader(getClass(), "/");
    //for my templates (raw servlets) WEBroot/WEB-INF/ftl/
    final TemplateLoader webloaderApr = new WebappTemplateLoader(servletContext, "/WEB-INF/ftl/");

    //click uses WEBroot/click and (fallback) classpath:/META-INF/resources/click

    FileTemplateLoader fileLoader = null;
    try {//for GAE, hosting
      final File confFtlDir = new File("../conf/ftl/");//= tomcat/conf/ftl
      if (confFtlDir.isDirectory() && !confFtlDir.isHidden()) {
        fileLoader = new FileTemplateLoader(confFtlDir, true);
      }//i
    } catch (Throwable ignore){}

    final TemplateLoader[] loaders = fileLoader != null
      ? new TemplateLoader[]{fileLoader, webloaderRoot, classLoaderRoot, webloaderApr}
      : new TemplateLoader[]{            webloaderRoot, classLoaderRoot, webloaderApr};
    configuration.setTemplateLoader(new MultiTemplateLoader(loaders));

    // Set the template cache duration in seconds
    if (configService.isProductionMode() || configService.isProfileMode()) {
      configuration.setTemplateUpdateDelay(getCacheDuration());

    } else {
      configuration.setTemplateUpdateDelay(2);//no cache
    }//i

    // Set an error handler that prints errors so they are readable with a HTML browser.
    configuration.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);

    // Use beans wrapper (recommended for most applications)
    configuration.setObjectWrapper(ObjectWrapper.BEANS_WRAPPER);

    //final String charset = configService.getCharset(); if (charset != null) {
    // Set the default charset of the template files
    configuration.setDefaultEncoding("UTF-8");

    // Set the charset of the output. This is actually just a hint, that
    // templates may require for URL encoding and for generating META element
    // that uses http-equiv="Content-type".
    configuration.setOutputEncoding("UTF-8");

    // Set the default locale
    if (configService.getLocale() != null) {
      configuration.setLocale(configService.getLocale());
    }//i

    //${Class["java.lang.System"].currentTimeMillis()} or ${Class["java.io.File"].listRoots()}
    configuration.setSharedVariable("Class", BeansWrapper.getDefaultInstance().getStaticModels());
    //${Enum["java.math.RoundingMode"].UP}
    configuration.setSharedVariable("Enum", BeansWrapper.getDefaultInstance().getEnumModels());

    try {
      configuration.setSharedVariable("servletContext", ObjectWrapper.BEANS_WRAPPER.wrap(servletContext));
    } catch (Exception e) {
      configService.getLogService().warn("onInit: can't wrap ServletContext "+ servletContext +". "+ this, e);
    }//t


    final WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(servletContext);
    if (ctx != null) {
      try { //регистрируем ${Spring.freemarkerConfigurationBase.defaultEncoding}, Sql("")
        final Map<String,TemplateModel> tmm = BeanFactoryUtils
            .beansOfTypeIncludingAncestors(ctx, TemplateModel.class, true, false);
        for (Map.Entry<String, TemplateModel> tm : tmm.entrySet()) {
          configuration.setSharedVariable(tm.getKey().trim(), tm.getValue());
        }//f

        ctx.publishEvent(new FTLSvcConfiguredAppEvent(this, configuration, configService));
      } catch (Exception ignore){}
    }//i !null

    //for non-click&non-spring users:
    servletContext.setAttribute(TemplateService.class.getName(), this);
    servletContext.setAttribute(Configuration.class.getName(), configuration);

    // Attempt to load click error page and not found templates from the WEB click directory
    try {
      configuration.getTemplate(ERROR_PAGE_PATH);
      deployedErrorTemplate = true;
    } catch (IOException ignore) {}

    try {
      configuration.getTemplate(NOT_FOUND_PAGE_PATH);
      deployedNotFoundTemplate = true;
    } catch (IOException ignore) {}
  }//onInit


  public static class FTLSvcConfiguredAppEvent extends ApplicationEvent {
    private final Configuration configuration;
    private final ConfigService configService;

    public FTLSvcConfiguredAppEvent (Object ftss, Configuration configuration, ConfigService configService) {
      super(ftss);
      this.configuration = configuration;
      this.configService = configService;
    }//new


    public Configuration getConfiguration () { return configuration; }

    public ConfigService getConfigService () { return configService; }
  }//FTLSvcConfiguredAppEvent

}