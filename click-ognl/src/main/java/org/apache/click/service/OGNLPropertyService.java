package org.apache.click.service;

import ognl.DefaultMemberAccess;
import ognl.MemberAccess;
import ognl.Ognl;
import ognl.OgnlException;
import ognl.TypeConverter;
import org.apache.click.util.ClickUtils;
import org.apache.click.util.PropertyUtils;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Provides an OGNL based property services.
 */
public class OGNLPropertyService implements PropertyService {
  /** OGNL Expression cache with support for multiple classloader caching */
  private static final ConcurrentMap<ClassLoader, ConcurrentMap<String,Object>>
    EXPRESSION_CL_CACHE = new ConcurrentHashMap<>();

  /** The OGNL object member accessor. */
  protected MemberAccess memberAccess;

  /** The OGNL data marshalling Type Converter instance. */
  protected final TypeConverter typeConverter = new OGNLTypeConverter();


  /**
   * @see PropertyService#onInit(ServletContext)
   *
   * @param servletContext the application servlet context
   * @throws IOException if an IO error occurs initializing the service
   */
  public void onInit(ServletContext servletContext) throws IOException {}

  /** @see PropertyService#onDestroy() */
  public void onDestroy() {}

  /**
   * Return the property value for the given object and property name.
   * <p/>
   * For performance and backward compatibility reasons this method uses
   * reflection internally to get the property value.
   * <p/>
   * This method is thread-safe, and caches reflected accessor methods in an
   * internal synchronized cache
   * <p/>
   * If the given source object is a <tt>Map</tt> this method will simply
   * return the value for the given key name.
   *
   * @see PropertyService#getValue(Object, String)
   *
   * @param source the source object
   * @param name the name of the property
   * @return the property value for the given source object and property name
   */
  public Object getValue(Object source, String name) {
    return PropertyUtils.getValue(source, name);
  }

  /**
   * Return the property value for the given object and property name.
   * <p/>
   * For performance and backward compatibility reasons this method uses
   * reflection internally to get the property value.
   * <p/>
   * This method uses reflection internally to get the property value.
   * <p/>
   * This method caches the reflected property methods in the given Map cache.
   * You must NOT modify the cache. Also note cache is ONLY valid for the
   * current thread, as access to the cache is not synchronized. If you need
   * multithreaded access to shared cache use a thread-safe Map object, such
   * as <tt>Collections.synchronizedMap(new HashMap())</tt>.
   * <p/>
   * If the given source object is a <tt>Map</tt> this method will simply
   * return the value for the given key name.
   *
   * @see PropertyService#getValue(Object, String, Map)
   *
   * @param source the source object
   * @param name the name of the property
   * @param cache the cache of reflected property Method objects, do NOT modify
   * this cache
   * @return the property value for the given source object and property name
   */
  public Object getValue(Object source, String name, Map<?,?> cache) {
    return PropertyUtils.getValue(source, name, cache);
  }

  /**
   * Set the named property value on the target object using the OGNL library.
   *
   * @see PropertyService#setValue(Object, String, Object)
   *
   * @param target the target object to set the property of
   * @param propertyName the name of the property to set
   * @param newValue the property value to set
   */
  public void setValue (Object target, String propertyName, Object newValue) {
    try {
      Map<?, ?> ognlContext = Ognl.createDefaultContext(target, null, typeConverter, getMemberAccess());

      ConcurrentMap<String, Object> expressionCache = ClickUtils.classLoaderCacheGET(EXPRESSION_CL_CACHE);// expressionCache→ConcurrentMap<String,Object>
      Object expression = expressionCache.computeIfAbsent(propertyName,
        k -> ognlParseExpression(propertyName));

      Ognl.setValue(expression, ognlContext, target, newValue);

    } catch (OgnlException | IllegalCallerException e) {
      throw new IllegalArgumentException("Ognl.parseExpression/setValue failed: ("+target.getClass().getName()+
        ") "+target+"."+propertyName+" = ("+ newValue.getClass().getName()+") "+newValue,
        e instanceof OgnlException ? e : e.getCause());
    }
  }


  static Object ognlParseExpression (String propertyName) {
    try {
      return Ognl.parseExpression(propertyName);
    } catch (OgnlException e) {
      throw new IllegalCallerException(propertyName, e);
    }
  }

  /**
   * Return the OGNL object MemberAccess instance.
   *
   * @return the OGNL object MemberAccess instance
   */
  protected MemberAccess getMemberAccess() {
    if (memberAccess == null) {
      memberAccess = new DefaultMemberAccess(true);
    }
    return memberAccess;
  }
}