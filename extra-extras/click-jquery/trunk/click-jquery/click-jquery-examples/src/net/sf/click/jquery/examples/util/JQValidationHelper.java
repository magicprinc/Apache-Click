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
package net.sf.click.jquery.examples.util;

import java.util.List;
import org.apache.click.control.Checkbox;
import org.apache.click.control.Field;
import org.apache.click.control.FileField;
import org.apache.click.control.Form;
import org.apache.click.control.RadioGroup;
import org.apache.click.control.Select;
import org.apache.click.control.TextArea;
import org.apache.click.control.TextField;
import org.apache.click.extras.control.EmailField;
import org.apache.click.extras.control.NumberField;
import org.apache.click.extras.control.PickList;
import org.apache.click.util.ContainerUtils;
import org.apache.click.util.HtmlStringBuffer;

/**
 * Renders the necessary validation rules and messages for the Form fields.
 * <p/>
 * Based on the jQuery Validator plugin: http://docs.jquery.com/Plugins/Validation
 */
public class JQValidationHelper {

    public String getRules(Form form) {
        List<Field> fields = ContainerUtils.getFields(form);
        HtmlStringBuffer buffer = new HtmlStringBuffer(fields.size() * 50);
        for (Field field : fields) {
            addRules(buffer, field);
        }
        return buffer.toString();
    }

    public String getMessages(Form form) {
        List<Field> fields = ContainerUtils.getFields(form);
        HtmlStringBuffer buffer = new HtmlStringBuffer(fields.size() * 50);
        for (Field field : fields) {
            addMessages(buffer, field);
        }
        return buffer.toString();
    }

    public String getCustomValidators() {
        return "jQuery.validator.addMethod(\"combo\", function(value, element) {"
            + " var first = jQuery(\"#\" + element.id + \" option:first\").val();"
            + " return first != value }, \"\");";
    }

    private void addRules(HtmlStringBuffer buffer, Field field) {
        if (field instanceof NumberField) {
            renderRules(buffer, (NumberField) field);
        } else if (field instanceof EmailField) {
            renderRules(buffer, (EmailField) field);
        } else if (field instanceof TextField) {
            renderRules(buffer, (TextField) field);
        } else if (field instanceof TextArea) {
            renderRules(buffer, (TextArea) field);
        } else if (field instanceof Select) {
            renderRules(buffer, (Select) field);
        } else if (field instanceof FileField) {
            renderRules(buffer, (FileField) field);
        } else if (field instanceof Checkbox) {
            renderRules(buffer, (Checkbox) field);
        } else if (field instanceof RadioGroup) {
            renderRules(buffer, (RadioGroup) field);
        } else if (field instanceof PickList) {
            renderRules(buffer, (PickList) field);
        }
    }

    private void addMessages(HtmlStringBuffer buffer, Field field) {
        if (field instanceof NumberField) {
            renderMessages(buffer, (NumberField) field);
        } else if (field instanceof EmailField) {
            renderMessages(buffer, (EmailField) field);
        } else if (field instanceof TextField) {
            renderMessages(buffer, (TextField) field);
        } else if (field instanceof TextArea) {
            renderMessages(buffer, (TextArea) field);
        } else if (field instanceof Select) {
            renderMessages(buffer, (Select) field);
        } else if (field instanceof FileField) {
            renderMessages(buffer, (FileField) field);
        } else if (field instanceof Checkbox) {
            renderMessages(buffer, (Checkbox) field);
        } else if (field instanceof RadioGroup) {
            renderMessages(buffer, (RadioGroup) field);
        } else if (field instanceof PickList) {
            renderMessages(buffer, (PickList) field);
        }
    }

    public void renderRules(HtmlStringBuffer buffer, TextField field) {
        if (buffer.length() > 0) {
            buffer.append(",\n");
        }
        boolean renderSep = false;
        ruleStart(buffer, field.getName());

        if (renderRequiredRule(buffer, field.isRequired(), renderSep)) {
            renderSep = true;
        }
        renderLengthRule(buffer, field.getMinLength(), field.getMaxLength(), renderSep);
        ruleEnd(buffer);
    }

