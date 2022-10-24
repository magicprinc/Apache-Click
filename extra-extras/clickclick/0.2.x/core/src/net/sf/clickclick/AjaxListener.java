/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.clickclick;

import net.sf.clickclick.util.Partial;
import org.apache.click.ActionListener;
import org.apache.click.Control;

/**
 * Provides an Ajax listener interface for receiving Ajax events.
 * <p/>
 * The class that is interested in processing an Ajax event
 * implements this interface, and the object created with that class is
 * registered with a control, using the controls's <tt>setActionListener</tt>
 * method. When an Ajax event occurs, that object's <tt>onAjaxAction</tt> method
 * is invoked.
 * <p/>
 * The {@link #onAjaxAction(org.apache.click.Control)} method returns a
 * {@link net.sf.clickclick.util.Partial} object which contains the server's
 * response.
 *
 * <h3>Listener Example</h3>
 *
 * An AjaxListener example is provided below:
 *
 * <pre class="prettyprint">
 * public MyPage extends Page {
 *
 *    public ActionLink link = new ActionLink();
 *
 *    public MyPage() {
 *
 *       link.setActionListener(new AjaxListener() {
 *           public Partial onAjaxAction(Control source) {
 *               return onLinkClick();
 *           }
 *        });
 *    }
 *
 *    public Partial onLinkClick() {
 *       Partial partial = new Partial();
 *       partial.setContent("&lt;script&gt;alert('Hello World');&lt;/script&gt;");
 *       return partial;
 *    }
 * }
 * </pre>
 */
public interface AjaxListener extends ActionListener {

    /**
     * This method is invoked for ajax requests and returns a
     * {@link net.sf.clickclick.util.Partial} response.
     * <p/>
     * A Partial instance represents a partial response to a user request.
     *
     * @param source the source of the action event
     * @return the partial response to render, or null if no response should
     * be rendered
     */
    public Partial onAjaxAction(Control source);

}
