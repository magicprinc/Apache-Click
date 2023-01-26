package org.apache.click.util;

import org.apache.click.service.PropertyServiceTestCase;
import org.apache.click.servlet.MockServletContext;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Tests for PropertyUtils.
 */
public class PropertyUtilsTest extends PropertyServiceTestCase {

  @Override protected void setUp (){
    propertyService = new PropertyUtils();

    try {
      propertyService.onInit(new MockServletContext());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  protected void tearDown() {
    try {
      propertyService.onDestroy();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  final PropertyUtils pu = new PropertyUtils();

  /** Sanity checks for PropertyUtils. */
  public void testGetProperty() {
    try {
      pu.getValue(new Object(), "username", new HashMap<>());
      fail();
    } catch (Exception e) {
      assertTrue(true);
    }

    try {
      pu.getValue(new Object(), "class", new HashMap<>());
      assertTrue(true);
    } catch (Exception e) {
      fail();
    }

    ParentObject testObject = new ParentObject();
    Map<?, ?> cache = new HashMap<>();

    assertNull(pu.getValue(testObject, "name", cache));
    assertNull(pu.getValue(testObject, "value", cache));
    assertNull(pu.getValue(testObject, "date", cache));
    assertNull(pu.getValue(testObject, "child", cache));

    assertNull(pu.getValue(testObject, "name"));
    assertNull(pu.getValue(testObject, "value"));
    assertNull(pu.getValue(testObject, "date"));
    assertNull(pu.getValue(testObject, "child"));

    ParentObject parentObject =
        new ParentObject("malcolm", null, new Date(), Boolean.TRUE,
            new ChildObject("edgar", "medgar@avoka.com"));

    assertEquals("malcolm", pu.getValue(parentObject, "name",
        cache));
    assertNull(pu.getValue(parentObject, "value", cache));
    assertNotNull(pu.getValue(parentObject, "date", cache));
    assertNotNull(pu.getValue(parentObject, "valid", cache));
    assertEquals("edgar", pu.getValue(parentObject, "child.name",
        cache));
    assertEquals("medgar@avoka.com", pu.getValue(parentObject,
        "child.email", cache));


    assertEquals("malcolm", pu.getValue(parentObject, "name"));
    assertNull(pu.getValue(parentObject, "value"));
    assertNotNull(pu.getValue(parentObject, "date"));
    assertNotNull(pu.getValue(parentObject, "valid"));
    assertEquals("edgar", pu.getValue(parentObject, "child.name"));
    assertEquals("medgar@avoka.com", pu.getValue(parentObject,
        "child.email"));
  }

  /** Test that PropertyUtils can extract value from Map. */
  public void testMap() {
    Map<String, Object> map = new HashMap<>();
    map.put("name", "malcolm");

    assertEquals("malcolm", pu.getValue(map, "name"));
  }
}