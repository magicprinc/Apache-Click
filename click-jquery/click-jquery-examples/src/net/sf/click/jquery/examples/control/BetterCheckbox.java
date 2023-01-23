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
package net.sf.click.jquery.examples.control;

import org.apache.click.control.Checkbox;
import org.apache.click.util.HtmlStringBuffer;

/**
 * BetterCheckbox is different from Checkbox in the following ways.
 *
 * - BetterCheckbox can have a value, not just true/false
 *
 * - BetterCheckbox will be checked if the incoming parameter matches its given value,
 * otherwise it will be unchecked
 *
 * - If BetterCheckbox is disabled and enabled via JS, one needs to add a
 * hidden field with the same name as the checkbox and BetterCheckbox will be
 * enabled automatically. This behavior isn't supported by the default Checkbox.
 */
public class BetterCheckbox extends Checkbox {

    // ----------------------------------------------------------- Constructors

    public BetterCheckbox(String name) {
        super(name);
    }

    public BetterCheckbox(String name, String label) {
        super(name, label);
    }

    public BetterCheckbox(String name, boolean required) {
        super(name, required);
    }

    public BetterCheckbox() {
    }

    // public methods ---------------------------------------------------------

    @Override
    public String getValue() {
        return (value != null) ? value : "";
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public Object getValueObject() {
        if (value == null || value.length() == 0) {
            return null;
        } else {
            return value;
        }
    }

    @Override
    public void setValueObject(Object object) {
        if (object != null) {
            value = object.toString();
        }
    }

    /**
     * Set the {@link #checked} property to true if the fields value is
     * submitted.
     */
    @Override
    public void bindRequestValue() {
        String param = getContext().getRequestParameter(getName());
        if (getValue().equals(param)) {
            setChecked(true);
        } else {
            setChecked(false);
        }
    }

    @Override
    public void render(HtmlStringBuffer buffer) {
        buffer.elementStart(getTag());

        buffer.appendAttribute("type", getType());
        buffer.appendAttribute("name", getName());
        buffer.appendAttribute("id", getId());
        buffer.appendAttribute("value", getValue());
        buffer.appendAttribute("title", getTitle());
        if (isValid()) {
            removeStyleClass("error");
        } else {
            addStyleClass("error");
        }
        if (getTabIndex() > 0) {
            buffer.appendAttribute("tabindex", getTabIndex());
        }
        if (isChecked()) {
            buffer.appendAttribute("checked", "checked");
        }

        appendAttributes(buffer);

        if (isDisabled() || isReadonly()) {
            buffer.appendAttributeDisabled();
        }

        buffer.elementEnd();

        if (getHelp() != null) {
            buffer.append(getHelp());
        }

        // checkbox element does not support "readonly" element, so as a work around
        // we make the field "disabled" and render a hidden field to submit its value
        if (isReadonly() && isChecked()) {
            buffer.elementStart("input");
            buffer.appendAttribute("type", "hidden");
            buffer.appendAttribute("name", getName());
            buffer.appendAttributeEscaped("value", getValue());
            buffer.elementEnd();
        }

        /*
        buffer.elementStart("input");
        buffer.appendAttribute("type", "hidden");
        buffer.appendAttribute("name", getName());
        buffer.appendAttributeEscaped("value", "0");
        buffer.elementEnd();*/
    }
}
