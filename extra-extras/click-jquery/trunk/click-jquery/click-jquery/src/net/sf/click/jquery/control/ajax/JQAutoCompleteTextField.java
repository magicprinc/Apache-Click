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
import net.sf.click.jquery.behavior.JQAutoCompleteBehavior;
import org.apache.click.control.TextField;

/**
 * Provide an AutoCompleteTextField control.
 * <p/>
 * <b>Please note:</b> JQAutoCompleteTextField uses {@link net.sf.click.jquery.behavior.JQAutoCompleteBehavior}
 * to provide the Ajax functionality.
 * <p/>
 * Below is an example showing how to use the auto-complete text field:
 *
 * <pre class="prettyprint">
 * private Form form = new Form("form");
 *
 * public MyPage() {
 *     addControl(form);
 *
 *     final JQAutoCompleteTextField autoField = new JQAutoCompleteTextField("autoField") {
 *
 *         // When the user enters text into the field, this method is called,
 *         // passing in the current value of the text field
 *         &#64;Override
 *         public List getAutoCompleteList(String criteria) {
 *             List suggestions = getPostCodeService().getPostCodeLocations(criteria);
 *             return suggestions;
 *         }
 *     };
 * } </pre>
 */
public abstract class JQAutoCompleteTextField extends TextField {

    // -------------------------------------------------------------- Variables

    /** The JQuery helper object. */
    protected JQAutoCompleteBehavior jqBehavior;

    // ----------------------------------------------------------- Constructors

    /**
     * Construct the JQAutoCompleteTextField with the given name.
     * The default text field size is 20 characters.
     *
     * @param name the name of the field
     */
    public JQAutoCompleteTextField(String name) {
        super(name);
    }

    /**
     * Construct the JQAutoCompleteTextField with the given name and required
     * status. The default text field size is 20 characters.
     *
     * @param name the name of the field
     * @param required the field required status
     */
    public JQAutoCompleteTextField(String name, boolean required) {
        super(name);
        setRequired(required);
    }

    /**
     * Construct the JQAutoCompleteTextField with the given name and label.
     * The default text field size is 20 characters.
     *
     * @param name the name of the field
     * @param label the label of the field
     */
    public JQAutoCompleteTextField(String name, String label) {
        super(name, label);
    }

    /**
     * Construct the JQAutoCompleteTextField with the given name, label and
     * required status. The default text field size is 20 characters.
     *
     * @param name the name of the field
     * @param label the label of the field
     * @param required the field required status
     */
    public JQAutoCompleteTextField(String name, String label, boolean required) {
        super(name, label, required);
    }

    /**
     * Construct the JQAutoCompleteTextField with the given name, label and size.
     *
     * @param name the name of the field
     * @param label the label of the field
     * @param size the size of the field
     */
    public JQAutoCompleteTextField(String name, String label, int size) {
        super(name, label, size);
    }

    /**
     * Create a JQAutoCompleteTextField with no name defined.
     * <p/>
     * <b>Please note</b> the control's name must be defined before it is valid.
     */
    public JQAutoCompleteTextField() {
    }

    // ------------------------------------------------------ Public Properties

    /**
     * Return the JQuery Helper instance.
     *
     * @return the jqHelper instance
     */
    public JQAutoCompleteBehavior getJQBehavior() {
        if(jqBehavior == null) {
            jqBehavior = new JQAutoCompleteBehavior() {

                @Override
                protected List getAutoCompleteList(String criteria) {
                    return JQAutoCompleteTextField.this.getAutoCompleteList(criteria);
                }
            };
        }

        return jqBehavior;
    }

    /**
     * Set the JQuery Behavior instance.
     *
     * @param jqBehavior the JQuery Behavior instance
     */
    public void setJQBehavior(JQAutoCompleteBehavior jqBehavior) {
        this.jqBehavior = jqBehavior;
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Initialize the JQAutoCompleteTextField Ajax functionality.
     */
    @Override
    public void onInit() {
        super.onInit();
        addBehavior(getJQBehavior());
    }

    // Protected Methods ------------------------------------------------------

    protected abstract List getAutoCompleteList(String criteria);
}
