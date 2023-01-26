package org.apache.click.service;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.util.Map;

/**
 * Provide a property service with property get and set utility methods.
 *
 * <h3>Configuration</h3>
 * The default {@link PropertyService} implementation was OGNLPropertyService for
 * backward compatibility reasons. Please note {@link MVELPropertyService} provides
 * better property write performance than the OGNL property service.
 * <p/>
 * You can instruct Click to use a different implementation by adding
 * the following element to your <tt>click.xml</tt> configuration file.
 *
 * <pre class="codeConfig">
 * &lt;?xml version="1.0" encoding="UTF-8" standalone="yes"?&gt;
 * &lt;click-app charset="UTF-8"&gt;
 *
 *     &lt;pages package="org.apache.click.examples.page"/&gt;
 *
 *     &lt;<span class="red">property-service</span> classname="<span class="blue">org.apache.click.service.MVELPropertyService</span>"/&gt;
 *
 * &lt;/click-app&gt; </pre>
 * see OGNLPropertyService
 * @see MVELPropertyService
 * @see org.apache.click.util.PropertyUtils
 */
public interface PropertyService {

  /**
   * Initialize the PropertyService with the given application configuration
   * service instance.
   * <p/>
   * This method is invoked after the PropertyService has been constructed.
   *
   * @param servletContext the application servlet context
   * @throws IOException if an IO error occurs initializing the service
   */
  void onInit (ServletContext servletContext) throws IOException;

  /** Destroy the PropertyService. */
  void onDestroy ();

  /**
   * Return the property value for the given object and property name.
   *
   * @param source the source object
   * @param name the name of the property
   * @return the property value for the given source object and property name
   */
  Object getValue (Object source, String name);

  /**
   * Return the property value for the given object and property name. The
   * cache parameter may be used by the implementing service to provide
   * improved performance.
   *
   * @param source the source object
   * @param name the name of the property
   * @param cache the cache of reflected property Method objects, do NOT modify this cache
   * @return the property value for the given source object and property name
   */
  default Object getValue (Object source, String name, Map<?,?> cache){
    return getValue(source, name);
  }

  /**
   * Set the named property value on the target object.
   *
   * @param target the target object to set the property of
   * @param name the name of the property to set
   * @param value the property value to set
   */
  void setValue (Object target, String name, Object value);


  static String distinctClassName (Object root){
    var name = root.getClass().getName();
    int i = name.lastIndexOf('$');
    if (i<0){ return name;}

    if (isNumber(name.substring(i + 1))){
      return name.substring(0, i);// anonymous classes doesn't (usually) have properties
    } else {
      return name; // nested class Foo.Bar
    }
  }

  private static boolean isNumber (String s){
    for (int i=0, len=s.length(); i<len; i++){
      if (!Character.isDigit(s.charAt(i)) ){ return false;}
    }
    return true;
  }
}