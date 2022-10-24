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
package net.sf.clickclick.control.html;

import org.apache.click.control.AbstractControl;
import org.apache.click.control.Field;
import org.apache.click.control.Label;
import org.apache.click.util.ClickUtils;
import org.apache.click.util.HtmlStringBuffer;

/**
 * This control renders a <em>label</em> for a target field. If no label is
 * specified the label will be rendered as an empty String.
 * <p/>
 * The following example shows how to use a HtmlLabel control to render a
 * <tt>label</tt> for a given Field.
 * <p/>
 * Given the Page <tt>MyPage.java</tt>:
 *
 * <pre class="prettyprint">
 * public MyPage extends Page {
 *
 *     public onInit() {
 *         Field field = new TextField("my-field");
 *
 *         // Create a new HtmlLabel for the field
 *         HtmlLabel fieldLabel = new HtmlLabel("my-field-label", field, "My Field");
 *
 *         // Add the HtmlLabel to the Page control list
 *         addControl(fieldLabel);
 *     }
 * } </pre>
 *
 * and the template <tt>my-page.htm</tt>:
 *
 * <pre class="prettyprint">
 * $my-field-label $field</pre>
 *
 * will render as:
 *
 * <div class="border">
 * <label for="my-field-label">My Field: </label><input type="text" name="my-field" id="my-field"/>
 * </div>
 */
public class HtmlLabel extends Label {

    // -------------------------------------------------------------- Variables

    /** The target Field object. */
    protected Field target;

    /**
     * The String to render after the label for example ":", default value is null.
     */
    protected String suffix;

    /**
     * Create a default HtmlLabel instance.
     */
    public HtmlLabel() {
    }

    /**
     * Create a HtmlLabel with the given name.
     *
     * @param name the name of the HtmlLabel
     */
    public HtmlLabel(String name) {
        if (name != null) {
            setName(name);
        }
    }

    /**
     * Create a HtmlLabel for the given field.
     *
     * @param target the target field
     */
    public HtmlLabel(Field target) {
        this(target, ClickUtils.toLabel(target.getName()), null);
    }

    /**
     * Create a HtmlLabel with the given name and label.
     *
     * @param name the name of the HtmlLabel
     * @param label the label of the HtmlLabel
     */
    public HtmlLabel(String name, String label) {
        this(name, null, label);
    }

    /**
     * Create a HtmlLabel for the given name and field.
     *
     * @param name the name of the HtmlLabel
     * @param target the target field
     */
    public HtmlLabel(String name, Field target) {
        this(name, target, ClickUtils.toLabel(target.getName()), null);
    }

    /**
     * Create a HtmlLabel for the given field and label.
     *
     * @param target the target field
     * @param label the label to render for the field
     */
    public HtmlLabel(Field target, String label) {
        this(target, label, null);
    }

    /**
     * Create a HtmlLabel for the given field and label.
     *
     * @param name the name of the HtmlLabel
     * @param target the target field
     * @param label the label to render for the field
     */
    public HtmlLabel(String name, Field target, String label) {
        this(name, target, label, null);
    }

    /**
     * Create a HtmlLabel for the given field, label and HTML
     * <tt>accesskey</tt> attribute.
     *
     * @param target the target field
     * @param label the label to render for the field
     * @param accesskey the HTML accesskey attribute
     */
    public HtmlLabel(Field target, String label, String accesskey) {
        this(null, target, label, accesskey);
    }

    /**
     * Create a HtmlLabel for the given name, field, label and HTML
     * <tt>accesskey</tt> attribute.
     *
     * @param name the name of the HtmlLabel
     * @param target the target field
     * @param label the label to render for the field
     * @param accesskey the HTML accesskey attribute
     */
    public HtmlLabel(String name, Field target, String label, String accesskey) {
        if (name != null) {
            setName(name);
        }
        this.target = target;
        this.label = label;
        if (accesskey != null) {
            setAttribute("accesskey", accesskey);
        }
    }

    // ------------------------------------------------------ Public Properties

    /**
     * Return the HtmlLabel html tag: <tt>label</tt>.
     *
     * @see AbstractControl#getTag()
     *
     * @return this controls html tag
     */
    public String getTag() {
        return "label";
    }

    /**
     * Set the parent of the label.
     *
     * @see org.apache.click.Control#setParent(Object)
     *
     * @param parent the parent of the Control
     * @throws IllegalArgumentException if the given parent instance is
     * referencing <tt>this</tt> object: <tt>if (parent == this)</tt>
     */
    public void setParent(Object parent) {
        if (parent == this) {
            throw new IllegalArgumentException("Cannot set parent to itself");
        }

        this.parent = parent;
    }

