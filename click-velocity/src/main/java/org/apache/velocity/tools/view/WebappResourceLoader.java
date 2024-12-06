package org.apache.velocity.tools.view;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.click.util.ClickUtils;
import org.apache.commons.collections.ExtendedProperties;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;
import org.apache.velocity.util.ExtProperties;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
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
 *
 * @author <a href="mailto:geirm@optonline.net">Geir Magnusson Jr.</a>
 * @author Nathan Bubna
 * @author <a href="mailto:claude@savoirweb.com">Claude Brisson</a>
 @see org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader
 @see org.apache.velocity.runtime.resource.loader.FileResourceLoader
 */
@Slf4j
public class WebappResourceLoader extends ResourceLoader {
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
  public void init (ExtProperties configuration) {
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
	public Reader getResourceReader (String name, String encoding) throws ResourceNotFoundException {
    if (StringUtils.isEmpty(name)){
      throw new ResourceNotFoundException("WebappResourceLoader: No template name provided");
    }
    if (name.startsWith("/")){
      name = name.substring(1).trim();
    }
		try {
			val is = servletContext.getResourceAsStream('/'+ name);// [META-INF/resources] /name
			if (is != null){ return rdr(is, encoding); }
		} catch (Exception e){
			log.debug("ServletContext.getResourceAsStream failed: web {}", name, e);
		}
		try {
			val is = ClickUtils.getResourceAsStream(name, getClass());// /resources/ name
			if (is != null){ return rdr(is, encoding); }
		} catch (Exception e){
			log.debug("Class.getResourceAsStream failed: /{}", name, e);
		}
		try {
			val is = ClickUtils.getResourceAsStream("templates/"+ name, getClass());// /resources/templates/ name ~ spring boot
			if (is != null){ return rdr(is, encoding); }
		} catch (Exception e){
			log.debug("Class.getResourceAsStream failed: templates/{}", name, e);
		}
		try {
			val is = ClickUtils.getResourceAsStream("static/"+ name, getClass());
			if (is != null){ return rdr(is, encoding); }
		} catch (Exception e){
			log.debug("Class.getResourceAsStream failed: static/ {}", name, e);
		}
		throw new ResourceNotFoundException(NOT_FOUND.formatted(name));
	}
	private Reader rdr (InputStream is, String encoding) throws ResourceNotFoundException {
		try {
			return buildReader(is, encoding);
		} catch (IOException e){// UnsupportedEncodingException
			throw new ResourceNotFoundException("Unknown encoding: "+ encoding, e);
		}
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

   @param resource Resource the resource to check
   @return long The time when the resource was last modified or 0 if the file can't be read
  */
  @Override
  public long getLastModified (Resource resource) {
		try {
			String rootPath = servletContext.getRealPath("/");
			if (rootPath != null) return new File(rootPath, resource.getName()).lastModified();// ? cachedFile.canRead()
		} catch (Exception e){
			log.warn("getLastModified: Could not get last modified time for {}", resource.getName(), e);
		}
		try {
			URL url = servletContext.getResource(resource.getName());// web /
			if (url != null) return url.openConnection().getLastModified();
		} catch (Exception e){
			log.warn("getLastModified: Could not get last modified time for {}", resource.getName(), e);
		}
		try {
			URL url = ClickUtils.getResource(resource.getName(), getClass());// /
			if (url != null) return url.openConnection().getLastModified();
		} catch (Exception e){
			log.warn("getLastModified: Could not get last modified time for {}", resource.getName(), e);
		}
		try {
			URL url = ClickUtils.getResource("templates/"+resource.getName(), getClass());
			if (url != null) return url.openConnection().getLastModified();
		} catch (Exception e){
			log.warn("getLastModified: Could not get last modified time for {}", resource.getName(), e);
		}
		try {
			URL url = ClickUtils.getResource("static/"+ resource.getName(), getClass());
			if (url != null) return url.openConnection().getLastModified();
		} catch (Exception e){
			log.warn("getLastModified: Could not get last modified time for {}", resource.getName(), e);
		}
		return 0;
	}
}