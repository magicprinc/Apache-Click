package net.sf.click.jquery.examples.page.controls;

import net.sf.click.jquery.examples.control.BetterCheckbox;
import net.sf.click.jquery.examples.domain.Customer;
import net.sf.click.jquery.examples.page.BorderPage;
import org.apache.click.ActionListener;
import org.apache.click.Control;
import org.apache.click.control.Checkbox;
import org.apache.click.control.Form;
import org.apache.click.control.Submit;
import org.apache.click.control.TextField;

/**
 *
 */
public class BetterCheckboxPage extends BorderPage {

    private Form form;

    private TextField nameField = new TextField("name", true);
    private BetterCheckbox check1 = new BetterCheckbox("check1", true);
    private BetterCheckbox check2 = new BetterCheckbox("check2");
    private BetterCheckbox check3 = new BetterCheckbox("check3");

    private Checkbox toggle = new Checkbox("toggle");

    private Customer customer;

    @Override
    public void onInit() {
        form = new Form("form");
        addControl(form);

        form.add(nameField);
        form.add(check1);
        check1.setDisabled(true);

        check1.setValue("stock");
        form.add(check2);
        check2.setValue("bonds");
        form.add(check3);
        check3.setValue("savings");

        form.add(toggle);

        Submit submit = new Submit("submit");
        submit.setActionListener(new ActionListener() {

            public boolean onAction(Control source) {
                if (form.isValid()) {
                    form.copyTo(getCustomer());
                }
                return true;
            }
        });
        form.add(submit);

        form.copyFrom(getCustomer());

        addModel("customer", customer);
    }

    Customer getCustomer() {
        if (customer == null) {
            customer = new Customer();
            customer.setName("bob");
            customer.setInvestments("apple");
        }
        return customer;
    }

}
