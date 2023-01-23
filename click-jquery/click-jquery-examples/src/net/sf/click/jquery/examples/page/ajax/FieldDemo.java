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
import net.sf.click.jquery.examples.control.html.Span;
import net.sf.click.jquery.examples.control.html.Text;
import net.sf.click.jquery.examples.page.BorderPage;
import net.sf.click.jquery.taconite.JQTaconite;
import org.apache.click.Control;
import org.apache.click.ActionResult;
import org.apache.click.control.Field;
import org.apache.click.control.TextField;

/**
 * Demonstrates how to update a label while editing a Field using Ajax.
 *
 * In this demo the JQHelper is used to "decorate" a TextField with Ajax
 * functionality.
 */
public class FieldDemo extends BorderPage {

	private static final long serialVersionUID = 1L;

    private Field field = new TextField("field");
    private Span label = new Span("label", "label");

    public FieldDemo() {

        // Register an Ajax listener on the field which is invoked on every
        // "keyup" event.
        field.addBehavior(new JQBehavior() {

            @Override
            public ActionResult onAction(Control source, JQEvent event) {
                JQTaconite actionResult = new JQTaconite();

                // Set the label content to the latest field value
                label.add(new Text(field.getValue()));
 
                // Replace the label in the browser with the new one
                actionResult.replace(label);
                return actionResult;
            }
        });

        // Set Ajax to fire on keyup events
        JQBehavior behavior = new JQBehavior(JQEvent.KEYUP);
        field.addBehavior(behavior);

        // Switch off the Ajax busy indicator
        behavior.setShowBusyIndicator(false);

        // Delay Ajax invoke for 350 millis, otherwise too many Ajax requests
        // are made to the server
        behavior.setDelay(350);

        addControl(field);
        addControl(label);
    }
}
