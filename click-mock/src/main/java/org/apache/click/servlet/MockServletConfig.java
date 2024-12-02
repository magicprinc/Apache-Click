package org.apache.click.servlet;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.annotation.Nullable;
import javax.servlet.ServletConfig;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Mock implementation of {@link javax.servlet.ServletConfig}.
 * <p/>
 * Implements all of the methods from the standard ServletConfig class plus
 * helper methods to aid setting up a config.
 * @see MockServletContext
 */
public class MockServletConfig implements ServletConfig {

	/** The servlet context. */
	@Getter(onMethod_=@Override) @Setter private MockServletContext servletContext;

	/** The servlet name. */
	@Getter(onMethod_=@Override) @Setter private String servletName;

	private final Map<String,String> initParams = new ConcurrentHashMap<>();

	/**
	 * Create a new MockServletConfig instance with the specified servletName
	 * and servletContext.
	 *
	 * @param servletName the servlet name
	 * @param servletContext the servlet context
	 */
	public MockServletConfig (@NonNull String servletName, @NonNull MockServletContext servletContext) {
		this.servletContext = servletContext;
		this.servletName = servletName;
	}//new

	/**
	 * Add an init parameter.
	 *
	 * @param name The parameter name
	 * @param value The parameter value
	 */
	public void addInitParameter (String name, String value) {
		initParams.put(name, value);
	}

	/**
	 * Add the map of init parameters.
	 *
	 * @param initParameters A map of init parameters
	 */
	public void addInitParameters (Map<String, String> initParameters) {
		initParams.putAll(initParameters);
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
		return Collections.enumeration(initParams.keySet());
	}

	/**
	 * Returns a String containing the value of the named initialization
	 * parameter, or null if the parameter does not exist.
	 *
	 * @param name a String specifying the name of the initialization parameter
	 * @return a String containing the value of the initialization parameter
	 */
	@Override  @Nullable
	public String getInitParameter (String name) {
		return initParams.get(name);
	}
}