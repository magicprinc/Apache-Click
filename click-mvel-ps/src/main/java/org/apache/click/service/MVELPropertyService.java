package org.apache.click.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.val;
import org.mvel2.MVEL;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Provides an MVEL based property services.
 *
 * There is perf.comparison of MVEL, OGNL, SpEL and self-made reflection in {@link PropertyServiceBase} in tests!
 */
public class MVELPropertyService extends PropertyServiceBase {
  /** Expression cache with support for multiple classloader caching */
  final Cache<String,Serializable> EXPRESSION_CACHE = Caffeine.newBuilder()
      .maximumSize(10_000)
      .expireAfterAccess(1, TimeUnit.HOURS)
      .build();


  /** @see PropertyService#onDestroy */
  @Override public void onDestroy (){
    super.onDestroy();
    EXPRESSION_CACHE.invalidateAll();
  }


  protected Serializable cacheOrParse (Object root, String name){
    return EXPRESSION_CACHE.asMap()
        .computeIfAbsent(
            PropertyService.distinctClassName(root)+'#'+name,
            s->MVEL.compileExpression(name));
  }

  /**
   * Set the named property value on the target object using the MVEL library.
   *
   * @see PropertyService#setValue(Object, String, Object)
   *
   * @param target the target object to set the property of
   * @param name the name of the property to set
   * @param newValue the property value to set
   */
  @Override public void setValue (Object target, String name, @Nullable Object newValue){
    if (target == null || name == null){ return;}

    // "SomeObj.propertyName = value"
    val expression = "obj."+ name.trim() +"=vvv";

    Serializable compiledExpression = cacheOrParse(target, expression);

    final Map<String,Object> vars = new HashMap<>();
    vars.put("obj", target);   // "SomeObj" → real someObj object
    vars.put("vvv", newValue); // "value"   → real value

    MVEL.executeExpression(compiledExpression, vars);
  }
}