package org.apache.click.extras.control;

import org.apache.click.Control;
import org.apache.click.control.Field;
import org.apache.click.control.FieldSet;
import org.apache.click.control.FileField;
import org.apache.click.control.Form;
import org.apache.click.control.TextArea;
import org.apache.click.control.TextField;
import org.apache.click.util.ContainerUtils;

/**
 * Form make Field label in form, so if you need label use wrap().
 *
 * Other solutions: descendants of Form, FieldSet, HtmlForm or HtmlFieldSet and custom layout "managers".
 *
 * ?Div extends AbstractContainer public String getTag(){return "div";}
 *
 * @author Andrey Fink [rybin.andrey at gmail.com]
 *
 * @see AbstractContainerField#getContainer()
 * @see org.apache.click.control.FieldSet#renderFields(org.apache.click.util.HtmlStringBuffer)
 */
public class SimpleContainerField extends AbstractContainerField {

  public static final String TAG_DIV  = "div";
  public static final String TAG_SPAN = "span";
  public static final String TAG_P    = "p";
  public static final String TAG_NONE = null;

  private String tag;

  //

  public SimpleContainerField () {}//new


  public SimpleContainerField (final String name) {
    super(name);
  }//new


  public SimpleContainerField (final String name, final String label) {
    super(name, label);
  }//new


  public static SimpleContainerField of (Form form, Field main, Control... ctrls) {
    final SimpleContainerField c = new SimpleContainerField();
    form.add(c);
    return c.wrap(main, ctrls);
  }//wrap


  public static SimpleContainerField of (FieldSet fieldSet, Field main, Control... ctrls) {
    final SimpleContainerField c = new SimpleContainerField();
    fieldSet.add(c);
    return c.wrap(main, ctrls);
  }//wrap


  /**
   * Tip: Call after form.add(SimpleContainerField).
   */
  public SimpleContainerField wrap (Field main, Control... ctrls) {
    setLabel(main.getLabel());
    setRequired(main.isRequired());
    setDisabled(main.isDisabled());
    setError(main.getError());

    add(main);
    for (Control ctrl : ctrls) {
      add(ctrl);
    }//f

    return this;
  }//wrap


  public SimpleContainerField span () { tag = TAG_SPAN;  return this; }

  public SimpleContainerField div () { tag = TAG_DIV; return this; }

  public void setTag (String value) { tag = value; }


  @Override public String getTag () { return tag; }


  /** @see org.apache.click.control.FieldSet#insert(org.apache.click.Control, int) */
  @Override public Control insert (Control control, int index) {
    ContainerUtils.insert(this, control, index, getControlMap());

    if (control instanceof Field field){

      Form frm = getForm();
      field.setForm(frm);

      if (frm != null && frm.getDefaultFieldSize() > 0) {
        if (field instanceof TextField) {
          ((TextField) field).setSize(frm.getDefaultFieldSize());

        } else if (field instanceof FileField) {
          ((FileField) field).setSize(frm.getDefaultFieldSize());

        } else if (field instanceof TextArea) {
          ((TextArea) field).setCols(frm.getDefaultFieldSize());
        }//i
      }//i
    }//i

    return control;
  }//insert

}