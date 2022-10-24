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
package net.sf.click.jquery.examples.page.basic.tooltip1;

import java.util.HashMap;
import java.util.List;
import net.sf.click.jquery.behavior.JQBehavior;
import net.sf.click.jquery.examples.page.BorderPage;
import org.apache.click.control.Form;
import org.apache.click.control.TextField;
import org.apache.click.element.CssImport;
import org.apache.click.element.JsImport;
import org.apache.click.element.JsScript;

/**
 *
 */
public class TooltipPage extends BorderPage {

    private static final long serialVersionUID = 1L;

    @Override
    public void onInit() {
        Form form = new Form("form");

        TextField field = new TextField("firstname");

        // Tooltip is generated from the field title. Tooltip heading and body
        // is separated by the pipe '|' character
        field.setTitle("First Name|Enter your firstname");
        form.add(field);

        field = new TextField("lastname");
        field.setTitle("Last Name|Enter your lastname");
        form.add(field);

        field = new TextField("age");
        field.setTitle("Age|Enter your age");
        form.add(field);

        addControl(form);
    }

    @Override
    public List getHeadElements() {
        if (headElements == null) {
            headElements = super.getHeadElements();

            // Import jquery.js, basic.js and basic.css
            headElements.add(new JsImport(JQBehavior.jqueryPath));
            headElements.add(new JsImport("/click-jquery/example/tooltip1/jquery.cluetip.js"));
            headElements.add(new CssImport("/click-jquery/example/tooltip1/jquery.cluetip.css"));
            headElements.add(new JsScript("/basic/tooltip1/tooltip.js", new HashMap()));
        }
        return headElements;
    }
}
