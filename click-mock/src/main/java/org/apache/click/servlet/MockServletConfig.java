package org.apache.click.servlet;

import lombok.Getter;
import lombok.Setter;

import javax.servlet.ServletConfig;
import java.util.Enumeration;
import java.util.Map;

/**
 * Mock implementation of {@link javax.servlet.ServletConfig}.
 * <p/>
 * Implements all of the methods from the standard ServletConfig class plus
 * helper methods to aid setting up a config.
 */
public class MockServletConfig implements ServletConfig {

	/** The servlet context. */
	@Setter private MockServletContext servletContext;

	/** The servlet name. */
	@Getter(onMethod_=@Override) @Setter private String servletName = "mock click servlet";

	/**
	 * Create a new MockServletConfig instance with the specified servletName.
	 *
	 * @param servletName the servlet name
	 */
	public MockServletConfig (String servletName) {
		this(servletName, null);
	}

	/**
	 * Create a new MockServletConfig instance with the specified servletContext.
	 *
	 * @param servletContext the servletContext
	 */
	public MockServletConfig (MockServletContext servletContext) {
		this(null, servletContext);
	}

	/**
	 * Create a new MockServletConfig instance with the specified servletName
	 * and servletContext.
	 *
	 * @param servletName the servlet name
	 * @param servletContext the servlet context
	 */
	public MockServletConfig (String servletName, MockServletContext servletContext) {
		this(servletName, servletContext, null);
	}

	/**
	 * Create a new MockServletConfig instance with the specified servletName,
	 * servletContext and initialization parameters.
	 *
	 * @param servletName the servlet name
	 * @param servletContext the servlet context
	 * @param initParameters the initialization parameters
	 */
	public MockServletConfig (String servletName, MockServletContext servletContext, Map<String,String> initParameters) {
		this.servletContext = servletContext;
		this.servletName = servletName;
		addInitParameters(initParameters);
	}

	@Override
	public MockServletContext getServletContext () {
		if (servletContext == null){
			servletContext = new MockServletContext();
			servletContext.addInitParameter("pages", "org.apache.click.pages");
		}
		return servletContext;
	}

	/**
	 * Add an init parameter.
	 *
	 * @param name The parameter name
	 * @param value The parameter value
	 */
	public void addInitParameter (String name, String value) {
		getServletContext().addInitParameter(name, value);
	}

	/**
	 * Add the map of init parameters.
	 *
	 * @param initParameters A map of init parameters
	 */
	public void addInitParameters (Map<String, String> initParameters) {
		getServletContext().addInitParameters(initParameters);
	}

	/**
	 * Returns the names of the servlet's initialization parameters as an
	 * Enumeration of String objects, or an empty Enumeration if the servlet
	 * has no initialization parameters.
	 *
	 * @return enumeration of initialization parameters
	 */
	@Override
	public Enumeration<String> getInitParameterNames () {
		return getServletContext().getInitParameterNames();
	}

	/**
	 * Returns a String containing the value of the named initialization
	 * parameter, or null if the parameter does not exist.
	 *
	 * @param name a String specifying the name of the initialization parameter
	 * @return a String containing the value of the initialization parameter
	 */
	@Override
	public String getInitParameter (String name) {
		return getServletContext().getInitParameter(name);
	}
}