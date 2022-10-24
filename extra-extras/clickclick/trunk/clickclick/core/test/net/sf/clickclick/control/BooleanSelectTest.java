package net.sf.clickclick.control;

import junit.framework.TestCase;
import org.apache.click.MockContext;
import org.apache.click.servlet.MockRequest;

public class BooleanSelectTest extends TestCase {

    /**
     * should never break, illegal data == false
     */
    public void testGetBoolean() {
        BooleanSelect bsField = new BooleanSelect("bs");

        bsField.setValue("true");
        assertEquals(Boolean.TRUE, bsField.getBoolean());
        bsField.setValue("false");
        assertEquals(Boolean.FALSE, bsField.getBoolean());
        bsField.setValue("trUe");
        assertEquals(Boolean.TRUE, bsField.getBoolean());
        bsField.setValue("fAlsE");
        assertEquals(Boolean.FALSE, bsField.getBoolean());
        bsField.setValue("");
        assertNull(bsField.getBoolean());
        bsField.setValue(null);
        assertNull(bsField.getBoolean());
        bsField.setValue("some illegal value");
        assertEquals(Boolean.FALSE, bsField.getBoolean());
    }

    /**
     * Should be of the Boolean persuasion or null
     */
    public void testGetValueObject() {
        BooleanSelect bsField = new BooleanSelect("bs");

        bsField.setValueObject("true");
        assertTrue(bsField.getValueObject() instanceof Boolean);
        bsField.setValueObject("false");
        assertTrue(bsField.getValueObject() instanceof Boolean);
        bsField.setValueObject("");
        assertNull(bsField.getValueObject());
        bsField.setValueObject(this);
        assertTrue(bsField.getValueObject() instanceof Boolean);
    }

    public void testOnProcess() {
        MockContext mockContext = MockContext.initContext();
        MockRequest request = mockContext.getMockRequest();

        BooleanSelect bsField = new BooleanSelect("bs");
        bsField.onInit();
        assertEquals("bs", bsField.getName());

        // -- bad value - somebody tampered with the formdata
        request.getParameterMap().put("bs", "bad bad");
        assertTrue(bsField.onProcess());
        assertTrue(bsField.isValid());
        assertEquals(Boolean.FALSE, bsField.getBoolean());

        // -- true
        request.getParameterMap().put("bs", "true");
        assertTrue(bsField.onProcess());
        assertTrue(bsField.isValid());
        assertEquals(Boolean.TRUE, bsField.getBoolean());

        // -- false
        request.getParameterMap().put("bs", "false");
        assertTrue(bsField.onProcess());
        assertTrue(bsField.isValid());
        assertEquals(Boolean.FALSE, bsField.getBoolean());

        // -- no option selected
        bsField.setRequired(true);
        request.getParameterMap().remove("bs");
        assertTrue(bsField.onProcess());
        assertFalse(bsField.isValid());
        assertNull(bsField.getBoolean());
    }
}
