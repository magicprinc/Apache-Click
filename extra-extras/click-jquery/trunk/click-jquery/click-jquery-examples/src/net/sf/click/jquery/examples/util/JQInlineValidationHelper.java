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
import net.sf.click.jquery.examples.control.JQColorPicker;
import org.apache.click.control.Checkbox;
import org.apache.click.control.Field;
import org.apache.click.control.FileField;
import org.apache.click.control.Form;
import org.apache.click.control.Radio;
import org.apache.click.control.RadioGroup;
import org.apache.click.control.Select;
import org.apache.click.control.TextArea;
import org.apache.click.control.TextField;
import org.apache.click.extras.control.ColorPicker;
import org.apache.click.extras.control.CreditCardField;
import org.apache.click.extras.control.EmailField;
import org.apache.click.extras.control.NumberField;
import org.apache.click.extras.control.PickList;
import org.apache.click.extras.control.TelephoneField;
import org.apache.click.util.ContainerUtils;
import org.apache.click.util.HtmlStringBuffer;
import org.apache.commons.lang.StringUtils;

/**
 * Decorates Form fields with the necessary validation rules based on the
 * following jQuery validator plugin:
 * http://www.position-absolute.com/articles/jquery-form-validator-because-form-validation-is-a-mess/
 */
public class JQInlineValidationHelper {

    public void decorate(Form form) {
        List<Field> fields = ContainerUtils.getFields(form);
        for (Field field : fields) {
            decorate(field);
        }
    }

    public void decorate(Field field) {
        if (field instanceof NumberField) {
            decorate((NumberField) field);
        } else if (field instanceof CreditCardField) {
            decorate((CreditCardField) field);
        } else if (field instanceof JQColorPicker) {
            decorate((JQColorPicker) field);
        }/* else if (field instanceof ColorPicker) { // ColorPicker uses Prototype which clashes with jQuery
            decorate((ColorPicker) field);
        }*/ else if (field instanceof EmailField) {
            decorate((EmailField) field);
        } else if (field instanceof TelephoneField ) {
            decorate((TelephoneField) field);
        } else if (field instanceof TextField) {
            decorate((TextField) field);
        } else if (field instanceof TextArea) {
            decorate((TextArea) field);
        } else if (field instanceof Select) {
            decorate((Select) field);
        } else if (field instanceof FileField) {
            decorate((FileField) field);
        } else if (field instanceof Checkbox) {
            decorate((Checkbox) field);
        } else if (field instanceof RadioGroup) {
            decorate((RadioGroup) field);
        } else if (field instanceof PickList) {
            decorate((PickList) field);
        }
    }

    public void decorate(TextField field) {
        HtmlStringBuffer buffer = new HtmlStringBuffer();
        renderRequired(buffer, field.isRequired());
        renderLength(buffer, field.getMinLength(), field.getMaxLength());
        if (buffer.length() > 0) {
            buffer.append("]");
        }
        addClassStyle(field, buffer);
    }

    public void decorate(TextArea field) {
        HtmlStringBuffer buffer = new HtmlStringBuffer();
        renderRequired(buffer, field.isRequired());
        renderLength(buffer, field.getMinLength(), field.getMaxLength());
        if (buffer.length() > 0) {
            buffer.append("]");
        }
        addClassStyle(field, buffer);
    }

    public void decorate(Select field) {
        HtmlStringBuffer buffer = new HtmlStringBuffer();
        renderRequired(buffer, field.isRequired());
        if (buffer.length() > 0) {
            buffer.append("]");
        }
        addClassStyle(field, buffer);
    }

    public void decorate(FileField field) {
        HtmlStringBuffer buffer = new HtmlStringBuffer();
        renderRequired(buffer, field.isRequired());
        if (buffer.length() > 0) {
            buffer.append("]");
        }
        addClassStyle(field, buffer);
    }

    public void decorate(Checkbox field) {
        HtmlStringBuffer buffer = new HtmlStringBuffer();
        renderRequired(buffer, field.isRequired());
        if (buffer.length() > 0) {
            buffer.append("]");
        }
        addClassStyle(field, buffer);
    }

    public void decorate(RadioGroup field) {
        List<Radio> radioList = field.getRadioList();
        for(Radio radio : radioList) {
            HtmlStringBuffer buffer = new HtmlStringBuffer();
            renderRequired(buffer, field.isRequired());

            if (buffer.length() > 0) {
                buffer.append("]");
            }
            addClassStyle(radio, buffer);
        }
    }

