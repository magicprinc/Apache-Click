package org.apache.click.service;

import junit.framework.TestCase;
import lombok.Data;
import lombok.val;
import org.apache.click.util.ChildObject;
import org.apache.click.util.ParentObject;
import org.apache.click.util.PropertyUtilsTest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class PropertyServiceTestCase extends TestCase {

  protected PropertyService propertyService = null;


  public void test_getValue () {
    try {
      propertyService.getValue(new Object(), "username", new HashMap<>());
      fail();
    } catch (Exception ignore){
    }
    try {
      propertyService.getValue(new Object(), "class", new HashMap<>());// GET - ok
    } catch (Exception e){
      fail();
    }

    ParentObject testObject = new ParentObject();
    Map<?, ?> cache = new HashMap<>();

    assertNull(propertyService.getValue(testObject, "name", cache));
    assertNull(propertyService.getValue(testObject, "value", cache));
    assertNull(propertyService.getValue(testObject, "date", cache));
    assertNull(propertyService.getValue(testObject, "child", cache));

    assertNull(propertyService.getValue(testObject, "name"));
    assertNull(propertyService.getValue(testObject, "value"));
    assertNull(propertyService.getValue(testObject, "date"));
    assertNull(propertyService.getValue(testObject, "child"));

    ParentObject parentObject = new ParentObject("malcolm", null, new Date(), Boolean.TRUE,
        new ChildObject("edgar", "medgar@avoka.com"));

    assertEquals("malcolm", propertyService.getValue(parentObject, "name", cache));
    assertNull(propertyService.getValue(parentObject, "value", cache));
    assertNotNull(propertyService.getValue(parentObject, "date", cache));
    assertNotNull(propertyService.getValue(parentObject, "valid", cache));
    assertEquals("edgar", propertyService.getValue(parentObject, "child.name", cache));
    assertEquals("medgar@avoka.com", propertyService.getValue(parentObject, "child.email", cache));


    assertEquals("malcolm", propertyService.getValue(parentObject, "name"));
    assertNull(propertyService.getValue(parentObject, "value"));
    assertNotNull(propertyService.getValue(parentObject, "date"));
    assertNotNull(propertyService.getValue(parentObject, "valid"));
    assertEquals("edgar", propertyService.getValue(parentObject, "child.name"));
    assertEquals("medgar@avoka.com", propertyService.getValue(parentObject, "child.email"));

    Map<String, Object> map = new HashMap<>();
    map.put("name", "malcolm");

    assertEquals("malcolm", propertyService.getValue(map, "name"));
  }


  public void test_setValue () {
    ParentObject parentObject = new ParentObject();

    propertyService.setValue(parentObject, "name", "malcolm");
    assertEquals("malcolm", parentObject.getName());

    propertyService.setValue(parentObject, "value", "value");
    assertEquals("value", parentObject.getValue());

    Date date = new Date();
    propertyService.setValue(parentObject, "date", date);
    assertEquals(date, parentObject.getDate());

    propertyService.setValue(parentObject, "valid", true);
    assertEquals(Boolean.TRUE, parentObject.getValid());

    Map<String, Object> map = new HashMap<>();
    propertyService.setValue(map, "name", "malcolm");
    assertEquals("malcolm", map.get("name"));

    parentObject.setChild(new ChildObject());

    propertyService.setValue(parentObject, "child.name", "malcolm");
    assertEquals("malcolm", parentObject.getChild().getName());
  }

  public static class Demo {
    public final Map<String,Object> map = new HashMap<>();

    public Demo () {
      map.put("foo", 42);
    }

    public Demo demo;

    static class Kaka {}
  }

  public void testSimple () {
    var d = new Demo();
    d.demo = new Demo();

    propertyService.setValue(d, "map.bar", 17);
    assertEquals(17, propertyService.getValue(d, "map.bar"));
    propertyService.setValue(d, "map.zoo", null);// otherwise SpEL will complain "Map doesn't have property zoo"
    assertNull(propertyService.getValue(d, "map.zoo"));
    assertEquals(42, propertyService.getValue(d, "map.foo"));

    d.demo.map.put("L3", new Deep(new Deep(new Deep(null))));
    d.map.put("m", d);

    assertEquals("Vasya", propertyService.getValue(d, "map.m.demo.map.L3.left.right.child.name"));
  }

  @Data
  public static class Deep {
    private Deep left;
    private Deep right;
    private ChildObject child = new ChildObject("Vasya", "a@b.c");

    public Deep (Deep x) {
      this.left = x;  right = x;
    }
  }

  public void testClassName () {
    val d = new Demo(){
      @Override public int hashCode () {
        return 0;
      }
    };
    // org.apache.click.service.PropertyServiceTestCase$1@0
    assertTrue(d.toString().startsWith("org.apache.click.service.PropertyServiceTestCase$"));
    // org.apache.click.service.PropertyServiceTestCase$1
    assertTrue(d.getClass().getTypeName().startsWith("org.apache.click.service.PropertyServiceTestCase$"));

    assertEquals("org.apache.click.service.PropertyServiceTestCase$Demo$Kaka", Demo.Kaka.class.getTypeName());
  }


  public void testGetProperty() {
    try {
      propertyService.getValue(new Object(), "username", new HashMap<>());
      fail();
    } catch (Exception e) {
      assertTrue(true);
    }

    try {
      propertyService.getValue(new Object(), "class", new HashMap<>());
      assertTrue(true);
    } catch (Exception e) {
      fail();
    }

    ParentObject testObject = new ParentObject();
    Map<?, ?> cache = null;

    assertNull(propertyService.getValue(testObject, "name", cache));
    assertNull(propertyService.getValue(testObject, "value", cache));
    assertNull(propertyService.getValue(testObject, "date", cache));
    assertNull(propertyService.getValue(testObject, "child", cache));

    assertNull(propertyService.getValue(testObject, "name"));
    assertNull(propertyService.getValue(testObject, "value"));
    assertNull(propertyService.getValue(testObject, "date"));
    assertNull(propertyService.getValue(testObject, "child"));

    ParentObject parentObject = new ParentObject("malcolm", null, new Date(), Boolean.TRUE,
            new ChildObject("edgar", "medgar@avoka.com"));

    assertEquals("malcolm", propertyService.getValue(parentObject, "name",
        cache));
    assertNull(propertyService.getValue(parentObject, "value", cache));
    assertNotNull(propertyService.getValue(parentObject, "date", cache));
    assertNotNull(propertyService.getValue(parentObject, "valid", cache));
    assertEquals("edgar", propertyService.getValue(parentObject, "child.name", cache));
    assertEquals("medgar@avoka.com", propertyService.getValue(parentObject, "child.email", cache));


    assertEquals("malcolm", propertyService.getValue(parentObject, "name"));
    assertNull(propertyService.getValue(parentObject, "value"));
    assertNotNull(propertyService.getValue(parentObject, "date"));
    assertNotNull(propertyService.getValue(parentObject, "valid"));
    assertEquals("edgar", propertyService.getValue(parentObject, "child.name"));
    assertEquals("medgar@avoka.com", propertyService.getValue(parentObject, "child.email"));
  }

  /** Test that PropertyUtils can extract value from Map. */
  public void testMap() {
    Map<String, Object> map = new HashMap<>();
    map.put("name", "malcolm");
    map.put("aa.bbb.cccc", 42);

    assertEquals("malcolm", propertyService.getValue(map, "name"));
    if (this instanceof PropertyUtilsTest){
      assertEquals(42, propertyService.getValue(map, "aa.bbb.cccc"));
    }//only our Reflection getValue can do it
  }

}