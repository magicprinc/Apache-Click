package org.apache.click.service;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.util.Eval;
import lombok.val;
import org.codehaus.groovy.control.CompilerConfiguration;

import java.util.function.BiConsumer;

/**
 * Provides a <a href="https://groovy-lang.org/">Groovy</a> based property services.
 * <br>See <a href="https://docs.groovy-lang.org/latest/html/documentation/guide-integrating.html">Integrating Groovy in a Java application</a>
 * @see Eval#me(String, Object, String)
 */
public class GroovyPropertyService extends PropertyServiceBase {
//  private final Cache<String,Node> EXPRESSION_CACHE = Caffeine.newBuilder()
//      .maximumSize(10_000)
//      .expireAfterAccess(1, TimeUnit.HOURS).build();

  /** @see PropertyService#onDestroy() */
  @Override public void onDestroy (){
    super.onDestroy();

//    EXPRESSION_CACHE.invalidateAll();
  }


  final GroovyShell groovyShell = createGroovyShell();

  /** We don't use Binding (== no vars), so it's safe to share GroovyShell and Binding between threads:
   ××× binding.setVariable("t", target);  binding.setVariable("v", newValue) → Unsafe in multi-thread env! */
  private GroovyShell createGroovyShell (){
    val binding = new Binding();// not actually used but required in Scripts

    val groovyCompilerConfiguration = new CompilerConfiguration();
    groovyCompilerConfiguration.setSourceEncoding("UTF-8");
    groovyCompilerConfiguration.setScriptBaseClass(BiConsumerScript.class.getName());// to make Script implement BiConsumer

    return new GroovyShell(binding, groovyCompilerConfiguration);
  }

  /**
   * Set the named property value on the target object using the Groovy scripting capabilities.
   *
   * @see PropertyService#setValue(Object, String, Object)
   *
   * @param target the target object to set the property of
   * @param propertyName the name of the property to set
   * @param newValue the property value to set
   */
  @SuppressWarnings({"unchecked", "GrMethodMayBeStatic"}) @Override
  public void setValue (Object target, String propertyName, Object newValue){
    val property = propertyName.trim();
    val scriptFileName = property.replace('.', '_') + ".groovy";
    val groovyScript =
"@Override void accept (Object t, Object v){\n"+
"  t."+property +" = v;\n"+
"}";

    val script = groovyShell.parse(groovyScript, scriptFileName);

    val bc = (BiConsumer<Object,Object>) script;

    bc.accept(target, newValue);
  }
}