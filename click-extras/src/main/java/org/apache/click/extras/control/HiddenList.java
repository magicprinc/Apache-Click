package org.apache.click.extras.control;

import org.apache.click.Context;
import org.apache.click.control.Field;
import org.apache.click.util.HtmlStringBuffer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Provides a control for rendering a list of
 * {@link org.apache.click.control.HiddenField Hidden Fields}:
 * &nbsp; &lt;input type='hidden'&gt;.
 * <p/>
 * Click also provides the {@link org.apache.click.control.HiddenField} to render
 * &lt;input type="hidden"&gt;, however HiddenField can not render multiple values
 * under the same name.
 * <p/>
 * HiddenList supports multiple values under the same name by rendering
 * the values as multiple hidden fields.
 *
 * <h3>HiddenList Examples</h3>
 *
 * <pre class="prettyprint">
 * HiddenList hiddenList = new HiddenList("customerId");
 * hiddenList.addValue("123");
 * hiddenList.addValue("678"); </pre>
 *
 * This <code>HiddenList</code> would generate following HTML:
 *
 * <pre class="prettyprint">
 * &lt;input type="hidden" name="customerId" id="form-customerId_1" value="123"/&gt;
 * &lt;input type="hidden" name="customerId" id="form-customerId_2" value="678"/&gt; </pre>
 */
public class HiddenList extends Field {
  private static final long serialVersionUID = 3886989373361066580L;

  /** The hidden values. */
  protected List<String> valueObject;


  /**
   * Create a HiddenList with the given name.
   *
   * @param name the name of the field
   */
  public HiddenList(String name) {
    super(name);
  }

  /**
   * Create a default HiddenList.
   * <p/>
   * <b>Please note</b> the control's name must be defined before it is valid.
   */
  public HiddenList() {}


  /**
   * Set the list of hidden values.
   *
   * @param valueObject a list of Strings
   */
  @SuppressWarnings("unchecked") @Override
  public void setValueObject(Object valueObject) {
    if (!(valueObject instanceof List<?>)) {
      throw new IllegalArgumentException("the valueObject must be a List.");
    }
    this.valueObject = (List<String>) valueObject;
  }

  /**
   * Returns the list of added values as a <tt>java.util.List</tt> of Strings.
   *
   * @return a list of Strings
   */
  @Override
  public Object getValueObject() {
    if (this.valueObject == null) {
      this.valueObject = new ArrayList<>();
    }
    return this.valueObject;
  }

  /**
   * This method delegates to {@link #getValueObject()} to return the
   * hidden values as a <tt>java.util.List</tt> of Strings.
   *
   * @return a list of Strings
   */
  @SuppressWarnings("unchecked")
  public List<String> getValues() {
    return (List<String>) getValueObject();
  }

  /**
   * Add the given value to this <code>HiddenList</code>.
   *
   * @param value the hidden value to add
   */
  public void addValue(String value) {
    getValues().add(value);
  }

  // Public Methods ---------------------------------------------------------

  /**
   * This method binds the submitted request values to the HiddenList values.
   * <p/>
   * <b>Please note:</b> while it is possible to explicitly bind the field
   * value by invoking this method directly, it is recommended to use the
   * "<tt>bind</tt>" utility methods in {@link org.apache.click.util.ClickUtils}
   * instead. See {@link org.apache.click.util.ClickUtils#bind(org.apache.click.control.Field)}
   * for more details.
   */
  @Override
  public void bindRequestValue() {
    String[] values = Context.getThreadLocalContext().getRequestParameterValues(getName());

    if (values != null) {
      List<String> list = new ArrayList<>();
      Collections.addAll(list, values);
      setValueObject(list);
    }
  }

  /**
   * Returns true.
   *
   * @see Field#isHidden()
   *
   * @return true
   */
  @Override
  public boolean isHidden() {
    return true;
  }

  /**
   * Render the HTML representation of the HiddenField.
   *
   * @see org.apache.click.Control#render(org.apache.click.util.HtmlStringBuffer)
   *
   * @param buffer the specified buffer to render the control's output to
   */
  @Override
  public void render(HtmlStringBuffer buffer) {
    List<String> values = getValues();

    for (int i = 0; i < values.size(); i++) {
      buffer.elementStart("input");
      buffer.appendAttribute("type", "hidden");
      buffer.appendAttribute("name", getName());
      buffer.appendAttribute("id", getId() + "_" + (i + 1));
      buffer.appendAttribute("value", values.get(i));
      buffer.elementEnd();
    }
  }

}