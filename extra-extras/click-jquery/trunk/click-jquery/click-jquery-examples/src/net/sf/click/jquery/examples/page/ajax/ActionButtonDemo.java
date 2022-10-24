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
package net.sf.click.jquery.examples.page.ajax;

import net.sf.click.jquery.JQEvent;
import net.sf.click.jquery.behavior.JQBehavior;
import net.sf.click.jquery.examples.control.JQActionButton;
import net.sf.click.jquery.examples.page.BorderPage;
import net.sf.click.jquery.taconite.JQTaconite;
import org.apache.click.Control;
import org.apache.click.ActionResult;
import org.apache.commons.lang.math.NumberUtils;

public class ActionButtonDemo extends BorderPage {

	private static final long serialVersionUID = 1L;

    private JQActionButton button = new JQActionButton("button", "Counter");

    public ActionButtonDemo() {
        addControl(button);
        button.addBehavior(new JQBehavior() {

            @Override
            public ActionResult onAction(Control source, JQEvent event) {
                JQTaconite actionResult = new JQTaconite();

                incrementCounter();

                // Replace the button with the updated button
                actionResult.replace(button);

                // Using a CSS selector, replace the target counter with the
                // updated button value
                actionResult.replaceContent("#target", button.getValue());
                return actionResult;
            }
        });
    }

    private void incrementCounter() {
        int value = NumberUtils.toInt(button.getValue());
        value++;
        button.setValue(Integer.toString(value));
    }
}
