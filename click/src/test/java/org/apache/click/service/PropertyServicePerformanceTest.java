package org.apache.click.service;

import junit.framework.TestCase;
import org.apache.click.util.ChildObject;
import org.apache.click.util.ParentObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class PropertyServicePerformanceTest extends TestCase {

  plus w/o reflection

  public void test_MVELService() throws Exception {
    PropertyService ps = new MVELPropertyService();
    TestRunner testRunner = new TestRunner(ps, false);
    testRunner.testPropertyServiceRead(ps);
    testRunner.testPropertyServiceWrite(ps);//warm-up
    TestRunner.reset();

    for (int i = 0; i < 100; i++) {
      Thread testThread = new Thread(new TestRunner(ps, i == 49));
      testThread.start();
    }

    Thread.sleep(10_000);
  }

  public void test_OGNLService() throws Exception {
    TestRunner.reset();

    PropertyService ps = new OGNLPropertyService();

    for (int i = 0; i < 50; i++) {
      Thread testThread = new Thread(new TestRunner(ps, i == 49));
      testThread.start();
    }

    Thread.sleep(10_000);
  }


  public class TestRunner implements Runnable {
    public static final AtomicLong readDuration = new AtomicLong();
    public static final AtomicLong writeDuration = new AtomicLong();

    static void reset () {
      readDuration.set(0);
      writeDuration.set(0);
    }

    final PropertyService propertyService;
    final boolean print;

    public TestRunner(PropertyService propertyService, boolean print) {
      this.propertyService = propertyService;
      this.print = print;
    }

    public void run() {
      long read  = readDuration.addAndGet(testPropertyServiceRead(propertyService));
      long write = writeDuration.addAndGet(testPropertyServiceWrite(propertyService));

      if (print) {
        System.err.printf("%1s cumulative  read test in %2d ms \n", propertyService.getClass().getSimpleName(), read);
        System.err.printf("%1s cumulative write test in %2d ms \n", propertyService.getClass().getSimpleName(), write);
      }
    }

    long testPropertyServiceRead(PropertyService propertyService) {
      long start = System.currentTimeMillis();

      for (int i = 0; i < 10_000; i++) {
        performReadTest(propertyService);
      }
      return System.currentTimeMillis() - start;// duration millis
    }

    long testPropertyServiceWrite(PropertyService propertyService) {
      long start = System.currentTimeMillis();

      for (int i = 0; i < 10000; i++) {
        performWriteTest(propertyService, i);
      }
      return System.currentTimeMillis() - start;// duration millis
    }

  }//TestRunner




  private void performReadTest(PropertyService propertyService) {
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

    ParentObject parentObject =
      new ParentObject("malcolm", null, new Date(), Boolean.TRUE,
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

  private void performWriteTest(PropertyService propertyService, int index) {
    ParentObject parentObject = new ParentObject();

    propertyService.setValue(parentObject, "name", "malcolm" + index);
    assertEquals("malcolm" + index, parentObject.getName());

    propertyService.setValue(parentObject, "value", "value" + index);
    assertEquals("value" + index, parentObject.getValue());

    Date date = new Date();
    propertyService.setValue(parentObject, "date", date);
    assertEquals(date, parentObject.getDate());

    propertyService.setValue(parentObject, "valid", true);
    assertEquals(Boolean.TRUE, parentObject.getValid());

    Map<String, Object> map = new HashMap<>();
    propertyService.setValue(map, "name", "malcolm" + index);
    assertEquals("malcolm" + index, map.get("name"));

    parentObject.setChild(new ChildObject());

    propertyService.setValue(parentObject, "child.name", "malcolm" + index);
    assertEquals("malcolm" + index, parentObject.getChild().getName());
  }
}