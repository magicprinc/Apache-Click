package org.apache.click.service;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Provides a static resource service interface.
 *
 * <h3>Configuration</h3>
 * The default ResourceService is {@link ClickResourceService}.
 * <p/>
 * However, you can instruct Click to use a different implementation by adding
 * the following element to your <tt>click.xml</tt> configuration file.
 *
 * <pre class="codeConfig">
 * &lt;?xml version="1.0" encoding="UTF-8" standalone="yes"?&gt;
 * &lt;click-app charset="UTF-8"&gt;
 *
 *     &lt;pages package="com.mycorp.page"/&gt;
 *
 *     &lt;<span class="red">resource-service</span> classname="<span class="blue">com.mycorp.service.DynamicResourceService</span>"&gt;
 *
 * &lt;/click-app&gt; </pre>
 */
public interface ResourceService {

  /**
   * Initialize the ResourceService with the given application configuration
   * service instance.
   * <p/>
   * This method is invoked after the ResourceService has been constructed.
   *
   * @param servletContext the application servlet context
   * @throws IOException if an IO error occurs initializing the service
   */
  void onInit (ServletContext servletContext) throws IOException;

  /** Destroy the ResourceService. */
  void onDestroy ();

  /**
   * Return true if the request is for a static resource.
   *
   * @param request the servlet request
   * @return true if the request is for a static resource
   */
  boolean isResourceRequest (HttpServletRequest request);

  /**
   * Render the resource request to the given servlet resource response.
   *
   * @param request the servlet resource request
   * @param response the servlet response
   * @throws IOException if an IO error occurs, rendering the resource
   */
  void renderResource (HttpServletRequest request, HttpServletResponse response) throws IOException;

}