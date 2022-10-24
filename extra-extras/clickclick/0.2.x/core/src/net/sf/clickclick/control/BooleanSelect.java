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
package net.sf.clickclick.control;

import org.apache.click.control.Select;
import org.apache.click.extras.control.DateField;
import org.apache.click.extras.control.IntegerField;
import org.apache.commons.lang.StringUtils;

/**
 * Provides a Select control that can be used as either a simple two-option
 * select (TRUE / FALSE) or as a tristate select (UNSET / TRUE / FALSE).
 *
 * <table class='htmlHeader' cellspacing='6'>
 * <tr>
 * <td>BooleanSelect</td>
 * <td>
 * <select title='BooleanSelect Control'>
 * <option value=''></option>
 * <option value='true'>yes</option>
 * <option value='false'>no</option>
 * </select>
 * </td>
 * </tr>
 * </table>
 *
 * <h4>Use</h4>
 * Tristate is handy when you want to confirm that the user made a concious
 * descision and not just accepted the default value.
 * <p/>
 * Please note that tristate can best be used in combination with
 * <i>required</i> and that the <i>required</i> property is useless when used
 * in combination with the two-state (as there will always be an option selected).
 * <p/>
 * The default value when creating a BooleanSelect field is:
 * <i>Required</i> == <i>Tristate</i>.
 * <p/>
 * Changing one of these properties after creation will not influence the other.
 * 
 * <h4>Values</h4>
 * <p/>
 * In holding with {@link IntegerField}, {@link DateField} and others the
 * <code>BooleanSelect</code> field provides <code>getBoolean()</code> and
 * <code>setBoolean()</code> methods.
 * <p/>
 * The <code>setValue()</code> and <code>setObjectValue()</code> only allow
 * Boolean objects or Strings(containing "true" or "false").
 * <p/>
 * <tt>null</tt> or empty strings are allowed in all these method and means:
 * value not set (unset). In keeping with the other Fields, <tt>null</tt>
 * values are stored and returned as "" (empty string). The
 * <tt>getValueObject()</tt> method does not return an empty string but
 * <code>null</code> instead.
 *
 * <h4>Makeup</h4>
 * <p/>
 * The labels used for the true and false options can be customized in two ways:
 * manually or with one of the built-in notations
 * <p/>
 * Use the <tt>setOptionLabels()</tt> convenience methods or the individual
 * properties to manually set.
 * <p/>
 * Or use the <tt>setNotation()</tt> method to use one of the built-in notations.
 * <p/>
 * Following notations are provided and backed by the resource-bundle (i18n).
 * <ul>
 * <li> <code>TRUE / FALSE</code> -- <i>default</i>
 * <li> <code>YES / NO</code>
 * <li> <code>ON / OFF</code>
 * <li> <code>ACTIVE / INACTIVE</code>
 * <li> <code>OPEN / CLOSED</code>
 * </ul>
 * <p/>
 * Remark: Options are created in <tt>control.onInit()</tt> all makeup needs to
 * be done in <code>page.onInit()</code> or earlier; changes made in
 * <tt>onRender()</tt>, <tt>onPost()</tt> or action listeners, will not be
 * reflected in the final result.
 *
 * @see Select
 */
public class BooleanSelect extends Select {

    // -------------------------------------------------------------- Constants

    private static final long  serialVersionUID = 1L;

    /** Indicates the Select Option labels are custom set. */
    public static final String CUSTOM = "_custom_";

    /** Indicates the Select Option labels are set to TRUE / FALSE. */
    public static final String TRUEFALSE = "default";

    /** Indicates the Select Option labels are set to YES / NO. */
    public static final String YESNO = "yesno";

    /** Indicates the Select Option labels are set to ON / OFF. */
    public static final String ONOFF = "onoff";

    /** Indicates the Select Option labels are set to ACTIVE / INACTIVE. */
    public static final String ACTIVEINACTIVE = "activeinactive";

    /** Indicates the Select Option labels are set to OPEN / CLOSED. */
    public static final String OPENCLOSED = "openclosed";

    // -------------------------------------------------------------- Variables

    /** Indicates if tristate is enabled or not, false by default. */
    private boolean tristate = false;

    /** The default notation, {@link #TRUEFALSE}. */
    private String notation = TRUEFALSE;

    /** The {@link #CUSTOM} <tt>true</tt> label. */
    private String customTrue = null;

    /** The {@link #CUSTOM} <tt>false</tt> label. */
    private String customFalse = null;

    /** The {@link #CUSTOM} <tt>unset</tt> label. */
    private String customUnset = null;

    /**
     * Create a Select field with no name defined.
     * <p/>
     * <b>Please note</b> the control's name must be defined before it is valid.
     */
    public BooleanSelect() {
        super();
    }

