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
package net.sf.clickclick.examples.page.repeat;

import java.util.List;

import net.sf.clickclick.control.html.table.HeaderRow;
import net.sf.clickclick.control.html.table.HtmlTable;
import net.sf.clickclick.control.html.table.Row;
import net.sf.clickclick.control.paginator.SimplePaginator;
import net.sf.clickclick.control.repeater.Repeater;
import net.sf.clickclick.control.repeater.RepeaterRow;
import net.sf.clickclick.examples.domain.Customer;
import net.sf.clickclick.examples.page.BorderPage;

import org.apache.click.dataprovider.DataProvider;

/**
 *
 */
public class PagingRepeaterPage extends BorderPage {

    private Repeater repeater;
    private SimplePaginator paginator;

    @Override
    public void onInit() {

        paginator = new SimplePaginator("paginator");
        paginator.setItemsPerPage(10);
        addControl(paginator);

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
        repeater.setDataProvider(new DataProvider() {

            public List getData() {
                int numberOfCustomers = getCustomerService().getCustomers().size();

                // Set totalItems in order to call getFirstItem and getLastItem
                paginator.setTotalItems(numberOfCustomers);

                int start = paginator.getFirstItem();
                int end = paginator.getLastItem();

                return getCustomerService().getCustomers().subList(start, end);
            }
        });

        addControl(table);
    }
}
