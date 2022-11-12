package org.apache.click.util;

import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.click.Context;
import org.apache.click.service.ConfigService;
import org.apache.click.service.PropertyService;
import org.checkerframework.checker.nullness.qual.Nullable;

import javax.servlet.ServletContext;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.apache.click.util.ClickUtils.GET_GETTER;
import static org.apache.click.util.ClickUtils.IS_GETTER;
import static org.apache.click.util.ClickUtils.toPropertyName;


/**
 * Provide property getter and setter utility methods.
 *
 * This class is provided for BACKWARD COMPATIBILITY.
 */
@Slf4j
public class PropertyUtils {
  /**
   Provides a synchronized cache of get_value reflection methods, with support for multiple class loaders.
   */
  private static final ConcurrentMap<ClassLoader, ConcurrentMap<CacheKey, Method>> GET_METHOD_CLASSLOADER_CACHE
    = new ConcurrentHashMap<>();


  /**
   * Return the property value for the given object and property name. This
   * method uses reflection internally to get the property value.
   * <p/>
   * This method is thread-safe, and caches reflected accessor methods in an
   * internal synchronized cache.
   * <p/>
   * If the given source object is a {@link Map} this method will simply
   * return the value for the given key name.
   *
   * @param source the source object
   * @param propertyName the name of the property
   * @return the property value for the given source object and property name
   */
  public static Object getValue (Object source, String propertyName) {
    return getValue(source, propertyName, ClickUtils.classLoaderCacheGET(GET_METHOD_CLASSLOADER_CACHE));
  }

  /**
   * Return the property value for the given object and property name. This
   * method uses reflection internally to get the property value.
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
   * @param source the source object
   * @param propertyName the name of the property
   * @param getMethodCache the cache of reflected property Method objects, do NOT modify this cache
   * @return the property value for the given source object and property name
   */
  @Nullable public static Object getValue (Object source, String propertyName, @NonNull Map<?,?> getMethodCache) {
    if (source instanceof Map<?,?> map) {
      return map.get(propertyName);
    }

    String basePart = propertyName;
    String remainingPart = null;
    int baseIndex = propertyName.indexOf('.');// foo.bar
    if (baseIndex != -1) {
      basePart = propertyName.substring(0, baseIndex);
      remainingPart = propertyName.substring(baseIndex + 1);
    }

    Object value = getObjectPropertyValue(source, basePart, getMethodCache);

    if (remainingPart == null || value == null) {
      return value;

    } else {
      return getValue(value, remainingPart, getMethodCache);
    }
  }

  /**
   * Return the property value for the given object and property name using the MVEL library.
   *
   * @param target the target object to set the property of
   * @param name the name of the property to set
   * @param newValue the property value to set
   */
  public static void setValue (Object target, String name, Object newValue) {
    getPropertyService().setValue(target, name, newValue);
  }



  /**
   * Return the property value for the given object and property name. This
   * method uses reflection internally to get the property value.
   *
   * @param source the source object
   * @param name the name of the property
   * @param cache the cache of reflected property Method objects
   * @return the property value for the given source object and property name
   */
  @Nullable private static Object getObjectPropertyValue (Object source, String name, Map<?,?> cache) {
    final CacheKey methodNameKey = new CacheKey(source, name);
    final Map<CacheKey, Method> getMethodCache = ClickUtils.castUnsafe(cache);

    Method method = getMethodCache.get(methodNameKey);
    if (method != null) {
      try {// eventually everything is in the cache
        return method.invoke(source);
      } catch (Exception e) {
        log.warn("getObjectPropertyValue: cached method {}={} failed @ ({}) {}", name, method,
            methodNameKey.getSourceClass().getName(), source,  e);
      }
    }
    try {
      return methodNameKey.invoke(toPropertyName(GET_GETTER, name), source, getMethodCache);
    } catch (Exception ignore) {// NoSuchMethodException, InvocationTargetException, IllegalAccessException
    }
    try {
      return methodNameKey.invoke(toPropertyName(IS_GETTER, name), source, getMethodCache);
    } catch (Exception ignore) {// NoSuchMethodException, InvocationTargetException, IllegalAccessException
    }
    // final one - the reporting one
    try {
      return methodNameKey.invoke(name, source, getMethodCache);// as is ~ fooBar
    } catch (NoSuchMethodException e) {
      throw new IllegalArgumentException("getObjectPropertyValue: No matching getter method found for property '"
          + name + "' on (" + methodNameKey.getSourceClass().getName()+") "+source,  e);
    } catch (Exception e) {
      throw new IllegalArgumentException("getObjectPropertyValue: Error getting property '" + name + "' from ("+
        methodNameKey.getSourceClass().getName()+") "+source,  e);
    }
  }

  private static PropertyService getPropertyService() {
    ServletContext servletContext = Context.getThreadLocalContext().getServletContext();

    ConfigService configService = ClickUtils.getConfigService(servletContext);

    return configService.getPropertyService();
  }

  /**
   <a href="http://www.javaspecialists.eu/archive/Issue134.html">
   See DRY Performance article by Kirk Pepperdine.
   </a>
   */
  public static final class CacheKey {
    /** Class to encapsulate in cache key. */
    @Getter
    private final Class<?> sourceClass;
    /** Property to encapsulate in cache key. */
    private final String property;

    /**
     * Constructs a new CacheKey for the given object and property.
     *
     * @param source the object to build the cache key for
     * @param property the property to build the cache key for
     */
    public CacheKey (@NonNull Object source, @NonNull String property) {
      this.sourceClass = source.getClass();
      this.property = property;
    }//new

    /**
     * @see Object#equals(Object)
     *
     * @param o the object with which to compare this instance with
     * @return true if the specified object is the same as this object
     */
    @Override
    public boolean equals (Object o) {
      if (this == o) {
        return true;
      }
      if (!(o instanceof CacheKey that)) {
        return false;
      }
      return sourceClass.equals(that.sourceClass) &&
        property.equals(that.property);
    }

    @Override public String toString () {
      return sourceClass.getName()+"."+property;
    }

    /**
     * @see Object#hashCode()
     *
     * @return a hash code value for this object.
     */
    @Override public int hashCode() {
      return sourceClass.hashCode()*31 + property.hashCode();
    }

    public Object invoke (String methodName, Object source, Map<CacheKey, Method> getMethodCache) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
      Method method = sourceClass.getMethod(methodName);// NoSuchMethodException
      Object returnValue = method.invoke(source);// InvocationTargetException, IllegalAccessException
      getMethodCache.put(this, method);// only if ^^ ok
      return returnValue;
    }
  }//CacheKey
}