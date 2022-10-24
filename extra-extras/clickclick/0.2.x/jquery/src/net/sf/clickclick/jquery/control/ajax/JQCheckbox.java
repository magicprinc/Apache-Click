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
import net.sf.clickclick.jquery.helper.JQHelper;
import net.sf.clickclick.jquery.util.JQEvent;
import net.sf.clickclick.util.AjaxUtils;
import org.apache.click.control.Checkbox;

/**
 * Provide an Ajax enabled Checkbox control.
 * <p/>
 * <b>Please note:</b> JQCheckbox uses {@link net.sf.clickclick.jquery.helper.JQHelper}
 * to provide the Ajax functionality.
 * <p/>
 * Below is an example showing how to use a JQCheckbox:
 *
 * <pre class="prettyprint">
 * private Form form = new Form("form");
 *
 * public MyPage() {
 *     addControl(form);
 *
 *     JQCheckbox checkbox = new JQCheckbox("check");
 *
 *     // Set an Ajax listener on the checkbox that return a Taconite (Partial)
 *     // instance
 *     checkbox.setActionListener(new AjaxAdapter() {
 *
 *         &#64;Override
 *         public Partial onAjaxAction(Control source) {
 *             Taconite partial = new Taconite();
 *
 *             // When a checkbox is clicked, we print the isChecked value to
 *             // the element with the ID '#myTarget'.
 *             partial.prepend("#myTarget", "<p>You have " + (checkbox.isChecked() ? "checked" : "unchecked") + " the checkbox.</p>");
 *
 *             return partial;
 *         }
 *     });
 *
 *     form.add(select);
 * } </pre>
 */
public class JQCheckbox extends Checkbox {

    // -------------------------------------------------------------- Variables

    /** The JQuery helper object. */
    protected JQHelper jqHelper;

    // ------------------------------------------------------------ Constructor

    /**
     * Create a default JQSelect field.
     */
    public JQCheckbox() {
    }

    /**
     * Create a JQSelect field with the given name.
     *
     * @param name the name of the control
     */
    public JQCheckbox(String name) {
        super(name);
    }

    /**
     * Create a JQSelect field with the given name and label.
     *
     * @param name the name of the control
     * @param label the label of the control
     */
    public JQCheckbox(String name, String label) {
        super(name, label);
    }

    /**
     * Create a JQSelect field with the given name and required status.
     *
     * @param name the name of the field
     * @param required the field required status
     */
    public JQCheckbox(String name, boolean required) {
        super(name, required);
    }

    /**
     * Create a JQSelect field with the given name, label and required status.
     *
     * @param name the name of the field
     * @param label the label of the field
     * @param required the field required status
     */
    public JQCheckbox(String name, String label, boolean required) {
        super(name, label);
        setRequired(required);
    }

    // ------------------------------------------------------ Public Properties

    /**
     * Return the JQuery Helper instance.
     *
     * @return the jqHelper instance
     */
    public JQHelper getJQueryHelper() {
        if (jqHelper == null) {
            jqHelper = new JQHelper(this);
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
     * Initialize the JQSelect Ajax functionality.
     */
    @Override
    public void onInit() {
        super.onInit();
        AjaxControlRegistry.registerAjaxControl(this);
    }

    /**
     * Return control HEAD elements.
     *
     * @return the control HEAD elements
     */
    @Override
    public List getHeadElements() {
        if (headElements == null) {
            headElements = super.getHeadElements();

            bind(JQEvent.CLICK);
            getJQueryHelper().addHeadElements(headElements);
        }
        return headElements;
    }

    /**
     * Synonymous to JQuery <a href="http://docs.jquery.com/Events/bind">bind</a>
     * / <a href="http://docs.jquery.com/Events/live">live</a> functionality.
     *
     * @see net.sf.clickclick.jquery.helper.JQHelper#bind(java.lang.String, net.sf.clickclick.jquery.util.JQEvent)
     *
     * @param event the event to bind the control to
     */
    public void bind(JQEvent event) {
        getJQueryHelper().bind(AjaxUtils.getSelector(this), event);
    }
}
