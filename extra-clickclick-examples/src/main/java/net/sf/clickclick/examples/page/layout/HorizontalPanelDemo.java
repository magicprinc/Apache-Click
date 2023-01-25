package net.sf.clickclick.examples.page.layout;

import net.sf.clickclick.control.html.Div;
import net.sf.clickclick.control.html.HtmlLabel;
import net.sf.clickclick.control.panel.HorizontalPanel;
import net.sf.clickclick.examples.page.BorderPage;
import org.apache.click.control.Checkbox;
import org.apache.click.control.Field;
import org.apache.click.control.TextField;
import org.apache.click.extras.control.HtmlForm;
import org.apache.click.extras.control.IntegerField;

import java.io.Serial;

public class HorizontalPanelDemo extends BorderPage {
  @Serial private static final long serialVersionUID = 5470033727110522618L;

  private final HtmlForm form = new HtmlForm("form");

  @Override public void onInit() {
    createLayoutDemo();
    createLayoutWithForm();
  }

  /**
   * Demo 1
   */
  private void createLayoutDemo() {
    HorizontalPanel horizontalPanel = new HorizontalPanel("demo1");

    Div div = new Div();
    // Use normal CSS properties to style the divs
    div.setStyle("background", "red");
    div.setStyle("width", "100px");
    div.setStyle("height", "100px");
    horizontalPanel.add(div);

    div = new Div();
    div.setStyle("background", "yellow");
    div.setStyle("width", "100px");
    div.setStyle("height", "100px");
    horizontalPanel.add(div);

    div = new Div();
    div.setStyle("background", "blue");
    div.setStyle("width", "100px");
    div.setStyle("height", "100px");

    horizontalPanel.add(div);

    addControl(horizontalPanel);
  }

  /**
   * Demo 2
   */
  private void createLayoutWithForm() {
    HorizontalPanel horizontalPanel = new HorizontalPanel();
    form.add(horizontalPanel);

    addField(new TextField("name"), horizontalPanel);
    addField(new IntegerField("age"), horizontalPanel);
    addField(new Checkbox("married"), horizontalPanel);

    addControl(form);
  }

  private void addField(Field field, HorizontalPanel horizontalPanel) {
    HtmlLabel label = new HtmlLabel(field);
    horizontalPanel.add(label);
    horizontalPanel.add(field);
  }
}