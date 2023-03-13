package com.mycorp.page;

import com.mycorp.domain.Customer;
import org.apache.click.Page;
import org.apache.click.control.Checkbox;
import org.apache.click.control.FieldSet;
import org.apache.click.control.Form;
import org.apache.click.control.HiddenField;
import org.apache.click.control.Option;
import org.apache.click.control.Select;
import org.apache.click.control.Submit;
import org.apache.click.control.TextField;
import org.apache.click.extras.control.DateField;
import org.apache.click.extras.control.DoubleField;
import org.apache.click.extras.control.EmailField;
import org.apache.click.extras.control.IntegerField;
import org.apache.click.util.Bindable;



public class EditCustomer extends BorderPage {
  private static final long serialVersionUID = 7494162676914271159L;

  // Public controls are automatically added to the page
  @Bindable public Form form = new Form("form");
  @Bindable public HiddenField referrerField = new HiddenField("referrer", String.class);

  // Public variables can automatically have their value set by request parameters
  @Bindable public Long id;

  private final HiddenField idField = new HiddenField("id", Long.class);

  private final Select investmentSelect = new Select("investments");

  public EditCustomer() {
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

    fieldSet.add(investmentSelect);
    fieldSet.add(new DateField("dateJoined"));
    fieldSet.add(new Checkbox("active"));

    form.add(new Submit("ok", "  OK  ", this, "onOkClick"));
    form.add(new Submit("cancel", this, "onCancelClick"));
  }

  @Override public void onInit() {
    super.onInit();

    investmentSelect.add(Option.EMPTY_OPTION);
    investmentSelect.addAll(getCustomerService().getInvestmentCatetories());
  }

  /**
   * When page is first displayed on the GET request.
   *
   * @see Page#onGet()
   */
  @Override
  public void onGet() {
    if (id != null) {
      Customer customer = getCustomerService().getCustomerForID(id);

      if (customer != null) {
        form.copyFrom(customer);
      }
    }
  }

  public boolean onOkClick() {
    if (form.isValid()) {
      Long id = (Long) idField.getValueObject();
      Customer customer = getCustomerService().getCustomerForID(id);

      if (customer == null) {
        customer = new Customer();
      }
      form.copyTo(customer);

      getCustomerService().saveCustomer(customer);

      String referrer = referrerField.getValue();
      if (referrer != null) {
        setRedirect(referrer);
      } else {
        setRedirect(HomePage.class);
      }
    }
    return true;
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