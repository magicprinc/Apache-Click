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
package net.sf.click.ajax4click.control;

import java.util.Iterator;
import org.apache.click.Context;
import org.apache.click.control.ActionButton;
import org.apache.click.util.ClickUtils;
import org.apache.click.util.HtmlStringBuffer;

/**
 * Provides a server-side Ajax enabled ActionButton.
 * <p/>
 * <b>Please note:</b> AjaxActionButton does not by itself  provide any client-side
 * Ajax functionality,it is up to you to add the necessary JavaScript.
  */
public class AjaxActionButton extends ActionButton {

    // ----------------------------------------------------------- Constructors

    /**
     * Create an AjaxActionButton with the given name.
     *
     * @param name the button name
     */
    public AjaxActionButton(String name) {
        super(name);
    }

    /**
     * Create a AjaxActionButton with the given name and label.
     *
     * @param name the button name
     * @param label the button display label
     */
    public AjaxActionButton(String name, String label) {
        super(name, label);
    }

    /**
     * Create a AjaxActionButton with the given name, label and id.
     *
     * @param name the link name
     * @param label the link display label
     * @param id the link id
     */
    public AjaxActionButton(String name, String label, String id) {
        super(name, label);
        setId(id);
    }

    /**
     * Create an AjaxActionButton with no name defined.
     * <p/>
     * <b>Please note</b> the control's name must be defined before it is valid.
     */
    public AjaxActionButton() {
        super();
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Return the AjaxActionButton href attribute for the given value.
     * This method will encode the URL with the session ID if required using
     * <tt>HttpServletResponse.encodeURL()</tt>.
     *
     * @param value the button value parameter
     * @return the button HTML href attribute value
     */
    public String getHref(Object value) {
        Context context = getContext();
        String uri = ClickUtils.getRequestURI(context.getRequest());

        HtmlStringBuffer buffer =
            new HtmlStringBuffer(uri.length() + getName().length() + 40);

        buffer.append(uri);
        buffer.append("?");
        buffer.append(ACTION_BUTTON);
        buffer.append("=");
        buffer.append(getName());

        if (value != null) {
            buffer.append("&amp;");
            buffer.append(VALUE);
            buffer.append("=");
            buffer.append(ClickUtils.encodeUrl(value, context));
        }

        if (hasParameters()) {
            Iterator i = getParameters().keySet().iterator();
            while (i.hasNext()) {
                String param = i.next().toString();
                if (!param.equals(ACTION_BUTTON) && !param.equals(VALUE)) {
                    Object paramValue = getParameters().get(param);
                    String encodedValue
                        = ClickUtils.encodeUrl(paramValue, context);
                    buffer.append("&amp;");
                    buffer.append(param);
                    buffer.append("=");
                    buffer.append(encodedValue);
                }
            }
        }

        return context.getResponse().encodeURL(buffer.toString());
    }

    /**
     * Return the AjaxActionButton href attribute value.
     *
     * @return the button HTML href attribute value
     */
    public String getHref() {
        return getHref(getValueObject());
    }

    /**
     * Render the button to the given buffer.
     *
     * @param buffer the buffer to render to
     */
    @Override
    public void render(HtmlStringBuffer buffer) {
        buffer.elementStart(getTag());

        buffer.appendAttribute("type", getType());
        buffer.appendAttribute("name", getName());
        buffer.appendAttribute("id", getId());
        buffer.appendAttribute("value", getLabel());
        buffer.appendAttribute("title", getTitle());
        if (getTabIndex() > 0) {
            buffer.appendAttribute("tabindex", getTabIndex());
        }

        String onClickAction = " href=\"" + getHref() + "\"";
        buffer.append(onClickAction);

        appendAttributes(buffer);

        if (isDisabled()) {
            buffer.appendAttributeDisabled();
        }

        buffer.elementEnd();
    }
}
