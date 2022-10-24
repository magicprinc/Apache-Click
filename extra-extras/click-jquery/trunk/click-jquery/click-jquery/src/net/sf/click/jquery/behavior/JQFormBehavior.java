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
package net.sf.click.jquery.behavior;

import java.util.List;
import java.util.Map;
import net.sf.click.jquery.util.JSONLiteral;
import net.sf.click.jquery.util.JSONWriter;
import org.apache.click.Context;
import org.apache.click.Control;
import org.apache.click.Page;
import org.apache.click.control.Form;
import org.apache.click.control.HiddenField;
import org.apache.click.element.Element;
import org.apache.click.element.JsImport;
import org.apache.click.element.JsScript;
import org.apache.click.util.HtmlStringBuffer;

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
 *             public ActionResult onAjaxAction(Control source) {
 *                 Taconite actionResult = new Taconite();
 *
 *                 // 1. Replace the Form in the browser with the current one
 *                 actionResult.replace(form);
 *
 *                 return actionResult;
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
public class JQFormBehavior extends JQBehavior {

    // -------------------------------------------------------------- Constants

    private static final long serialVersionUID = 1L;

    /**
     * The JQuery Form plugin (http://www.malsup.com/jquery/form/):
     * "<tt>/click-jquery/form/jquery.form.js</tt>".
     */
    public static String jqueryFormPath = "/click-jquery/form/jquery.form.js";

    // -------------------------------------------------------------- Variables

    protected boolean resetForm;

    protected boolean clearForm;

    protected String dataType = "xml";

    // ----------------------------------------------------------- Constructors

    /**
     * Create a JQFormBehavior for the given target Form.
     */
    public JQFormBehavior() {
        setData("X-Requested-With", "XMLHttpRequest");
    }

    // Public Properties ------------------------------------------------------

    /**
     * Return the URL for ajax requests or null if no url was set. If no url was
     * set the Form <tt>"action"</tt> attribute is used.
     *
     * @return the URL for ajax requests or null if no url was set
     */
    @Override
    public String getUrl() {
        return url;
    }

    /**
     * @return the resetForm
     */
    public boolean isResetForm() {
        return resetForm;
    }

    /**
     * @param resetForm the resetForm to set
     */
    public void setResetForm(boolean resetForm) {
        this.resetForm = resetForm;
    }

    /**
     * @return the clearForm
     */
    public boolean isClearForm() {
        return clearForm;
    }

    /**
     * @param clearForm the clearForm to set
     */
    public void setClearForm(boolean clearForm) {
        this.clearForm = clearForm;
    }

    /**
     * @return the dataType
     */
    public String getDataType() {
        return dataType;
    }

    /**
     * @param dataType the dataType to set
     */
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    // Protected Methods ------------------------------------------------------

    @Override
    protected void addHeadElementsOnce(Control source) {
        super.addHeadElementsOnce(source);

        List<Element> headElements = source.getHeadElements();

        JsImport jsImport = new JsImport(jqueryFormPath);
        if (!headElements.contains(jsImport)) {
            headElements.add(jsImport);
        }
    }

    @Override
    protected void addSetupScript(Control source) {
        if (!(source instanceof Form)) {
            throw new IllegalStateException("JQFormBehavior source must be a Form, not a "
                + source.getClass().getSimpleName());
        }

        // Add the Form ID as a HiddenField to trigger Ajax callback
        Form form = (Form) source;

        if (form.getField(form.getId()) == null) {
            form.add(new HiddenField(form.getId(), "1"));
        }

        super.addSetupScript(source);
    }

    @Override
    protected void setupScript(JsScript script, Control source) {
        Map templateModel = createTemplateModel(page, source, getContext());
        templateModel.remove("context");
        String json = new JSONWriter().write(templateModel);

        HtmlStringBuffer buffer = new HtmlStringBuffer();
        buffer.append("jQuery(document).ready(function(){");
        buffer.append("Click.jq.ajaxFormTemplate(");
        buffer.append(json);
        buffer.append(");");

        buffer.append("});");

        script.setContent(buffer.toString());
    }

    @Override
    protected Map<String, Object> createTemplateModel(Page page, Control control, Context context) {
        // Remove data so that super implementation does not serialize data into parameters
        Map<String, Object> localData = getData();
        setData(null);

        Map<String, Object> templateModel = super.createTemplateModel(page, control, context);

        // Restore data
        setData(localData);

        Form form = (Form) control;

        if (form.isJavaScriptValidation()) {
            addModel(templateModel, "jsValidate", true, page, context);

            StringBuilder sb = new StringBuilder("on_");
            sb.append(form.getId());
            sb.append("_submit");

            addModel(templateModel, "jsValidateFunc", new JSONLiteral(sb.toString()), page, context);
        }

        if (hasData()) {
            addModel(templateModel, "data", getData(), page, context);
        }
        if (isResetForm()) {
            addModel(templateModel, "resetForm", true, page, context);
        }
        if (isClearForm()) {
            addModel(templateModel, "clearForm", true, page, context);
        }
        return templateModel;
    }

    @Override
    protected void addJSDebugScript(List<Element> headElements) {
        JsScript jsScript = new JsScript();
        jsScript.setId("enable_js_debugging");
        if (headElements.contains(jsScript)) {
            return;
        }

        HtmlStringBuffer buffer = new HtmlStringBuffer(100);
        buffer.append("if (typeof jQuery !== 'undefined') {\n");
        buffer.append("  if (typeof jQuery.taconite !== 'undefined') {jQuery.taconite.debug=true}\n");
        buffer.append("  if (typeof Click !== 'undefined' && typeof Click.jq !== 'undefined') {Click.jq.debug = true}\n");
        buffer.append("  if (typeof jQuery.fn.ajaxSubmit !== 'undefined') {jQuery.fn.ajaxSubmit.debug = true}\n");
        buffer.append("}");
        jsScript.setContent(buffer.toString());
        headElements.add(jsScript);
    }
}
