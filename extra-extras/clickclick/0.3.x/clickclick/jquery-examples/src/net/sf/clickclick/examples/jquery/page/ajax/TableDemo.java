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
package net.sf.clickclick.examples.jquery.page.ajax;

import net.sf.clickclick.examples.jquery.page.BorderPage;
import net.sf.clickclick.jquery.control.ajax.JQActionLink;
import net.sf.clickclick.jquery.Taconite;
import net.sf.clickclick.util.AjaxAdapter;
import net.sf.clickclick.util.Partial;
import org.apache.click.Control;
import org.apache.click.control.ActionLink;
import org.apache.click.control.Column;
import org.apache.click.control.Table;
import org.apache.click.extras.control.TableInlinePaginator;

public class TableDemo extends BorderPage {

    private Table table;

    public TableDemo() {

        buildTable();

        addControl(table);

        table.getControlLink().setActionListener(new AjaxAdapter() {

            public Partial onAjaxAction(Control source) {
                Taconite partial = new Taconite();

                // Note: table must be processed in order to update paging and
                // sorting state
                table.onProcess();

                // Note: there is no onRender event during Ajax calls, so we
                // invoke onRender explicitly in order to set table rows
                onRender();

                // If an external Table Paginator is used, we must take care to
                // remove left over paginator elements
                // partial.remove(".pagelinks, .pagebanner, .pagelinks-nobanner");

                // Append a table after the link
                partial.replace(table);

                return partial;
            }
        });
    }

    public void onRender() {
        table.setRowList(getCustomerService().getCustomers());
    }

    private void buildTable() {
        table = new Table("table") {

            /**
             * Return the Ajax enabled ActionLink instead of Table's default
             * ActionLink
             */
            @Override
            public ActionLink getControlLink() {
                if (controlLink == null) {
                    controlLink = new JQActionLink();
                }
                return controlLink;
            }
        };

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
}
