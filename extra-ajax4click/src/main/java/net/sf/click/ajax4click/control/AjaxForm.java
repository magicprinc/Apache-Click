package net.sf.click.ajax4click.control;

import org.apache.click.Context;
import org.apache.click.ControlRegistry;
import org.apache.click.control.Field;
import org.apache.click.control.Form;
import org.apache.click.element.Element;
import org.apache.click.element.JsScript;
import org.apache.click.util.HtmlStringBuffer;
import org.apache.commons.lang.StringUtils;

import java.io.Serial;
import java.util.List;

/**
 * Provide an Ajax ready Form control.
 * <p/>
 * <b>Please note:</b> AjaxForm does not by itself  provide any client-side Ajax
 * functionality. It is up to you to add the necessary JavaScript.
 * <p/>
 * AjaxFrom provides the following:
 * <ul>
 * <li>Ensures that the Form Focus script is properly evaluated when the form is
 * returned from an Ajax request.
 * </li>
 * <li>Register the Form with the ControlRegistry as an Ajax target control.
 * </li>
 * </ul>
 *
 * Below is an example showing how to use the AjaxForm:
 *
 * <pre class="prettyprint">
 * private AjaxForm form = new AjaxForm("form");
 *
 * public MyPage() {
 *
 *     addControl(form);
 *
 *     // Setup fields
 *     form.add(new TextField("firstName", true));
 *     form.add(new TextField("lastName", true));
 *     form.add(new EmailField("email", "E-Mail"));
 *
 *     Submit submit = new Submit("submit");
 *     form.add(submit);
 *
 *     // Set a Behavior on the Submit button, which will be invoked when
 *     // form is submitted
 *     submit.(new JQBhehavior() {
 *
 *        &#64;Override
 *        public MultiActionResult onAction(Control source, JQEvent event) {
 *            MultiActionResult actionResult = new MultiActionResult();
 *
 *            if (form.isValid()) {
 *                // Save the form data to the database
 *                saveForm();
 *            }
 *
 *            // Update the form
 *            actionResult.replace(form);
 *
 *            return actionResult;
 *        }
 *    });
 * } </pre>
 */
public class AjaxForm extends Form {
  @Serial private static final long serialVersionUID = 7206688366093594607L;

  /** The JavaScript focus function HEAD element. */
  protected JsScript focusScript;

  // Constructors -----------------------------------------------------------

  /**
   * Create a default AjaxForm and register the form as an
   * {@link org.apache.click.ControlRegistry#registerAjaxTarget(org.apache.click.Control) Ajax target}.
   */
  public AjaxForm() {
    ControlRegistry.registerAjaxTarget(this);
  }

  /**
   * Create a AjaxForm with the given name and register the form as an
   * {@link org.apache.click.ControlRegistry#registerAjaxTarget(org.apache.click.Control) Ajax target}.
   *
   * @param name the name of the form
   */
  public AjaxForm(String name) {
    super(name);
    ControlRegistry.registerAjaxTarget(this);
  }

  // Protected Methods ------------------------------------------------------

  /**
   * Return AjaxForm HEAD elements.
   *
   * @return the AjaxForm HEAD elements
   */
  @Override
  public List<Element> getHeadElements() {
    if (headElements == null) {
      headElements = super.getHeadElements();

      if (Context.getThreadLocalContext().isAjaxRequest()){
        focusScript = new JsScript();
        headElements.add(focusScript);
      }
    }
    return headElements;
  }

  /**
   * Render the Form field focus JavaScript to the string buffer.
   *
   * @param buffer the StringBuffer to render to
   * @param formFields the list of form fields
   */
  @Override
  protected void renderFocusJavaScript(HtmlStringBuffer buffer, List<Field> formFields) {
    if (Context.getThreadLocalContext().isAjaxRequest()){
      HtmlStringBuffer tempBuf = new HtmlStringBuffer();
      super.renderFocusJavaScript(tempBuf, formFields);
      String temp = tempBuf.toString();
      if (StringUtils.isBlank(temp)) {
        return;
      }
      String prefix = "<script type=\"text/javascript\"><!--";
      temp = temp.substring(prefix.length());
      String suffix = "//--></script>\n";
      int end = temp.indexOf(suffix);
      temp = temp.substring(0, end);
      focusScript.setContent(temp);
    } else {
      super.renderFocusJavaScript(buffer, formFields);
    }
  }
}