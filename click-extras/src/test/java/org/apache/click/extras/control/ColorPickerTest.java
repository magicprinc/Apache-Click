package org.apache.click.extras.control;

import junit.framework.TestCase;
import org.apache.click.MockContext;
import org.apache.click.servlet.MockRequest;

import java.util.regex.Pattern;

public class ColorPickerTest extends TestCase {

    public void testHexPattern() {
        Pattern pat = ColorPicker.HEX_PATTERN;
        assertTrue(pat.matcher("#ffffff").matches());
        assertTrue(pat.matcher("#1a9bf2").matches());
        assertTrue(pat.matcher("#1A9BC2").matches());
        assertTrue(pat.matcher("#fff").matches());
        assertTrue(pat.matcher("#E3F").matches());
        assertTrue(pat.matcher("#123").matches());
        assertTrue(pat.matcher("#a4b").matches());

        assertFalse(pat.matcher("#123456789").matches());
        assertFalse(pat.matcher("").matches());
        assertFalse(pat.matcher("FFFFFF").matches());
        assertFalse(pat.matcher("GF").matches());
        assertFalse(pat.matcher("#G12").matches());
        assertFalse(pat.matcher("#A2").matches());
        assertFalse(pat.matcher("#A2A2A").matches());
        assertFalse(pat.matcher("#1234").matches());
    }

    public void testValidate() {
        MockContext mockContext = MockContext.initContext();
        MockRequest mr = mockContext.getMockRequest();
        var params = mr.getParameterMap();

        ColorPicker cp = new ColorPicker("color");

        params.put("color",new String[]{"#fff"});
        assertTrue(cp.onProcess());
        assertTrue(cp.isValid());
        assertEquals("#fff",cp.getValue());

        cp = new ColorPicker("color");
        params.remove("color");
        assertTrue(cp.onProcess());
        assertTrue(cp.isValid());
        assertEquals("",cp.getValue());

        params.put("color", new String[]{""});
        cp.setRequired(true);
        assertTrue(cp.onProcess());
        assertFalse(cp.isValid());

        cp = new ColorPicker("color");

        params.put("color", new String[]{"invalid"});
        assertTrue(cp.onProcess());
        assertFalse(cp.isValid());
        assertEquals("invalid",cp.getValue());
    }
}