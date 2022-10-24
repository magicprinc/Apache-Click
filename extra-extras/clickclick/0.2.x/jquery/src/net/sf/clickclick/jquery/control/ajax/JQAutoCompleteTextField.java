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

import java.util.Iterator;
import java.util.List;
import net.sf.clickclick.AjaxControlRegistry;
import net.sf.clickclick.jquery.helper.JQAutoCompleteHelper;
import net.sf.clickclick.jquery.helper.JQHelper;
import net.sf.clickclick.jquery.util.JQEvent;
import net.sf.clickclick.util.AjaxAdapter;
import net.sf.clickclick.util.AjaxUtils;
import net.sf.clickclick.util.Partial;
import org.apache.click.Control;
import org.apache.click.control.TextField;
import org.apache.click.util.HtmlStringBuffer;

/**
 * Provide an AutoCompleteTextField control.
 * <p/>
 * <b>Please note:</b> JQAutoCompleteTextField uses {@link net.sf.clickclick.jquery.helper.JQHelper}
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
    protected JQAutoCompleteHelper jqHelper;

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
    public JQAutoCompleteHelper getJQueryHelper() {
        if(jqHelper == null) {
            jqHelper = new JQAutoCompleteHelper(this);
        }
        return jqHelper;
    }

    /**
     * Set the JQuery Helper instance.
     *
     * @param jqHelper the JQuery Helper instance
     */
    public void setJQueryHelper(JQAutoCompleteHelper jqHelper) {
        this.jqHelper = jqHelper;
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Initialize the JQAutoCompleteTextField Ajax functionality.
     */
    @Override
    public void onInit() {
        super.onInit();
        AjaxControlRegistry.registerAjaxControl(this);

        // Register an Ajax listener which returns the list of suggestions
        setActionListener(new AjaxAdapter() {

            @Override
            public Partial onAjaxAction(Control source) {
                Partial partial = new Partial();
                List autocompleteList = getAutoCompleteList(getValue());
                if (autocompleteList != null) {
                    HtmlStringBuffer buffer = new HtmlStringBuffer(autocompleteList.size() * 5);
                    for (Iterator it = autocompleteList.iterator(); it.hasNext(); ) {
                        buffer.append(it.next());
                        if (it.hasNext()) {
                            buffer.append("\n");
                        }
                    }
                    partial.setContent(buffer.toString());
                }

                return partial;
            }
        });
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

    // ------------------------------------------------------ Protected Methods

    /**
     * Return the list of auto complete suggestions for the given criteria.
     * <p/>
     * When the user enters text into the text field, this method is invoked,
     * passing in the current value of the text field.
     * <p/>
     * This method must be implemented by users to return the list of suggestions
     * based on the current text field value, the criteria.
     *
     * @param criteria the search criteria
     * @return the list of auto complete suggestions
     */
    protected abstract List getAutoCompleteList(String criteria);
}
