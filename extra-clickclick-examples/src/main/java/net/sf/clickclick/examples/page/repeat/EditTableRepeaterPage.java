package net.sf.clickclick.examples.page.repeat;

import net.sf.clickclick.control.Text;
import net.sf.clickclick.control.data.DataRow;
import net.sf.clickclick.control.html.table.Cell;
import net.sf.clickclick.control.html.table.HtmlTable;
import net.sf.clickclick.control.repeater.Repeater;
import net.sf.clickclick.control.repeater.RepeaterRow;
import net.sf.clickclick.examples.domain.Customer;
import net.sf.clickclick.examples.page.BorderPage;
import org.apache.click.ActionListener;
import org.apache.click.control.ActionLink;
import org.apache.click.control.FieldSet;
import org.apache.click.control.Form;
import org.apache.click.control.HiddenField;
import org.apache.click.control.Submit;
import org.apache.click.control.TextField;
import org.apache.click.dataprovider.DataProvider;
import org.apache.click.extras.control.DateField;
import org.apache.click.extras.control.DoubleField;
import org.apache.click.extras.control.IntegerField;
import org.apache.commons.lang.StringUtils;

import java.io.Serial;
import java.util.List;

/**
 *
 */
public class EditTableRepeaterPage extends BorderPage {
  @Serial private static final long serialVersionUID = -969289669171816620L;

  private final Form form = new Form("form");

  @Override
  public void onInit() {
    createMasterView();
    createDetailView();
  }

  void createMasterView() {

    final HtmlTable table = new HtmlTable("table");
    table.setAttribute("class", "gray");
    table.setBorder(0);

    table.setHeader("Id", "Name", "Age", "Date Joined", "Holdings", "Action");

    Repeater repeater = new Repeater() {

      public void buildRow(final Object item, final RepeaterRow row, final int index) {
        Customer customer = (Customer) item;

        DataRow tableRow = new DataRow();
        tableRow.add(customer, "id");
        tableRow.add(customer, "name");
        tableRow.add(customer, "age", (buffer, object, context)->{
          Customer customer1 = (Customer) object;
          buffer.append(customer1.getAge());
        });
        tableRow.add(customer, "dateJoined", "{0,date,dd MMM yyyy}");
        tableRow.add(customer, "holdings", "{0,number,currency}");

        Cell actions = new Cell();
        tableRow.add(actions);

        ActionLink delete = new ActionLink("delete");
        delete.setActionListener((ActionListener) source->{
          // Remove item from Repeater
          removeItem(item);

          // Here we can delete customer from DB

          // Perform redirect to guard against user hitting refresh
          // and setting the ActionLink value to the deleted recordId
          setRedirect(EditTableRepeaterPage.class);
          return false;
        });

        actions.add(delete);

        ActionLink edit = new ActionLink("edit");
        edit.setActionListener((ActionListener) source->{
          // Copy the item to edit to the Form. This sets the Page
          // into edit mode.
          form.copyFrom(item);
          return true;
        });
        actions.add(new Text(" | "));
        actions.add(edit);

        row.add(tableRow);
      }
    };

    table.add(repeater);

    repeater.setDataProvider((DataProvider) ()->getTopCustomers());

    addControl(table);
  }

  void createDetailView() {
    // Setup customers form
    FieldSet fieldSet = new FieldSet("customer");

    final HiddenField idField = new HiddenField("id", Long.class);
    fieldSet.add(idField);

    fieldSet.add(new TextField("name"));
    fieldSet.add(new DateField("dateJoined"));
    fieldSet.add(new IntegerField("age"));
    fieldSet.add(new DoubleField("holdings"));

    form.add(fieldSet);

    Submit submit = new Submit("save");
    submit.setActionListener((ActionListener) source->{
      if (form.isValid()) {
        String id = idField.getValue();
        Customer customer;
        if (StringUtils.isBlank(id)) {
          // Create new customer. This call assigns a unique ID value
          customer = getCustomerService().createCustomer();

          // Update the idField value to the new customer ID value
          idField.setValueObject(customer.getId());

          getCustomerService().getCustomers().add(0, customer);
        } else {
          customer = getCustomerService().findCustomer(id);
        }
        form.copyTo(customer);

        // Here we can update customer in db

        // Perform redirect to ensure the form changes are reflected
        // by the Repeater
        setRedirect(EditTableRepeaterPage.class);
      }
      return true;
    });
    form.add(submit);

    Submit cancel = new Submit("cancel");
    cancel.setActionListener((ActionListener) source->{
      form.clearValues();
      form.clearErrors();
      return true;
    });
    form.add(cancel);

    addControl(form);
  }

  public List<Customer> getTopCustomers() {
    List<Customer> customers = getCustomerService().getCustomers();
    int size = Math.min(5, customers.size());
    return customers.subList(0, size);
  }
}