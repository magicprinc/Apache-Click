package org.apache.click.examples;

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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication // (scanBasePackageClasses = { LunaApplication.class, RemoteMvelConsoleController.class })
@EnableWebMvc
@ComponentScan(// == <context:component-scan base-package="org.apache.click.examples" scope-resolver="org.apache.click.extras.spring.PageScopeResolver"/>
		basePackages = "org.apache.click.examples",
		scopeResolver = PageScopeResolver.class
)
public class ExamplesApplication implements WebMvcConfigurer {

  public static void main (String[] args) {
		System.out.println(" *** main thread started *** "+Thread.currentThread());
    SpringApplication.run(ExamplesApplication.class, args);
    System.err.println(" *** main thread finishes *** "+Thread.currentThread());
  }

	private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
			"classpath:/META-INF/resources/",// todo classpath*:
			"classpath:/META-INF/resources/click/",
			"classpath:/resources/",
			"classpath:/resources/click/",
			"classpath:/static/",
			"classpath:/static/click/",
			"classpath:/public/"
	};

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
		registry.addResourceHandler("/static/**").addResourceLocations("classpath*:/static/").setCachePeriod(3600);// .resourceChain(true)
		registry.addResourceHandler("/resources/**").addResourceLocations("classpath:/resources/", "classpath:/static/", "classpath:/public/").setCachePeriod(3600).resourceChain(true);
	}

	/**
	 The Spring Click Servlet which handles *.htm requests.
	 Add mapping informing ClickServlet to serve static resources contained under /click/* directly from Click's JAR files.
	 Please note, you only need this mapping in restricted environments where Click cannot deploy resources to the file system.
	 */
	@Bean
	public ServletRegistrationBean<SpringClickServlet> clickServlet() {
		val reg = new ServletRegistrationBean<>(new SpringClickServlet(), "*.htm", "/click/*");
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

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("forward:/home.htm");
	}

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
}