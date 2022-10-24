/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.clickclick.control.repeater;

import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
import net.sf.clickclick.control.Text;
import net.sf.clickclick.control.data.DataCell;
import net.sf.clickclick.control.data.DataDecorator;
import net.sf.clickclick.control.html.table.Cell;
import net.sf.clickclick.control.html.table.HtmlTable;
import net.sf.clickclick.control.html.table.Row;
import net.sf.clickclick.domain.Customer;
import org.apache.click.ActionListener;
import org.apache.click.Context;
import org.apache.click.Control;
import org.apache.click.MockContext;
import org.apache.click.control.ActionLink;
import org.apache.click.dataprovider.DataProvider;
import org.apache.click.util.HtmlStringBuffer;

/**
 *
 */
public class RepeaterPerfTest extends TestCase {

    public void testPerf() {
        MockContext context = MockContext.initContext();
        List<Customer> customers=  getTopCustomers();

        System.out.println(internalTestPerf(customers));
        internalTestPerf(customers);

        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            internalTestPerf(customers);
            context.reset();
        }
        System.out.println("Time : " + (System.currentTimeMillis() - start));
        assertTrue(customers.size() == 10);

    }

    private HtmlTable internalTestPerf(final List<Customer> customers) {

        final HtmlTable table = new HtmlTable("table");
        table.setAttribute("class", "gray");
        table.setBorder(0);

        table.setHeader("Id", "Name", "Age", "Date Joined", "Holdings", "Action");

        Repeater repeater = new Repeater() {

            public void buildRow(final Object item, final RepeaterRow row, final int index) {
                Customer customer = (Customer) item;

                Row tableRow = new Row();
                tableRow.add(new DataCell(customer, "id"));

                tableRow.add(new DataCell(customer, "name"));
                DataCell dc = new DataCell(customer, "age");
                dc.setDecorator(new DataDecorator() {
                    public void render(HtmlStringBuffer buffer, Object object,
                        Context context) {
                        Customer customer = (Customer) object;
                        buffer.append(customer.getAge());
                    }
                });
                tableRow.add(dc);
                tableRow.add(new DataCell(customer, "dateJoined", "{0,date,dd MMM yyyy}"));
                tableRow.add(new DataCell(customer, "holdings", "{0,number,currency}"));

                ActionLink delete = new ActionLink("delete");
                delete.setActionListener(new ActionListener() {
                    public boolean onAction(Control source) {
                        removeItem(item);
                        return false;
                    }
                });
                Cell actions = new Cell();
                tableRow.add(actions);
                actions.add(delete);

                ActionLink edit = new ActionLink("edit");
                edit.setActionListener(new ActionListener() {
                    public boolean onAction(Control source) {
                        return true;
                    }
                });
                actions.add(new Text(" | "));
                actions.add(edit);

                row.add(tableRow);
            }
        };

        table.add(repeater);

        repeater.setDataProvider(new DataProvider() {
            public List<Customer> getData() {
                return customers;
            }
        });

        //addControl(table);
        //System.out.println(table);
        //table.toString();
        return table;
    }

    private List<Customer> getTopCustomers() {
        List<Customer> customers = new ArrayList();

        for (int i = 0; i < 10; i++) {
            Customer customer = new Customer();
            customer.setId(new Long(i));
            customer.setName("abc" + i);
            customers.add(customer);
        }
        return customers;
    }
}
