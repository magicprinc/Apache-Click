package org.apache.click.util;

import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.click.Context;
import org.apache.click.service.ConfigService;
import org.apache.click.service.PropertyService;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.mvel2.MVEL;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
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
public class PropertyUtils implements PropertyService {
  /**
   Provides a synchronized cache of get_value reflection methods, with support for multiple class loaders.
   */
  private static final ConcurrentMap<CacheKey,AccessibleObject> GET_METHOD_CLASSLOADER_CACHE = new ConcurrentHashMap<>();


  @Override public void onInit (ServletContext servletContext) throws IOException {}
  @Override public void onDestroy (){}



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
    int baseIndex = name.indexOf('.');// foo.bar
    if (baseIndex >= 0){
      basePart = name.substring(0, baseIndex).trim(); // foo
      remainingPart = name.substring(baseIndex + 1).trim(); // bar
    }//i found

    Object value = getObjectPropertyValue(source, basePart);

    if (remainingPart == null || value == null){
      return value;

    } else {
      return getValue(value, remainingPart);
    }
  }

  /**
   * Return the property value for the given object and property name using the MVEL library.
   *
   * @param target the target object to set the property of
   * @param name the name of the property to set
   * @param value the property value to set
   */
  @Override public void setValue (Object target, String name, Object value) {
    if (target == null){ return;}

    // "SomeObj.propertyName = value"
    val simpleName = target.getClass().getSimpleName();
    val expression = simpleName +"."+ name.trim() +"=value";

    Serializable compiledExpression = MCACHE.computeIfAbsent(expression, s->MVEL.compileExpression(expression));

    final Map<String,Object> vars = new HashMap<>();
    vars.put(simpleName, target);// "SomeObj" → real someObj object
    vars.put("value", value);    // "value"   → real value

    MVEL.executeExpression(compiledExpression, vars);
  }

  final ConcurrentHashMap<String,Serializable> MCACHE = new ConcurrentHashMap<>();


  /**
   * Return the property value for the given object and property name. This
   * method uses reflection internally to get the property value.
   *
   * @param source the source object
   * @param name the name of the property
   * @return the property value for the given source object and property name
   */
  @Nullable private static Object getObjectPropertyValue (Object source, String name){
    if (source instanceof Map<?,?> map && map.containsKey(name)){
      return map.get(name);
    }

    final CacheKey methodNameKey = new CacheKey(source, name);

    val ao = GET_METHOD_CLASSLOADER_CACHE.get(methodNameKey);
    if (ao instanceof Method method) {
      try {// eventually everything is in the cache
        return method.invoke(source);
      } catch (Exception e){
        log.warn("getObjectPropertyValue: cached method {}={} failed @ ({}) {}", name, method,
            methodNameKey.getSourceClass().getName(), source,  e);
      }
    } else if (ao instanceof Field f){
      try {// eventually everything is in the cache
        return f.get(source);
      } catch (Exception e){
        log.warn("getObjectPropertyValue: cached field {}={} failed @ ({}) {}", name, f,
            methodNameKey.getSourceClass().getName(), source,  e);
      }
    }

    try {
      return methodNameKey.invoke(toPropertyName(GET_GETTER, name), source);
    } catch (Exception ignore){}// NoSuchMethodException, InvocationTargetException, IllegalAccessException

    try {
      return methodNameKey.invoke(toPropertyName(IS_GETTER, name), source);
    } catch (Exception ignore){}// NoSuchMethodException, InvocationTargetException, IllegalAccessException

    try {
      return methodNameKey.get(name, source);
    } catch (Exception ignore){}// NoSuchMethodException, InvocationTargetException, IllegalAccessException

    // final one - the reporting one
    try {
      return methodNameKey.invoke(name, source);// as is ~ fooBar
    } catch (NoSuchMethodException e){
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

    public Object invoke (String methodName, Object source) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
      Method method = sourceClass.getMethod(methodName);// NoSuchMethodException
      Object returnValue = method.invoke(source);// InvocationTargetException, IllegalAccessException
      GET_METHOD_CLASSLOADER_CACHE.put(this, method);// only if ^^ ok
      return returnValue;
    }

    public Object get (String fieldName, Object source) throws IllegalAccessException, NoSuchFieldException, IllegalArgumentException {
      Field field = sourceClass.getField(fieldName);// NoSuchMethodException
      Object returnValue = field.get(source);// IllegalAccessException, IllegalArgumentException
      GET_METHOD_CLASSLOADER_CACHE.put(this, field);// only if ^^ ok
      return returnValue;
    }
  }//CacheKey
}