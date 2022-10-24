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
package net.sf.clickclick.util;

import org.apache.click.Control;
import org.apache.click.control.AbstractControl;
import org.apache.click.control.ActionLink;
import org.apache.click.util.HtmlStringBuffer;
import org.apache.commons.lang.StringUtils;

/**
 * Provide Ajax utility methods.
 */
public class AjaxUtils {

    /**
     * Return the given control CSS selector or null if no selector can be found.
     * <p/>
     * <b>Please note:</b> if is highly recommended to set a control's ID
     * attribute when dealing with Ajax requests.
     * <p/>
     * The algorith returns the selector in the following order:
     * <ol>
     *   <li>if control.getId() is set, preprend it with a '#' char
     *   and return the value. An example selector will be: <tt>#field-id</tt></li>
     *   <li>if control.getName() is not null the following checks are made:
     *     <ol>
     *       <li>if the control is of type {@link org.apache.click.control.ActionLink},
     *       its "<tt>class</tt>" attribute selector will be returned. For example:
     *       <tt>input[class=red]</tt>.
     *       <b>Please note:</b> if no class attribute is set, this method will
     *       automatically set link's "class" attribute to its name value and
     *       prefix the name with an underscore '_'. For example:
     *       <tt>input[class=_my-link]</tt>.
     *       </li>
     *
     *       <li>otherwise it is assumed the control will render its
     *       "<tt>name</tt>" attribute and the name attribute selector will be
     *       returned. For example: <tt>input[name=my-button]</tt>.
     *       </li>
     *     </ol>
     *   </li>
     *   <li>otherwise this method returns null.
     *   </li>
     * </ol>
     *
     * @param control the control which CSS selector to return
     * @return the control CSS selector or null if no selector can be found
     * @throws IllegalArgumentException if control is null
     */
    public static String getSelector(Control control) {
        if (control == null) {
            throw new IllegalArgumentException("Control cannot be null");
        }

        String id = control.getId();
        String name = control.getName();
        String select = null;

        if (StringUtils.isNotBlank(id)) {
            select = '#' + id;
        } else if (StringUtils.isNotBlank(name)) {
            String tag = null;

            // Try and create a more specific selector by retrieving the
            // control's tag
            if (control instanceof AbstractControl) {
                tag = StringUtils.defaultString(((AbstractControl) control).getTag());
            }

            HtmlStringBuffer buffer = new HtmlStringBuffer(20);

            // Handle ActionLink (perhaps other link controls too?) controls
            // differently as they don't render the "name" attribute, since
            // "name" is used by links for bookmarking purposes. Instead we set
            // the class attribute to the link's name and use that as the
            // selector.
            if (control instanceof ActionLink) {
                ActionLink link = (ActionLink) control;
                if (!link.hasAttribute("class")) {
                    link.setAttribute("class", '_' + name);
                }
                buffer.append(tag).append("[class*=");
                buffer.append(link.getAttribute("class")).append("]");

            } else {
                buffer.append(tag).append("[name=");
                buffer.append(name).append("]");
            }
            select = buffer.toString();
        }
        return select;
    }
}
