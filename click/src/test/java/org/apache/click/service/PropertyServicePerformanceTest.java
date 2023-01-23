package org.apache.click.service;

import junit.framework.TestCase;
import org.apache.click.extras.spring.SPELPropertyService;
import org.apache.click.util.ChildObject;
import org.apache.click.util.ParentObject;
import org.apache.click.util.PropertyUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class PropertyServicePerformanceTest extends TestCase {

  // todo plus w/o reflection  !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! todo todo todo

  public void test_MVELService() throws Exception {
    loopIt(new MVELPropertyService());
  }

  public void test_OGNLService() throws Exception {
    loopIt(new OGNLPropertyService());
  }

  public void test_SpELService() throws Exception {
    loopIt(new SPELPropertyService());
  }

  public void test_Reflection() throws Exception {
    loopIt(new PropertyUtils());
  }


  public void loopIt (PropertyService ps) throws Exception {
    warmUp(ps);

    for (int i = 0; i < 100; i++) {
      Thread testThread = new Thread(new TestRunner(ps));
      testThread.start();
    }

    while (TestRunner.thread.get()<100){
      Thread.sleep(1_000);
    }

    System.err.printf("%1s cumulative  read test in %2d ms \n", ps.getClass().getSimpleName(), TestRunner.readDuration.get());
    System.err.printf("%1s cumulative write test in %2d ms \n", ps.getClass().getSimpleName(), TestRunner.writeDuration.get());
  }


  private static void warmUp (PropertyService ps) {
    long read = TestRunner.testPropertyServiceRead(ps);
    long write = TestRunner.testPropertyServiceWrite(ps);//warm-up
    System.err.printf("WWW %1s cumulative  read test in %2d ms \n", ps.getClass().getSimpleName(), read);
    System.err.printf("WWW %1s cumulative write test in %2d ms \n", ps.getClass().getSimpleName(), write);
    TestRunner.reset();
  }


  public static class TestRunner implements Runnable {
    public static final AtomicLong readDuration = new AtomicLong();
    public static final AtomicLong writeDuration = new AtomicLong();
    public static final AtomicInteger thread = new AtomicInteger();

    static void reset () {
      readDuration.set(0);  writeDuration.set(0);  thread.set(0);
    }

    final PropertyService propertyService;

    public TestRunner(PropertyService propertyService) {
      this.propertyService = propertyService;
    }

    public void run() {
      readDuration.addAndGet(testPropertyServiceRead(propertyService));
      writeDuration.addAndGet(testPropertyServiceWrite(propertyService));
      thread.incrementAndGet();
    }

    static long testPropertyServiceRead(PropertyService propertyService) {
      long start = System.currentTimeMillis();

      for (int i = 0; i < 10_000; i++) {
        performReadTest(propertyService);
      }
      return System.currentTimeMillis() - start;// duration millis
    }

    static long testPropertyServiceWrite(PropertyService propertyService) {
      long start = System.currentTimeMillis();

      for (int i = 0; i < 10000; i++) {
        performWriteTest(propertyService, i);
      }
      return System.currentTimeMillis() - start;// duration millis
    }
  }//TestRunner


  static final Map<?, ?> cache = new HashMap<>(); // NOT USED

  static void performReadTest(PropertyService propertyService) {
    ParentObject testObject = new ParentObject();

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

  static void performWriteTest(PropertyService propertyService, int index) {
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