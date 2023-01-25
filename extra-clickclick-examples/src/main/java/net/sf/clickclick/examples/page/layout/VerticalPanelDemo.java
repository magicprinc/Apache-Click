package net.sf.clickclick.examples.page.layout;

import net.sf.clickclick.control.html.Div;
import net.sf.clickclick.control.html.HtmlLabel;
import net.sf.clickclick.control.html.Span;
import net.sf.clickclick.control.panel.VerticalPanel;
import net.sf.clickclick.examples.page.BorderPage;
import org.apache.click.control.AbstractControl;
import org.apache.click.control.Checkbox;
import org.apache.click.control.Field;
import org.apache.click.control.TextField;
import org.apache.click.extras.control.HtmlForm;
import org.apache.click.extras.control.IntegerField;
import org.apache.click.util.HtmlStringBuffer;

import java.io.Serial;

public class VerticalPanelDemo extends BorderPage {
  @Serial private static final long serialVersionUID = -2425148036118986768L;

  private final HtmlForm form = new HtmlForm("form");

  @Override public void onInit() {
    createLayoutDemo();
    createLayoutWithForm();
  }

  /**
   * Demo 1
   */
  private void createLayoutDemo() {
    VerticalPanel verticalPanel = new VerticalPanel("demo1");

    Div div = new Div();
    // Use normal CSS properties to style the divs
    div.setStyle("background", "red");
    div.setStyle("width", "100px");
    div.setStyle("height", "100px");
    verticalPanel.add(div);

    div = new Div();
    div.setStyle("background", "yellow");
    div.setStyle("width", "100px");
    div.setStyle("height", "100px");
    verticalPanel.add(div);

    div = new Div();
    div.setStyle("background", "blue");
    div.setStyle("width", "100px");
    div.setStyle("height", "100px");
    verticalPanel.add(div);

    addControl(verticalPanel);
  }

  /**
   * Demo 2
   */
  private void createLayoutWithForm() {
    addControl(form);

    VerticalPanel verticalPanel = new VerticalPanel();
    form.add(verticalPanel);

    addField(new TextField("name"), verticalPanel);
    addField(new IntegerField("age"), verticalPanel);
    addField(new Checkbox("married"), verticalPanel);
  }

  private void addField(Field field, VerticalPanel verticalPanel) {
    HtmlLabel label = new HtmlLabel(field);
    verticalPanel.add(new FieldLabelCombo(field, label));
  }

  static class FieldLabelCombo extends AbstractControl {
    @Serial private static final long serialVersionUID = -8140884658901043978L;

    private final Field field;
    private final HtmlLabel label;
    public FieldLabelCombo (Field field, HtmlLabel label) {
      this.field = field;
      this.label = label;
    }
    @Override public void render (HtmlStringBuffer buffer) {
      Span span = new Span();
      // Float the label to the left with and set a width of 100 pixels
      span.setAttribute("style", "width:100px; display:block; float:left");
      span.add(label);
      span.render(buffer);

      span = new Span();
      // Float the field to the right
      span.setAttribute("style", "display:block; float:left");
      span.add(field);
      span.render(buffer);
    }
  }
}