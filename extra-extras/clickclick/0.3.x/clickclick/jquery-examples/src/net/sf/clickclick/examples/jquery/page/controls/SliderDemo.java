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
package net.sf.clickclick.examples.jquery.page.controls;

import java.util.HashMap;
import java.util.List;
import net.sf.clickclick.examples.jquery.control.ui.UISliderField;
import net.sf.clickclick.examples.jquery.page.BorderPage;
import org.apache.click.control.Form;
import org.apache.click.control.HiddenField;
import org.apache.click.control.Submit;
import org.apache.click.control.TextField;
import org.apache.click.element.Element;
import org.apache.click.element.JsScript;

public class SliderDemo extends BorderPage {

    public SliderDemo() {
        Form form = new Form("form");
        addControl(form);

        form.add(new TextField("firstname"));
        form.add(new TextField("lastname"));
        HiddenField ageField = new HiddenField("age", Integer.class);
        form.add(ageField);
        form.add(new UISliderField("slider"));

        form.add(new Submit("submit"));
    }

     public List<Element> getHeadElements() {
        if (headElements == null) {
            headElements = super.getHeadElements();

            // Add calendar-demo.js script
            headElements.add(new JsScript("/controls/slider-demo.js", new HashMap()));
        }
        return headElements;
    }
}
