package org.apache.click.util;


import java.io.Serializable;

/**
 * Provides a flash session attribute. The flash attribute simply wraps an
 * existing session attribute providing a marker to the Context and SessionMap
 * classes that the session attribute should be removed once it has been
 * accessed.
 * <p/>
 * Flash session attributes appear only once in a session, after they have been
 * accessed they are removed from the session. Flash attributes are typically
 * used for display information messages to a user, and can be used with page
 * redirects.
 * <p/>
 * Note you generally do not use FlashAttribute directly, but indirectly via the
 * Context setFlashAttribute() method. For example:
 *
 * <pre class="codeJava">
 * <span class="kw">public boolean</span> onOkClick() {
 *     // Perform logic
 *     ..
 *     String message = <span class="st">"Operation successfully completed"</span>;
 *
 *     getContext().setFlashAttribute(<span class="st">"message"</span>, message);
 *     setRedirect(HomePage.<span class="kw">class</span>);
 *     <span class="kw">return false</span>;
 * }
 * </pre>
 *
 * To test for the existence of a flash attribute and then render it, you should
 * set it as a variable in your template, because once it has been accessed from
 * the session it is removed. Using Velocity as an example:
 *
 * <pre class="codeHtml">
 * <span class="kw">#set</span> (<span class="st">$message</span> = <span class="st">$session.message</span>)
 * <span class="kw">#if</span> ($message)
 *   &lt;p&gt;<span class="st">$message</span>&lt;/p&gt;
 * <span class="kw">#end</span> </pre>
 *
 * @see org.apache.click.Context
 * @see SessionMap
 */
public class FlashAttribute implements Serializable {
  private static final long serialVersionUID = 5607837929201234313L;

  /** The session attribute value. */
  protected Object value;

  /**
   * Create a session flash attribute with the given value.
   *
   * @param value the flash session attribute value
   */
  public FlashAttribute(Object value) {
    this.value = value;
  }

  /**
   * Return the flash session attribute value.
   *
   * @return the flash session attribute value
   */
  public Object getValue() {
    return value;
  }

  /**
   * Return the string representation of the flash attribute.
   *
   * @see Object#toString()
   *
   * @return the string representation of the flash attribute
   */
  @Override public String toString() {
    if (value != null) {
      return value.toString();
    } else {
      return getClass().getName() + "[value=null]";
    }
  }

}