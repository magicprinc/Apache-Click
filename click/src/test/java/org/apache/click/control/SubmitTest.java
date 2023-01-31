package org.apache.click.control;

import junit.framework.TestCase;
import org.apache.click.MockContext;
import org.apache.click.servlet.MockRequest;

/**
 * Test Button behavior.
 */
public class SubmitTest extends TestCase {
  /**
   * Test Submit onProcess behavior.
   */
  public void testOnProcess() {
    MockContext context = MockContext.initContext();
    MockRequest request = context.getMockRequest();

    Submit button = new Submit("button");
    assertEquals("button", button.getName());

    assertTrue(button.onProcess());

    request.setParameter("button", "true");
    assertTrue(button.onProcess());

    final boolean[] check = new boolean[1];
    button.setActionListener(control -> {
        check[0] = true;
        return false;
      });

    // No request param -> no action listener executed
    request.removeParameter("button");
    assertTrue(button.onProcess());
    context.executeActionListeners();
    assertFalse(check[0]);

    // Disabled button with request param
    request.setParameter("button", "true");
    button.setDisabled(true);
    assertTrue(button.onProcess());
    context.executeActionListeners();
    assertTrue(button.isValid());
    assertFalse(button.isDisabled());
    assertTrue(check[0]);

    // Disabled button without request param
    request.removeParameter("button");
    button.setDisabled(true);
    assertTrue(button.onProcess());
    assertTrue(button.isValid());
    assertTrue(button.isDisabled());
  }

  /**
   * Coverage test of constructors.
   */
  public void testConstructors() {
    Submit button = new Submit();
    assertNull(button.getName());

    button = new Submit("button", "label");
    assertEquals("button", button.getName());
    assertEquals("label", button.getLabel());

    Listener l = new Listener();
    assertEquals("button", button.getName());
    button = new Submit("button", l, "onAction");

    try {
      button = new Submit("button", null, "onAction");
      fail("Should throw exception");
    } catch (RuntimeException expected) {}

    try {
      button = new Submit("button", l, null);
      fail("Should throw exception");
    } catch (RuntimeException expected) { }

    button = new Submit("button", "label", l, "onAction");
    assertEquals("button", button.getName());
    assertEquals("label", button.getLabel());

    try {
      button = new Submit("button", "label", null, "onAction");
      fail("Should throw exception");
    } catch (RuntimeException expected) { }

    try {
      button = new Submit("button", "label", l, null);
      fail("Should throw exception");
    } catch (RuntimeException expected) { }

  }

  static class Listener {
    public boolean fired;
    public boolean onAction() {
      fired = true;
      return true;
    }
  }

  /**
   * Coverage test of onClick.
   */
  public void testCancelJavaScriptValidation() {
    MockContext.initContext();

    Submit button = new Submit("button");
    assertFalse(button.getCancelJavaScriptValidation());

    button = new Submit("button");
    button.setCancelJavaScriptValidation(false);
    assertFalse(button.getCancelJavaScriptValidation());

    button.setCancelJavaScriptValidation(true);
    assertTrue(button.getCancelJavaScriptValidation());
    assertNotNull(button.getAttribute("onclick"));
  }

  /**
   * Coverage test of tab-index.
   */
  public void testTabIndex() {
    MockContext.initContext();

    Submit button = new Submit("button");
    button.setTabIndex(5);

    assertTrue(button.toString().contains("tabindex=\"5\""));
  }

  /**
   * Coverage test of disabled property.
   */
  public void testDisabled() {
    MockContext.initContext();

    Submit button = new Submit("button");
    button.setDisabled(true);

    assertTrue(button.toString().contains("disabled=\"disabled\""));
  }
}