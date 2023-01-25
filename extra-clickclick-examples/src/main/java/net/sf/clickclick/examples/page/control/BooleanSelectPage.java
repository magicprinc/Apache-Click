package net.sf.clickclick.examples.page.control;

import net.sf.clickclick.control.BooleanSelect;
import net.sf.clickclick.examples.page.BorderPage;
import org.apache.click.control.FieldSet;
import org.apache.click.control.Form;
import org.apache.click.control.Label;
import org.apache.click.control.Select;
import org.apache.click.control.Submit;
import org.apache.click.extras.control.PageSubmit;

import java.io.Serial;
import java.util.Locale;

public class BooleanSelectPage extends BorderPage {
  @Serial private static final long serialVersionUID = -909157006568352034L;

  public Form           locForm    = new Form();
  public Form           form       = new Form();
  private final FieldSet unstyled   = new FieldSet("BooleanSelect");
  private final Select locale     = new Select("Select Locale");
  private final Submit sbOk       = new Submit("ok", "  OK  ", this, "onSubmitClick");

  private final BooleanSelect twostate   = new BooleanSelect("Twostate");
  private final BooleanSelect tristate   = new BooleanSelect("Tristate", true);
  private final BooleanSelect custom     = new BooleanSelect("Custom labels");

  private final BooleanSelect notationTF = new BooleanSelect("True/False");
  private final BooleanSelect notationYN = new BooleanSelect("Yes/No");
  private final BooleanSelect notationOO = new BooleanSelect("On/Off");
  private final BooleanSelect notationAI = new BooleanSelect("Active/Inactive");
  private final BooleanSelect notationOC = new BooleanSelect("Open/Closed");

  public String         result     = "";

  @Override public void onInit() {
    // -- personalizing BooleanSelect fields
    custom.setOptionLabels("Okidoki", "No way!");

    notationTF.setNotation(BooleanSelect.TRUEFALSE); // default so not really necessary
    notationYN.setNotation(BooleanSelect.YESNO); // you can also set this option in the constructor
    notationOO.setNotation(BooleanSelect.ONOFF);
    notationAI.setNotation(BooleanSelect.ACTIVEINACTIVE);
    notationOC.setNotation(BooleanSelect.OPENCLOSED);

    // -- adding fields to form & page --
    unstyled.add(twostate);
    unstyled.add(tristate);
    unstyled.add(custom);
    unstyled.add(new Label("<i>Notations backed by resource bundle.</i>"));
    unstyled.add(notationTF);
    unstyled.add(notationYN);
    unstyled.add(notationOO);
    unstyled.add(notationAI);
    unstyled.add(notationOC);

    locale.addAll(new String[] { "[set by browser]", "EN", "NL" });
    locForm.add(locale);
    locForm.add(new Label("<small><i>this will reset the form</i></small>"));
    locForm.add(new Submit("update", "update", this, "onUpdateClick"));

    form.add(unstyled);
    form.add(sbOk);
    form.add(new PageSubmit("cancel", this.getClass()));
  }

  @Override public void onRender() {
    String l = getContext().getLocale().getLanguage();
    locale.setValue(l.toUpperCase());
    locale.setLabel("Change Locale (current: " + l + ")");
  }

  public boolean onSubmitClick() {
    if (form.isValid()) {
      result = "Tristate value is: " + tristate.getBoolean().toString().toUpperCase();
    }
    return true;
  }

  public boolean onUpdateClick() {
    if ("EN".equals(locale.getValue()))
      getContext().setLocale(new Locale("en"));
    else if ("NL".equals(locale.getValue()))
      getContext().setLocale(new Locale("nl"));
    else
      getContext().setLocale(Locale.getDefault());

    // must do a reload of the page to see the changes
    setRedirect(this.getClass());
    return false;
  }
}