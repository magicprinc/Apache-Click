package org.apache.click.extras.control;

import junit.framework.TestCase;
import org.apache.click.MockContext;
import org.apache.click.servlet.MockRequest;

public class IntegerFieldTest extends TestCase {

    public void testOnProcess() {
        MockContext mockContext = MockContext.initContext();
        MockRequest request = mockContext.getMockRequest();

        IntegerField intField = new IntegerField("id");
        assertEquals("id", intField.getName());

        request.getParameterMap().put("id", new String[]{"1234"});

        assertTrue(intField.onProcess());
        assertTrue(intField.isValid());
        assertEquals("1234", intField.getValue());
        assertEquals(1234, intField.getValueObject());

        request.getParameterMap().put("id", new String[]{"123.4"});

        assertTrue(intField.onProcess());
        assertFalse(intField.isValid());
        assertEquals("123.4", intField.getValue());
        assertNull(intField.getValueObject());

        // Test not required + blank value
        request.getParameterMap().put("id", new String[]{""});

        assertTrue(intField.onProcess());
        assertTrue(intField.isValid());
        assertEquals("", intField.getValue());
        assertNull(intField.getValueObject());

        // Test not required + blank value
        request.getParameterMap().put("id", new String[]{""});
        intField.setRequired(true);
        assertTrue(intField.onProcess());
        assertFalse(intField.isValid());
        assertEquals("", intField.getValue());
        assertNull(intField.getValueObject());

        request.getParameterMap().clear();

        request.getParameterMap().put("id", new String[]{"0"});

        intField.setRequired(true);
        assertTrue(intField.onProcess());
        assertTrue(intField.isValid());
        assertEquals("0", intField.getValue());
        assertNotNull(intField.getValueObject());
        assertEquals(0, intField.getValueObject());

        intField.setRequired(false);
        assertTrue(intField.onProcess());
        assertTrue(intField.isValid());
        assertEquals("0", intField.getValue());
        assertNotNull(intField.getValueObject());
        assertEquals(0, intField.getValueObject());

        request.getParameterMap().put("id", new String[]{"10"});

        intField.setMinValue(10);
        assertTrue(intField.onProcess());
        assertTrue(intField.isValid());
        assertEquals("10", intField.getValue());
        assertEquals(10, intField.getValueObject());

        intField.setMinValue(11);
        assertTrue(intField.onProcess());
        assertFalse(intField.isValid());
        assertEquals("10", intField.getValue());
        assertEquals(10, intField.getValueObject());

        request.getParameterMap().put("id", new String[]{"20"});

        intField.setMaxValue(20);
        assertTrue(intField.onProcess());
        assertTrue(intField.isValid());
        assertEquals("20", intField.getValue());
        assertEquals(20, intField.getValueObject());

        intField.setMaxValue(20);
        assertTrue(intField.onProcess());
        assertTrue(intField.isValid());
        assertEquals("20", intField.getValue());
        assertEquals(20, intField.getValueObject());

        intField.setMaxValue(19);
        assertTrue(intField.onProcess());
        assertFalse(intField.isValid());
        assertEquals("20", intField.getValue());
        assertEquals(20, intField.getValueObject());

        assertEquals(20, intField.getInteger().intValue());
        assertEquals(20L, intField.getLong().longValue());

        request.getParameterMap().put("id", new String[]{"-20"});

        intField.setMinValue(-21);
        assertTrue(intField.onProcess());
        assertTrue(intField.isValid());
        assertEquals("-20", intField.getValue());
        assertEquals(-20, intField.getValueObject());
    }
}