package org.apache.click.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.SneakyThrows;
import lombok.val;
import ognl.MemberAccess;
import ognl.Node;
import ognl.Ognl;
import ognl.OgnlContext;
import ognl.OgnlException;
import ognl.TypeConverter;

import java.util.concurrent.TimeUnit;

/**
 * Provides an OGNL based property services.
 */
public class OGNLPropertyService extends PropertyServiceBase {
  /** OGNL Expression cache with support for multiple classloader caching
   private static final ConcurrentMap‹String,Node› EXPRESSION_CACHE = new ConcurrentHashMap
   */
  private final Cache<String,Node> EXPRESSION_CACHE = Caffeine.newBuilder()
      .maximumSize(10_000)
      .expireAfterAccess(1, TimeUnit.HOURS).build();

  /** The OGNL object member accessor. */
  protected MemberAccess memberAccess = new DefaultMemberAccess();

  /** The OGNL data marshalling Type Converter instance. */
  protected final TypeConverter typeConverter = new OGNLTypeConverter();


  /** @see PropertyService#onDestroy() */
  @Override public void onDestroy (){
    super.onDestroy();

    EXPRESSION_CACHE.invalidateAll();
  }


  private OgnlContext createContext (Object root){
    return (OgnlContext) Ognl.createDefaultContext(root, memberAccess, null, typeConverter);// Map
  }

  private Node cacheOrParse (Object root, String name) {
    return EXPRESSION_CACHE.asMap().computeIfAbsent(PropertyService.distinctClassName(root)+'#'+name,
        k -> ognlParseExpression(name));
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
  @Override public void setValue (Object target, String propertyName, Object newValue) {
    try {
      val name = propertyName.trim();
      val ognlContext = createContext(target);

      //ConcurrentMap<String, Object> expressionCache = ClickUtils.classLoaderCacheGET(EXPRESSION_CL_CACHE);// expressionCache→ConcurrentMap<String,Object>
      Node expression = cacheOrParse(target, name);

      Ognl.setValue(expression, ognlContext, target, newValue);

    } catch (Exception e){// OgnlException | IllegalCallerException
      throw new IllegalArgumentException("Ognl.parseExpression/setValue failed: ("+target.getClass().getName()+
        ") "+target+"."+propertyName+" = ("+ newValue.getClass().getName()+") "+newValue,
        e instanceof OgnlException ? e : e.getCause());
    }
  }

  @SneakyThrows
  static Node ognlParseExpression (String propertyName){
    return (Node) Ognl.parseExpression(propertyName);// OgnlException
  }
}