    public void renderMessages(HtmlStringBuffer buffer, TextField field) {
        if (buffer.length() > 0) {
            buffer.append(",\n");
        }
        boolean renderSep = false;
        ruleStart(buffer, field.getName());

        if (renderRequiredMessage(buffer, field, "field-required-error", field.isRequired(), renderSep)) {
            renderSep = true;
        }

        renderLengthMessage(buffer, field, field.getMinLength(), field.getMaxLength(), renderSep);
        ruleEnd(buffer);
    }

    public void renderRules(HtmlStringBuffer buffer, TextArea field) {
        if (buffer.length() > 0) {
            buffer.append(",\n");
        }
        boolean renderSep = false;
        ruleStart(buffer, field.getName());

        if (renderRequiredRule(buffer, field.isRequired(), renderSep)) {
            renderSep = true;
        }
        renderLengthRule(buffer, field.getMinLength(), field.getMaxLength(), renderSep);
        ruleEnd(buffer);
    }

    public void renderMessages(HtmlStringBuffer buffer, TextArea field) {
        if (buffer.length() > 0) {
            buffer.append(",\n");
        }
        boolean renderSep = false;
        ruleStart(buffer, field.getName());

        if (renderRequiredMessage(buffer, field, "field-required-error", field.isRequired(), renderSep)) {
            renderSep = true;
        }

        renderLengthMessage(buffer, field, field.getMinLength(), field.getMaxLength(), renderSep);
        ruleEnd(buffer);
    }

    public void renderRules(HtmlStringBuffer buffer, Select field) {
        if (buffer.length() > 0) {
            buffer.append(",\n");
        }
        boolean renderSep = false;
        ruleStart(buffer, field.getName());

        renderSelectRule(buffer, field.isRequired(), renderSep);
        ruleEnd(buffer);
    }

    public void renderMessages(HtmlStringBuffer buffer, Select field) {
        if (buffer.length() > 0) {
            buffer.append(",\n");
        }
        boolean renderSep = false;
        ruleStart(buffer, field.getName());

        renderSelectMessage(buffer, field, "select-error", field.isRequired(), renderSep);
        ruleEnd(buffer);
    }

    public void renderRules(HtmlStringBuffer buffer, FileField field) {
        if (buffer.length() > 0) {
            buffer.append(",\n");
        }
        boolean renderSep = false;
        ruleStart(buffer, field.getName());

        renderRequiredRule(buffer, field.isRequired(), renderSep);
        ruleEnd(buffer);
    }

    public void renderMessages(HtmlStringBuffer buffer, FileField field) {
        if (buffer.length() > 0) {
            buffer.append(",\n");
        }
        boolean renderSep = false;
        ruleStart(buffer, field.getName());

        renderRequiredMessage(buffer, field, "file-required-error", field.isRequired(), renderSep);
        ruleEnd(buffer);
    }

    public void renderRules(HtmlStringBuffer buffer, Checkbox field) {
        if (buffer.length() > 0) {
            buffer.append(",\n");
        }
        boolean renderSep = false;
        ruleStart(buffer, field.getName());

        renderRequiredRule(buffer, field.isRequired(), renderSep);
        ruleEnd(buffer);
    }

    public void renderMessages(HtmlStringBuffer buffer, Checkbox field) {
        if (buffer.length() > 0) {
            buffer.append(",\n");
        }
        boolean renderSep = false;
        ruleStart(buffer, field.getName());

        renderRequiredMessage(buffer, field, "not-checked-error", field.isRequired(), renderSep);
        ruleEnd(buffer);
    }

    public void renderRules(HtmlStringBuffer buffer, RadioGroup field) {
        if (buffer.length() > 0) {
            buffer.append(",\n");
        }
        boolean renderSep = false;
        ruleStart(buffer, field.getName());

        renderRequiredRule(buffer, field.isRequired(), renderSep);
        ruleEnd(buffer);
    }

