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
package net.sf.click.jquery.examples.page.ajax;

import java.util.List;
import net.sf.click.jquery.JQEvent;
import net.sf.click.jquery.behavior.JQBehavior;
import net.sf.click.jquery.examples.domain.Customer;
import net.sf.click.jquery.examples.page.BorderPage;
import net.sf.click.jquery.taconite.JQTaconite;
import org.apache.click.Control;
import org.apache.click.ActionResult;
import org.apache.click.control.ActionLink;
import org.apache.click.control.Column;
import org.apache.click.control.Table;
import org.apache.click.dataprovider.PagingDataProvider;
import org.apache.click.extras.control.TableInlinePaginator;

public class StatefulTableDemo extends BorderPage {

	private static final long serialVersionUID = 1L;

    private ActionLink link = new ActionLink("link", "Load Table");

    private Table table;

    private TableAjaxBehavior tableAjaxBehavior  = new TableAjaxBehavior();

    public StatefulTableDemo() {
        setStateful(true);

        buildTable();

        table.setDataProvider(new PagingDataProvider() {

            public List<Customer> getData() {
                int start = table.getFirstRow();
                int count = table.getPageSize();
                String sortColumn = table.getSortedColumn();
                boolean ascending = table.isSortedAscending();

                return getCustomerService().getCustomersForPage(start, count, sortColumn, ascending);
            }

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

    // ---------------------------------------------------------- Inner classes

    private class TableAjaxBehavior extends JQBehavior {

        @Override
        public ActionResult onAction(Control source, JQEvent event) {
            JQTaconite actionResult = new JQTaconite();

            // Note: table must be processed in order to update paging and
            // sorting
            table.onProcess();

            // Replace the content of the element (with ID #target) with table
            actionResult.replaceContent("#target", table);

            return actionResult;
        }
    }
}
