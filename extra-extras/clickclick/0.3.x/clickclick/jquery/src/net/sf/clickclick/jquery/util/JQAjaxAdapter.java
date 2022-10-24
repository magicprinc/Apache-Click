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
package net.sf.clickclick.jquery.util;

import net.sf.clickclick.util.AjaxAdapter;
import net.sf.clickclick.util.Partial;
import org.apache.click.Control;

/**
 * Provides an abstract adapter for receiving JQuery Ajax callbacks.
 * <p/>
 * This adapter defines the method
 * {@link #onAjaxAction(org.apache.click.Control, net.sf.clickclick.jquery.util.JQEvent)}
 * so that implementors receive both the source control and source event.
 * <p/>
 * For example:
 *
 * <pre class="prettyprint">
 * public class MyPage extends BorderPage {
 *
 *     public MyPage() {
 *         JQActionLink link = new JQActionLink("mylink");
 *         link.setId("myid");
 *
 *         link.setActionListener(new JQAjaxAdapter() {
 *
 *             &#64;Override
 *             public Partial onAjaxAction(Control source, JQEvent event) {
 *
 *                 Taconite partial = new Taconite();
 *
 *                 if (event == JQEvent.CLICK) {
 *                     ...
 *                 } else if (event == JQEvent.DOUBLE_CLICK) {
 *                     ...
 *                 }
 *
 *                 return partial;
 *             }
 *         });
 *     }
 * } </pre>
 */
public class JQAjaxAdapter extends AjaxAdapter {

    /**
     * @see net.sf.clickclick.AjaxListener#onAjaxAction(org.apache.click.Control)
     *
     * @param source the source of the action event
     * @return the partial response to render, or null if no response should
     * be rendered
     */
    @Override
    public Partial onAjaxAction(Control source) {
        JQEvent event = JQEvent.lookup(JQEvent.EVENT_PARAM);
        return onAjaxAction(source, event);
    }

    /**
     * This method is invoked for ajax requests and returns a
     * {@link net.sf.clickclick.util.Partial} response.
     * <p/>
     * A Partial instance represents a partial response to a user request.
     *
     * @param source the source of the action event
     * @event event the event that triggered the Ajax action
     * @return the partial response to render, or null if no response should
     * be rendered
     */
    public Partial onAjaxAction(Control source, JQEvent event) {
        return null;
    }

}
