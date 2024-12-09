package net.sf.click.examples.page;

import lombok.val;
import org.apache.click.ClickServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;

@SpringBootApplication // (scanBasePackageClasses = { LunaApplication.class, RemoteMvelConsoleController.class })
// @EnableWebMvc implements WebMvcConfigurer
public class ChartApplication {
  public static void main (String[] args) {
		System.out.println(" *** main thread started *** "+Thread.currentThread());
    SpringApplication.run(ChartApplication.class, args);
    System.err.println(" *** main thread finishes *** "+Thread.currentThread());
  }

//	private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
//			"classpath:/META-INF/resources/",// todo classpath*:
//			"classpath:/resources/",
//			"classpath:/static/",
//			"classpath:/public/"
//	};
//	@Override public void addResourceHandlers(ResourceHandlerRegistry registry) {
//		registry.addResourceHandler("/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
//	}
//@Override public void addViewControllers(ViewControllerRegistry registry) {
//	registry.addViewController("/").setViewName("forward:/home.htm");
//}

	@Bean
	public ServletRegistrationBean<ClickServlet> clickServlet () {
		val reg = new ServletRegistrationBean<>(new ClickServlet(), "*.htm");// , "/click/*"
		reg.setName("ClickServlet");
		reg.setLoadOnStartup(0);

		// Set init parameters
		val params = new HashMap<String,String>();
		params.put("pages", "net.sf.click.examples.page");
		params.put("mode", "trace");
		params.put("deployFiles", "disable");
		reg.setInitParameters(params);

		return reg;
	}
}