    public void decorate(NumberField field) {
        HtmlStringBuffer buffer = new HtmlStringBuffer();
        renderRequired(buffer, field.isRequired());
        renderValue(buffer, field.getMinValue(), field.getMaxValue());
        if (buffer.length() > 0) {
            buffer.append("]");
        }
        addClassStyle(field, buffer);
    }

    public void decorate(EmailField field) {
        HtmlStringBuffer buffer = new HtmlStringBuffer();
        renderRequired(buffer, field.isRequired());
        renderLength(buffer, field.getMinLength(), field.getMaxLength());
        renderCustom(buffer, "email");
        if (buffer.length() > 0) {
            buffer.append("]");
        }
        addClassStyle(field, buffer);
    }

    public void decorate(TelephoneField field) {
        HtmlStringBuffer buffer = new HtmlStringBuffer();
        renderRequired(buffer, field.isRequired());
        renderLength(buffer, field.getMinLength(), field.getMaxLength());
        renderCustom(buffer, "telephone");
        if (buffer.length() > 0) {
            buffer.append("]");
        }
        addClassStyle(field, buffer);
    }

    public void decorate(CreditCardField field) {
        HtmlStringBuffer buffer = new HtmlStringBuffer();
        renderRequired(buffer, field.isRequired());
        renderLength(buffer, field.getMinLength(), field.getMaxLength());
        renderCreditCard(buffer);

        if (buffer.length() > 0) {
            buffer.append("]");
        }
        addClassStyle(field, buffer);
    }

    public void decorate(ColorPicker field) {
        HtmlStringBuffer buffer = new HtmlStringBuffer();
        renderRequired(buffer, field.isRequired());
        renderCustom(buffer, "color");

        if (buffer.length() > 0) {
            buffer.append("]");
        }
        addClassStyle(field, buffer);
    }

    public void decorate(JQColorPicker field) {
        HtmlStringBuffer buffer = new HtmlStringBuffer();
        renderRequired(buffer, field.isRequired());
        renderCustom(buffer, "color");

        if (buffer.length() > 0) {
            buffer.append("]");
        }
        addClassStyle(field, buffer);
    }

    public void decorate(PickList field) {
        HtmlStringBuffer buffer = new HtmlStringBuffer();
        if (field.isRequired()) {
            buffer.append("validate[picklist['");
            buffer.append(field.getId());
            buffer.append("']]");
        }
        addClassStyle(field, buffer);
    }

    public void renderRequired(HtmlStringBuffer buffer, boolean required) {
        if (!required) {
            return;
        }

        if (buffer.length() > 0) {
            buffer.append(",");
        } else {
            buffer.append("validate[");
        }
        buffer.append("required");
    }

    public void renderLength(HtmlStringBuffer buffer, int min, int max) {
        if (min <= 0 && max <= 0) {
            return;
        }

        if (buffer.length() > 0) {
            buffer.append(",");
        } else {
            buffer.append("validate[");
        }
        if (min > 0 && max > 0) {
            buffer.append("length[").append(min).append(",").append(max).append("]");
        } else if (min > 0) {
            buffer.append("minlength[").append(min).append("]");
        } else if (max > 0) {
            buffer.append("maxlength[").append(max).append("]");
        }
    }

    public void renderValue(HtmlStringBuffer buffer, double min, double max) {
        if (min == Double.NEGATIVE_INFINITY && max == Double.POSITIVE_INFINITY) {
            return;
        }

        if (buffer.length() > 0) {
            buffer.append(",");
        } else {
            buffer.append("validate[");
        }
        if (min != Double.NEGATIVE_INFINITY) {
            buffer.append("minvalue[").append(min).append("]");
        }
        if (max != Double.POSITIVE_INFINITY) {
            buffer.append("maxvalue[").append(max).append("]");
        }
    }

    public void renderCreditCard(HtmlStringBuffer buffer) {
        if (buffer.length() > 0) {
            buffer.append(",");
        } else {
            buffer.append("validate[");
        }
        buffer.append("creditcard");
    }

    public void renderCustom(HtmlStringBuffer buffer, String custom) {
        if (StringUtils.isBlank(custom)) {
            return;
        }

        if (buffer.length() > 0) {
            buffer.append(",");
        } else {
            buffer.append("validate[");
        }
        buffer.append("custom[");
        buffer.append(custom);
        buffer.append("]");
    }

    private void addClassStyle(Field field, HtmlStringBuffer buffer) {
        String curValue = field.getAttribute("class");
        if (curValue != null) {
            buffer.append(" ").append(curValue);
        }
        field.setAttribute("class", buffer.toString());
    }
}
