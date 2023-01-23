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
package net.sf.click.jquery.examples.control;

import net.sf.click.jquery.examples.control.html.Div;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.click.jquery.behavior.JQBehavior;
import org.apache.click.control.TextField;
import org.apache.click.util.HtmlStringBuffer;
import org.apache.click.element.CssImport;
import org.apache.click.element.Element;
import org.apache.click.element.JsImport;
import org.apache.click.element.JsScript;

/**
 * Provides a ColorPicker control based on the JQuery ColorPicker plugin:
 * http://www.eyecon.ro/colorpicker/.
 * <p/>
 * Example usage:
 *
 * <pre class="prettyprint">
 * private Form form = new Form("form");
 *
 * public MyPage() {
 *     JQColorPicker colorPicker = new JQColorPicker("colorPicker");
 *     colorPicker.setValue("#FFFFFF"); // Set color to white
 *     form.add(colorPicker);
 *
 *     form.add(new Submit("submit"));
 *     addControl(form);
 * } </pre>
 */
public class JQColorPicker extends TextField {

    // -------------------------------------------------------------- Constants

    /**
     * The ColorPicker JS library:
     * "<tt>/click-jquery/example/colorpicker/js/colorpicker.js</tt>".
     */
    public static String colorPickerJsImport =
        "/click-jquery/example/colorpicker/js/colorpicker.js";

    /**
     * The ColorPicker default CSS:
     * "<tt>/click-jquery/example/colorpicker/css/colorpicker.css</tt>".
     */
    public static String colorPickerCssImport =
        "/click-jquery/example/colorpicker/css/colorpicker.css";

    /**
     * The ColorPicker CSS Template:
     * "<tt>/click-jquery/example/colorpicker/jquery.colorpicker.style.css</tt>".
     */
    public static String colorPickerStyleImport =
        "/click-jquery/example/colorpicker/jquery.colorpicker.style.css";

    // -------------------------------------------------------------- Variables

    /** The color picker image div. */
    protected Div image = new Div();

    /**
     * The ColorPicker JavaScript template:
     * "<tt>/click-jquery/example/template/colorpicker/jquery.colorpicker.template.js</tt>".
     */
    protected String template = "/click-jquery/example/template/colorpicker/jquery.colorpicker.template.js";

    /** The color picker JavaScript template model. */
    protected Map jsModel;

    // ----------------------------------------------------------- Constructors

    /**
     * Create a default JQColorPicker.
     */
    public JQColorPicker() {
    }

    /**
     * Create a JQColorPicker with the given name.
     *
     * @param name the name of the control
     */
    public JQColorPicker(String name) {
        if (name != null) {
            setName(name);
        }
    }

    /**
     * Create a JQColorPicker with the given name and required status.
     *
     * @param name the name of the control
     * @param required the field required status
     */
    public JQColorPicker(String name, boolean required) {
        if (name != null) {
            setName(name);
        }
        setRequired(required);
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Set the name of the color picker.
     *
     * @param name the name of the control
     */
    @Override
    public void setName(String name) {
        super.setName(name);
        image.setId(getId() + "_image");
    }

    /**
     * Return the color picker JavaScript template.
     *
     * @return the template the JavaScript template
     */
    public String getTemplate() {
        return template;
    }

    /**
     * Set the color picker JavaScript template.
     *
     * @param template the JavaScript template
     */
    public void setTemplate(String template) {
        this.template = template;
    }

    /**
     * Return the JavaScript template model.
     *
     * @return the JavaScript template model
     */
    public Map getJsModel() {
        if(jsModel == null) {
            jsModel = new HashMap();
        }
        return jsModel;
    }

    /**
     * Set the JavaScript template model.
     *
     * @param model the JavaScript template model
     */
    public void setJsModel(Map model) {
        this.jsModel = model;
    }

    /**
     * Return the JQColorPicker resources: {@link #colorPickerCssImport},
     * {@link net.sf.click.jquery.behavior.AbstractJQBehavior#jqueryPath},
     * {@link #colorPickerJsImport},
     * {@link #template} and
     * {@link #colorPickerStyleImport}.
     *
     * @return the list of head elements
     */
    @Override
    public List<Element> getHeadElements() {
        if (headElements == null) {
            headElements = super.getHeadElements();

            headElements.add(new CssImport(colorPickerCssImport));
            headElements.add(new JsImport(JQBehavior.jqueryPath));
            headElements.add(new JsImport(colorPickerJsImport));
        }

        addJsTemplate(headElements);
        addCustomCssImport(headElements);
        return headElements;
    }

    /**
     * Render the HTML representation of the JQColorPicker.
     *
     * @param buffer the buffer to render to
     */
    @Override
    public void render(HtmlStringBuffer buffer) {
        super.render(buffer);

        image.setAttribute("class", "colorPickerImage");

        Div colorPickerBorder = new Div();
        colorPickerBorder.setAttribute("class", "colorPickerSelector");
        image.add(colorPickerBorder);

        Div colorPickerBackground = new Div();
        colorPickerBorder.add(colorPickerBackground);
        colorPickerBackground.setAttribute("style", "background-color: #"
            + getValue());

        image.render(buffer);
    }

    // ------------------------------------------------------ Protected Methods

    /**
     * Add the JQColorPicker JavaScript {@link #template} to the list of head elements.
     * <p/>
     * You can override this method to add your own template.
     *
     * @param headElements the list of head elements to include for this control
     */
    protected void addJsTemplate(List headElements) {
        String id = getId();
        if (id == null) {
            throw new IllegalStateException("Color picker name is not set.");
        }

        JsScript jsScript = new JsScript();
        jsScript.setId(id + "_jqcolorpicker_js");

        if (!headElements.contains(jsScript)) {
            jsScript.setExecuteOnDomReady(true);

            // Create the data model to pass to the templates
            Map model = getJsModel();
            model.put("fieldId", id);
            model.put("imageId", image.getId());

            jsScript.setModel(model);
            jsScript.setTemplate(getTemplate());
            headElements.add(jsScript);
        }
    }

    /**
     * Add the JQColorPicker CSS style import to the list of head elements.
     * <p/>
     * The CSS style import is: <tt>{@link #colorPickerStyleImport}</tt>.
     * <p/>
     * You can override this method to add your own custom import.
     *
     * @param headElements the list of head elements to include for this control
     */
    protected void addCustomCssImport(List headElements) {
        headElements.add(new CssImport(colorPickerStyleImport));
    }
}
