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
package net.sf.clickclick.util;

import net.sf.clickclick.AjaxListener;
import org.apache.click.ActionListener;
import org.apache.click.Control;

/**
 * Provides an abstract adapter for receiving Ajax callbacks.
 */
public class AjaxAdapter implements AjaxListener {

    /**
     * This method is only invoked for non-ajax requests.
     * <p/>
     * For ajax requests {@link #onAjaxAction(Control)} is invoked.
     *
     * @see ActionListener#onAction(Control)
     *
     * @param source the source of the action event
     * @return true if control and page processing should continue or false
     * otherwise.
     */
    public boolean onAction(Control source) {
        return true;
    }

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
    public Partial onAjaxAction(Control source) {
        return null;
    }
}
