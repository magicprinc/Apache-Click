package org.apache.click.service;

import org.mvel2.PropertyAccessor;
import org.mvel2.integration.PropertyHandler;
import org.mvel2.integration.PropertyHandlerFactory;
import org.mvel2.integration.VariableResolverFactory;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.util.Map;

/**
 * Provides an MVEL based property services.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class MVELPropertyService2 implements PropertyService {

  /**
   * @see PropertyService#onInit(ServletContext)
   *
   * @param servletContext the application servlet context
   * @throws IOException if an IO error occurs initializing the service
   */
  public void onInit (ServletContext servletContext) throws IOException {
    PropertyHandlerFactory.registerPropertyHandler(Map.class, new PropertyHandler() {
      @Override public Object getProperty (String name, Object contextObj, VariableResolverFactory variableFactory){
        return ((Map) contextObj).get(name);
      }

      @Override public Object setProperty (String name, Object contextObj, VariableResolverFactory variableFactory, Object value){
        return ((Map) contextObj).put(name, value);
      }
    });

  }

  /** @see PropertyService#onDestroy */
  public void onDestroy() {}

  /**
   * @see PropertyService#getValue(Object, String)
   *
   * @param source the source object
   * @param name the name of the property
   * @return the property value for the given source object and property name
   */
  public Object getValue (Object source, String name) {
    return PropertyAccessor.get(name.trim(), source);
  }

  /**
   * @see PropertyService#getValue(Object, String, Map)
   *
   * @param source the source object
   * @param name the name of the property
   * @param cache the cache of reflected property Method objects, do NOT modify
   * this cache
   * @return the property value for the given source object and property name
   */
  public Object getValue (Object source, String name, Map<?,?> cache) {
    return getValue(source, name);
  }

  /**
   * Set the named property value on the target object using the MVEL library.
   *
   * @see PropertyService#setValue(Object, String, Object)
   *
   * @param target the target object to set the property of
   * @param name the name of the property to set
   * @param value the property value to set
   */
  public void setValue (Object target, String name, Object value) {
    PropertyAccessor.set(target, name.trim(), value);
  }
}