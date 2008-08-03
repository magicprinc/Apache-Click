/*
 * Copyright 2004-2008 Malcolm A. Edgar
 *
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
package net.sf.click.control;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.sf.click.Control;
import net.sf.click.util.ClickUtils;
import net.sf.click.util.ContainerUtils;
import net.sf.click.util.HtmlStringBuffer;
import org.apache.commons.lang.StringUtils;

/**
 * Provides a FieldSet container control: &nbsp; &lt;fieldset&gt;.
 *
 * <table style='margin-bottom: 1.25em'>
 * <tr>
 * <td>
 * <fieldset id='form-paymentDetails'>
 * <legend id='form-paymentDetails-legend'>FieldSet</legend>
 * <table class='fields' id='form-paymentDetails-fields'>
 * <tr class='fields'>
 * <td class='fields' align='left'><label>Card Name</label><font color="red">*</font></td>
 * <td align='left'><input type='text' name='cardName' id='form-cardName' value='' size='20'/></td>
 * </tr>
 * <tr class='fields'>
 * <td class='fields' align='left'><label>Card Number</label><font color="red">*</font></td>
 * <td align='left'><input type='text' name='cardNumber' id='form-cardNumber' value='' size='19' onkeypress='javascript:return integerFilter(event);' maxlength='19'/><select name='cardtype' id='form-cardtype' size='1'><option selected='selected' value='VISA'>Visa</option><option value='MASTER'>Master</option><option value='AMEX'>AmEx</option><option value='DINERS'>Diners</option><option value='DISCOVER'>Discover</option></select></td>
 * </tr>
 * <tr class='fields'>
 * <td class='fields' align='left'><label>Expiry</label><font color="red">*</font></td>
 * <td align='left'><input type='text' name='expiry' id='form-expiry' value='' size='4' onkeypress='javascript:return integerFilter(event);' maxlength='4'/></td>
 * </tr>
 * </table>
 * </fieldset>
 * </td>
 * </tr>
 * </table>
 *
 * FieldSet provides a container for laying out form <tt>Field</tt> controls.
 *
 * <h3>FieldSet Example</h3>
 *
 * An FieldSet example containing credit card payment details is provided below:
 *
 * <pre class='codeJava'>
 * <span class='kw'>public class</span> PaymentDetails() {
 *
 *     <span class="kw">public</span> Form form = <span class='kw'>new</span> Form();
 *
 *     <span class='kw'>public</span> PaymentDetails() {
 *         FieldSet paymentFieldSet = <span class='kw'>new</span> FieldSet(<span class='st'>"paymentDetails"</span>);
 *         form.add(paymentFieldSet);
 *
 *         paymentFieldSet.add(<span class='kw'>new</span> TextField(<span class='st'>"cardName"</span>, <span class='kw'>true</span>));
 *         paymentFieldSet.add(<span class='kw'>new</span> CreditCardField(<span class='st'>"cardNumber"</span>, <span class='kw'>true</span>));
 *         IntegerField expiryField = <span class='kw'>new</span> IntegerField(<span class='st'>"expiry"</span>, <span class='kw'>true</span>);
 *         expiryField.setSize(4);
 *         expiryField.setMaxLength(4);
 *         paymentFieldSet.add(expiryField);
 *
 *         form.add(<span class='kw'>new</span> Submit(<span class='st'>"ok"</span>, <span class='st'>"    OK    "</span>, <span class='kw'>this</span>, <span class='st'>"onOkClick"</span>);
 *         form.add(<span class='kw'>new</span> Submit(<span class='st'>"  Cancel  "</span>, <span class='kw'>this</span>, <span class='st'>"onCancelClick"</span>));
 *     }
 * } </pre>
 *
 * When the FieldSet is processed it invokes the <tt>onProcess()</tt> method
 * of its contained Fields. Beyond this the FieldSet performs no server side
 * processing, and should be considered simply as a container for laying out
 * form fields.
 *
 * <p/>
 * See also the W3C HTML reference:
 * <a title="W3C HTML 4.01 Specification"
 *    href="../../../../../html/interact/forms.html#h-17.10">FIELDSET</a>
 *
 * @author Malcolm Edgar
 */