    public void renderMessages(HtmlStringBuffer buffer, RadioGroup field) {
        if (buffer.length() > 0) {
            buffer.append(",\n");
        }
        boolean renderSep = false;
        ruleStart(buffer, field.getName());

        renderRequiredMessage(buffer, field, "select-error", field.isRequired(), renderSep);
        ruleEnd(buffer);
    }

    public void renderRules(HtmlStringBuffer buffer, NumberField field) {
        if (buffer.length() > 0) {
            buffer.append(",\n");
        }
        boolean renderSep = false;
        ruleStart(buffer, field.getName());

        if (renderRequiredRule(buffer, field.isRequired(), renderSep)) {
            renderSep = true;
        }
        renderValueRule(buffer, field.getMinValue(), field.getMaxValue(), renderSep);
        ruleEnd(buffer);
    }

    public void renderMessages(HtmlStringBuffer buffer, NumberField field) {
        if (buffer.length() > 0) {
            buffer.append(",\n");
        }
        boolean renderSep = false;
        ruleStart(buffer, field.getName());

        if (renderRequiredMessage(buffer, field, "field-required-error", field.isRequired(), renderSep)) {
            renderSep = true;
        }

        renderValueMessage(buffer, field, field.getMinValue(), field.getMaxValue(), renderSep);
        ruleEnd(buffer);
    }

    public void renderRules(HtmlStringBuffer buffer, EmailField field) {
        if (buffer.length() > 0) {
            buffer.append(",\n");
        }
        boolean renderSep = false;
        ruleStart(buffer, field.getName());

        if (renderRequiredRule(buffer, field.isRequired(), renderSep)) {
            renderSep = true;
        }
        if (renderLengthRule(buffer, field.getMinLength(), field.getMaxLength(), renderSep)) {
            renderSep = true;
        }
        renderEmailRule(buffer, renderSep);
        ruleEnd(buffer);
    }

    public void renderMessages(HtmlStringBuffer buffer, EmailField field) {
        if (buffer.length() > 0) {
            buffer.append(",\n");
        }
        boolean renderSep = false;
        ruleStart(buffer, field.getName());

        if (renderRequiredMessage(buffer, field, "field-required-error", field.isRequired(), renderSep)) {
            renderSep = true;
        }

        if (renderLengthMessage(buffer, field, field.getMinLength(), field.getMaxLength(), renderSep)) {
            renderSep = true;
        }
        renderEmailMessage(buffer, field, renderSep);
        ruleEnd(buffer);
    }

    public void renderRules(HtmlStringBuffer buffer, PickList field) {
        if (buffer.length() > 0) {
            buffer.append(",\n");
        }
        boolean renderSep = false;
        ruleStart(buffer, field.getName());

        renderRequiredRule(buffer, field.isRequired(), renderSep);
        ruleEnd(buffer);
    }

    public void renderMessages(HtmlStringBuffer buffer, PickList field) {
        if (buffer.length() > 0) {
            buffer.append(",\n");
        }
        boolean renderSep = false;
        ruleStart(buffer, field.getName());

        renderRequiredMessage(buffer, field, "field-required-error", field.isRequired(), renderSep);
        ruleEnd(buffer);
    }

    // --------------------------------------------------------- Helper Methods

    public boolean renderRequiredRule(HtmlStringBuffer buffer, boolean required, boolean renderSep) {
        if (!required) {
            return false;
        }

        if (renderSep) {
            buffer.append(", ");
        }
        buffer.append("required:").append(required);
        return true;
    }

    public boolean renderRequiredMessage(HtmlStringBuffer buffer, Field field,
        String messageName, boolean required, boolean renderSep) {
        if (!required) {
            return false;
        }

        if (renderSep) {
            buffer.append(", ");
        }
        String message = field.getMessage(messageName, getErrorLabel(field));
        buffer.append("required:\"").append(message).append("\"");
        return true;
    }

    public boolean renderSelectRule(HtmlStringBuffer buffer, boolean required, boolean renderSep) {
        if (!required) {
            return false;
        }

        if (renderSep) {
            buffer.append(", ");
        }
        buffer.append("combo:").append(required);
        return true;
    }

