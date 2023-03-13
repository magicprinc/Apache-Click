package org.apache.click.extras.control;

import junit.framework.TestCase;
import org.apache.click.MockContext;
import org.apache.click.servlet.MockRequest;
import org.apache.commons.lang.StringUtils;

import java.util.Date;
import java.util.Locale;

/**
 * Provides DateField JUnit TestCase.
 */
public class DateFieldTest extends TestCase {

  public void testNullParameter() {
    MockContext mockContext = MockContext.initContext();
    MockRequest request = mockContext.getMockRequest();

    DateField dateField = new DateField("dateField");
    assertEquals("dateField", dateField.getName());

    request.getParameterMap().put("dateField", "");
    dateField.onProcess();
    Date date = dateField.getDate();
    assertNull(date);

    request.getParameterMap().put("dateField", " ");
    dateField.onProcess();
    date = dateField.getDate();
    assertNull(date);

    request.getParameterMap().put("dateField", null);
    dateField.onProcess();
    date = dateField.getDate();
    assertNull(date);

    dateField.setValue(null);
    date = dateField.getDate();
    assertNull(date);
  }

  /**
   * DateField should cache Date value instead of reparsing the string
   * value each time.
   *
   * CLK-316
   */
  public void testIntegerCacheValue() {
    MockContext mockContext = MockContext.initContext();
    MockRequest request = mockContext.getMockRequest();
    mockContext.setLocale(Locale.US);

    DateField dateField = new DateField("dateField");
    dateField.setFormatPattern("dd MMM yyyy H m s S");
    String requestParam = "06 Oct 2008 2 30 59 999";
    request.getParameterMap().put("dateField", requestParam);

    assertTrue(dateField.onProcess());

    // Check that the value equals the request parameter
    assertEquals(requestParam, dateField.getValue());

    // Retrieve the date from field: this should cache the Date
    Date date  = dateField.getDate();
    // Check that upon second retrieval the cached value is returned
    assertSame(date, dateField.getDate());

    // Check that getValueObject also returns the cached value
    assertSame(date, dateField.getValueObject());

    // Set date on the dateField and check that time value is not lost
    dateField.setDate(date);

    assertEquals(requestParam, dateField.getValue());
  }

  /**
   * Test Calendar format pattern.
   *
   * @throws java.lang.Exception
   */
  public void testFormatPattern() {
    var enUs = new Locale("en", "US");
    MockContext.initContext(enUs);
    Locale.setDefault(enUs);
    assertEquals("en_US", Locale.getDefault().toString());
    assertEquals("en_US", MockContext.getThreadLocalContext().getLocale().toString());

    DateField calendarField = new DateField("Delivery date");
    assertEquals("dd MMM yyyy", calendarField.getFormatPattern());
    assertEquals("dd NNN yyyy", calendarField.getCalendarPattern());

    calendarField = new DateField("Delivery date");
    calendarField.setFormatPattern(" dd MMM yyyy ");
    assertEquals(" dd MMM yyyy ", calendarField.getFormatPattern());
    assertEquals(" dd NNN yyyy ", calendarField.getCalendarPattern());

    calendarField = new DateField("Delivery date");
    calendarField.setFormatPattern("dd/MMM/yyyy");
    assertEquals("dd/MMM/yyyy", calendarField.getFormatPattern());
    assertEquals("dd/NNN/yyyy", calendarField.getCalendarPattern());

    calendarField = new DateField("Delivery date");
    calendarField.setFormatPattern("dd.MMM.yyyy");
    assertEquals("dd.MMM.yyyy", calendarField.getFormatPattern());
    assertEquals("dd.NNN.yyyy", calendarField.getCalendarPattern());

    calendarField = new DateField("Delivery date");
    calendarField.setFormatPattern("dd.MM.yy");
    assertEquals("dd.MM.yy", calendarField.getFormatPattern());
    assertEquals("dd.MM.yy", calendarField.getCalendarPattern());

    calendarField = new DateField("Delivery date");
    calendarField.setFormatPattern("d/M/yy");
    assertEquals("d/M/yy", calendarField.getFormatPattern());
    assertEquals("d/M/yy", calendarField.getCalendarPattern());
  }

  /**
   * Check that help text is not rendered twice.
   *
   * CLK-574
   */
  public void testRenderHelp() {
    MockContext.initContext();

    DateField dateField = new MyDateField("date");

    int matches = StringUtils.countMatches(dateField.toString(), "Help Me!");

    assertEquals(1, matches);
  }

  public static class MyDateField extends DateField {
    private static final long serialVersionUID = -9018676841770962557L;

    public MyDateField(String name) {
      super(name);
    }
  }
}