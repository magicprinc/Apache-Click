package org.apache.click.examples.page.introduction;

import org.apache.click.control.Form;
import org.apache.click.control.Submit;
import org.apache.click.control.TextField;
import org.apache.click.examples.page.BorderPage;

import java.io.Serial;

/**
 * Provides a simple Form example Page.
 * <p/>
 * Note the public scope Form control field is automatically added to the Page's
 * list of controls and the String msg field is automatically added to the
 * Page's model.
 * <p/>
 * The form <tt>onSubmit</tt> control listener is invoked when the submit button
 * is clicked.
 */
public class SimpleForm extends BorderPage {
  @Serial private static final long serialVersionUID = -8294087090143874857L;

  private final Form form = new Form("form");


  public SimpleForm() {
    addControl(form);

    var tf = new TextField("name", true);
    tf.setTitle("Enter anything or your name");
    tf.setHelp("This is help message ;-)");
    form.add(tf);
    form.add(new Submit("OK"));

    form.setListener(this, "onSubmit");
  }


  /**
   * Handle the form submit event.
   */
  public boolean onSubmit() {
    if (form.isValid()) {
      String msg = "Your name is " + form.getFieldValue("name");
      addModel("msg", msg);
    }
    return true;
  }

}