public class FieldSet extends AbstractContainer {

    // -------------------------------------------------------------- Constants

    private static final long serialVersionUID = 1L;

    // -------------------------------------------------------- Instance Variables

    /** The map of field width values. */
    protected Map fieldWidths;

    /** The parent Form. */
    protected Form form;

    /** The FieldSet label. */
    protected String label;

    /** The FieldSet legend. */
    protected String legend;

    /** The FieldSet legend attributes map. */
    protected Map legendAttributes;

    /** The render the fieldset border flag, default value is true. */
    protected boolean showBorder = true;

    /**
     * This property serves as a hint to the number of table columns the fieldset
     * is rendered with.
     *<p/>
     * Currently only {@link Form} acts upon this property.
     */
    protected Integer columns;

    /** The Field disabled value. */
    protected boolean disabled;

    /** The Field is readonly flag. */
    protected boolean readonly;

    // ------------------------------------------------------ Constructorrs

    /**
     * Create a FieldSet with the given name.
     *
     * @param name the fieldset name element value
     */
    public FieldSet(String name) {
        super(name);
    }

    /**
     * Create a FieldSet with the given name and legend.
     *
     * @param name the fieldset name
     * @param legend the fieldset legend element value
     */
    public FieldSet(String name, String legend) {
        super(name);
        setLegend(legend);
    }

    /**
     * Create a FieldSet with no name defined.
     * <p/>
     * <b>Please note</b> the control's name must be defined before it is valid.
     */
    public FieldSet() {
    }

    // ------------------------------------------------------ Public Methods

    /**
     * Add a Field to the FieldSet and return the added instance.
     * <p/>
     * <b>Please note</b> if the FieldSet's parent is a {@link Form}, the
     * Fields inside the FieldSet will be laid out by the Form.
     *
     * @see Container#add(net.sf.click.Control)
     *
     * @param control the control to add to the FieldSet and return
     * @param index the index at which the control is to be inserted
     * @return the control that was added to the FieldSet
     * @throws IllegalArgumentException if the control is null, the Field's name
     * is not defined, the container already contains a control with the same
     * name, if the control's parent is a Page or if the control is neither a
     * Field nor FieldSet
     */
    public Control insert(Control control, int index) {
         if (control == null) {
            throw new IllegalArgumentException("Field parameter cannot be null");
        }
        if (control instanceof Field) {
            Field field = (Field) control;
            if (StringUtils.isBlank(field.getName())) {
               String msg = "Field name not defined: " + field.getClass().getName();
                throw new IllegalArgumentException(msg);
            }
            if (getControlMap().containsKey(field.getName())
                && !(field instanceof Label)) {

                throw new IllegalArgumentException(
                    "FieldSet already contains field named: " + field.getName());
            }
            getControls().add(field);

            getControlMap().put(field.getName(), field);

            Form form = getForm();
            field.setForm(form);

            field.setParent(this);

            if (form != null && form.getDefaultFieldSize() > 0) {
                if (field instanceof TextField) {
                    ((TextField) field).setSize(form.getDefaultFieldSize());

                } else if (field instanceof FileField) {
                    ((FileField) field).setSize(form.getDefaultFieldSize());

                } else if (field instanceof TextArea) {
                    ((TextArea) field).setCols(form.getDefaultFieldSize());
                }
            }

        } else {
            super.insert(control, index);
        }

        return control;
    }

    /**
     * Add the field to the fieldSet, and set the fields form property. The
     * field will be added to {@link #getControlMap()} using its name.
     * <p/>
     * Field instances will be add to {@link #getControls()}.
     *
     * @see #add(net.sf.click.Control)
     *
     * @param field the field to add to the form
     * @return the field added to this form
     * @throws IllegalArgumentException if the field is null, the field name
     * is not defined, the fieldSet already contains a control with the same name
     * or if the field's parent is a Page
     */
    public Field add(Field field) {
        insert(field, getControls().size());
        return field;
    }

