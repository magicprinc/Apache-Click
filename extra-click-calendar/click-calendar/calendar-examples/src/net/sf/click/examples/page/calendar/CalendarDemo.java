package net.sf.click.examples.page.calendar;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.click.control.Checkbox;
import org.apache.click.control.Field;
import org.apache.click.control.FieldSet;
import org.apache.click.control.Form;
import org.apache.click.control.Submit;
import net.sf.click.examples.page.BorderPage;
import net.sf.click.examples.page.HomePage;
import net.sf.click.extras.control.CalendarField;
import org.apache.click.extras.control.DateField;
import org.apache.click.extras.control.PageSubmit;
import org.apache.click.util.ContainerUtils;
import org.apache.commons.lang.BooleanUtils;

/**
 * Demonstrates CalendarField usage.
 *
 * @author Bob Schellink
 */
public class CalendarDemo extends BorderPage {

    public Form form = new Form();

    public Form optionsForm = new Form();

    private Checkbox allFieldsRequired = new Checkbox("allFieldsRequired");

    private Checkbox jsValidate = new Checkbox("jsValidate",
                                               "JavaScript Validate");

    // ------------------------------------------------------------ Constructor

    public CalendarDemo() {
        form.setErrorsPosition(Form.POSITION_TOP);

        FieldSet fieldSet = new FieldSet("calendars", "Calendar and Date Field");
        form.add(fieldSet);

        CalendarField calendarField = new CalendarField("calendarField");
        fieldSet.add(calendarField);

        DateField dateField = new DateField("dateField");
        fieldSet.add(dateField);

        form.add(new Submit("submit"));
        form.add(new PageSubmit("cancel", HomePage.class));

        // Settings Form
        fieldSet = new FieldSet("options", "Form Options");
        allFieldsRequired.setAttribute("onchange", "optionsForm.submit();");
        fieldSet.add(allFieldsRequired);
        jsValidate.setAttribute("onchange", "optionsForm.submit();");
        fieldSet.add(jsValidate);
        optionsForm.add(fieldSet);
        optionsForm.setListener(this, "onOptionsSubmit");
    }

    // --------------------------------------------------------- Event Handlers

    /**
     * @see org.apache.click.Page#onInit()
     */
    @Override
    public void onInit() {
        super.onInit();
        applyOptions();
    }

    public boolean onOptionsSubmit() {
        Map options = new HashMap();
        options.put("allFieldsRequired", Boolean.valueOf(allFieldsRequired.isChecked()));
        options.put("javaScriptValidate", Boolean.valueOf(jsValidate.isChecked()));
        setSessionObject(options);
        applyOptions();
        return true;
    }

    // -------------------------------------------------------- Private Methods

    private void applyOptions() {
        Map options = (Map) getSessionObject(HashMap.class);

        boolean javascriptValidate = BooleanUtils.toBoolean(String.valueOf(options.get("javaScriptValidate")));
        boolean required = BooleanUtils.toBoolean(String.valueOf(options.get("allFieldsRequired")));
        form.setJavaScriptValidation(javascriptValidate);
        List formFiels = ContainerUtils.getInputFields(form);
        for (Iterator i = formFiels.iterator(); i.hasNext();) {
            Field field = (Field) i.next();
            field.setRequired(required);
        }

        allFieldsRequired.setChecked(required);
        jsValidate.setChecked(javascriptValidate);
    }
}

