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

import java.text.SimpleDateFormat;
import java.util.Date;
import net.sf.click.jquery.JQEvent;
import net.sf.click.jquery.behavior.JQBehavior;
import net.sf.click.jquery.examples.control.html.Span;
import net.sf.click.jquery.examples.page.BorderPage;
import net.sf.click.jquery.taconite.JQTaconite;
import org.apache.click.Control;
import org.apache.click.ActionResult;
import org.apache.click.control.ActionLink;

public class TextDemo extends BorderPage {

	private static final long serialVersionUID = 1L;

    private ActionLink link = new ActionLink("update", "Request server time");

    private SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private Span label = new Span("label");

    public TextDemo() {
        addControl(link);
        link.setId("updateId");

        label.setText("Server time : " + dateFormat.format(new Date()));
        addControl(label);

        link.addBehavior(new JQBehavior() {

            @Override
            public ActionResult onAction(Control source, JQEvent event) {
                JQTaconite actionResult = new JQTaconite();

                // Using a CSS selector to replace the Label content with the latest
                // Date
                label.setText("Current time : " + dateFormat.format(new Date()));
                actionResult.replace(label);
                return actionResult;
            }
        });
    }
}