    /**
     * Add the field to the fieldset and specify the field's width in columns.
     * <p/>
     * Note HiddenFields types are not valid arguments for this method.
     *
     * @param field the field to add to the fieldset
     * @param width the width of the field in table columns
     * @return the field added to this fieldset
     * @throws IllegalArgumentException if the field is null, field's name is
     * not defined, a HiddenField, the fieldset already contains a control with
     * the same name, if the field's parent is a Page or the width &lt; 1
     */
    public Field add(Field field, int width) {
        if (field == null) {
            throw new IllegalArgumentException("Field parameter cannot be null");
        }
        if (field instanceof HiddenField) {
            String msg = "Not valid a valid field type: " + field.getClass().getName();
            throw new IllegalArgumentException(msg);
        }
        if (width < 1) {
            throw new IllegalArgumentException("Invalid field width: " + width);
        }

        add(field);
        getFieldWidths().put(field.getName(), new Integer(width));
        return field;
    }

    /**
     * @see Container#remove(net.sf.click.Control)
     *
     * @param control the control to remove from the container
     * @return true if the control was removed from the container
     * @throws IllegalArgumentException if the control is null
     */
    public boolean remove(Control control) {
        boolean removed = super.remove(control);

        if (control instanceof Field) {
            Field field = (Field) control;
            getFieldWidths().remove(field.getName());
            field.setForm(null);
        }
        return removed;
    }

    /**
     * Remove the given field from the fieldset.
     *
     * @param field the field to remove from the fieldset
     *
     * @throws IllegalArgumentException if the field is null
     */
    public void remove(Field field) {
        remove((Control) field);
    }

    /**
     * Remove the named field from the fieldset, returning true if removed
     * or false if not found.
     *
     * @param name the name of the field to remove from the fieldset
     * @return true if the named field was removed or false otherwise
     */
    public boolean removeField(String name) {
        Control control = ContainerUtils.findControlByName(this, name);

        if (control != null) {
            return remove(control);

        } else {
            return false;
        }
    }

    /**
     * Return the fieldsets's html tag: <tt>fieldset</tt>.
     *
     * @see AbstractControl#getTag()
     *
     * @return this controls html tag
     */
    public String getTag() {
        return "fieldset";
    }

    /**
     * Return true if the FieldSet is disabled. The FieldSet will also be
     * disabled if the parent Form is disabled.
     * <p/>
     * <b>Important Note</b>: disabled fieldset also disables all its fields
     * which will not submit their values in a HTML form POST. This may cause
     * validation issues in a form submission. Please note this is a HTML
     * limitation and is not due to Click.
     *
     * @return true if the Field is disabled
     */
    public boolean isDisabled() {
        Form form = getForm();
        if (form != null && form.isDisabled()) {
            return true;
        } else {
            return disabled;
        }
    }

    /**
     * Set the FieldSet disabled flag which in turn will disable all its fields.
     * <p/>
     * <b>Important Note</b>: disabled fields will not submit their values in
     * a HTML form POST. This may cause validation issues in a form submission.
     * Please note this is a HTML limitation and is not due to Click.
     *
     * @param disabled the Field disabled flag
     */
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    /**
     * Return true if the FieldSet is readonly. The FieldSet will also be
     * readonly if the parent Form is readonly.
     *
     * @return true if the FieldSet is a readonly
     */
    public boolean isReadonly() {
        Form form = getForm();
        if (form != null && form.isReadonly()) {
            return true;
        } else {
            return readonly;
        }
    }
 
