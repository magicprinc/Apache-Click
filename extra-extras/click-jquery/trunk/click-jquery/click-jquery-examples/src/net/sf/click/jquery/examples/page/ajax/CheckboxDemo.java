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
import net.sf.click.jquery.examples.page.BorderPage;
import net.sf.click.jquery.taconite.JQTaconite;
import org.apache.click.Control;
import org.apache.click.ActionResult;
import org.apache.click.control.Checkbox;
import org.apache.click.control.Form;

public class CheckboxDemo extends BorderPage {

	private static final long serialVersionUID = 1L;

    private Form form = new Form("form");
    private Checkbox checkbox = new Checkbox("check");

    public CheckboxDemo() {
        form.add(checkbox);

        checkbox.addBehavior(new JQBehavior() {

            @Override
            public ActionResult onAction(Control source, JQEvent event) {
                JQTaconite actionResult = new JQTaconite();

                actionResult.prepend("#myTarget", "<p>You have " + (checkbox.isChecked() ? "checked" : "unchecked") + " the checkbox.</p>");

                return actionResult;
            }
        });

        addControl(form);
    }
}
