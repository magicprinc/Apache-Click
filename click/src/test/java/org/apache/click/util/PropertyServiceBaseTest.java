package org.apache.click.util;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.click.service.PropertyServiceBase;
import org.apache.click.service.PropertyServiceTestCase;
import org.apache.click.servlet.MockServletContext;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.apache.click.util.ClickUtils.SETTER;
import static org.apache.click.util.ClickUtils.toPropertyName;

/** Tests for PropertyUtils → {@link PropertyServiceBase}. */
@SuppressWarnings("deprecation")  @Slf4j
public class PropertyServiceBaseTest extends PropertyServiceTestCase {

  public static class PropertyServiceReflection extends PropertyServiceBase {
    @SuppressWarnings({"unchecked", "rawtypes"}) @Override
    public void setValue (Object target, String propertyName, Object newValue){
      val name = propertyName.trim();

      if (target instanceof Map map && map.containsKey(name)){
        map.put(name, newValue);// replace existing
        return;
      }

      String basePart = name;
      String remainingPart = null;
      int baseIndex = name.indexOf('.');// foo.bar.zoo
      if (baseIndex >= 0){
        basePart = name.substring(0, baseIndex).trim(); // foo
        remainingPart = name.substring(baseIndex + 1).trim(); // bar.zoo
      }//i found else last part of the path ~ zoo

      if (remainingPart != null){
        Object next = getObjectPropertyValue(target, basePart);
        if (next == null){
          return;// do nothing: null breaks the path
        }
        setValue(next, remainingPart, newValue);
        return;
      }//else we are in the last .part of the path

      setObjectPropertyValue(target, basePart, newValue);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void setObjectPropertyValue (Object target, String name, Object newValue){
      if (target instanceof Map map){
        map.put(name, newValue);
        return;
      }
      val methodNameKey = new CacheKey(target, name+'!');//setter key
      // eventually, everything is in the cache
      val ao = REFLECTION_CACHE.asMap().get(methodNameKey);
      if (ao instanceof Method method){
        try {
          method.invoke(target, newValue);
          return;
        } catch (Exception e){
          log.warn("getObjectPropertyValue: failed to invoke cached setter {}={} to {} @ ({}) {}", name, method, newValue,
              methodNameKey.getSourceClass().getTypeName(), target, e);
        }
      } else if (ao instanceof Field f){
        try {
          f.set(target, newValue);
          return;
        } catch (Exception e){
          log.warn("getObjectPropertyValue: failed to set cached field {}={} to {} @ ({}) {}", name, f, newValue,
              methodNameKey.getSourceClass().getTypeName(), target,  e);
        }
      }

      try {
        set(methodNameKey, name, target, newValue);
        return;
      } catch (Exception ignore){}// NoSuchMethodException, InvocationTargetException, IllegalAccessException

      // final one - the reporting one
      try {
        invoke(methodNameKey, name, target, newValue);// as is ~ fooBar
      } catch (NoSuchMethodException e){
        throw new IllegalArgumentException("getObjectPropertyValue: No matching setter method found for property '"
            + name + "' on (" + methodNameKey.getSourceClass().getTypeName()+") "+target,  e);
      } catch (Exception e) {
        throw new IllegalArgumentException("getObjectPropertyValue: Error setting property '" + name + "' from ("+
            methodNameKey.getSourceClass().getTypeName()+") "+target,  e);
      }
    }
    private void invoke (CacheKey k, String methodName, Object target, Object newValue) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
      //Method method = k.getSourceClass().getMethod(methodName, newValue.getClass());// NoSuchMethodException todo get all methods, find one in list
      var setName = toPropertyName(SETTER, methodName);
      val methods = k.getSourceClass().getMethods();
      for (var method : methods){
        if (method.getParameterCount() == 1 && method.getReturnType() == Void.TYPE){
          var mn = method.getName();
          if (mn.equals(methodName) || mn.equals(setName)){
            // to do check param type isAssignable from newValue?
            method.invoke(target, newValue);// InvocationTargetException, IllegalAccessException
            REFLECTION_CACHE.put(k, method);// only if ^^ ok
            break;
          }
        }
      }
    }
    private void set (CacheKey k, String fieldName, Object target, Object newValue) throws IllegalAccessException, NoSuchFieldException, IllegalArgumentException {
      Field field = k.getSourceClass().getField(fieldName);// NoSuchMethodException
      field.set(target, newValue);// IllegalAccessException, IllegalArgumentException
      REFLECTION_CACHE.put(k, field);// only if ^^ ok
    }
  }//PropertyServiceReflection

  @Override protected void setUp (){
    propertyService = new PropertyServiceReflection();
    try {
      propertyService.onInit(new MockServletContext());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override protected void tearDown() {
    try {
      propertyService.onDestroy();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }


  /** Sanity checks for PropertyUtils. */
  @Override public void testGetProperty() {
    try {
      propertyService.getValue(new Object(), "username");
      fail();
    } catch (Exception e) {
      assertTrue(e.toString(),
          e.toString().startsWith(
"java.lang.IllegalArgumentException: getObjectPropertyValue: No matching getter method found for property 'username' on (java.lang.Object) java.lang.Object@"));
    }

    try {
      propertyService.getValue(new Object(), "class", new HashMap<>());
    } catch (Exception e) {
      fail();
    }

    ParentObject testObject = new ParentObject();
    Map<?, ?> cache = null;

    assertNull(propertyService.getValue(testObject, "name", cache));
    assertNull(propertyService.getValue(testObject, "value", null));
    assertNull(propertyService.getValue(testObject, "date", cache));
    assertNull(propertyService.getValue(testObject, "child", cache));

    assertNull(propertyService.getValue(testObject, "name"));
    assertNull(propertyService.getValue(testObject, "value"));
    assertNull(propertyService.getValue(testObject, "date"));
    assertNull(propertyService.getValue(testObject, "child"));

    ParentObject parentObject =
        new ParentObject("malcolm", null, new Date(), Boolean.TRUE,
            new ChildObject("edgar", "medgar@avoka.com"));

    assertEquals("malcolm", propertyService.getValue(parentObject, "name",
        cache));
    assertNull(propertyService.getValue(parentObject, "value", cache));
    assertNotNull(propertyService.getValue(parentObject, "date", cache));
    assertNotNull(propertyService.getValue(parentObject, "valid", cache));
    assertEquals("edgar", propertyService.getValue(parentObject, "child.name",
        cache));
    assertEquals("medgar@avoka.com", propertyService.getValue(parentObject,
        "child.email", cache));


    assertEquals("malcolm", propertyService.getValue(parentObject, "name"));
    assertNull(propertyService.getValue(parentObject, "value"));
    assertNotNull(propertyService.getValue(parentObject, "date"));
    assertNotNull(propertyService.getValue(parentObject, "valid"));
    assertEquals("edgar", propertyService.getValue(parentObject, "child.name"));
    assertEquals("medgar@avoka.com", propertyService.getValue(parentObject,
        "child.email"));
  }

  /** Test that PropertyUtils can extract value from Map. */
  @Override public void testMap() {
    Map<String, Object> map = new HashMap<>();
    map.put("name", "malcolm");

    assertEquals("malcolm", propertyService.getValue(map, "name"));
  }
}