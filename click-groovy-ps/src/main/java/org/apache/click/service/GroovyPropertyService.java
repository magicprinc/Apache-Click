package org.apache.click.service;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.util.Eval;
import lombok.val;

/**
 * Provides an OGNL based property services.
 * @see Eval#me(String, Object, String)
 */
public class GroovyPropertyService extends PropertyServiceBase {
  /** OGNL Expression cache with support for multiple classloader caching
   private static final ConcurrentMap‹String,Node› EXPRESSION_CACHE = new ConcurrentHashMap
   */
//  private final Cache<String,Node> EXPRESSION_CACHE = Caffeine.newBuilder()
//      .maximumSize(10_000)
//      .expireAfterAccess(1, TimeUnit.HOURS).build();



  /** @see PropertyService#onDestroy() */
  @Override public void onDestroy (){
    super.onDestroy();

//    EXPRESSION_CACHE.invalidateAll();
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
  @Override public void setValue (Object target, String propertyName, Object newValue){
    val name = propertyName.trim();

    Binding b = new Binding();
    b.setVariable("t", target);
    b.setVariable("v", newValue);
    GroovyShell sh = new GroovyShell(b);
    sh.evaluate("t."+name+"=v", name.replace('.','_')+".groovy");
  }

}