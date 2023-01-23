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
package net.sf.click.jquery.examples.page.ajax.form;

import net.sf.click.jquery.control.ajax.JQForm;
import net.sf.click.jquery.examples.control.html.Div;
import net.sf.click.jquery.examples.control.html.Text;
import org.apache.click.Control;
import net.sf.click.jquery.examples.page.BorderPage;
import net.sf.click.jquery.taconite.JQTaconite;
import org.apache.click.ActionResult;
import org.apache.click.ajax.DefaultAjaxBehavior;
import org.apache.click.control.Form;
import org.apache.click.control.Submit;
import org.apache.click.control.TextField;
import org.apache.click.extras.control.EmailField;

/**
 * This example demonstrates using the ajax aware JQForm (JQuery Form).
 *
 * An AjaxListener is registered on the Form's Submit button which is fired
 * when the Form is submitted via Ajax.
 *
 * If the Form is successfully completed, a success response is returned,
 * otherwise an error response is returned.
 */
public class FormDemo extends BorderPage {

	private static final long serialVersionUID = 1L;

    private Form form = new JQForm("form");

    private Div msgHolder = new Div("msgHolder");

    private Text textMsg = new Text();

    @Override
    public void onInit() {
        super.onInit();

        addControl(form);
        addControl(msgHolder);
        msgHolder.add(textMsg);

        // Setup fields
        form.add(new TextField("firstName", true));
        form.add(new TextField("lastName", true));
        form.add(new EmailField("email", "E-Mail"));

        Submit submit = new Submit("save");
        form.add(submit);

        // Set Behavior on Submit which will be invoked when form is submitted
        submit.addBehavior(new DefaultAjaxBehavior() {

            @Override
            public ActionResult onAction(Control source) {
                System.out.println("Submit clicked!");
                if (form.isValid()) {
                    saveForm();
                    return createSuccessResponse();
                } else {
                    return createErrorResponse();
                }
            }
        });


        Submit cancel = new Submit("cancel");
        form.add(cancel);

        // Set Behavior on Submit which will be invoked when form is submitted
        cancel.addBehavior(new DefaultAjaxBehavior() {

            @Override
            public ActionResult onAction(Control source) {
                System.out.println("Cancel clicked!");
                JQTaconite actionResult = new JQTaconite();

                // Clear the current values and update the Form in the browser
                form.clearValues();
                form.clearErrors();
                actionResult.replace(form);
                return actionResult;
            }
        });
    }

    private void saveForm() {
        System.out.println("Form saved to database");
    }

    /**
     * Return a partial response (using a Taconite action result)
     * that does the following:
     *
     * 1. Replace the Form in the browser with the current Form
     * 2. Style the message holder with a green background which indicates success
     * 3. Replace the message holder with the current message holder
     */
    private ActionResult createSuccessResponse() {
        JQTaconite actionResult = new JQTaconite();

        // 1. Replace the Form in the browser with the current one
        actionResult.replace(form);

        // Set a success message
        textMsg.setText("Successfully submitted Form");

        // 2. Style the message holder with a red background
        msgHolder.setAttribute("style", "color:white; background: green; border: black 1px solid;padding: 5px; float: left");

        // 3. Replace the message holder in the browser with the current one
        actionResult.replace(msgHolder);

        return actionResult;
    }

    /**
     * Return a partial response (using a Taconite action result)
     * that does the following:
     *
     * 1. Replace the Form in the browser with the current Form
     * 2. Style the message holder with a red background which indicates an error
     * 3. Replace the message holder with the current message holder
     */
    private ActionResult createErrorResponse() {
        JQTaconite actionResult = new JQTaconite();

        // 1. Replace the Form in the browser with the current one
        actionResult.replace(form);

        // Set an error message
        textMsg.setText("Form contained errors.");

        // 2. Style the message holder with a red background
        msgHolder.setAttribute("style", "color:white; background: red; border: black 1px solid;padding: 5px; float: left");

        // 3. Replace the message holder in the browser with the current one
        actionResult.replace(msgHolder);

        return actionResult;
    }
}
