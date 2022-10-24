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
import net.sf.clickclick.control.Text;
import net.sf.clickclick.control.data.DataDecorator;
import net.sf.clickclick.control.data.DataRow;
import net.sf.clickclick.control.html.table.Cell;
import net.sf.clickclick.control.html.table.HtmlTable;
import net.sf.clickclick.control.repeater.Repeater;
import net.sf.clickclick.control.repeater.RepeaterRow;
import net.sf.clickclick.examples.domain.Customer;
import net.sf.clickclick.examples.page.BorderPage;
import org.apache.click.ActionListener;
import org.apache.click.Context;
import org.apache.click.Control;
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
import org.apache.click.util.HtmlStringBuffer;
import org.apache.commons.lang.StringUtils;

/**
 *
 */
public class TableRepeaterPage extends BorderPage {

    private Form form = new Form("form");

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
                tableRow.add(customer, "age", new DataDecorator() {
                    public void render(HtmlStringBuffer buffer, Object object,
                        Context context) {
                        Customer customer = (Customer) object;
                        buffer.append(customer.getAge());
                    }
                });
                tableRow.add(customer, "dateJoined", "{0,date,dd MMM yyyy}");
                tableRow.add(customer, "holdings", "{0,number,currency}");

                Cell actions = new Cell();
                tableRow.add(actions);

                ActionLink delete = new ActionLink("delete");
                delete.setActionListener(new ActionListener() {
                    public boolean onAction(Control source) {
                        // Remove item from Repeater
                        removeItem(item);

                        // TODO
                        // delete customer from DB

                        // Perform redirect to guard against user hitting refresh
                        // and setting the ActionLink value to the deleted recordId
                        setRedirect(TableRepeaterPage.class);
                        return false;
                    }
                });

                actions.add(delete);

                ActionLink edit = new ActionLink("edit");
                edit.setActionListener(new ActionListener() {
                    public boolean onAction(Control source) {
                        // Copy the item to edit to the Form. This sets the Page
                        // into edit mode.
                        form.copyFrom(item);
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
        	public List getData() {
        		return getTopCustomers();
        	}
        });

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
        submit.setActionListener(new ActionListener() {
            public boolean onAction(Control source) {
                if (form.isValid()) {
                    String id = idField.getValue();
                    Customer customer = null;
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

                    // In real world app we would save to DB

                    // Perform redirect to ensure the form changes are reflected
                    // by the Repeater
                    setRedirect(TableRepeaterPage.class);
                }
                return true;
            }
        });
        form.add(submit);

        Submit cancel = new Submit("cancel");
        cancel.setActionListener(new ActionListener(){
            public boolean onAction(Control source) {
                form.clearValues();
                form.clearErrors();
                return true;
            }
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
