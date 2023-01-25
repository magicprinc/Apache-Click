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
package net.sf.click.ajax4click.examples.page.basic;

import net.sf.click.ajax4click.MultiActionResult;
import net.sf.click.ajax4click.control.AjaxForm;
import net.sf.click.ajax4click.examples.page.BorderPage;
import net.sf.click.ajax4click.jquery.JQBehavior;
import net.sf.click.ajax4click.jquery.JQEvent;
import net.sf.click.extras.control.CalendarField;
import org.apache.click.ActionResult;
import org.apache.click.Control;
import org.apache.click.control.ActionLink;
import org.apache.click.control.Submit;
import org.apache.click.control.TextField;

/**
 * Note: this example uses Click-Calendar instead of Click's DateField (based on Prototype.js)
 * which clashes with jQuery.
 */
public class LoadFormDynamically extends BorderPage {

    private AjaxForm form;

    @Override
    public void onInit() {
        super.onInit();
        addModel("title", "Load Form Dynamically");
        ActionLink link = new ActionLink("link", "Load Form");
        link.setId("linkId");
        addControl(link);

        buildForm();

        // Form is loaded through Ajax. Only add form if it was posted
        if (form.isFormSubmission()) {
            addControl(form);
        }

        link.addBehavior(new JQBehavior() {

            @Override
            public ActionResult onAction(Control source, JQEvent event) {
                MultiActionResult result = new MultiActionResult();
                result.add(form);
                return result;
            }
        });

        addControl(link);
    }

    private void buildForm() {
        form = new AjaxForm("form");
        TextField textField = new TextField("field", true);
        CalendarField calendarField = new CalendarField("date", true);
        Submit submit = new Submit("send");

        form.add(submit);
        form.add(textField);
        form.add(calendarField);
        form.add(submit);

            submit.addBehavior(new JQBehavior() {

            @Override
            public ActionResult onAction(Control source, JQEvent event) {
                MultiActionResult result = new MultiActionResult();
                result.add(form);
                return result;
            }
        });
    }
}
