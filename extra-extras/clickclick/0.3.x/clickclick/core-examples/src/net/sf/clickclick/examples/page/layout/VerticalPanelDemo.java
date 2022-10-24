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
package net.sf.clickclick.examples.page.layout;

import org.apache.click.control.AbstractControl;
import org.apache.click.control.Checkbox;
import org.apache.click.control.Field;
import org.apache.click.control.TextField;
import org.apache.click.extras.control.HtmlForm;
import org.apache.click.extras.control.IntegerField;
import org.apache.click.util.HtmlStringBuffer;
import net.sf.clickclick.control.html.HtmlLabel;
import net.sf.clickclick.control.html.Div;
import net.sf.clickclick.control.html.Span;
import net.sf.clickclick.control.panel.VerticalPanel;
import net.sf.clickclick.examples.page.BorderPage;

public class VerticalPanelDemo extends BorderPage {

    private HtmlForm form = new HtmlForm("form");

    public void onInit() {
        createLayoutDemo();
        createLayoutWithForm();
    }

    /**
     * Demo 1
     */
    private void createLayoutDemo() {
        VerticalPanel verticalPanel = new VerticalPanel("demo1");

        Div div = new Div();
        // Use normal CSS properties to style the divs
        div.setStyle("background", "red");
        div.setStyle("width", "100px");
        div.setStyle("height", "100px");
        verticalPanel.add(div);
        
        div = new Div();
        div.setStyle("background", "yellow");
        div.setStyle("width", "100px");
        div.setStyle("height", "100px");
        verticalPanel.add(div);

        div = new Div();
        div.setStyle("background", "blue");
        div.setStyle("width", "100px");
        div.setStyle("height", "100px");
        verticalPanel.add(div);

        addControl(verticalPanel);
    }

    /**
     * Demo 2
     */      
    private void createLayoutWithForm() {
        addControl(form);

        VerticalPanel verticalPanel = new VerticalPanel();
        form.add(verticalPanel);

        addField(new TextField("name"), verticalPanel);
        addField(new IntegerField("age"), verticalPanel);
        addField(new Checkbox("married"), verticalPanel);
    }

    private void addField(Field field, VerticalPanel verticalPanel) {
        HtmlLabel label = new HtmlLabel(field);
        verticalPanel.add(new FieldLabelCombo(field, label));
    }

    class FieldLabelCombo extends AbstractControl {
        private Field field;
        private HtmlLabel label;
        public FieldLabelCombo (Field field, HtmlLabel label) {
            this.field = field;
            this.label = label;
        }
        public void render (HtmlStringBuffer buffer) {
            Span span = new Span();
            // Float the label to the left with and set a width of 100 pixels
            span.setAttribute("style", "width:100px; display:block; float:left");
            span.add(label);
            span.render(buffer);

            span = new Span();
            // Float the field to the right
            span.setAttribute("style", "display:block; float:left");
            span.add(field);
            span.render(buffer);
        }
    }
}