    /**
     * Create a Select field with the given name.
     *
     * @param name the name of the field
     */
    public BooleanSelect(String name) {
        super(name);
    }

    /**
     * Create a Select field with the given name.
     * <p/>
     * If required is true, tristate will automatically be set to true.
     *
     * @param name the name of the field
     * @param required the required property
     */
    public BooleanSelect(String name, boolean required) {
        super(name, required);
        setTristate(required);
    }

    /**
     * Create a Select field with the given name and label.
     *
     * @param name the name of the field
     * @param label the label of the field
     */
    public BooleanSelect(String name, String label) {
        super(name, label);
    }

    /**
     * Create a Select field with the given name and label.
     *
     * @param name the name of the field
     * @param label the label of the field
     * @param notation the notation to be used for the option labels
     */
    public BooleanSelect(String name, String label, String notation) {
        super(name, label);
        setNotation(notation);
    }

    /**
     * Create a Select field with the given name and label.
     * <p/>
     * If required is true, tristate will automatically be set to true.
     *
     * @param name the name of the field
     * @param label the label of the field
     * @param required the required property
     */
    public BooleanSelect(String name, String label, boolean required) {
        super(name, label, required);
        setTristate(required);
    }

    /**
     * Create a Select field with the given name and label.
     * <p/>
     * If required is true, tristate will automatically be set to true.
     *
     * @param name the name of the field
     * @param label the label of the field
     * @param notation the notation to be used for the optionlabels
     * @param required the required property
     */
    public BooleanSelect(String name, String label, String notation, boolean required) {
        super(name, label, required);
        setTristate(required);
        setNotation(notation);
    }

    // ------------------------------------------------------------------

    /**
     * Options are added to the Select control. Make sure you call
     * super.onInit() if you override this method.
     */
    public void onInit() {
        // Being called more than once? We could keep the existing options,
        // but they might have changed
        if (getOptionList().size() > 0) {
            getOptionList().clear();
        }

        if (tristate) {
            add(new StyledOption("", "unset", getOptionLabelUnset()));
        }
        add(new StyledOption(Boolean.TRUE.toString(), "true",
            getOptionLabelTrue()));

        add(new StyledOption(Boolean.FALSE.toString(), "false",
            getOptionLabelFalse()));

        super.onInit();
    }

    /**
     * Return the value of the field as a Boolean, or null if the value was not
     * set.
     *
     * @return <code>True</code> or <code>False</code> if value was set,
     * <code>null</code> otherwise
     */
    public Boolean getBoolean() {
        String value = getValue();

        // We need this extra check as Boolean.valueOf does not return null but
        // false on illegal values.
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        return Boolean.valueOf(getValue());
    }

    /**
     * Set the value of the field to the given Boolean value. The Boolean can
     * be null, meaning the value is not set.
     *
     * @param value the field's value, can be <tt>null</tt>, meaning the value
     * is not set
     */
    public void setBoolean(Boolean value) {
        this.value = ((value == null) ? "" : value.toString());
    }

    /**
     * Return the value of the field.
     *
     * @return the value of the field
     */
    public Object getValueObject() {
        return getBoolean();
    }

    /**
     * Set the value of the field to the given object.
     *
     * @param object the value of the field
     */
    public void setValueObject(Object object) {
        if (object == null) {
            setValue("");
        } else {
            setValue(object.toString());
        }
    }

    /**
     * @throws UnsupportedOperationException if invoked
     */
    public void setMultiple(boolean value) {
        throw new UnsupportedOperationException("This operation is not"
            + " supported.");
    }

    /**
     * Convenience method to set the options labels. The notation will be set
     * to {@link #CUSTOM}.
     * <p/>
     * The unset option label value will default to "".
     *
     * @param trueOptionLabel the true option label
     * @param falseOptionLabel the false option label
     */
    public void setOptionLabels(String trueOptionLabel, String falseOptionLabel) {
        setOptionLabels(trueOptionLabel, falseOptionLabel, "");
    }

    /**
     * Convenience method to set the options labels. The notation will be set
     * to {@link #CUSTOM}.
     *
     * @param trueOptionLabel the true option label
     * @param falseOptionLabel the false option label
     * @param unsetOptionLabel the unset option label
     */
    public void setOptionLabels(String trueOptionLabel, String falseOptionLabel,
        String unsetOptionLabel) {

        setOptionLabelTrue(trueOptionLabel);
        setOptionLabelFalse(falseOptionLabel);
        setOptionLabelUnset(unsetOptionLabel);
        setNotation(CUSTOM);
    }

