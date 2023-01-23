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
package net.sf.click.jquery.examples.page.basic.tooltip2;

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
        form.add(field);
        field.setTitle("Enter your firstname");

        field = new TextField("lastname");
        form.add(field);
        field.setTitle("Enter your lastname");

        field = new TextField("age");
        form.add(field);
        field.setTitle("Enter your age");

        addControl(form);
    }

    @Override
    public List getHeadElements() {
        if (headElements == null) {
            headElements = super.getHeadElements();

            // Import jquery.js, basic.js and basic.css
            headElements.add(new JsImport(JQBehavior.jqueryPath));
            headElements.add(new JsImport("/click-jquery/example/tooltip2/jquery.tools.js"));
            headElements.add(new JsScript("/basic/tooltip2/tooltip.js", new HashMap()));
            headElements.add(new CssImport("/basic/tooltip2/tooltip.css"));
       }
       return headElements;
    }
}
