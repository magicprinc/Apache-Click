package org.apache.click.examples.page.control;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.click.control.Form;
import org.apache.click.control.Option;
import org.apache.click.control.Submit;
import org.apache.click.examples.page.BorderPage;
import org.apache.click.extras.control.PickList;



/**
 * Provides a Select example Page.
 */
@Slf4j
public class PickListDemo extends BorderPage {

  private static final long serialVersionUID = 1L;

  private final Form form = new Form("form");

  private final PickList pickList = new PickList("languages");


  public PickListDemo() {
    addControl(form);

    pickList.setHeaderLabel("Languages", "Selected");

    pickList.add(new Option("002", "C/C++"));
    pickList.add(new Option("003", "C#"));
    pickList.add(new Option("004", "Fortran"));
    pickList.add(new Option("005", "Java"));
    pickList.add(new Option("006", "Ruby"));
    pickList.add(new Option("007", "Perl"));
    pickList.add(new Option("008", "Visual Basic"));

    log.info("PickListDemo Form was created");
    form.add(pickList);

    form.add(new Submit("ok", " OK ", this, "onOkClick"));
    val cancel = new Submit("cancel", this, "onCancelClick");
    cancel.setTitle("Clear selection");
    form.add(cancel);
  }


  @Override public void onInit () {
    super.onInit();
    if (getContext().getRequest().getParameterMap().isEmpty()){
      pickList.addSelectedValue("004");
    }
  }//new


  public boolean onOkClick() {
    val selectedValues = pickList.getSelectedValues();
    addModel("selectedValues", selectedValues);
    return true;
  }

  public boolean onCancelClick() {
    pickList.getSelectedValues().clear();
    return true;
  }

}