package org.apache.click.examples.page.control;

import lombok.var;
import org.apache.click.ActionListener;
import org.apache.click.control.FieldSet;
import org.apache.click.control.Form;
import org.apache.click.control.TextField;
import org.apache.click.examples.page.BorderPage;
import org.apache.click.extras.control.SubmitLink;

import java.util.Date;

/**
 * This example demonstrates how to use a SubmitLink control together with the Form control.
 */
public class SubmitLinkDemo extends BorderPage {
  private static final long serialVersionUID = 1L;


  public SubmitLinkDemo() {
    demo1();

    demo2();

    demo3();

    demo4();
  }//new


  public void demo1() {
    // Create a submit link.
    final SubmitLink submitLink = new SubmitLink("save");

    Form form = new Form("demo1");
    addControl(form);

    FieldSet fieldSet = new FieldSet("fieldSet");
    form.add(fieldSet);

    fieldSet.add(new TextField("name"));

    // Add the submit link to the fieldSet
    fieldSet.add(submitLink);

    // The SubmitLink action listener
    submitLink.setActionListener((ActionListener) source->{
      String demo1Msg = submitLink.getName() + ".onAction invoked at " + new Date();
      addModel("demo1Msg", demo1Msg);
      return true;
    });
  }

  public void demo2() {
    // Create a submit link which includes parameters.
    final SubmitLink paramLink = new SubmitLink("paramLink");

    Form form = new Form("demo2");
    addControl(form);

    FieldSet fieldSet = new FieldSet("fieldSet");
    form.add(fieldSet);

    fieldSet.add(new TextField("name"));

    // Add some parameters to the parametrized submit link
    paramLink.setValue("myValue");
    paramLink.setParameter("x", "100");

    // Add the parametrized submit link to the FieldSet
    fieldSet.add(paramLink);

    // The Parametrized SubmitLink action listener
    paramLink.setActionListener((ActionListener) source->{
      String demo2Msg = paramLink.getName() + ".onAction invoked at " + new Date();
      var f = fieldSet.getField("name");
      demo2Msg += "<br>Parameters:" + paramLink.getParameters()+"<br>source:"+source.getName()+'/'+source.getId()+'/'+source.getClass().getSimpleName()+
          ". FieldSet:"+ f.getName()+"="+f.getValueObject();
      addModel("demo2Msg", demo2Msg);
      return true;
    });
  }

  public void demo3() {
    // Create a standalone submit link.
    final SubmitLink standaloneLink = new SubmitLink("standaloneLink");

    // Add the Standalone SubmitLink to the Page
    addControl(standaloneLink);

    // The Standalone SubmitLink action listener
    standaloneLink.setActionListener((ActionListener) source->{
      String demo3Msg = source.getName() + ".onAction invoked at "+ new Date();
      addModel("demo3Msg", demo3Msg);
      return true;
    });
  }

  public void demo4() {
    // Create a submit link
    final SubmitLink confirmationLink = new SubmitLink("confirmationLink");

    Form form = new Form("demo4");
    addControl(form);

    FieldSet fieldSet = new FieldSet("fieldSet");
    form.add(fieldSet);

    fieldSet.add(new TextField("name"));

    // Add the submit link to the FieldSet
    fieldSet.add(confirmationLink);

    // Set custom JavaScript for the onclick event. The confirmSubmit function
    // is defined in the page template -> submit-link-demo.htm
    String clickEvent = "return confirmSubmit(this, '" + form.getId() + "', 'Are you sure?');";
    confirmationLink.setOnClick(clickEvent);

    // The Parametrized SubmitLink action listener
    confirmationLink.setActionListener((ActionListener) source->{
      String demo4Msg = confirmationLink.getName() + ".onAction invoked at " + new Date();
      addModel("demo4Msg", demo4Msg);
      return true;
    });
  }
}