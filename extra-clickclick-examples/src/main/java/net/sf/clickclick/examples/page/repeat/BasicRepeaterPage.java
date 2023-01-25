package net.sf.clickclick.examples.page.repeat;

import net.sf.clickclick.control.html.table.HeaderRow;
import net.sf.clickclick.control.html.table.HtmlTable;
import net.sf.clickclick.control.html.table.Row;
import net.sf.clickclick.control.repeater.Repeater;
import net.sf.clickclick.control.repeater.RepeaterRow;
import net.sf.clickclick.examples.domain.Customer;
import net.sf.clickclick.examples.page.BorderPage;
import org.apache.click.dataprovider.DataProvider;

import java.io.Serial;
import java.util.List;

/**
 *
 */
public class BasicRepeaterPage extends BorderPage {
  @Serial private static final long serialVersionUID = -7825363838544615968L;

  private Repeater repeater;

  @Override
  public void onInit() {
    final HtmlTable table = new HtmlTable("table");
    table.setAttribute("class", "gray");
    table.setBorder(0);
    HeaderRow header = new HeaderRow();
    table.add(header);

    header.add("Name");
    header.add("Age");
    header.add("Holdings");

    repeater = new Repeater() {

      @Override
      public void buildRow(Object item, RepeaterRow row, int index) {
        Customer customer = (Customer) item;

        Row tableRow = new Row();
        row.add(tableRow);

        tableRow.add(customer.getName());
        tableRow.add(customer.getAge());
        tableRow.add(getFormat().currency(customer.getHoldings()));
      }
    };

    table.add(repeater);

    repeater.setDataProvider((DataProvider) ()->{
      List<Customer> customers = getCustomerService().getCustomers();
      if (customers.size() > 5) {
        customers = customers.subList(0, 5);
      }
      return customers;
    });

    addControl(table);
  }
}