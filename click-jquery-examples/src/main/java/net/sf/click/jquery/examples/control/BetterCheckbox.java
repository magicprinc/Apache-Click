package net.sf.click.jquery.examples.control;

import org.apache.click.Context;
import org.apache.click.control.Checkbox;
import org.apache.click.util.HtmlStringBuffer;

import java.io.Serial;

/**
 * BetterCheckbox is different from Checkbox in the following ways.
 *
 * - BetterCheckbox can have a value, not just true/false
 *
 * - BetterCheckbox will be checked if the incoming parameter matches its given value,
 * otherwise it will be unchecked
 *
 * - If BetterCheckbox is disabled and enabled via JS, one needs to add a
 * hidden field with the same name as the checkbox and BetterCheckbox will be
 * enabled automatically. This behavior isn't supported by the default Checkbox.
 */
public class BetterCheckbox extends Checkbox {
  @Serial private static final long serialVersionUID = 2164603967901746163L;


  public BetterCheckbox(String name) {
    super(name);
  }

  public BetterCheckbox(String name, String label) {
    super(name, label);
  }

  public BetterCheckbox(String name, boolean required) {
    super(name, required);
  }

  public BetterCheckbox() {
  }

  // public methods ---------------------------------------------------------

  @Override
  public String getValue() {
    return (value != null) ? value : "";
  }

  @Override
  public void setValue(String value) {
    this.value = value;
  }

  @Override
  public Object getValueObject() {
    if (value == null || value.length() == 0) {
      return null;
    } else {
      return value;
    }
  }

  @Override
  public void setValueObject(Object object) {
    if (object != null) {
      value = object.toString();
    }
  }

  /**
   * Set the {@link #checked} property to true if the fields value is
   * submitted.
   */
  @Override
  public void bindRequestValue() {
    String param = Context.getThreadLocalContext().getRequestParameter(getName());
    setChecked(getValue().equals(param));
  }

  @Override
  public void render(HtmlStringBuffer buffer) {
    buffer.elementStart(getTag());

    buffer.appendAttribute("type", getType());
    buffer.appendAttribute("name", getName());
    buffer.appendAttribute("id", getId());
    buffer.appendAttribute("value", getValue());
    buffer.appendAttribute("title", getTitle());
    if (isValid()) {
      removeStyleClass("error");
    } else {
      addStyleClass("error");
    }
    if (getTabIndex() > 0) {
      buffer.appendAttribute("tabindex", getTabIndex());
    }
    if (isChecked()) {
      buffer.appendAttribute("checked", "checked");
    }

    appendAttributes(buffer);

    if (isDisabled() || isReadonly()) {
      buffer.appendAttributeDisabled();
    }

    buffer.elementEnd();

    if (getHelp() != null) {
      buffer.append(getHelp());
    }

    // checkbox element does not support "readonly" element, so as a work around
    // we make the field "disabled" and render a hidden field to submit its value
    if (isReadonly() && isChecked()) {
      buffer.elementStart("input");
      buffer.appendAttribute("type", "hidden");
      buffer.appendAttribute("name", getName());
      buffer.appendAttributeEscaped("value", getValue());
      buffer.elementEnd();
    }

        /*
        buffer.elementStart("input");
        buffer.appendAttribute("type", "hidden");
        buffer.appendAttribute("name", getName());
        buffer.appendAttributeEscaped("value", "0");
        buffer.elementEnd();*/
  }
}