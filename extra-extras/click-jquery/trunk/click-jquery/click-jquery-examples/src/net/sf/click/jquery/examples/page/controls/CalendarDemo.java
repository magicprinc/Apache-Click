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
package net.sf.click.jquery.examples.page.controls;

import java.util.List;
import java.util.Map;
import net.sf.click.jquery.examples.control.ui.UICalendarField;
import net.sf.click.jquery.examples.page.BorderPage;
import org.apache.click.control.Form;
import org.apache.click.control.Submit;
import org.apache.click.control.TextField;
import org.apache.click.element.JsScript;
import org.apache.click.util.ClickUtils;

public class CalendarDemo extends BorderPage {

	private static final long serialVersionUID = 1L;

    public CalendarDemo() {
        Form form = new Form("form");
        addControl(form);

        form.add(new TextField("firstname"));
        form.add(new TextField("lastname"));
        form.add(new UICalendarField("calendar"));

        form.add(new Submit("submit"));
    }

    @Override
    public List getHeadElements() {
        if (headElements == null) {
            headElements = super.getHeadElements();

            Map jsModel = ClickUtils.createTemplateModel(this, getContext());
            headElements.add(new JsScript("/controls/calendar-demo.js", jsModel));
        }
        return headElements;
    }
}
