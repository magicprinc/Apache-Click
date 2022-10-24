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
package net.sf.clickclick.jquery.control.ajax;

import java.util.List;
import net.sf.clickclick.AjaxControlRegistry;
import net.sf.clickclick.control.ajax.AjaxForm;
import net.sf.clickclick.jquery.helper.JQFormHelper;
import net.sf.clickclick.jquery.helper.JQHelper;
import org.apache.click.element.JsScript;
import org.apache.click.util.HtmlStringBuffer;
import org.apache.commons.lang.StringUtils;

/**
 * Provide an Ajax enabled Form control.
 * <p/>
 * <b>Please note:</b> JQForm uses {@link net.sf.clickclick.jquery.helper.JQHelper}
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
 *        public Partial onAjaxAction(Control source) {
 *            Taconite partial = new Taconite();
 *
 *            if (form.isValid()) {
 *                // Save the form data to the database
 *                saveForm();
 *            }
 *
 *            // Update the form
 *            partial.replace(form);
 *
 *            return partial;
 *        }
 *    });
 * } </pre>
 */
public class JQForm extends AjaxForm {

    // -------------------------------------------------------------- Variables

    /** The JQuery helper object. */
    protected JQHelper jqHelper;

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
     * Return the JQuery Helper instance.
     *
     * @return the jqHelper instance
     */
    public JQHelper getJQueryHelper() {
        if (jqHelper == null) {
             jqHelper = new JQFormHelper(this);
        }
        return jqHelper;
    }

    /**
     * Set the JQuery Helper instance.
     *
     * @param jqHelper the JQuery Helper instance
     */
    public void setJQueryHelper(JQHelper jqHelper) {
        this.jqHelper = jqHelper;
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Initialize the JQForm Ajax functionality.
     */
    @Override
    public void onInit() {
        super.onInit();
        AjaxControlRegistry.registerAjaxControl(this);
    }

    /**
     * Return JQForm HEAD elements.
     *
     * @return the JQForm HEAD elements
     */
    @Override
    public List getHeadElements() {
        if (headElements == null) {
            headElements = super.getHeadElements();

            getJQueryHelper().addHeadElements(headElements);

            if (getContext().isAjaxRequest()) {
                focusScript = new JsScript();
                headElements.add(focusScript);
            }
        }
        return headElements;
    }

    /**
     * Set the JavaScript client side form validation flag.
     *
     * @param validate the JavaScript client side validation flag
     */
    @Override
    public void setJavaScriptValidation(boolean validate) {
        super.setJavaScriptValidation(validate);
        getJQueryHelper().getModel().put("javascriptValidate", validate);
    }

    /**
     * Render the Form field focus JavaScript to the string buffer.
     *
     * @param buffer the StringBuffer to render to
     * @param formFields the list of form fields
     */
    @Override
    protected void renderFocusJavaScript(HtmlStringBuffer buffer, List formFields) {
        if (!getContext().isAjaxRequest()) {
            super.renderFocusJavaScript(buffer, formFields);
        } else {
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
        if (getJavaScriptValidation()) {
            // The default implementation renders an inline onsubmit handler on form.
            // Here we skip rendering that inilne onsubmit handler which is instead
            // handled by the jquery.form.template.js
            setJavaScriptValidation(false);
            super.renderHeader(buffer, formFields);
            setJavaScriptValidation(true);
        } else {
            super.renderHeader(buffer, formFields);
        }
    }
}
