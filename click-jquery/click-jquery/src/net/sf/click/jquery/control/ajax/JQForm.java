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
package net.sf.click.jquery.control.ajax;

import java.util.List;
import net.sf.click.jquery.behavior.JQFormBehavior;
import org.apache.click.control.Field;
import org.apache.click.control.Form;
import org.apache.click.control.HiddenField;
import org.apache.click.element.Element;
import org.apache.click.element.JsScript;
import org.apache.click.util.HtmlStringBuffer;
import org.apache.commons.lang.StringUtils;

/**
 * Provide an Ajax enabled Form control.
 * <p/>
 * <b>Please note:</b> JQForm uses {@link net.sf.click.jquery.behavior.JQFormBehavior}
 * to provide the Ajax functionality.
 * <p/>
 * Below is an example showing how to use the JQForm:
 *
 * <pre class="prettyprint">
 * private JQForm form = new JQForm("form");
 *
 * public MyPage() {
 *
 *     addControl(form);
 *
 *     // Setup fields
 *     form.add(new TextField("firstName", true));
 *     form.add(new TextField("lastName", true));
 *     form.add(new EmailField("email", "E-Mail"));
 *
 *     Submit submit = new Submit("submit");
 *     form.add(submit);
 *
 *     // Set an Ajax listener on the Submit button, which will be invoked when
 *     // form is submitted
 *     submit.setActionListener(new AjaxAdapter() {
 *
 *        &#64;Override
 *        public ActionResult onAjaxAction(Control source) {
 *            Taconite actionResult = new Taconite();
 *
 *            if (form.isValid()) {
 *                // Save the form data to the database
 *                saveForm();
 *            }
 *
 *            // Update the form
 *            actionResult.replace(form);
 *
 *            return actionResult;
 *        }
 *    });
 * } </pre>
 */
public class JQForm extends Form {

    // -------------------------------------------------------------- Variables

    /** The JQuery Behavior object. */
    protected JQFormBehavior jqBehavior;

    /** The JavaScript focus function HEAD element. */
    protected JsScript focusScript;

    // ----------------------------------------------------------- Constructors

    /**
     * Create a default JQForm.
     */
    public JQForm() {
    }

    /**
     * Create a JQForm with the given name.
     *
     * @param name the name of the form
     */
    public JQForm(String name) {
        super(name);
    }

    // ------------------------------------------------------ Public Properties

    /**
     * Return the JQuery Behavior instance.
     *
     * @return the jqBehavior instance
     */
    public JQFormBehavior getJQBehavior() {
        if (jqBehavior == null) {
             jqBehavior = new JQFormBehavior();
        }
        return jqBehavior;
    }

    /**
     * Set the JQuery Behavior instance.
     *
     * @param jqBehavior the JQuery Behavior instance
     */
    public void setJQBehavior(JQFormBehavior jqBehavior) {
        this.jqBehavior = jqBehavior;
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Add the {@link #getJQBehavior()} to the control.
     */
    @Override
    public void onInit() {
        super.onInit();
        addBehavior(getJQBehavior());
    }

    /**
     * Render the link to the given buffer.
     * <p/>
     * This method delegates to {@link #addIdField()} to add a hidden field
     * containing the Form's ID which ensures Ajax requests from this form are
     * identified.
     *
     * @param buffer the buffer to render to
     */
    @Override
    public void render(HtmlStringBuffer buffer) {
        addIdField();
        super.render(buffer);
    }

    // ------------------------------------------------------ Protected Methods

    /**
     * Adds a hidden Field containing the Form's ID which ensures Ajax requests
     * from this form can be identified.
     */
    protected void addIdField() {
        // Add the Form ID as a HiddenField to trigger Ajax callback
        String id = getId();
        if (getField(id) == null) {
            add(new HiddenField(id, "1"));
        }
    }

    /**
     * Return JQForm HEAD elements.
     *
     * @return the JQForm HEAD elements
     */
    @Override
    public List<Element> getHeadElements() {
        if (headElements == null) {
            headElements = super.getHeadElements();

            if (getContext().isAjaxRequest()) {
                focusScript = new JsScript();
                headElements.add(focusScript);
            }
        }
        return headElements;
    }

    /**
     * Render the Form field focus JavaScript to the string buffer.
     *
     * @param buffer the StringBuffer to render to
     * @param formFields the list of form fields
     */
    @Override
    protected void renderFocusJavaScript(HtmlStringBuffer buffer, List<Field> formFields) {
        if (getContext().isAjaxRequest()) {
            HtmlStringBuffer tempBuf = new HtmlStringBuffer();
            super.renderFocusJavaScript(tempBuf, formFields);
            String temp = tempBuf.toString();
            if (StringUtils.isBlank(temp)) {
                return;
            }
            String prefix = "<script type=\"text/javascript\"><!--";
            temp = temp.substring(prefix.length());
            String suffix = "//--></script>\n";
            int end = temp.indexOf(suffix);
            temp = temp.substring(0, end);
            focusScript.setContent(temp);
        } else {
            super.renderFocusJavaScript(buffer, formFields);
        }
    }

    /**
     * Render the given form start tag and the form hidden fields to the given
     * buffer.
     *
     * @param buffer the HTML string buffer to render to
     * @param formFields the list of form fields
     */
    @Override
    protected void renderHeader(HtmlStringBuffer buffer, List formFields) {
        if (isJavaScriptValidation()) {
            // The default implementation renders an inline onsubmit handler on form.
            // Here we skip rendering that inline onsubmit handler which is instead
            // handled by the jquery.form.template.js
            setJavaScriptValidation(false);
            super.renderHeader(buffer, formFields);
            setJavaScriptValidation(true);
        } else {
            super.renderHeader(buffer, formFields);
        }
    }
}
