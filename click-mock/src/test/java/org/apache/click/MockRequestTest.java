package org.apache.click;

import junit.framework.TestCase;
import org.apache.click.control.TextArea;
import org.apache.click.servlet.MockRequest;

import java.io.Serial;

/**
 * Sanity tests for MockRequest.
 */
public class MockRequestTest extends TestCase {

    /** Indicates that the textArea actionListener was invoked. */
    private boolean actionCalled = false;

    /**
     * Check that MockRequest can dynamically add parameters and trigger
     * a Controls action listener.
     */
    @SuppressWarnings("unchecked")
    public void testDynamicRequest() {
        MockContext context = MockContext.initContext();
        MockRequest request = context.getMockRequest();

        TextArea textArea = new TextArea("text");
        assertEquals("text", textArea.getName());

        request.setParameter("param", "value");
        request.getParameterMap().put("text", new String[]{"textvalue"});

        // Registry a listener which must be invoked
        textArea.setActionListener(new ActionListener() {
            @Serial private static final long serialVersionUID = 1L;

            @Override public boolean onAction(Control source) {
                // When action is invoked, set flag to true
                return actionCalled = true;
            }
        });
        assertTrue(textArea.onProcess());

        // Fire all action events that was registered in the onProcess method
        context.executeActionListeners();

        assertTrue("TextArea action was not invoked", actionCalled);
        assertTrue(textArea.isValid());
        assertEquals("textvalue", textArea.getValue());
        assertEquals("textvalue", textArea.getValueObject());

        // Check that getParameterMap() is modifiable by adding a
        // key/value pair.
        context = (MockContext) Context.getThreadLocalContext();
        context.getRequest().getParameterMap().put("textvalue",	new String[]{textArea.getValue()});
    }
}