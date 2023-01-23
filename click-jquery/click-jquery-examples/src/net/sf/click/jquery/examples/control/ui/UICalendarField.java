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

import java.util.ArrayList;
import java.util.List;
import net.sf.click.jquery.behavior.JQBehavior;
import net.sf.click.jquery.examples.util.JQUILibrary;
import org.apache.click.element.CssImport;
import org.apache.click.element.Element;
import org.apache.click.element.JsImport;
import org.apache.click.extras.control.DateField;
import org.apache.click.util.HtmlStringBuffer;

/**
 * Provide a Calendar field based on the JQuery UI Datepicker widget:
 * http://docs.jquery.com/UI/Datepicker.
 */
public class UICalendarField extends DateField {

    // ----------------------------------------------------------- Constructors

    /**
     * Create a default dialog.
     */
    public UICalendarField() {
        this(null);
    }

    /**
     * Create a dialog with the given name.
     *
     * @param name the name of the dialog
     */
    public UICalendarField(String name) {
        super(name);
        setAttribute("class", JQUILibrary.style);
    }

    // --------------------------------------------------------- Public Methods

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
            headElements = new ArrayList<Element>(0);

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

    // ------------------------------------------------------ Protected Methods

    /**
     * This method renders nothing since JQuery takes care of the button.
     *
     * @param buffer the buffer to render output to
     */
    @Override
    protected void renderCalendarButton(HtmlStringBuffer buffer) {
    }
}
