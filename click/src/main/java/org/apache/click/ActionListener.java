package org.apache.click;

import java.io.Serializable;

/**
 * Provides a listener interface for receiving control action events.
 * The usage model is similar to the <tt>java.awt.event.ActionListener</tt>
 * interface.
 * <p/>
 * The class that is interested in processing an action event
 * implements this interface, and the object created with that class is
 * registered with a control, using the control's <tt>setActionListener</tt>
 * method. When the action event occurs, that object's <tt>onAction</tt> method
 * is invoked.
 *
 * <h3>Listener Example</h3>
 *
 * An ActionListener example is provided below:
 *
 * <pre class="prettyprint">
 * public MyPage extends Page {
 *
 *    public ActionLink link = new ActionLink();
 *
 *    public MyPage() {
 *
 *       link.setActionListener(new ActionListener() {
 *           public boolean onAction(Control source) {
 *               return onLinkClick();
 *           }
 *        });
 *    }
 *
 *    public boolean onLinkClick() {
 *       ..
 *       return true;
 *    }
 * }
 * </pre>
 */
@FunctionalInterface
public interface ActionListener extends Serializable {

  /**
   * Return true if the control and page processing should continue,
   * or false otherwise.
   *
   * @param source the source of the action event
   * @return true if control and page processing should continue or false otherwise.
   */
  boolean onAction (Control source);

}