package net.sf.click.jquery.examples.control;

import org.apache.click.Context;
import org.apache.click.control.ActionButton;
import org.apache.click.util.ClickUtils;
import org.apache.click.util.HtmlStringBuffer;



/**
 * Provides a server-side Ajax enabled ActionButton.
 * <p/>
 * <b>Please note:</b> AjaxActionButton does not work out-of-the-box since no
 * client-side Ajax support is provided.
 */
public class JQActionButton extends ActionButton {
  private static final long serialVersionUID = 1630094895580196821L;


  /**
   * Create an JQActionButton with the given name.
   *
   * @param name the button name
   */
  public JQActionButton(String name) {
    super(name);
  }

  /**
   * Create a JQActionButton with the given name and label.
   *
   * @param name the button name
   * @param label the button display label
   */
  public JQActionButton(String name, String label) {
    super(name, label);
  }

  /**
   * Create a JQActionButton with the given name, label and id.
   *
   * @param name the link name
   * @param label the link display label
   * @param id the link id
   */
  public JQActionButton(String name, String label, String id) {
    super(name, label);
    setId(id);
  }

  /**
   * Create an JQActionButton with no name defined.
   * <p/>
   * <b>Please note</b> the control's name must be defined before it is valid.
   */
  public JQActionButton() {
    super();
  }

  // --------------------------------------------------------- Public Methods

  /**
   * Return the AjaxActionButton href attribute for the given value.
   * This method will encode the URL with the session ID if required using
   * <tt>HttpServletResponse.encodeURL()</tt>.
   *
   * @param value the button value parameter
   * @return the button HTML href attribute value
   */
  public String getHref(Object value) {
    Context context = Context.getThreadLocalContext();
    String uri = ClickUtils.getRequestURI(context.getRequest());

    HtmlStringBuffer buffer =
        new HtmlStringBuffer(uri.length() + getName().length() + 40);

    buffer.append(uri);
    buffer.append("?");
    buffer.append(ACTION_BUTTON);
    buffer.append("=");
    buffer.append(getName());

    if (value != null) {
      buffer.append("&amp;");
      buffer.append(VALUE);
      buffer.append("=");
      buffer.append(ClickUtils.encodeUrl(value, context));
    }

    if (hasParameters()) {
      for (String name : getParameters().keySet()){
        if (!name.equals(ACTION_BUTTON) && !name.equals(VALUE)) {
          Object paramValue = getParameters().get(name);
          String encodedValue
              = ClickUtils.encodeUrl(paramValue, context);
          buffer.append("&amp;");
          buffer.append(name);
          buffer.append("=");
          buffer.append(encodedValue);
        }
      }
    }

    return context.getResponse().encodeURL(buffer.toString());
  }

  /**
   * Return the AjaxActionButton href attribute value.
   *
   * @return the button HTML href attribute value
   */
  public String getHref() {
    return getHref(getValueObject());
  }

  /**
   * Render the button to the given buffer.
   *
   * @param buffer the buffer to render to
   */
  @Override
  public void render(HtmlStringBuffer buffer) {
    buffer.elementStart(getTag());

    buffer.appendAttribute("type", getType());
    buffer.appendAttribute("name", getName());
    buffer.appendAttribute("id", getId());
    buffer.appendAttribute("value", getLabel());
    buffer.appendAttribute("title", getTitle());
    if (getTabIndex() > 0) {
      buffer.appendAttribute("tabindex", getTabIndex());
    }

    String onClickAction = " href=\"" + getHref() + "\"";
    buffer.append(onClickAction);

    appendAttributes(buffer);

    if (isDisabled()) {
      buffer.appendAttributeDisabled();
    }

    buffer.elementEnd();
  }
}