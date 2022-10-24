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
package net.sf.clickclick.examples.jquery.page.ajax;

import net.sf.clickclick.control.Text;
import net.sf.clickclick.control.html.Span;
import net.sf.clickclick.examples.jquery.page.BorderPage;
import net.sf.clickclick.jquery.helper.JQHelper;
import net.sf.clickclick.jquery.Taconite;
import net.sf.clickclick.jquery.util.JQAjaxAdapter;
import net.sf.clickclick.jquery.util.JQEvent;
import net.sf.clickclick.util.Partial;
import org.apache.click.Control;
import org.apache.click.control.Field;
import org.apache.click.control.TextField;

/**
 * Demonstrates how to update a label while editing a Field using Ajax.
 *
 * In this demo the JQHelper is used to "decorate" a TextField with Ajax
 * functionality.
 */
public class FieldDemo extends BorderPage {

    private Field field = new TextField("field");
    private Span label = new Span("label", "label");

    public FieldDemo() {

        // Register an Ajax listener on the field which is invoked on every
        // "keyup" event.
        field.setActionListener(new JQAjaxAdapter() {

            @Override
            public Partial onAjaxAction(Control source, JQEvent event) {
                Taconite partial = new Taconite();

                // Set the label content to the latest field value
                label.add(new Text(field.getValue()));
 
                // Replace the label in the browser with the new one
                partial.replace(label);
                return partial;
            }
        });

        JQHelper helper = new JQHelper(field);

        // Switch off the Ajax busy indicator
        helper.setShowIndicator(false);

        // Delay Ajax invoke for 350 millis, otherwise too many Ajax requests
        // are made to the server
        helper.setThreshold(350);

        // Set Ajax to fire on keyup events
        helper.setEvent(JQEvent.KEYUP);

        // Ajaxify the the Field
        helper.ajaxify();

        addControl(field);
        addControl(label);
    }
}
