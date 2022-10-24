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
package net.sf.clickclick.jquery.helper;

import java.util.List;
import org.apache.click.control.Form;
import org.apache.click.control.HiddenField;
import org.apache.click.element.JsImport;

/**
 * Provide a specialized JQuery helper that Ajax enables a target Form.
 * <p/>
 * This helper has an associated JavaScript template that can be modified
 * according to your needs. Click <a href="../../../../../js/template/jquery.form.template.js.txt">here</a>
 * to view the template.
 * <p/>
 * JQFormHelper can either be embedded inside a custom Form, or used to decorate
 * the Form.
 *
 * <h3>Embedded example</h3>
 *
 * Below is an example of a custom Form with an embedded JQFormHelper that
 * enables Ajax behavior:
 *
 * <pre class="prettyprint">
 * public class JQForm extends AjaxForm {
 *
 *     // The embedded JQuery Form helper object.
 *     private JQFormHelper jqFormHelper;
 *
 *     // Constructor
 *     public JQForm(String name) {
 *         super(name);
 *     }
 *
 *     // Initialize the Ajax functionality
 *     &#64;Override
 *     public void onInit() {
 *         super.onInit();
 *         AjaxControlRegistry.registerAjaxControl(this);
 *     }
 *
 *     &#64;Override
 *     public List getHeadElements() {
 *         if (headElements == null) {
 *             headElements = super.getHeadElements();
 *             getJQueryHelper().addHeadElements(headElements);
 *         }
 *         return headElements;
 *     }
 *
 *     public JQHelper getJQueryHelper() {
 *         if (jqHelper == null) {
 *             jqHelper = new JQFormHelper(this);
 *         }
 *         return jqHelper;
 *     }
 * } </pre>
 *
 * Below is an example how to decorate a Form to update itself when the
 * form is submitted:
 *
 * <pre class="prettyprint">
 * public class FormDemo extends BorderPage {
 *
 *     private Form form = new Form("form");
 *
 *     public FormDemo() {
 *
 *         form.add(new TextField("firstname");
 *         form.add(new TextField("lastname");
 *         form.add(new IntegerField("age");
 *
 *         // Register an Ajax listener on the form which is invoked when the
 *         // form is submitted.
 *         form.setActionListener(new AjaxAdapter() {
 *             public Partial onAjaxAction(Control source) {
 *                 Taconite partial = new Taconite();
 *
 *                 // 1. Replace the Form in the browser with the current one
 *                 partial.replace(form);
 *
 *                 return partial;
 *             }
 *         });
 *
 *         JQFormHelper helper = new JQFormHelper(form);
 *
 *         // Ajaxify the the Field
 *         helper.ajaxify();
 *
 *         addControl(form);
 *     }
 * } </pre>
 */
public class JQFormHelper extends JQHelper {

    // -------------------------------------------------------------- Constants

    private static final long serialVersionUID = 1L;

    /**
     * The JQuery Form plugin (http://www.malsup.com/jquery/form/):
     * "<tt>/clickclick/jquery/form/jquery-form.js</tt>".
     */
    public static String jqueryFormImport = "/clickclick/jquery/form/jquery.form.js";

    // -------------------------------------------------------------- Variables

    /**
     * The path of the template to render:
     * "<tt>/clickclick/jquery/template/jquery.form.template.js</tt>".
     */
    protected String template = "/clickclick/jquery/template/jquery.form.template.js";

    // ----------------------------------------------------------- Constructors

    /**
     * Create a JQFormHelper for the given target Form.
     *
     * @param form the helper target Form
     */
    public JQFormHelper(Form form) {
        super(form);
        setTemplate(template);
    }

    /**
     * Add the necessary JavaScript imports and scripts to the given
     * headElements list to enable the target Form to submit Ajax requests.
     *
     * @param headElements the list which to add all JavaScript imports and
     * scripts to enable the target Form to submit Ajax requests
     */
    @Override
    public void addHeadElements(List headElements) {
        super.addHeadElements(headElements);
        JsImport jsImport = new JsImport(jqueryFormImport);
        if (!headElements.contains(jsImport)) {
            headElements.add(jsImport);
        }

        // Add the Form ID as a HiddenField to trigger Ajax callback
        Form form = (Form) getControl();

        if (form == null) {
            throw new IllegalStateException("The JQFormHelper#control cannot be null");
        }

        if (form.getField(form.getId()) == null) {
            form.add(new HiddenField(form.getId(), "1"));
        }
    }
}
