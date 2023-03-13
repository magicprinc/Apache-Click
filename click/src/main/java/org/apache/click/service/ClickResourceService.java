package org.apache.click.service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.click.util.ClickUtils;
import org.apache.commons.io.IOUtils;

import javax.annotation.Nullable;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Provides a default Click static resource service class. This class will
 * serve static resources contained in the web applications JARs, under the
 * resource path META-INF/resources and which are contained under the WAR file
 * web root.
 * <p/>
 * This service is useful for application servers which do not allow Click to
 * automatically deploy resources to the web root directory.
 */
@Slf4j
public class ClickResourceService implements ResourceService {

  /** The click resources CACHE !!! CACHE !!!. */
  final Map<String, byte[]> resourceCache = new ConcurrentHashMap<>();

  /** The application configuration service. */
  private ConfigService configService;

  /**
   * @see ResourceService#onInit(ServletContext)
   *
   * @param servletContext the application servlet context
   * @throws IOException if an IO error occurs initializing the service
   */
  @Override public void onInit (ServletContext servletContext) throws IOException {
    configService = ClickUtils.getConfigService(servletContext);
  }

  /**
   * @see ResourceService#onDestroy()
   */
  @Override public void onDestroy (){
    resourceCache.clear();
  }

  /**
   * @see ResourceService#isResourceRequest(HttpServletRequest)
   *
   * @param request the servlet request
   * @return true if the request is for a static click resource
   */
  @Override public boolean isResourceRequest (HttpServletRequest request) {
    String resourcePath = ClickUtils.getResourcePath(request);

    // If not a click page and not JSP and not a directory
    return !configService.isTemplate(resourcePath) && !resourcePath.endsWith("/");
  }

  /**
   * @see ResourceService#renderResource(HttpServletRequest, HttpServletResponse)
   *
   * @param request the servlet resource request
   * @param response the servlet response
   * @throws IOException if an IO error occurs rendering the resource
   */
  @Override public void renderResource (HttpServletRequest request, HttpServletResponse response) throws IOException {

    String resourcePath = ClickUtils.getResourcePath(request);

    byte[] resourceData = resourceCache.get(resourcePath);

    if (resourceData == null){// Lazily load resource
      resourceData = loadResourceData(resourcePath);

      if (resourceData == null){
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
        return;
      }
    }

    String mimeType = ClickUtils.getMimeType(resourcePath);
    if (mimeType != null){
      response.setContentType(mimeType);
    }

    if (log.isDebugEnabled()){
      log.debug("handleRequest: {} {}", request.getMethod(), request.getRequestURL());
    }
    // renderResource(response, resourceData)
    OutputStream outputStream = null;
    try {
      response.setContentLength(resourceData.length);

      outputStream = response.getOutputStream();
      outputStream.write(resourceData);

    } finally {
      ClickUtils.flush(outputStream);
    }
  }


  /**
   * Store the resource under the given resource path.
   *
   * ! Only cache in production/profile mode !
   *
   * @param resourcePath the path to store the resource under
   * @param data the resource byte array
   */
  private void storeResourceData (String resourcePath, @Nullable byte[] data){
    if (data != null && (configService.isProductionMode() || configService.isProfileMode())){
      resourceCache.put(resourcePath, data);
    }
  }

  /**
   * Load the resource for the given resourcePath. This method will load the
   * resource from the servlet context, and if not found, load it from the
   * classpath under the folder 'META-INF/resources'.
   *
   * @param resourcePath the path to the resource to load
   * @return the resource as a byte array
   * @throws IOException if the resources cannot be loaded
   */
  private byte[] loadResourceData(String resourcePath) throws IOException {

    byte[] resourceData;

    ServletContext servletContext = configService.getServletContext();

    resourceData = getServletResourceData(servletContext, resourcePath);
    if (resourceData != null){
      storeResourceData(resourcePath, resourceData);

    } else {
      resourceData = getClasspathResourceData("META-INF/resources" + resourcePath);

      if (resourceData != null) {
        storeResourceData(resourcePath, resourceData);
      }
    }

    return resourceData;
  }

  /**
   * Load the resource for the given resourcePath from the servlet context.
   *
   * @param servletContext the application servlet context
   * @param resourcePath the path of the resource to load
   * @return the byte array for the given resource path
   * @throws IOException if the resource could not be loaded
   */
  private byte[] getServletResourceData (ServletContext servletContext, String resourcePath) throws IOException {
    InputStream inputStream = null;
    try {
      inputStream = servletContext.getResourceAsStream(resourcePath);

      if (inputStream != null) {
        return IOUtils.toByteArray(inputStream);
      } else {
        return null;
      }

    } finally {
      ClickUtils.close(inputStream);
    }
  }

  /**
   * Load the resource for the given resourcePath from the classpath.
   *
   * @param resourcePath the path of the resource to load
   * @return the byte array for the given resource path
   * @throws IOException if the resource could not be loaded
   */
  private byte[] getClasspathResourceData (@NonNull String resourcePath) throws IOException {
    URL url = ClickUtils.getResource(resourcePath, getClass());
    if (url == null){ return null;}

    InputStream inputStream = url.openStream();
    try {

      if (inputStream != null) {
        return IOUtils.toByteArray(inputStream);
      } else {
        return null;
      }

    } finally {
      ClickUtils.close(inputStream);
    }
  }
}