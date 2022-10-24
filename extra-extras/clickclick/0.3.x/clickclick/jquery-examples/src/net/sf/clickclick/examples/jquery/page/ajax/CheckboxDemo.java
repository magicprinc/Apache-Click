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

import net.sf.clickclick.examples.jquery.page.BorderPage;
import net.sf.clickclick.jquery.Taconite;
import net.sf.clickclick.jquery.control.ajax.JQCheckbox;
import net.sf.clickclick.util.AjaxAdapter;
import net.sf.clickclick.util.Partial;
import org.apache.click.Control;
import org.apache.click.control.Form;

public class CheckboxDemo extends BorderPage {

    private Form form = new Form("form");
    private JQCheckbox checkbox = new JQCheckbox("check");

    public CheckboxDemo() {
        form.add(checkbox);

        checkbox.setActionListener(new AjaxAdapter() {

            @Override
            public Partial onAjaxAction(Control source) {
                Taconite partial = new Taconite();

                partial.prepend("#myTarget", "<p>You have " + (checkbox.isChecked() ? "checked" : "unchecked") + " the checkbox.</p>");

                return partial;
            }
        });

        addControl(form);
    }
}
