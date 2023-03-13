package org.apache.click.examples.page.panel;

import org.apache.click.control.Form;
import org.apache.click.control.Panel;
import org.apache.click.control.Submit;
import org.apache.click.control.Table;
import org.apache.click.control.TextField;
import org.apache.click.examples.domain.Customer;
import org.apache.click.examples.page.BorderPage;
import org.apache.click.examples.service.CustomerService;
import org.apache.click.util.Bindable;
import org.apache.click.util.HtmlStringBuffer;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * Demonstrates usage of the Panel Column Control.
 */
@Component
public class PanelColumnDemo extends BorderPage {
  private static final long serialVersionUID = 1L;

  //todo MVEL can't access non-public fields?
  @Bindable public String nameSearch;

  private final Panel panel = new Panel("panel", "/panel/customerDetailsPanel.htm");
  private final Form form = new Form("form");
  private final Table table;

  private final TextField textName = new TextField("name", true);

  @Resource(name="customerService")
  private CustomerService customerService;


  public PanelColumnDemo() {
    addControl(panel);
    addControl(form);

    table = new Table("table") {
      @Override
      protected void renderHeaderRow(HtmlStringBuffer buffer) {
        // We don't want to render table columns so we override #renderHeaderRow
        // to do nothing
      }
    };
    addControl(table);

    form.add(textName);
    textName.setFocus(true);
    form.add(new Submit("search", " Search ", this, "onSearch"));

    // The name of the PanelColumn is "customer" thus ${customer}
    // variable will be available in the template
    table.addColumn(new PanelColumn("customer", panel));
    table.setPageSize(3);
  }

  // Event Handlers ---------------------------------------------------------

  /**
   * Search button handler
   */
  public boolean onSearch() {
    if (form.isValid()) {
      String value = textName.getValue().trim();

      processSearch(value);

      return true;
    }
    return false;
  }

  @Override
  public void onPost() {
    handleRequest();
  }

  @Override
  public void onGet() {
    handleRequest();
  }

  // Private Methods --------------------------------------------------------

  private void handleRequest() {
    if (StringUtils.isNotEmpty(nameSearch)) {

      // Just fill the value so the user can see it
      textName.setValue(nameSearch);

      // And fill the table again.
      processSearch(nameSearch);
    }
  }

  /**
   * Search the Customer by name and create the Table control
   *
   * @param value
   */
  private void processSearch(String value) {
    // Search for user entered value
    List<Customer> list = customerService.getCustomersForName(value);

    table.setRowList(list);

    // Set the parameter in the pagination link,
    // so in the next page, we can fill the table again.
    table.getControlLink().setParameter("nameSearch", value);
  }
}