    /**
     * Returns the notation used for the BooleanSelect.
     *
     * @return the notation for the BooleanSelect
     */
    public String getNotation() {
        return notation;
    }

    /**
     * Change the option labels to one of the built-in notations.
     * <p/>
     * The following notations are provided which is also available through
     * the resource-bundle for i18n support.
     * <p/>
     * <ul>
     * <li>{@link #TRUEFALSE} (true/false}</li>
     * <li>{@link #YESNO} (yes/no)</li>
     * <li>{@link #ONOFF} (on/off)</li>
     * <li>{@link #ACTIVEINACTIVE} (active/inactive)</li>
     * <li>{@link #OPENCLOSED} (open/closed)</li>
     * <li>{@link #CUSTOM} (this option is automatically set when you provide
     * custom labels with: <tt>setOptionLabels()</tt>)</li>
     * </ul>
     *
     * The prefered way of setting the notation is by using the static
     * properties of this class:
     * <p/>
     * <code>setNotation(BooleanSelect.YESNO);</code>
     *
     * @param notation the notation to set the field's labels to
     */
    public void setNotation(String notation) {
        // TODO caveat: this method is not very robust as you can add an
        // illegal argument, it does allow you to add custom values to the
        // i18n resource bundle without recompiling though
        if (StringUtils.isEmpty(notation)) {
            throw new IllegalArgumentException(notation
                + " is not a valid option for notation.");
        }
        this.notation = notation;
    }

    /**
     * Return true if the tristate option is enabled, false otherwise.
     *
     * @return true if the tristate option is enabled, false otherwise.
     */
    public boolean isTristate() {
        return tristate;
    }

    /**
     * Set the tristate option of the field. If tristate is set to true this
     * indicates that the select field will contain three options, true, false
     * and unset. Otherwise the select will only contain two options, true and
     * false.
     *
     * @param tristate false = 2 options, true = 3 options
     */
    public void setTristate(boolean tristate) {
        this.tristate = tristate;
    }

    /**
     * Return the the label to use for the <tt>true</tt> option.
     *
     * @return custom value or message from language bundle according to the
     * chosen notation
     */
    public String getOptionLabelTrue() {
        String result;
        if (CUSTOM.equals(notation)) {
            if (customTrue == null) throw new IllegalStateException("You must"
                + " set custom option labels when you choose CUSTOM notation.");
            result = customTrue;
        } else {
            result = getMessage(notation + "-true");
            if (result == null) throw new RuntimeException("Could not find"
                + " resource with name: " + notation + "-true");
        }
        return result;
    }

    /**
     * Set the label of the <tt>true</tt> option.
     *
     * @param optionLabel the label of the true option
     */
    public void setOptionLabelTrue(String optionLabel) {
        if (StringUtils.isEmpty(optionLabel)) {
            throw new IllegalArgumentException("You must provide a value for"
                + " the TrueOptionLabel");
        }
        this.customTrue = optionLabel;
    }

    /**
     * Return the the label to use for the <tt>false</tt> option.
     *
     * @return custom value or message from language bundle according to the
     * chosen notation
     */
    public String getOptionLabelFalse() {
        String res;
        if (CUSTOM.equals(notation)) {
            if (customFalse == null) throw new IllegalStateException("You must"
                + " set custom option labels when you choose CUSTOM notation.");
            res = customFalse;
        } else {
            res = getMessage(notation + "-false");
            if (res == null) throw new RuntimeException("Could not find"
                + " resource with name: " + notation + "-false");
        }
        return res;
    }

    /**
     * Set the label of the <tt>false</tt> option.
     *
     * @param optionLabel the label of the false option
     */
    public void setOptionLabelFalse(String optionLabel) {
        if (StringUtils.isEmpty(optionLabel)) {
            throw new IllegalArgumentException("You must provide a value for the FalseOptionLabel");
        }
        this.customFalse = optionLabel;
    }

    /**
     * Return the the label to use for the <tt>unset</tt> option.
     *
     * @return custom value or message from language bundle according to the
     * chosen notation
     */
    public String getOptionLabelUnset() {
        String res;
        if (CUSTOM.equals(notation)) {
            if (customUnset == null) throw new IllegalStateException("You must"
                + " set custom option labels when you choose CUSTOM notation.");
            res = customUnset;
        } else {
            // are individual messages for the unset option, needed?
            res = "";
        }
        return res;
    }

    /**
     * Set the label of the <tt>unset</tt> option.
     *
     * @param optionLabel the label of the unset option
     */
    public void setOptionLabelUnset(String optionLabel) {
        if (optionLabel == null) throw new IllegalArgumentException("You must"
            + " provide a value for the UnsetOptionLabel"); // empty is allowed
        this.customUnset = optionLabel;
    }
}