    public boolean renderSelectMessage(HtmlStringBuffer buffer, Field field,
        String messageName, boolean required, boolean renderSep) {
        if (!required) {
            return false;
        }

        if (renderSep) {
            buffer.append(", ");
        }
        String message = field.getMessage(messageName, getErrorLabel(field));
        buffer.append("combo:\"").append(message).append("\"");
        return true;
    }

    public boolean renderLengthRule(HtmlStringBuffer buffer, int min, int max, boolean renderSep) {
        if (min <= 0 && max <= 0) {
            return false;
        }

        if (renderSep) {
            buffer.append(", ");
        }
        if (min > 0) {
            buffer.append("minlength:").append(min);
        }
        if (max > 0) {
            if (min > 0) {
                buffer.append(", ");
            }
            buffer.append("maxlength:").append(max);
        }
        return true;
    }

    public boolean renderLengthMessage(HtmlStringBuffer buffer, Field field,
        int min, int max, boolean renderSep) {
        if (min <= 0 && max <= 0) {
            return false;
        }

        if (renderSep) {
            buffer.append(", ");
        }

        String errorLabel = getErrorLabel(field);

        if (min > 0) {
            Object[] args = new Object[]{errorLabel, min};
            String message = field.getMessage("field-minlength-error", args);
            buffer.append("minlength:\"").append(message).append("\"");
        }
        if (max > 0) {
            if (min > 0) {
                buffer.append(", ");
            }
            Object[] args = new Object[]{errorLabel, max};
            String message = field.getMessage("field-maxlength-error", args);
            buffer.append("maxlength:\"").append(message).append("\"");
        }
        return true;
    }

    public boolean renderValueRule(HtmlStringBuffer buffer, double min,
        double max, boolean renderSep) {
        if (min == Double.NEGATIVE_INFINITY && max == Double.POSITIVE_INFINITY) {
            return false;
        }

        if (renderSep) {
            buffer.append(", ");
        }

        if (min != Double.NEGATIVE_INFINITY) {
            buffer.append("min:").append(min);
        }
        if (max != Double.POSITIVE_INFINITY) {
            if (min != Double.NEGATIVE_INFINITY) {
                buffer.append(", ");
            }
            buffer.append("max:").append(max);
        }
        return true;
    }

    public boolean renderValueMessage(HtmlStringBuffer buffer, Field field,
        double min, double max, boolean renderSep) {
        if (min == Double.NEGATIVE_INFINITY && max == Double.POSITIVE_INFINITY) {
            return false;
        }

        if (renderSep) {
            buffer.append(", ");
        }

        String errorLabel = getErrorLabel(field);
        if (min != Double.NEGATIVE_INFINITY) {
            Object[] args = new Object[]{errorLabel, min};
            String message = field.getMessage("number-minvalue-error", args);
            buffer.append("min:\"").append(message).append("\"");
        }
        if (max != Double.POSITIVE_INFINITY) {
            if (min != Double.NEGATIVE_INFINITY) {
                buffer.append(", ");
            }
            Object[] args = new Object[]{errorLabel, max};
            String message = field.getMessage("number-maxvalue-error", args);
            buffer.append("max:\"").append(message).append("\"");
        }
        return true;
    }

    public boolean renderEmailRule(HtmlStringBuffer buffer, boolean renderSep) {
        if (renderSep) {
            buffer.append(", ");
        }
        buffer.append("email:").append(true);
        return true;
    }

    public boolean renderEmailMessage(HtmlStringBuffer buffer, Field field,
        boolean renderSep) {
        if (renderSep) {
            buffer.append(", ");
        }
        String message = field.getMessage("email-format-error", getErrorLabel(field));
        buffer.append("email:\"").append(message).append("\"");
        return true;
    }

    private void ruleStart(HtmlStringBuffer buffer, String name) {
        buffer.append(name).append(":").append("{\n");
    }

    private void ruleEnd(HtmlStringBuffer buffer) {
        buffer.append("}");
    }

    private String getErrorLabel(Field field) {
        String label = field.getLabel().trim();
        label = (label.endsWith(":"))
                ? label.substring(0, label.length() - 1) : label;
        return label;
    }
}
