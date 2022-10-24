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
import net.sf.clickclick.control.html.table.Cell;
import net.sf.clickclick.control.html.table.HeaderRow;
import net.sf.clickclick.control.html.table.HtmlTable;
import net.sf.clickclick.control.html.table.Row;
import net.sf.clickclick.control.repeater.RepeaterRow;
import net.sf.clickclick.control.repeater.Repeater;
import net.sf.clickclick.examples.domain.Customer;
import net.sf.clickclick.examples.page.BorderPage;
import org.apache.click.ActionListener;
import org.apache.click.Control;
import org.apache.click.control.ActionLink;
import org.apache.click.control.FieldSet;
import org.apache.click.control.Form;
import org.apache.click.control.HiddenField;
import org.apache.click.control.Submit;
import org.apache.click.control.TextField;
import org.apache.click.extras.control.DateField;
import org.apache.click.extras.control.DoubleField;
import org.apache.click.extras.control.IntegerField;
import org.apache.commons.lang.StringUtils;

/**
 *
 */
public class TableRepeaterPage extends BorderPage {

    private Form form = new Form("form");

    public void onInit() {
        // In non-edit mode, render Master during onInit event in order to
        // apply incoming parameters.
        if (!isEditMode()) {
            createMasterView();
        }
        createDetailView();
    }

    public void onRender() {
        // In edit mode, render Master during onRender event in order to
        // see any changes made during editing.
        if (isEditMode()) {
            createMasterView();
        }
    }

    void createMasterView() {

        final HtmlTable table = new HtmlTable("table");
        table.setAttribute("class", "gray");
        table.setBorder(0);

        // Set Header Row
        Row row = new HeaderRow();
        row.add("Id");
        row.add("Name");
        row.add("Age");
        row.add("Date Joined");
        row.add("Holdings");
        row.add("Action");
        table.add(row);

        Repeater repeater = new Repeater() {

            public void buildRow(final Object item, final RepeaterRow row, final int index) {
                Customer customer = (Customer) item;

                Row tableRow = new Row();
                tableRow.add(customer.getId());
                tableRow.add(customer.getName());
                tableRow.add(customer.getAge());
                tableRow.add(getFormat().date(customer.getDateJoined()));
                tableRow.add(getFormat().currency(customer.getHoldings()));

                ActionLink delete = new ActionLink("delete");
                delete.setActionListener(new ActionListener() {
                    public boolean onAction(Control source) {
                        removeItem(item);

                        // Perform redirect to guard against user hitting refresh
                        // and setting the ActionLink value to the deleted recordId
                        setRedirect(TableRepeaterPage.class);
                        return false;
                    }
                });
                Cell actions = new Cell();
                tableRow.add(actions);
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

        repeater.setItems(getTopCustomers());

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
                        // Create new customer. This call assigs a unique ID value
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
        return getCustomerService().getCustomers().subList(0, 5);
    }

    // -------------------------------------------------------- Private Methods

    private boolean isEditMode() {
        String formName = getContext().getRequestParameter(Form.FORM_NAME);
        return StringUtils.isNotBlank(formName);
    }
}
