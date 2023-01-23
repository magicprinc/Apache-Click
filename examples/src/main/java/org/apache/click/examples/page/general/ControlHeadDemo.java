package org.apache.click.examples.page.general;

import org.apache.click.control.Field;
import org.apache.click.control.Form;
import org.apache.click.control.Submit;
import org.apache.click.element.CssImport;
import org.apache.click.element.Element;
import org.apache.click.element.JsImport;
import org.apache.click.examples.page.BorderPage;
import org.apache.click.util.HtmlStringBuffer;

import java.util.List;

/**
 * This example demonstrates how to manipulate the Head elements of a
 * custom StarRating Control.
 */
public class ControlHeadDemo extends BorderPage {

  public ControlHeadDemo() {

    Form form = new Form("form");

    StarRating rating = new StarRating("rating", 5, 2);
    form.add(rating);

    form.add(new Submit("save"));

    addControl(form);
  }

  /**
   * A custom StarRating Control based on the JQuery Rating plugin.
   */
  public static class StarRating extends Field {


    private final int maxStars;

    public StarRating(String name, int maxStars, int selectedValue) {
      super(name);
      this.maxStars = maxStars;
      setValue(Integer.toString(selectedValue));
    }

    /**
     * Return the list of HEAD elements.
     *
     * @return list the list of HEAD elements
     */
    @Override
    public List<Element> getHeadElements() {
      if (headElements == null) {
        headElements = super.getHeadElements();

        // Add the JQuery library to the control
        headElements.add(new JsImport("/assets/js/jquery-1.4.2.js"));

        // Add the Rating JavaScript library to the control
        headElements.add(new JsImport("/assets/rating/jquery.rating.js"));

        // Add the Rating Css to the control
        headElements.add(new CssImport("/assets/rating/jquery.rating.css"));
      }
      return headElements;
    }

    /**
     * Render the HTML representation of the StarRating control.
     *
     * @param buffer the buffer to render the output to
     */
    @Override
    public void render(HtmlStringBuffer buffer) {
      // Render a radio button for each star
      for (int i = 1; i <= maxStars; i++) {
        String strValue = Integer.toString(i);
        buffer.elementStart("input");
        buffer.appendAttribute("type", "radio");
        buffer.appendAttribute("name", getName());
        buffer.appendAttribute("value", strValue);
        buffer.appendAttribute("class", "star");
        if (strValue.equals(getValue())) {
          buffer.appendAttribute("checked", "checked");
        }
        buffer.elementEnd();
        buffer.append("\n");
      }
    }
  }
}