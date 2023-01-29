package org.apache.click.control;

import lombok.Getter;
import org.apache.click.Context;
import org.apache.commons.lang.StringUtils;

import java.io.Serial;

/**
 * Provides a Submit control: &nbsp; &lt;input type='submit'&gt;.
 *
 * <table class='htmlHeader cellspacing='6'>
 * <tr>
 * <td><input type='submit' value='Submit' title='Submit Control'/></td>
 * </tr>
 * </table>
 *
 * The Submit control supports server side processing and can be used to invoke
 * Control listeners.
 * <p/>
 * For an Submit code example see the {@link org.apache.click.control.Form}
 * Javadoc example.
 * <p/>
 * See also the W3C HTML reference
 * <a class="external" target="_blank" title="W3C HTML 4.01 Specification"
 *    href="http://www.w3.org/TR/html401/interact/forms.html#h-17.4">INPUT</a>
 */
public class Submit extends Button {
  @Serial private static final long serialVersionUID = -5837393715010800738L;

  /** The button is clicked (true if the submit button was clicked). */
  @Getter protected boolean clicked;


  /**
   * Create a Submit button with the given name.
   *
   * @param name the button name
   */
  public Submit(String name) {
    super(name);
  }

  /**
   * Create a Submit button with the given name and label.
   *
   * @param name the button name
   * @param label the button display label
   */
  public Submit(String name, String label) {
    super(name, label);
  }

  /**
   * Create a Submit button with the given name, listener object and
   * listener method.
   *
   * @param name the button name
   * @param listener the listener target object
   * @param method the listener method to call
   * @throws IllegalArgumentException if listener is null or if the method
   * is blank
   */
  public Submit(String name, Object listener, String method) {
    super(name);

    if (listener == null) {
      throw new IllegalArgumentException("Null listener parameter");
    }
    if (StringUtils.isBlank(method)) {
      throw new IllegalArgumentException("Blank listener method");
    }
    setListener(listener, method);
  }

  /**
   * Create a Submit button with the given name, label, listener object and
   * listener method.
   *
   * @param name the button name
   * @param label the button display label
   * @param listener the listener target object
   * @param method the listener method to call
   * @throws IllegalArgumentException if listener is null or if the method
   * is blank
   */
  public Submit(String name, String label, Object listener, String method) {
    super(name, label);

    if (listener == null) {
      throw new IllegalArgumentException("Null listener parameter");
    }
    if (StringUtils.isBlank(method)) {
      throw new IllegalArgumentException("Blank listener method");
    }
    setListener(listener, method);
  }

  /**
   * Create an Submit button with no name defined.
   * <p/>
   * <b>Please note</b> the control's name must be defined before it is valid.
   */
  public Submit() {
    super();
  }

  // Public Attributes ------------------------------------------------------


  /**
   * Return true if client side JavaScript form validation will be cancelled
   * by pressing this button.
   *
   * @return true if button will cancel JavaScript form validation
   */
  public boolean getCancelJavaScriptValidation() {
    String attrValue = getAttribute("onclick");

    return "form.onsubmit=null;".equals(attrValue);
  }

  /**
   * Set whether client side JavaScript form validation will be cancelled
   * by pressing this button.
   *
   * @param cancel the cancel JavaScript form validation flag
   */
  public void setCancelJavaScriptValidation(boolean cancel) {
    if (cancel) {
      setAttribute("onclick", "form.onsubmit=null;");
    } else {
      setAttribute("onclick", null);
    }
  }


  /**
   * Return the input type: '<tt>submit</tt>'.
   *
   * @return the input type: '<tt>submit</tt>'
   */
  @Override
  public String getType (){ return "submit";}



  /**
   * Bind the request submission, setting the {@link Field#value} and
   * {@link #clicked} properties if defined.
   */
  @Override
  public void bindRequestValue() {
    String requestValue = Context.getThreadLocalContext().getRequestParameter(getName());

    this.clicked = requestValue != null;
    setValue(requestValue);
  }

  /**
   * Process the submit event and return true to continue event processing.
   * <p/>
   * If the submit button is clicked and a Control listener is defined, the
   * listener method will be invoked.
   * <p/>
   * Submit buttons will be processed after all the non Button Form Controls
   * have been processed. Submit buttons will be processed in the order
   * they were added to the Form.
   *
   * @return true to continue Page event processing or false otherwise
   */
  @Override
  public boolean onProcess() {
    if (isDisabled()) {
      Context context = Context.getThreadLocalContext();

      // Switch off disabled property if control has incoming request
      // parameter. Normally this means the field was enabled via JS
      if (context.hasRequestParameter(getName())) {
        setDisabled(false);
      } else {
        // If field is disabled skip process event
        return true;
      }
    }

    bindRequestValue();

    if (isClicked()) {
      dispatchActionEvent();
    }

    return true;
  }
}