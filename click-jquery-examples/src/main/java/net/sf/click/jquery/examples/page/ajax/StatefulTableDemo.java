package net.sf.click.jquery.examples.page.ajax;

import net.sf.click.jquery.JQEvent;
import net.sf.click.jquery.behavior.JQBehavior;
import net.sf.click.jquery.examples.domain.Customer;
import net.sf.click.jquery.examples.page.BorderPage;
import net.sf.click.jquery.taconite.JQTaconite;
import org.apache.click.ActionResult;
import org.apache.click.Control;
import org.apache.click.control.ActionLink;
import org.apache.click.control.Column;
import org.apache.click.control.Table;
import org.apache.click.dataprovider.PagingDataProvider;
import org.apache.click.extras.control.TableInlinePaginator;

import java.io.Serial;
import java.util.List;

public class StatefulTableDemo extends BorderPage {
  @Serial private static final long serialVersionUID = -3077994465944158299L;

  private final ActionLink link = new ActionLink("link", "Load Table");
  private Table table;
  private final TableAjaxBehavior tableAjaxBehavior  = new TableAjaxBehavior();

  public StatefulTableDemo() {
    //setStateful(true);

    buildTable();

    table.setDataProvider(new PagingDataProvider<Customer>(){
      @Override
			public List<Customer> getData() {
        int start = table.getFirstRow();
        int count = table.getPageSize();
        String sortColumn = table.getSortedColumn();
        boolean ascending = table.isSortedAscending();

        return getCustomerService().getCustomersForPage(start, count, sortColumn, ascending);
      }
      @Override
			public int size() {
        return getCustomerService().getNumberOfCustomers();
      }
    });

    addControl(link);
    link.setId("linkId");

    addControl(table);

    // Register ajax listener on link to load table initially
    link.addBehavior(tableAjaxBehavior);
  }

  private void buildTable() {
    table = new Table("table");

    // Reuse the same behavior
    table.getControlLink().addBehavior(tableAjaxBehavior);

    // Setup customers table
    table.setClass(Table.CLASS_BLUE2);
    table.setSortable(true);
    table.setPageSize(10);
    table.setPaginator(new TableInlinePaginator(table));
    table.setPaginatorAttachment(Table.PAGINATOR_INLINE);
    table.setHoverRows(true);

    Column column = new Column("id");
    column.setWidth("50px");
    column.setSortable(false);
    table.addColumn(column);

    column = new Column("name");
    column.setWidth("140px;");
    table.addColumn(column);

    column = new Column("email");
    column.setAutolink(true);
    column.setWidth("230px;");
    table.addColumn(column);

    column = new Column("age");
    column.setTextAlign("center");
    column.setWidth("40px;");
    table.addColumn(column);

    column = new Column("holdings");
    column.setFormat("${0,number,#,##0.00}");
    column.setTextAlign("right");
    column.setWidth("100px;");
    table.addColumn(column);
  }


  private class TableAjaxBehavior extends JQBehavior {
    @Serial private static final long serialVersionUID = -4306726090265011755L;

    @Override
		public ActionResult onAction(Control source, JQEvent event) {
      JQTaconite actionResult = new JQTaconite();

      // Note: table must be processed in order to update paging and sorting
      table.onProcess();

      // Replace the content of the element (with ID #target) with table
      actionResult.replaceContent("#target", table);

      return actionResult;
    }
  }
}