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
import net.sf.clickclick.jquery.helper.JQHelper;
import net.sf.clickclick.jquery.util.JQEvent;
import net.sf.clickclick.util.AjaxUtils;
import org.apache.click.control.Radio;
import org.apache.click.control.RadioGroup;
import org.apache.click.element.Element;

public class JQRadioGroup extends RadioGroup {

    // -------------------------------------------------------------- Variables

    /** The JQuery helper object. */
    protected JQHelper jqHelper;

    // ------------------------------------------------------------ Constructor

    /**
     * Create a default JQRadioGroup field.
     */
    public JQRadioGroup() {
    }

    /**
     * Create a JQRadioGroup field with the given name.
     *
     * @param name the name of the control
     */
    public JQRadioGroup(String name) {
        super(name);
    }

    /**
     * Create a JQRadioGroup field with the given name and label.
     *
     * @param name the name of the control
     * @param label the label of the control
     */
    public JQRadioGroup(String name, String label) {
        super(name, label);
    }

    /**
     * Create a JQRadioGroup field with the given name and required status.
     *
     * @param name the name of the field
     * @param required the field required status
     */
    public JQRadioGroup(String name, boolean required) {
        super(name, required);
    }

    /**
     * Create a JQRadioGroup field with the given name, label and required status.
     *
     * @param name the name of the field
     * @param label the label of the field
     * @param required the field required status
     */
    public JQRadioGroup(String name, String label, boolean required) {
        super(name, label, required);
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
     * Initialize the JQRadioGroup Ajax functionality.
     */
    @Override
    public void onInit() {
        super.onInit();

        AjaxControlRegistry.registerAjaxControl(this);

        for (Radio radio : getRadioList()) {
            AjaxControlRegistry.registerAjaxControl(radio);
        }
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

    /**
     * @see org.apache.click.Control#getHeadElements()
     *
     * @return the list of HEAD elements to be included in the page
     */
    @Override
    public List<Element> getHeadElements() {
        if (headElements == null) {
            StringBuilder builder = new StringBuilder();
            Iterator<Radio> radios = getRadioList().iterator();
            while (radios.hasNext()) {
                Radio radio = radios.next();
                builder.append("#");
                builder.append(radio.getId());
                if (radios.hasNext()) {
                    builder.append(",");
                }
            }
            getJQueryHelper().bind(builder.toString(), JQEvent.CLICK);

            headElements = super.getHeadElements();
            getJQueryHelper().addHeadElements(headElements);
        }
        return headElements;
    }
}
