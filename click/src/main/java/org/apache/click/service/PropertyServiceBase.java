package org.apache.click.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.checkerframework.checker.nullness.qual.Nullable;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.apache.click.util.ClickUtils.GET_GETTER;
import static org.apache.click.util.ClickUtils.IS_GETTER;
import static org.apache.click.util.ClickUtils.toPropertyName;


/**
 * Provide REFLECTION-based property getter.
 *
 * !!! Setter removed 2023-01-30 (MVEL, OGNL, SpEL are used for setValue) !!!
 *
 */
@Slf4j
public abstract class PropertyServiceBase implements PropertyService {
  /**
   Provides a synchronized cache of get_value reflection methods, with support for multiple class loaders.
   private final ConcurrentMap‹CacheKey,AccessibleObject› GET_METHOD_CLASSLOADER_CACHE = new ConcurrentHashMap<>();
   */
  protected final Cache<CacheKey,AccessibleObject> REFLECTION_CACHE = Caffeine.newBuilder()
      .maximumSize(10_000)
      .expireAfterAccess(1, TimeUnit.HOURS)
      .build();

  /**
   * @see PropertyService#onInit(ServletContext)
   *
   * @param servletContext the application servlet context
   * @throws IOException if an IO error occurs initializing the service
   */
  @Override public void onInit (ServletContext servletContext) throws IOException {}


  @Override public void onDestroy () {
    REFLECTION_CACHE.invalidateAll();
  }

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
  @Override @Nullable public Object getValue (Object source, String propertyName){
    String name = propertyName.trim();

    if (source instanceof Map<?,?> map && map.containsKey(name)){
      return map.get(name);
    }

    String basePart = name;
    String remainingPart = null;
    int baseIndex = name.indexOf('.');// foo.bar.zoo
    if (baseIndex >= 0){
      basePart = name.substring(0, baseIndex).trim(); // foo
      remainingPart = name.substring(baseIndex + 1).trim(); // bar.zoo
    }//i found

    Object value = getObjectPropertyValue(source, basePart);

    if (remainingPart == null || value == null){// end of the road²
      return value;// real value or intermediate null

    } else {
      return getValue(value, remainingPart);// next object with rest of the path
    }
  }

  protected @Nullable AccessibleObject getCached (CacheKey key){
    return REFLECTION_CACHE.asMap().get(key);
  }

  /**
   * Return the property value for the given object and property name. This
   * method uses reflection internally to get the property value.
   *
   * @param source the source object
   * @param name the name of the property
   * @return the property value for the given source object and property name
   */
  @Nullable protected Object getObjectPropertyValue (Object source, String name){
    if (source instanceof Map<?,?> map && map.containsKey(name)){
      return map.get(name);
    }
    val methodNameKey = new CacheKey(source, name);
    // eventually, everything is in the cache
    val ao = getCached(methodNameKey);
    if (ao instanceof Method method){
      try {
        return method.invoke(source);
      } catch (Exception e){
        log.warn("getObjectPropertyValue: failed to invoke cached getter {}={} @ ({}) {}", name, method,
            methodNameKey.getSourceClass().getTypeName(), source,  e);
      }
    } else if (ao instanceof Field f){
      try {
        return f.get(source);
      } catch (Exception e){
        log.warn("getObjectPropertyValue: failed to get cached field {}={} @ ({}) {}", name, f,
            methodNameKey.getSourceClass().getTypeName(), source,  e);
      }
    }

    try {
      return invoke(methodNameKey, toPropertyName(GET_GETTER, name), source);
    } catch (Exception ignore){}// NoSuchMethodException, InvocationTargetException, IllegalAccessException

    try {
      return invoke(methodNameKey, toPropertyName(IS_GETTER, name), source);
    } catch (Exception ignore){}// NoSuchMethodException, InvocationTargetException, IllegalAccessException

    try {
      return get(methodNameKey, name, source);
    } catch (Exception ignore){}// NoSuchMethodException, InvocationTargetException, IllegalAccessException

    // final one - the reporting one
    try {
      return invoke(methodNameKey, name, source);// as is ~ fooBar
    } catch (NoSuchMethodException e){
      if (source instanceof Map<?,?> map){
        return null;// in Map such "field" can be easily created
      }
      throw new IllegalArgumentException("getObjectPropertyValue: No matching getter method found for property '"
          + name + "' on (" + methodNameKey.getSourceClass().getTypeName()+") "+source,  e);
    } catch (Exception e) {
      throw new IllegalArgumentException("getObjectPropertyValue: Error getting property '" + name + "' from ("+
        methodNameKey.getSourceClass().getTypeName()+") "+source,  e);
    }
  }

  private Object invoke (CacheKey k, String methodName, Object source) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = k.sourceClass.getMethod(methodName);// NoSuchMethodException
    Object returnValue = method.invoke(source);// InvocationTargetException, IllegalAccessException
    REFLECTION_CACHE.put(k, method);// only if ^^ ok
    return returnValue;
  }

  private Object get (CacheKey k, String fieldName, Object source) throws IllegalAccessException, NoSuchFieldException, IllegalArgumentException {
    Field field = k.sourceClass.getField(fieldName);// NoSuchMethodException
    Object returnValue = field.get(source);// IllegalAccessException, IllegalArgumentException
    REFLECTION_CACHE.put(k, field);// only if ^^ ok
    return returnValue;
  }

  /** <a href="http://www.javaspecialists.eu/archive/Issue134.html">
   See DRY Performance article by Kirk Pepperdine.</a> */
  protected static final class CacheKey {
    /** Class to encapsulate in cache key. */
    @Getter private final Class<?> sourceClass;
    /** Property to encapsulate in cache key. */
    private final String property;

    /**
     * Constructs a new CacheKey for the given object and property.
     *
     * @param source the object to build the cache key for
     * @param property the property to build the cache key for
     */
    public CacheKey (@NonNull Object source, @NonNull String property){
      this.sourceClass = source.getClass();  this.property = property;
    }//new

    /**
     * @see Object#equals(Object)
     *
     * @param o the object with which to compare this instance with
     * @return true if the specified object is the same as this object
     */
    @Override public boolean equals (Object o){
      if (this == o){
        return true;
      }
      if (!(o instanceof CacheKey that)){
        return false;
      }
      return sourceClass.equals(that.sourceClass) && property.equals(that.property);
    }

    @Override public String toString (){
      return sourceClass.getTypeName()+"."+property;
    }

    /**
     * @see Object#hashCode()
     *
     * @return a hash code value for this object.
     */
    @Override public int hashCode() {
      return sourceClass.hashCode()*31 + property.hashCode();
    }
  }//CacheKey
}