    /**
     * Return the target field.
     *
     * @return the target field
     */
    public Field getTarget() {
        return target;
    }

    /**
     * Set the target field.
     *
     * @param target the target field
     */
    public void setTarget(Field target) {
        this.target = target;
    }

    /**
     * Return the label suffix, for example ":", default value is null.
     *
     * @return the label suffix
     */
    public String getSuffix() {
        return suffix;
    }

    /**
     * Set the label suffix, for example ":".
     *
     * @param suffix the label suffix
     */
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    /**
     * Return the label "<tt>accesskey</tt>" attribute or null if this attribute
     * is not defined.
     *
     * @return the label accesskey attribute
     */
    public String getAccesskey() {
        if (hasAttributes()) {
            return getAttribute("accesskey");
        }
        return null;
    }

    /**
     * Set the label "<tt>accesskey</tt>" attribute.
     *
     * @param accesskey the label accesskey attribute
     */
    public void setAccesskey(String accesskey) {
        setAttribute("accesskey", accesskey);
    }

    /**
     * Return the Field value. This method delegates to {@link #getLabel()}.
     *
     * @return the Field value
     */
    public String getValue() {
        return getLabel();
    }

    /**
     * Set the Field value. This method delegates to {@link #setLabel(java.lang.String)}.
     *
     * @param value the Field value
     */
    public void setValue(String value) {
        setLabel(value);
    }

    /**
     * Return the object representation of the Field value.
     *
     * @see org.apache.click.control.Field#getValueObject()
     *
     * @return the object representation of the Field value
     */
    public Object getValueObject() {
        String label = getLabel();
        if (label == null || label.length() == 0) {
            return null;
        } else {
            return label;
        }
    }

    /**
     * Set the value of the field using the given object.
     *
     * @see org.apache.click.control.Field#setValueObject(java.lang.Object)
     *
     * @param object the object value to set
     */
    public void setValueObject(Object object) {
        if (object != null) {
            setLabel(object.toString());
        }
    }

    // --------------------------------------------------------- Public Methods

    /**
     * This method processes the page request returning true to continue
     * processing or false otherwise. The HtmlLabel <tt>onProcess()</tt> method
     * is typically invoked by the Form <tt>onProcess()</tt> method when
     * processing POST request.
     * <p/>
     * This method will bind the HtmlLabel request parameter value to the label,
     * validate the submission and invoke its callback listener if defined.
     *
     * @return true to continue Page event processing or false otherwise
     */
    public boolean onProcess() {
        bindRequestValue();

        if (getValidate()) {
            validate();
        }

        dispatchActionEvent();

        return true;
    }

    /**
     * Return the field's value from the request.
     *
     * @return the field's value from the request
     */
    protected String getRequestValue() {
        String value = getContext().getRequestParameter(getName());
        if (value != null) {
            if (isTrim()) {
                return value.trim();
            } else {
                return value;
            }
        } else {
            return getLabel();
        }
    }

    /**
     * Render the HTML representation of the HtmlLabel. The HtmlLabel is
     * rendered as an HTML <tt>&lt;label&gt;</tt> element e.g:
     * <tt>&lt;label for="firstnameId">Firstname:&lt;/label&gt;</tt>
     *
     * @param buffer the specified buffer to render the control's output to
     */
    public void render(HtmlStringBuffer buffer) {

        String label = getLabel();
        if (label == null) {
            label = "";
        }

        // Open tag: <label
        buffer.elementStart(getTag());

        buffer.appendAttribute("id", getId());
        buffer.appendAttribute("title", getTitle());

        Field target = getTarget();
        if (target != null) {
            // Set attribute to target field's id
            setAttribute("for", target.getId());
        }

        // Render all the labels attributes
        appendAttributes(buffer);

        // Close tag: <label for="firstname">
        buffer.closeTag();

        // Add label text: <label for="firstname">Firstname:
        buffer.append(label);
        if (getSuffix() != null) {
            buffer.append(getSuffix());
        }

        // Close tag: <label for="firstname">Firstname:</label>
        buffer.elementEnd(getTag());
    }

    /**
     * Returns the HTML representation of the HtmlLabel.
     * <p/>
     * This method delegates the rendering to the method
     * {@link #render(org.apache.click.util.HtmlStringBuffer)}.
     *
     * @see Object#toString()
     *
     * @return the HTML representation of this control
     */
    public String toString() {
        if (getTag() == null) {
            return "";
        }
        HtmlStringBuffer buffer = new HtmlStringBuffer(getControlSizeEst());
        render(buffer);
        return buffer.toString();
    }
}
