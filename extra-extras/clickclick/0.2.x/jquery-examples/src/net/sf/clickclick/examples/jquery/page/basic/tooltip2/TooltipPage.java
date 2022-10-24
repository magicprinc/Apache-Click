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
package net.sf.clickclick.examples.jquery.page.basic.tooltip2;

import java.util.HashMap;
import java.util.List;
import net.sf.clickclick.control.Text;
import net.sf.clickclick.control.html.Div;
import net.sf.clickclick.control.html.HtmlLabel;
import net.sf.clickclick.control.html.list.HtmlList;
import net.sf.clickclick.control.html.list.ListItem;
import net.sf.clickclick.examples.jquery.page.BorderPage;
import net.sf.clickclick.jquery.helper.JQHelper;
import org.apache.click.control.Field;
import org.apache.click.control.Form;
import org.apache.click.control.TextField;
import org.apache.click.element.CssImport;
import org.apache.click.element.JsImport;
import org.apache.click.element.JsScript;
import org.apache.click.extras.control.HtmlForm;

/**
 *
 */
public class TooltipPage extends BorderPage {

    public String title = "Basic";

    public void onInit() {
        Form form = new HtmlForm("form");
        HtmlList container = new HtmlList();
        form.add(container);

        TextField field = new TextField("firstname");
        addFieldAndTip(container, field, "Enter your firstname.");

        field = new TextField("lastname");
        form.add(field);
        addFieldAndTip(container, field, "Enter your lastname.");

        field = new TextField("age");
        form.add(field);
        addFieldAndTip(container, field, "Enter your age.");

        addControl(form);
    }

    public List getHeadElements() {
        if (headElements == null) {
            headElements = super.getHeadElements();

            // Import jquery.js, basic.js and basic.css
            headElements.add(new JsImport(JQHelper.jqueryImport));
            headElements.add(new JsImport("/clickclick/example/tooltip2/jquery.tools.min.js"));
            headElements.add(new JsScript("/basic/tooltip2/tooltip.js", new HashMap()));
            headElements.add(new CssImport("/basic/tooltip2/tooltip.css"));
       }
       return headElements;
    }

    private void addFieldAndTip(HtmlList list, Field field, String text) {
        ListItem item = new ListItem();
        list.add(item);
        item.add(new HtmlLabel(field));
        item.add(field);

        Div tip = new Div();
        tip.add(new Text(text));
        tip.setAttribute("class", "tooltip");
        item.add(tip);
    }
}
