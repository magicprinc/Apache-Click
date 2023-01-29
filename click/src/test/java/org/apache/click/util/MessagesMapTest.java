package org.apache.click.util;

import junit.framework.TestCase;
import org.apache.click.Context;
import org.apache.click.Control;
import org.apache.click.MockContext;
import org.apache.click.service.XmlConfigService;

import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;

/** Tests for {@link MessagesMap} */
public class MessagesMapTest extends TestCase {

  /** Test MessagesMap for English Locale. */
  @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
  public void testEnglishLocale() {
    MockContext.initContext(Locale.ENGLISH);

    // Load click-control.properties into map
    Map<String,String> map = Context.getThreadLocalContext().createMessagesMap(getClass(), Control.CONTROL_MESSAGES);

    assertFalse(map.isEmpty());
    assertTrue(map.size() > 28);

    assertTrue(map.containsKey("table-first-label"));
    assertEquals("First", map.get("table-first-label"));

    assertFalse(map.containsKey("First"));
    try {
      assertNull(map.get("First"));
      fail();
    } catch (MissingResourceException mre) {
      assertTrue(true);
    }
  }

  /**
   * Test MessagesMap works for alternative Locales.
   */
  @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
  public void testCanadianLocale() {
    MockContext.initContext(Locale.CANADA);

    // Load click-control.properties into map
    Map<String,String> map = (MessagesMap) Context.getThreadLocalContext().createMessagesMap(getClass(), Control.CONTROL_MESSAGES);

    assertFalse(map.isEmpty());
    assertTrue(map.size() > 28);

    assertTrue(map.containsKey("table-first-label"));
    assertEquals("First", map.get("table-first-label"));

    assertFalse(map.containsKey("First"));
    try {
      assertNull(map.get("First"));
      fail();
    } catch (MissingResourceException mre) {
      assertTrue(true);
    }
  }

  /**
   * Tests MessagesMap behavior when properties are missing.
   */
  @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
  public void testMissingResourceCaching() {
    MockContext.initContext(Locale.ENGLISH);

    // Load the non existing missingResource.properties into map
    MessagesMap map = (MessagesMap) Context.getThreadLocalContext().createMessagesMap(Object.class, "missingResource");

    assertTrue(map.isEmpty());
    assertEquals(0, map.size());

    assertFalse(map.containsKey("table-first-label"));

    try {
      assertNull(map.get("table-first-label"));
      fail();
    } catch (MissingResourceException mre) {
      assertTrue(true);
    }

    map = (MessagesMap) Context.getThreadLocalContext().createMessagesMap(Object.class, "missingResource");

    assertTrue(map.isEmpty());
    assertEquals(0, map.size());

    assertFalse(map.containsKey("table-first-label"));

    try {
      assertNull(map.get("table-first-label"));
      fail();
    } catch (MissingResourceException mre) {
      assertTrue(true);
    }
  }

  /**
   * Check that a custom Page's properties are picked up properly.
   */
  public void testPageResources() {
    MockContext.initContext(Locale.ENGLISH);

    MessagesMap map = (MessagesMap) Context.getThreadLocalContext().createMessagesMap(TestPage.class, "click-page");

    assertFalse(map.isEmpty());
    assertEquals(2, map.size());
  }

  /**
   * Check that message inheritance works properly for custom controls.
   */
  public void testMessageInheritance() {
    MockContext.initContext(Locale.ENGLISH);

    MessagesMap map = (MessagesMap) Context.getThreadLocalContext().createMessagesMap(Test2TextField.class, Control.CONTROL_MESSAGES);

    assertFalse(map.isEmpty());
    assertTrue(map.size() > 30);

    assertTrue(map.containsKey("name"));
    assertEquals("Test1TextField", map.get("name"));
    assertEquals("Test2TextField", map.get("classname"));
  }

  /**
   * Create two MessagesMaps for the same class eg. Object.class
   * with different global resources eg. "missingResource" and
   * "click-control". The first messagesMap is created specifying
   * "missingResource" as its global resource. Since no
   * missingResource.properties file exists, this messagesMap will be empty.
   *
   * Test that the second messagesMap, specifying "click-control" as its
   * global resource, should pick up the properties from the
   * click-control.properties file.
   *
   * CLK-274
   */
  public void testGlobalResourceKey() {
    MockContext.initContext(Locale.ENGLISH);
    MessagesMap emptyMap = (MessagesMap) Context.getThreadLocalContext().createMessagesMap(Object.class, "missingResource");
    assertTrue(emptyMap.isEmpty());

    MessagesMap map = (MessagesMap) Context.getThreadLocalContext().createMessagesMap(Object.class, "click-control");
    assertFalse(map.isEmpty());
    assertEquals("First", map.get("table-first-label"));
  }

  /**
   * Test that the base properties will be picked up when the specified
   * locale and default locale are non-existent.
   *
   * Also test that the base properties are picked up when default locale is
   * non-existent and specified locale is English.
   *
   * CLK-269
   */
  public void testBasePropertiesUsingNonExistentDefaultLocale() {
    Locale defaultLocale = Locale.getDefault();

    Locale locale = new Locale("xx", "XX");//bogus locale

    //Set bogus as the default locale
    Locale.setDefault(locale);

    MockContext.initContext(locale);
    XmlConfigService.clearMessagesMapCache();
    MessagesMap nonEnglishMessages = (MessagesMap) Context.getThreadLocalContext().createMessagesMap(Object.class, "click-control");
    XmlConfigService.clearMessagesMapCache();
    assertFalse(nonEnglishMessages.isEmpty());

    //Test that property from click-control.properties are picked up
    //when locale is bogus locale
    assertEquals("First", nonEnglishMessages.get("table-first-label"));

    //Test that property from click-control.properties are picked up
    //when specified locale is English and default Locale cannot be found
    MockContext.initContext(Locale.ENGLISH);
    XmlConfigService.clearMessagesMapCache();
    MessagesMap englishMessages = (MessagesMap) Context.getThreadLocalContext().createMessagesMap(Object.class, "click-control");
    XmlConfigService.clearMessagesMapCache();
    assertFalse(englishMessages.isEmpty());
    assertEquals("First", englishMessages.get("table-first-label"));

    // Restore default locale
    Locale.setDefault(defaultLocale);
  }

  /**
   * Test that the English locale properties will be picked up when the
   * default locale is French.
   *
   * CLK-269
   */
  public void testEnglishMessagesUsingFrenchDefaultLocale() {
    Locale defaultLocale = Locale.getDefault();

    Locale locale = new Locale("fr", "FR");

    //Set French as default locale
    Locale.setDefault(locale);

    MockContext.initContext(locale);
    XmlConfigService.clearMessagesMapCache();
    MessagesMap nonEnglishMessages = (MessagesMap) Context.getThreadLocalContext().createMessagesMap(Object.class, "click-control");
    XmlConfigService.clearMessagesMapCache();
    assertFalse(nonEnglishMessages.isEmpty());

    //Test that French property is picked up
    assertEquals(0, nonEnglishMessages.get("table-first-label").indexOf("Premi"));

    //While using a default French locale, test that a English specified
    //locale picks up properties from click-control.properties
    MockContext.initContext(Locale.ENGLISH);
    XmlConfigService.clearMessagesMapCache();
    MessagesMap englishMessages = (MessagesMap) Context.getThreadLocalContext().createMessagesMap(Object.class, "click-control");
    XmlConfigService.clearMessagesMapCache();
    assertFalse(englishMessages.isEmpty());
    assertEquals("First", englishMessages.get("table-first-label"));

    // Restore default locale
    Locale.setDefault(defaultLocale);
  }
}