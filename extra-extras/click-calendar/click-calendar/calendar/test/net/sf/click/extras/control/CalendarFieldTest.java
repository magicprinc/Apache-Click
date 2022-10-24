package net.sf.click.extras.control;

import junit.framework.TestCase;
import org.apache.click.MockContext;

/**
 * Tests for CalendarField.
 *
 * @author Bob Schellink
 */
public class CalendarFieldTest extends TestCase {

    /**
     * Test Calendar format pattern.
     *
     * @throws java.lang.Exception
     */
    public void testFormatPattern() {
        MockContext.initContext();
        CalendarField calendarField = new CalendarField("Delivery date");
        assertEquals("dd MMM yyyy", calendarField.getFormatPattern());
        assertEquals("%d %b %Y", calendarField.getCalendarPattern());

        calendarField = new CalendarField("Delivery date");
        calendarField.setFormatPattern(" dd MMM yyyy ");
        assertEquals(" dd MMM yyyy ", calendarField.getFormatPattern());
        assertEquals(" %d %b %Y ", calendarField.getCalendarPattern());

        calendarField = new CalendarField("Delivery date");
        calendarField.setFormatPattern("dd/MMM/yyyy");
        assertEquals("dd/MMM/yyyy", calendarField.getFormatPattern());
        assertEquals("%d/%b/%Y", calendarField.getCalendarPattern());

        calendarField = new CalendarField("Delivery date");
        calendarField.setFormatPattern("dd.MMM.yyyy");
        assertEquals("dd.MMM.yyyy", calendarField.getFormatPattern());
        assertEquals("%d.%b.%Y", calendarField.getCalendarPattern());

        calendarField = new CalendarField("Delivery date");
        calendarField.setFormatPattern("dd.MM.yy");
        assertEquals("dd.MM.yy", calendarField.getFormatPattern());
        assertEquals("%d.%m.%y", calendarField.getCalendarPattern());

        calendarField = new CalendarField("Delivery date");
        calendarField.setFormatPattern("d/M/yy");
        assertEquals("d/M/yy", calendarField.getFormatPattern());
        assertEquals("%e/%m/%y", calendarField.getCalendarPattern());
    }
}
