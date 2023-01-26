package org.apache.click.pages;

import org.apache.click.ActionListener;
import org.apache.click.Page;
import org.apache.click.control.Form;
import org.apache.click.control.Submit;
import org.apache.click.control.TextField;

/**
 Page which tests action listener functionality.
 */
public class ListenerPage extends Page {
  /** Form holder. */
  public Form form = new Form("form");

  /** Indicates if the submit Assertion succeeded or not. */
  public boolean success = false;


  /**
   Initialize page.
   */
  @Override public void onInit () {
    // Create and add submit button *before* adding the textField
    Submit submit = new Submit("save");
    form.add(submit);

    // Add listener on submit button
    submit.setActionListener((ActionListener) source->{
      // Assert that this listener can access the textfield value
      assert "one".equals(form.getFieldValue("field"));
      success = true;
      return true;
    });

    // Add textfield after the button.
    form.add(new TextField("field"));
  }
}