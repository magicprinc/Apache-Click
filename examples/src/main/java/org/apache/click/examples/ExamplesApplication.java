package org.apache.click.examples;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.click.examples.util.DatabaseInitListener;
import org.apache.click.extras.cayenne.DataContextFilter;
import org.apache.click.extras.filter.PerformanceFilter;
import org.apache.click.extras.spring.PageScopeResolver;
import org.apache.click.extras.spring.SpringClickServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.Lifecycle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 spring-boot-starter-web

 ⛔ If you want to take complete control of Spring MVC, you can add your own `@Configuration` annotated with `@EnableWebMvc`.
    If you want to keep Spring Boot MVC features, and you just want to add additional MVC configuration (interceptors, formatters, view controllers etc.)
 				you can add your own `@Bean` of type WebMvcConfigurerAdapter, but without `@EnableWebMvc`!

 https://stackoverflow.com/questions/42393211/how-can-i-serve-static-html-from-spring-boot

 spring-boot-autoconfigure-2.7.18.jar!\META-INF\spring-configuration-metadata.json
 <pre><code>
 "name": "spring.web.resources.static-locations",
 "type": "java.lang.String[]",
 "description": "Locations of static resources. Defaults to classpath:[\/META-INF\/resources\/, \/resources\/, \/static\/, \/public\/].",
 "sourceType": "org.springframework.boot.autoconfigure.web.WebProperties$Resources",
 "defaultValue": [
 "classpath:\/META-INF\/resources\/",
 "classpath:\/resources\/",
 "classpath:\/static\/",
 "classpath:\/public\/"
 ]

 "name": "spring.web.resources.cache.period",
 "type": "java.time.Duration",
 "description": "Cache period for the resources served by the resource handler. If a duration suffix is not specified, seconds will be used. Can be overridden by the 'spring.web.resources.cache.cachecontrol' properties.",
 "sourceType": "org.springframework.boot.autoconfigure.web.WebProperties$Resources$Cache"

 </code></pre>
 */
@SpringBootApplication // (scanBasePackageClasses = { LunaApplication.class, RemoteMvelConsoleController.class })
//@EnableWebMvc → WebMvcConfigurer
@ComponentScan(// == <context:component-scan base-package="org.apache.click.examples" scope-resolver="org.apache.click.extras.spring.PageScopeResolver"/>
	basePackages = "org.apache.click.examples",
	scopeResolver = PageScopeResolver.class,
	basePackageClasses = ExamplesApplication.class
)
@Slf4j
public class ExamplesApplication implements Lifecycle {
	public static void main (String[] args) {
		System.out.println(" *** main thread started *** "+Thread.currentThread());
		SpringApplication.run(ExamplesApplication.class, args);
		System.err.println(" *** main thread finishes *** "+Thread.currentThread());
  }
//	private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
//			"classpath:/META-INF/resources/",// todo classpath*:
//			"classpath:/resources/",
//			"classpath:/static/",
//			"classpath:/public/"
//	};
//	@Override
//	public void addResourceHandlers(ResourceHandlerRegistry registry) {
//		registry.addResourceHandler("/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
//		registry.addResourceHandler("/static/**").addResourceLocations("classpath*:/static/").setCachePeriod(3600);// .resourceChain(true)
//		registry.addResourceHandler("/resources/**").addResourceLocations("classpath:/resources/", "classpath:/static/", "classpath:/public/").setCachePeriod(3600).resourceChain(true);
//	}

	/**
	 The Spring Click Servlet which handles *.htm requests ~ "*.htm"
	 Add mapping informing ClickServlet to serve static resources contained under /click/* directly from Click's JAR files ~ "/click/*"
	 Please note, you only need this mapping in restricted environments where Click cannot deploy resources to the file system.
	 */
	@Bean
	public ServletRegistrationBean<SpringClickServlet> clickServlet() {
		//val reg = new ServletRegistrationBean<>(new SpringClickServlet(), "*.htm", "/click/*");
		val reg = new ServletRegistrationBean<>(new SpringClickServlet(), "*.htm");
		reg.setName("ClickServlet");
		reg.setLoadOnStartup(0);

		// Set init parameters
		val params = new HashMap<String,String>();
		params.put("mode", "trace");
		params.put("deployFiles", "disable");
		params.put("inject-page-beans", "true");
		params.put("pages", "org.apache.click.examples.page");
		params.put("property-service", "org.apache.click.service.MVELPropertyService");
		reg.setInitParameters(params);

		return reg;
	}

//	@Override
//	public void addViewControllers(ViewControllerRegistry registry) {
//		registry.addViewController("/").setViewName("forward:/home.htm");
//	}

	/**
	 Provides an in memory database initialization listener.
	 In a production application a separate database would be used, and this listener would not be needed
	 */
	@Bean
	public DatabaseInitListener databaseInitListener() {
		return new DatabaseInitListener();
	}

	/** Provides a thread local Cayenne DataContext filter */
	@Bean
	public FilterRegistrationBean<DataContextFilter> dataContextFilter () {
		val reg = new FilterRegistrationBean<DataContextFilter>();
		val filter = new DataContextFilter();
		reg.setInitParameters(Map.of("oscache-enabled", "true"));
		reg.setFilter(filter);
		reg.addUrlPatterns("*.htm");
		return reg;
	}

	/**
	 Provides a web application performance filter which compresses the response
	 and sets the Expires header on selected static resources.
	 The "cachable-paths" init parameter tells the filter resources can have their Expires header set so the browser will cache them.
	 The "excludes-path" init parameter tells the filter which requests should be ignored by the filter.
	 */
	@Bean
	public FilterRegistrationBean<PerformanceFilter> performanceFilter() {
		val reg = new FilterRegistrationBean<PerformanceFilter>();
		val filter = new PerformanceFilter();
		reg.setInitParameters(Map.of(
				"cachable-paths", "/assets/*",
				"exclude-paths", "*/excel-export.htm"
		));
		reg.setFilter(filter);
		reg.addUrlPatterns("*.css", "*.js", "*.gif", "*.png");
		reg.setServletNames(Collections.singletonList("ClickServlet"));
		return reg;
	}

	@Override
	public void start () {
		log.warn("INFO: ✅ start is called");
	}

	@Override
	public void stop () {
		log.warn("INFO: ⛔ stop is called");
	}

	@Override
	public boolean isRunning () {
		log.warn("INFO: ❔ isRunning is called");
		return false;
	}
}