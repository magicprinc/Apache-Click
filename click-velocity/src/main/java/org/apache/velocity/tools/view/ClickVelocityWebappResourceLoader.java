package org.apache.velocity.tools.view;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.click.util.ClickUtils;
import org.apache.commons.collections.ExtendedProperties;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.InputStream;
import java.net.URL;

/**
 * Resource loader that uses the ServletContext of a webapp to
 * load Velocity templates.  (it's much easier to use with servlets than
 * the standard FileResourceLoader, in particular the use of war files
 * is transparent).

 * The default search path is '/' (relative to the webapp root), but
 * you can change this behaviour by specifying one or more paths
 * by mean of as many webapp.resource.loader.path properties as needed
 * in the velocity.properties file.

 * All paths must be relative to the root of the webapp.

 * To enable caching and cache refreshing the webapp.resource.loader.cache and
 * webapp.resource.loader.modificationCheckInterval properties need to be
 * set in the velocity.properties file ... auto-reloading of global macros
 * requires the webapp.resource.loader.cache property to be set to 'false'.

 todo cache resources or use spring capabilities

 * @author <a href="mailto:geirm@optonline.net">Geir Magnusson Jr.</a>
 * @author Nathan Bubna
 * @author <a href="mailto:claude@savoirweb.com">Claude Brisson</a>
 @see org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader
 @see org.apache.velocity.runtime.resource.loader.FileResourceLoader
 */
@Slf4j
public class ClickVelocityWebappResourceLoader extends ResourceLoader {
  protected ServletContext servletContext;

  /**
   *  This is abstract in the base class, so we need it.
   *  <br>
   *  NOTE: this expects that the ServletContext has already been placed in the runtime's application attributes
   *        under its full class name (i.e. "javax.servlet.ServletContext").
   *
   * @param configuration the {@link ExtendedProperties} associated with this resource loader.
   */
  @Override
  public void init (ExtendedProperties configuration) {
    log.trace("WebappResourceLoader: initialization starting.");
    Object obj = rsvc.getApplicationAttribute(ServletContext.class.getName());
    if (obj instanceof ServletContext o)
      servletContext = o;// get the ServletContext!
    else
			throw new IllegalArgumentException("ServletContext must be configured as key: %s, but %s @ %s".formatted(ServletContext.class.getName(), obj, configuration));
    log.trace("WebappResourceLoader: initialization complete.");
  }

	private static final String NOT_FOUND = "WebappResourceLoader: Resource '%s' not found!";
  /**
   * Get an InputStream so that the Runtime can build a
   * template with it.
   *
   * @param name name of template to get ~ source
   * @return InputStream containing the template
   * @throws ResourceNotFoundException if template not found in classpath.
   */
  @Override
	public InputStream getResourceStream (String name) throws ResourceNotFoundException {
    if (StringUtils.isEmpty(name)){
      throw new ResourceNotFoundException("WebappResourceLoader: No template name provided");
    }
    if (name.startsWith("/")){
      name = name.substring(1);
    }
		try {
			val is = servletContext.getResourceAsStream('/'+ name);// [META-INF/resources] /name
			if (is != null){ return is; }
		} catch (Exception e){
			log.debug("ServletContext.getResourceAsStream failed: web {}", name, e);
		}
		try {
			val is = ClickUtils.getResourceAsStream(name, getClass());// /resources/ name
			if (is != null){ return is; }
		} catch (Exception e){
			log.debug("Class.getResourceAsStream failed: /{}", name, e);
		}
		try {
			val is = ClickUtils.getResourceAsStream("templates/"+ name, getClass());// /resources/templates/ name ~ spring boot
			if (is != null){ return is; }
		} catch (Exception e){
			log.debug("Class.getResourceAsStream failed: templates/{}", name, e);
		}
		try {
			val is = ClickUtils.getResourceAsStream("static/"+ name, getClass());
			if (is != null){ return is; }
		} catch (Exception e){
			log.debug("Class.getResourceAsStream failed: static/ {}", name, e);
		}
		throw new ResourceNotFoundException(NOT_FOUND.formatted(name));
	}

  /**
   * Checks to see if a resource has been deleted, moved or modified.
   *
   * @param resource Resource  The resource to check for modification
   * @return boolean  True if the resource has been modified
   */
  @Override
  public boolean isSourceModified (Resource resource) {
	  return getLastModified(resource) != resource.getLastModified();
  }

  /**
   Checks to see when a resource was last modified

	 rootPath is null if the servlet container cannot translate the virtual path to a real path for any reason
	 (such as when the content is being made available from a .war archive)

   @param templateResource Resource the resource to check
   @return long The time when the resource was last modified or 0 if the file can't be read
  */
  @Override
  public long getLastModified (Resource templateResource) {
		String name = templateResource.getName();
		if (name.startsWith("/")){
			name = name.substring(1);
		}
		try {
			String rootPath = servletContext.getRealPath("/");
			if (rootPath != null) return new File(rootPath, name).lastModified();// ? cachedFile.canRead()
		} catch (Exception e){
			log.warn("getLastModified: Could not get last modified time for {}", name, e);
		}
		try {
			URL url = servletContext.getResource('/'+name);// web /
			if (url != null) return url.openConnection().getLastModified();
		} catch (Exception e){
			log.warn("getLastModified: Could not get last modified time for {}", name, e);
		}
		try {
			URL url = ClickUtils.getResource(name, getClass());// /
			if (url != null) return url.openConnection().getLastModified();
		} catch (Exception e){
			log.warn("getLastModified: Could not get last modified time for {}", name, e);
		}
		try {
			URL url = ClickUtils.getResource("templates/"+name, getClass());
			if (url != null) return url.openConnection().getLastModified();
		} catch (Exception e){
			log.warn("getLastModified: Could not get last modified time for {}", name, e);
		}
		try {
			URL url = ClickUtils.getResource("static/"+ name, getClass());
			if (url != null) return url.openConnection().getLastModified();
		} catch (Exception e){
			log.warn("getLastModified: Could not get last modified time for {}", name, e);
		}
		return 0;
	}
}