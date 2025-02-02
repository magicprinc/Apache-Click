package org.apache.click.examples.page;

import org.apache.click.Page;
import org.apache.click.control.Checkbox;
import org.apache.click.control.FieldSet;
import org.apache.click.control.Form;
import org.apache.click.control.HiddenField;
import org.apache.click.control.Submit;
import org.apache.click.control.TextField;
import org.apache.click.examples.control.InvestmentSelect;
import org.apache.click.examples.domain.Customer;
import org.apache.click.examples.service.CustomerService;
import org.apache.click.extras.control.DateField;
import org.apache.click.extras.control.DoubleField;
import org.apache.click.extras.control.EmailField;
import org.apache.click.extras.control.IntegerField;
import org.apache.click.util.Bindable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 * Provides an edit Customer Form example. The Customer business object
 * is initially passed to this Page as a request attribute.
 * <p/>
 * Note the bindable "id" and "referrer" fields have their values automatically
 * set with any identically named request parameters after the page is created.
 * These fields will be used to populate the similarly named HiddenFields
 * on GET requests. See the onGet method below.
 */
@Component
public class EditCustomer extends BorderPage {
  private static final long serialVersionUID = 1L;

  private final Form form = new Form("form");
  private final HiddenField referrerField = new HiddenField("referrer", String.class);
  private final HiddenField idField = new HiddenField("id", Integer.class);

  // Bindable variables can automatically have their value set by request parameters
  @Bindable public Integer id;
  @Bindable public String referrer; // todo MVEL can't set value even after Field.setAccessible(true)

  @Resource(name="customerService")
  private CustomerService customerService;


  public EditCustomer() {
    addControl(form);

    form.add(referrerField);

    form.add(idField);

    FieldSet fieldSet = new FieldSet("customer");
    form.add(fieldSet);

    TextField nameField = new TextField("name", true);
    nameField.setMinLength(5);
    nameField.setFocus(true);
    fieldSet.add(nameField);

    fieldSet.add(new EmailField("email"));

    IntegerField ageField = new IntegerField("age");
    ageField.setMinValue(1);
    ageField.setMaxValue(120);
    ageField.setWidth("40px");
    fieldSet.add(ageField);

    DoubleField holdingsField = new DoubleField("holdings");
    holdingsField.setTextAlign("right");
    fieldSet.add(holdingsField);

    fieldSet.add(new InvestmentSelect("investments"));
    fieldSet.add(new DateField("dateJoined"));
    fieldSet.add(new Checkbox("active"));

    form.add(new Submit("ok", "  OK  ", this, "onOkClick"));
    form.add(new Submit("cancel", this, "onCancelClick"));
  }

  // Event Handlers ---------------------------------------------------------

  /**
   * When page is first displayed on the GET request.
   *
   * @see Page#onGet()
   */
  @Override
  public void onGet() {
    if (id != null) {
      Customer customer = customerService.getCustomerForID(id);

      if (customer != null) {
        // Copy customer data to form. The idField value will be set by
        // this call
        form.copyFrom(customer);
      }
    }

    if (referrer != null) {
      // Set HiddenField to bound referrer field
      referrerField.setValue(referrer);
    }
  }

  public boolean onOkClick() {
    if (form.isValid()) {
      Integer id = (Integer) idField.getValueObject();
      Customer customer = customerService.getCustomerForID(id);

      if (customer == null) {
        customer = new Customer();
      }
      form.copyTo(customer);

      customerService.saveCustomer(customer);

      String referrer = referrerField.getValue();
      if (referrer != null) {
        setRedirect(referrer);
      } else {
        setRedirect(HomePage.class);
      }

      return true;

    } else {
      return true;
    }
  }

  public boolean onCancelClick() {
    String referrer = referrerField.getValue();
    if (referrer != null) {
      setRedirect(referrer);
    } else {
      setRedirect(HomePage.class);
    }
    return true;
  }

}