package org.apache.click.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.val;
import org.apache.click.util.PropertyUtils;
import org.mvel2.MVEL;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Provides an MVEL based property services.
 *
 * TODO compare MVEL, OGNL and self-made reflection in {@link PropertyUtils}
 */
public class MVELPropertyService implements PropertyService {
  /** Expression cache with support for multiple classloader caching */
  final Cache<String,Serializable> EXPRESSION_CACHE =
      Caffeine.newBuilder().maximumSize(10_000).expireAfterAccess(1, TimeUnit.HOURS).build();


  /**
   * @see PropertyService#onInit(ServletContext)
   *
   * @param servletContext the application servlet context
   * @throws IOException if an IO error occurs initializing the service
   */
  @Override public void onInit (ServletContext servletContext) throws IOException {}

  /** @see PropertyService#onDestroy */
  @Override public void onDestroy() {
    EXPRESSION_CACHE.invalidateAll();
  }

  /**
   * @see PropertyService#getValue(Object, String)
   *
   * @param source the source object
   * @param propertyName the name of the property
   * @return the property value for the given source object and property name
   */
  @Override public Object getValue (Object source, String propertyName){
    if (source == null){ return null;}

    // return PropertyUtils.getValue(source, name);
    // "SomeObj.propertyName = value"
    val name = "this.?" + propertyName.trim().replace(".", ".?");// null-safe property access

    Serializable compiledExpression = cacheOrParse(source, name);

    return MVEL.executeExpression(compiledExpression, source);
  }


  protected Serializable cacheOrParse (Object root, String name){
    return EXPRESSION_CACHE.asMap().computeIfAbsent(PropertyService.distinctClassName(root)+'#'+name,
        s->MVEL.compileExpression(name));
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
  @Override public void setValue (Object target, String name, Object value){
    if (target == null){ return;}

    // "SomeObj.propertyName = value"
    val simpleName = target.getClass().getSimpleName();
    val expression = simpleName +"."+ name.trim() +"=value";

    Serializable compiledExpression = cacheOrParse(target, expression);

    final Map<String,Object> vars = new HashMap<>();
    vars.put(simpleName, target);// "SomeObj" → real someObj object
    vars.put("value", value);    // "value"   → real value

    MVEL.executeExpression(compiledExpression, vars);
  }
}