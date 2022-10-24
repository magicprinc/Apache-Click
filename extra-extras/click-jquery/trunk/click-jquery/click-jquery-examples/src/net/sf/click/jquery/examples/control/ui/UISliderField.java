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
package net.sf.click.jquery.examples.control.ui;

import java.util.List;
import net.sf.click.jquery.behavior.JQBehavior;
import net.sf.click.jquery.examples.control.html.Div;
import net.sf.click.jquery.examples.util.JQUILibrary;
import org.apache.click.control.Field;
import org.apache.click.control.HiddenField;
import org.apache.click.element.CssImport;
import org.apache.click.element.Element;
import org.apache.click.element.JsImport;
import org.apache.click.util.HtmlStringBuffer;

/**
 * Provide a Slider field based on the JQuery UI Slider widget:
 * http://docs.jquery.com/UI/Slider.
 */
public class UISliderField extends Field {

    // -------------------------------------------------------------- Variables

    private HiddenField valueHolder = new HiddenField();

    private Div div = new Div();

    // ----------------------------------------------------------- Constructors

    /**
     * Create a default slider field.
     */
    public UISliderField() {
        this((String) null);
    }

    /**
     * Create a slider field with the given name.
     *
     * @param name the name of the slider field
     */
    public UISliderField(String name) {
        if (name != null) {
            setName(name);
        }
        setAttribute("class", JQUILibrary.style);
    }

    // --------------------------------------------------------- Public Methods

    @Override
    public void setName(String name) {
        super.setName(name);
        div.setName(name + "_container");
        valueHolder.setName(name);
        valueHolder.setValueClass(String.class);
    }

    @Override
    public boolean onProcess() {
        return valueHolder.onProcess();
    }

    /**
     * Return the JQDialog resources:
     * {@link net.sf.click.jquery.behavior.AbstractJQBehavior#jqueryPath},
     * {@link net.sf.click.jquery.examples.util.JQUILibrary#jqueryUICssPath},
     * {@link net.sf.click.jquery.examples.util.JQUILibrary#jqueryUIJsPath}.
     *
     * @return the list of head elements
     */
    @Override
    public List<Element> getHeadElements() {
        if (headElements == null) {
            headElements = super.getHeadElements();

            CssImport cssImport = new CssImport(JQUILibrary.jqueryUICssPath);
            cssImport.setAttribute("media", "screen");
            headElements.add(cssImport);

            JsImport jsImport = new JsImport(JQBehavior.jqueryPath);
            headElements.add(jsImport);

            jsImport = new JsImport(JQUILibrary.jqueryUIJsPath);
            headElements.add(jsImport);
        }
        return headElements;
    }

    @Override
    public void render(HtmlStringBuffer buffer) {
        div.render(buffer);
        valueHolder.render(buffer);
    }
}
