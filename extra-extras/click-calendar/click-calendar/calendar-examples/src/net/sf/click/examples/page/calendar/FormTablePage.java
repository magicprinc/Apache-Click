package net.sf.click.examples.page.calendar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.click.control.Checkbox;
import org.apache.click.control.Column;
import org.apache.click.control.Form;
import org.apache.click.control.Submit;
import org.apache.click.control.Table;
import org.apache.click.control.TextField;
import net.sf.click.examples.domain.Customer;
import net.sf.click.examples.page.BorderPage;
import net.sf.click.extras.control.CalendarField;
import org.apache.click.extras.control.EmailField;
import org.apache.click.extras.control.FieldColumn;
import org.apache.click.extras.control.FormTable;
import org.apache.click.extras.control.NumberField;

/**
 * Provides an demonstration of Table control paging.
 *
 * @author Malcolm Edgar
 */
public class FormTablePage extends BorderPage {

    public FormTable table = new FormTable();

    // ------------------------------------------------------------ Constructor

    public FormTablePage() {
        // Setup customers table
        table.setClass(Table.CLASS_SIMPLE);
        table.setWidth("700px");
        table.getForm().setButtonAlign(Form.ALIGN_RIGHT);
        table.setPageSize(10);
        table.setShowBanner(true);

        table.addColumn(new Column("id"));

        FieldColumn column = new FieldColumn("name", new TextField());
        column.getField().setRequired(true);
        column.setVerticalAlign("baseline");
        table.addColumn(column);

        column = new FieldColumn("email", new EmailField());
        column.getField().setRequired(true);
        table.addColumn(column);

        NumberField numberField = new NumberField();
        numberField.setSize(10);
        column = new FieldColumn("holdings", numberField);
        column.setTextAlign("right");
        table.addColumn(column);

        column = new FieldColumn("dateJoined", new CalendarField());
        column.setDataStyle("white-space", "nowrap");
        table.addColumn(column);

        column = new FieldColumn("active", new Checkbox());
        column.setTextAlign("center");
        table.addColumn(column);

        table.getForm().add(new Submit("ok", "  OK  ", this, "onOkClick"));
        table.getForm().add(new Submit("cancel", this, "onCancelClick"));
    }

    // --------------------------------------------------------- Event Handlers

    /**
     * @see org.apache.click.Page#onInit()
     */
    @Override
    public void onInit() {
        super.onInit();

        // Please note the FormTable rowList MUST be populated before the
        // control is processed, i.e. do not populate the FormTable in the
        // Pages onRender() method.
        List customers = getCustomers();
        table.setRowList(customers);
    }

    public boolean onOkClick() {
        if (table.getForm().isValid()) {
            // Store changes
        }
        return true;
    }

    public boolean onCancelClick() {
        // TODO: rollback any changes made to the customers

        List customers = getCustomers();

        table.setRowList(customers);
        table.setRenderSubmittedValues(false);

        return true;
    }

    // -------------------------------------------------------- Private Methods

    private List getCustomers() {
        List customers = (List) getContext().getSessionAttribute("customers");
        if (customers == null) {
            customers = createCustomers();
            getContext().setSessionAttribute("customers", customers);
        }
        return customers;
    }

    private List createCustomers() {
        List list = new ArrayList();
        list.add(new Customer("1", "Brad Cole", 20, "brad@gmail.com", new Date((1997 - 1900), 5, 17), "Stocks", 632000));
        list.add(new Customer("2", "David Huew", 25, "david@yahoo.com", new Date((2001 - 1900), 2, 12), "Stocks", 434553));
        list.add(new Customer("3", "Steven Jones", 30, "steven@city.com", new Date((1988 - 1900), 11, 15), "Bonds", 99000));
        return list;
    }
}
