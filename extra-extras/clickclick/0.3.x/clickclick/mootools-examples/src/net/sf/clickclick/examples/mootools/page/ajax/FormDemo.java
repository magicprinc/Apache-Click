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
package net.sf.clickclick.examples.mootools.page.ajax;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import net.sf.clickclick.util.AjaxAdapter;
import org.apache.click.Context;
import org.apache.click.Control;
import org.apache.click.element.CssStyle;
import org.apache.click.element.Element;
import org.apache.click.element.JsScript;
import org.apache.click.control.Checkbox;
import org.apache.click.control.Option;
import org.apache.click.control.Select;
import org.apache.click.control.Submit;
import org.apache.click.control.TextField;
import org.apache.click.extras.control.EmailField;
import org.apache.click.util.HtmlStringBuffer;
import net.sf.clickclick.util.Partial;
import net.sf.clickclick.examples.mootools.page.BorderPage;
import net.sf.clickclick.mootools.controls.MTAjaxForm;

public class FormDemo extends BorderPage {

    private MTAjaxForm form = new MTAjaxForm("myForm");

    /**
     * Build form and fields
     */
    public void onInit() {
        super.onInit();
        form.addStyleClass("form_box");
        
        TextField textField = new TextField("firstName");
        textField.setValue("John");
        form.add(textField);

        textField = new TextField("lastName");
        textField.setValue("Q");
        form.add(textField);

        EmailField emailField = new EmailField("email", "E-Mail");
        emailField.setSize(20);
        emailField.setValue("john.q@mootools.net");
        form.add(emailField);

        Checkbox checkbox = new Checkbox("mooTooler");
        checkbox.setValue("yes");
        checkbox.setChecked(true);
        form.add(checkbox);

        Select select = new Select("new", "New to Mootools");
        select.add(new Option("yes"));
        select.add(new Option("no"));
        form.add(select);
        form.add(new Submit("submit"));
        
        addControl(form);

        // Set AjaxAdapter on Form that will be invoked when form is submitted
        form.setActionListener(new AjaxAdapter() {

            public Partial onAjaxAction(Control source) {
                System.out.println("Form saved to database");
                // Return a Partial response
                return createPartial();
            }
        });
    }

    public List<Element> getHeadElements() {
        if (headElements == null) {
            headElements = super.getHeadElements();

            Map model = new HashMap();
            Context context = getContext();
            String contextPath = context.getRequest().getContextPath();
            model.put("context", contextPath);

            String cssTemplate = "/ajax/form-demo.css";
            headElements.add(new CssStyle(context.renderTemplate(cssTemplate, model)));

            String jsTemplate = "/ajax/form-demo.js";
            headElements.add(new JsScript(context.renderTemplate(jsTemplate, model)));
        }
        return headElements;
    }

    /**
     * Create a Partial response
     */
    private Partial createPartial() {
        HtmlStringBuffer buffer = new HtmlStringBuffer();
        buffer.append("<pre>Map {\n");
        buffer.append("    [firstName] => ").append(form.getFieldValue("firstName")).append("\n");
        buffer.append("    [lastName] => ").append(form.getFieldValue("lastName")).append("\n");
        buffer.append("    [email] => ").append(form.getFieldValue("email")).append("\n");
        buffer.append("    [mootooler] => ").append(form.getFieldValue("mooTooler")).append("\n");
        buffer.append("    [new] => ").append(form.getFieldValue("new")).append("\n");
        buffer.append("}</pre>");
        return new Partial(buffer.toString());
    }
}