    /**
     * Set the FieldSet readonly flag which in turn will set all its fields
     * to readonly.
     *
     * @param readonly the FieldSet readonly flag
     */
    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
    }

    /**
     * Return the number of fieldset layout table columns. This property supplies
     * a hint to the number of table columns the fieldset should be rendered with.
     * <p/>
     * <b>Note</b> currently only {@link Form} acts upon the column value.
     * <p/>
     * By default this property inherits its value from the parent Form, but
     * can be specified to override the form value.
     *
     * @return the number of fieldset layout table columns
     */
    public int getColumns() {
        if (columns != null) {
            return columns.intValue();
        } else {
            return getForm().getColumns();
        }
    }

    /**
     * Set the number of fieldset layout table columns. This property supplies
     * a hint to the number of table columns the fieldset should be rendered with.
     * <p/>
     * <b>Note</b> currently only {@link Form} acts upon the column value.
     *
     * @param columns the number of fieldset layout table columns
     */
    public void setColumns(int columns) {
        this.columns = new Integer(columns);
    }

    /**
     * Return the render fieldset border flag. The border is the HTML
     * &lt;fieldset&gt; element.
     *
     * @return the render the fieldset border flag
     */
    public boolean getShowBorder() {
        return showBorder;
    }

    /**
     * Set the render fieldset border flag. The border is the HTML
     * &lt;fieldset&gt; element.
     *
     * @param value the render the fieldset border flag
     */
    public void setShowBorder(boolean value) {
        this.showBorder = value;
    }

    /**
     * Return the List of fields, ordered in addition order to the fieldset.
     *
     * @return the ordered List of fieldset fields
     */
    public List getFieldList() {
        return ContainerUtils.getFieldsAndLabels(this);
    }

    /**
     * Return the Map of fieldset fields, keyed on field name.
     *
     * @return the Map of fieldset fields, keyed on field name
     */
    public Map getFields() {
        return ContainerUtils.getFieldMap(this);
    }

    /**
     * Return the map of field width values, keyed on field name.
     *
     * @return the map of field width values, keyed on field name
     */
    public Map getFieldWidths() {
        if (fieldWidths == null) {
            fieldWidths = new HashMap();
        }
        return fieldWidths;
    }

    /**
     * Return the parent Form containing the FieldSet or null if no form is
     * present in the parent hierarchy.
     *
     * @return the parent Form containing the FieldSet
     */
    public Form getForm() {
        if (form != null) {
            return form;

        } else {
            // Find form in parent hierarchy
            return ContainerUtils.findForm(this);
        }
    }

    /**
     * Set the FieldSet's the parent <tt>Form</tt>.
     *
     * @param form FieldSet's parent <tt>Form</tt>
     */
    public void setForm(Form form) {
        if (form == null) {
            throw new IllegalArgumentException("Cannot set the FieldSet's form to null");
        }

        this.form = form;

        // Set the specified form on the fieldsSets children. This call is not
        // recursive to childrens children
        for (Iterator it = getControls().iterator(); it.hasNext();) {
            Control control = (Control) it.next();
            if (control instanceof Field) {
                ((Field) control).setForm(form);
            }
        }
    }

    /**
     * Return the HTML head import statements for contained fields.
     *
     * @see net.sf.click.Control#getHtmlImports()
     *
     * @return the HTML head import statements for the contained field stylesheet
     * and JavaScript files
     */
    public String getHtmlImports() {
        HtmlStringBuffer buffer = new HtmlStringBuffer(512);

        if (hasControls()) {
            for (int i = 0, size = getControls().size(); i < size; i++) {
                Control control = (Control) getControls().get(i);
                String htmlImports = control.getHtmlImports();
                if (htmlImports != null) {
                    buffer.append(htmlImports);
                }
            }
        }

        return buffer.toString();
    }

    /**
     * Return the fieldSet display label.
     *
     * @see Field#getLabel()
     *
     * @return the display label of the Field
     */
    public String getLabel() {
        if (label == null) {
            label = getMessage(getName() + ".label");
        }
        if (label == null) {
            label = ClickUtils.toLabel(getName());
        }
        return label;
    }

    /**
     * Set the Field display caption.
     *
     * @param label the display label of the Field
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Return the fieldset Legend element value: &lt;legend&gt;
     * <p/>
     * If the legend value is null, this method will attempt to find a
     * localized label message in the parent messages using the key:
     * <blockquote>
     * <tt>getName() + ".title"</tt>
     * </blockquote>
     * If not found then the message will be looked up in the
     * <tt>/click-control.properties</tt> file using the same key.
     * If a value cannot be found in the parent or control messages then the
     * FieldSet name will be converted into a legend using the
     * {@link ClickUtils#toLabel(String)} method.
     *
     * @return the fieldset Legend element value
     */
    public String getLegend() {
        if (legend == null) {
            legend = getMessage(getName() + ".legend");
        }
        if (legend == null) {
            String fsName = getName();
            if (fsName != null) {
                legend = ClickUtils.toLabel(fsName);
            }
        }
        return legend;
    }

    /**
     * Set the fieldset Legend element value: &lt;legend&gt;. If the legend
     * value is a zero length string no legend element will be rendered. You
     * can set a blank zero length string if you want to render the fieldset
     * border but don't want a legend caption.
     *
     * @param legend the fieldset Legend element value
     */
    public void setLegend(String legend) {
        this.legend = legend;
    }

    /**
     * Return the legend HTML attribute with the given name, or null if the
     * attribute does not exist.
     *
     * @param name the name of legend HTML attribute
     * @return the legend HTML attribute
     */
    public String getLegendAttribute(String name) {
        if (legendAttributes != null) {
            return (String) legendAttributes.get(name);
        } else {
            return null;
        }
    }

    /**
     * Set the fieldset HTML attribute with the given attribute name and value.
     *
     * @param name the name of the form HTML attribute
     * @param value the value of the form HTML attribute
     * @throws IllegalArgumentException if name parameter is null
     */
    public void setLegendAttribute(String name, String value) {
        if (name == null) {
            throw new IllegalArgumentException("Null name parameter");
        }

        if (legendAttributes == null) {
            legendAttributes = new HashMap(5);
        }

        if (value != null) {
            legendAttributes.put(name, value);
        } else {
            legendAttributes.remove(name);
        }
    }

    /**
     * Return the fieldset attributes Map.
     *
     * @return the fieldset attributes Map
     */
    public Map getLegendAttributes() {
        if (legendAttributes == null) {
            legendAttributes = new HashMap(5);
        }
        return legendAttributes;
    }

    /**
     * Return true if the fieldset has attributes or false otherwise.
     *
     * @return true if the fieldset has attributes on false otherwise
     */
    public boolean hasLegendAttributes() {
        if (legendAttributes != null && !legendAttributes.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Process the request invoking <tt>onProcess()</tt> on the contained
     * <tt>Control</tt> elements.
     *
     * @return true if all Controls were processed, or false if one Control returned
     * false
     */
    public boolean onProcess() {
        if (hasControls()) {
            for (Iterator it = getControls().iterator(); it.hasNext();) {
                Control control = (Control) it.next();
                String controlName = control.getName();
                if (controlName == null || !controlName.startsWith(Form.SUBMIT_CHECK)) {
                    boolean continueProcessing = control.onProcess();
                    if (!continueProcessing) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * This method is not supported and will throw a
     * <tt>UnsupportedOperationException</tt>.
     *
     * @see net.sf.click.Control#setListener(Object, String)
     *
     * @param listener the listener object with the named method to invoke
     * @param method the name of the method to invoke
     */
    public void setListener(Object listener, String method) {
        throw new UnsupportedOperationException("setListener not supported");
    }

    /**
     * Render the HTML representation of the FieldSet.
     * <p/>
     * If FieldSet is contained within a {@link Form} instance, this method will
     * delegate rendering to {@link Form#renderFieldSet(net.sf.click.util.HtmlStringBuffer, net.sf.click.control.FieldSet)}.
     * <p/>
     * The size of buffer is determined by {@link #getControlSizeEst()}.
     *
     * @param buffer the specified buffer to render the control's output to
     */
    public void render(HtmlStringBuffer buffer) {

        if (getShowBorder()) {
            buffer.elementStart(getTag());

            buffer.appendAttribute("id", getId());

            appendAttributes(buffer);

            if (isDisabled()) {
                buffer.appendAttributeDisabled();
            }

            buffer.closeTag();
            buffer.append("\n");

            String legend = getLegend();
            if (legend != null && legend.length() > 0) {
                buffer.elementStart("legend");
                if (hasLegendAttributes()) {
                    Object legendId = getLegendAttributes().get("id");
                    if (legendId != null) {
                        buffer.appendAttribute("id", legendId);
                    } else {
                        buffer.appendAttribute("id", getId() + "-legend");
                    }
                    buffer.appendAttributes(getLegendAttributes());
                } else {
                    buffer.appendAttribute("id", getId() + "-legend");
                }
                buffer.closeTag();
                buffer.append(getLegend());
                buffer.elementEnd("legend");
                buffer.append("\n");
            }
        }

        // Render Controls
        renderFields(buffer);

        // Render Buttons
        renderButtons(buffer);

        if (getShowBorder()) {
            buffer.elementEnd(getTag());
            buffer.append("\n");
        }
    }

    /**
     * Returns the HTML representation of the FieldSet.
     * <p/>
     * The rendering of the FieldSet is delegated to
     * {@link #render(net.sf.click.util.HtmlStringBuffer)}. The size of buffer
     * is determined by {@link #getControlSizeEst()}.
     *
     * @see Object#toString()
     *
     * @return the HTML representation of this control
     */
    public String toString() {
        return super.toString();
    }

    //-------------------------------------------- protected methods

    /**
     * Render the fieldsets form fields to the string buffer. This method will
     * apply the parent Forms properties to the layout and rendering of fields.
     *
     * @param buffer the StringBuffer to render to
     */
    protected void renderFields(HtmlStringBuffer buffer) {
        if (getFieldList().isEmpty()) {
            return;
        }

        buffer.elementStart("table");
        buffer.appendAttribute("class", "fields");
        buffer.appendAttribute("id", getId() + "-fields");
        buffer.closeTag();
        buffer.append("\n");

        int column = 1;
        boolean openTableRow = false;

        if (!hasControls()) {
            return;
        }

        List controls = getControls();

        for (int i = 0; i < controls.size(); i++) {
            Control control = (Control) controls.get(i);

            // Buttons are rendered separately
            if (control instanceof Button) {
                continue;
            }

            if (!isHidden(control)) {

                // Field width
                Integer width = (Integer) getFieldWidths().get(control.getName());

                if (column == 1) {
                    buffer.append("<tr class=\"fields\">\n");
                    openTableRow = true;
                }

                if (control instanceof Label) {
                    Label label = (Label) control;
                    buffer.append("<td class=\"fields\" align=\"");
                    buffer.append(getForm().getLabelAlign());
                    buffer.append("\"");

                    if (width != null) {
                        int colspan = (width.intValue() * 2);
                        buffer.appendAttribute("colspan", colspan);
                    } else {
                        buffer.appendAttribute("colspan", 2);
                    }

                    if (label.hasAttributes()) {
                        //Temporarily remove the style attribute
                        String tempStyle = null;
                        if (label.hasAttribute("style")) {
                            tempStyle = label.getAttribute("style");
                            label.setAttribute("style", null);
                        }
                        buffer.appendAttributes(label.getAttributes());

                        //Put style back in attribute map
                        if (tempStyle != null) {
                            label.setAttribute("style", tempStyle);
                        }
                    }
                    buffer.append(">");
                    label.render(buffer);
                    buffer.append("</td>\n");

                } else if (control instanceof Field) {
                    Field field = (Field) control;
                    Form form = getForm();
                    // Write out label
                    if (Form.POSITION_LEFT.equals(form.getLabelsPosition())) {
                        buffer.append("<td class=\"fields\"");
                        buffer.appendAttribute("align", form.getLabelAlign());
                        buffer.appendAttribute("style", form.getLabelStyle());
                        buffer.append(">");
                    } else {
                        buffer.append("<td class=\"fields\" valign=\"top\"");
                        buffer.appendAttribute("style", form.getLabelStyle());
                        buffer.append(">");
                    }

                    if (field.isRequired()) {
                        buffer.append(form.getMessage("label-required-prefix"));
                    } else {
                        buffer.append(form.getMessage("label-not-required-prefix"));
                    }
                    buffer.elementStart("label");
                    buffer.appendAttribute("for", field.getId());
                    if (field.isDisabled()) {
                        buffer.appendAttributeDisabled();
                    }
                    if (field.getError() != null) {
                        buffer.appendAttribute("class", "error");
                    }
                    buffer.closeTag();
                    buffer.append(field.getLabel());
                    buffer.elementEnd("label");
                    if (field.isRequired()) {
                        buffer.append(form.getMessage("label-required-suffix"));
                    } else {
                        buffer.append(form.getMessage("label-not-required-suffix"));
                    }

                    if (Form.POSITION_LEFT.equals(form.getLabelsPosition())) {
                        buffer.append("</td>\n");
                        buffer.append("<td align=\"left\"");
                        buffer.appendAttribute("style", form.getFieldStyle());

                        if (width != null) {
                            int colspan = (width.intValue() * 2) - 1;
                            buffer.appendAttribute("colspan", colspan);
                        }

                        buffer.append(">");
                    } else {
                        buffer.append("<br/>");
                    }

                    // Write out field
                    field.render(buffer);
                    buffer.append("</td>\n");
                } else {
                    buffer.append("<td class=\"fields\"");

                    if (width != null) {
                        int colspan = (width.intValue() * 2);
                        buffer.appendAttribute("colspan", colspan);
                    } else {
                        buffer.appendAttribute("colspan", 2);
                    }
                    buffer.append(">\n");
                    control.render(buffer);

                    buffer.append("</td>\n");
                }

                if (width != null) {
                    if (control instanceof Label || !(control instanceof Field)) {
                        column += width.intValue();

                    } else {
                        column += (width.intValue() - 1);
                    }
                }

                if (column >= getColumns()) {
                    buffer.append("</tr>\n");
                    openTableRow = false;
                    column = 1;
                } else {
                    column++;
                }

            }
        }

        if (openTableRow) {
            buffer.append("</tr>\n");
        }

        buffer.elementEnd("table");
        buffer.append("\n");
    }

    /**
     * Render the fieldset buttons to the string buffer.
     *
     * @param buffer the StringBuffer to render to
     */
    protected void renderButtons(HtmlStringBuffer buffer) {
        List buttons = ContainerUtils.getButtons(this);

        if (!buttons.isEmpty()) {
            buffer.append("<table class=\"buttons\" id=\"");
            buffer.append(getId());
            buffer.append("-buttons\">\n");
            buffer.append("<tr class=\"buttons\">");

            Form form = getForm();
            for (int i = 0, size = buttons.size(); i < size; i++) {
                buffer.append("<td class=\"buttons\"");
                buffer.appendAttribute("style", form.getButtonStyle());
                buffer.closeTag();

                Button button = (Button) buttons.get(i);
                button.render(buffer);

                buffer.append("</td>");
            }

            buffer.append("</tr>\n");
            buffer.append("</table>\n");
        }
    }

    // -------------------------------------------------------- Private Methods

    /**
     * Return true if the control is hidden, false otherwise.
     *
     * @param control control to check hidden status
     * @return true if the control is hidden, false otherwise
     */
    private boolean isHidden(Control control) {
        if (!(control instanceof Field)) {
            // Non-Field Controls can not be hidden
            return false;
        } else {
            return ((Field) control).isHidden();
        }
    }
}
