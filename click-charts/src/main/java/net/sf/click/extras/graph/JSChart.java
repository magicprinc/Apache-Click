package net.sf.click.extras.graph;

import com.google.errorprone.annotations.Keep;
import lombok.Getter;
import lombok.Setter;
import org.apache.click.ActionListener;
import org.apache.click.control.AbstractControl;
import org.apache.click.element.Element;
import org.apache.click.element.JsImport;
import org.apache.click.element.JsScript;
import org.apache.click.util.HtmlStringBuffer;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides a abstract JavaScript Chart control.
 */
@SuppressWarnings("serial") @Keep
public abstract class JSChart extends AbstractControl {

  /**
   * Height of the DIV element that encloses this chart, default height 350 px.
   * Return the height of the chart (the enclosing DIV element).
   * Set the height of the chart (of the enclosing DIV element), as a pixel value.
   */
  @Getter @Setter protected int chartHeight = 350;

  /**
   * Width of the DIV element that encloses this chart, default width 380 px.
   * Return the width of the chart (the enclosing DIV element).
   * Set the width of the chart (of the enclosing DIV element), as a pixel value.
   */
  @Getter @Setter protected int chartWidth = 380;

  /** The chart display caption == label of the chart.*/
  @Getter @Setter protected String label = "Chart";

  /** The list of X-Axis labels. */
  protected final List<String> xLabels = new ArrayList<>();

  /** The list of Y-Axis values. */
  protected final List<Integer> yValues = new ArrayList<>();


  /**
   * Adds a "point" to the grapic/chart at the end of the list.
   *
   * @param pointLabel the displayed label of the "point"
   * @param pointValue the value of the "point".
   */
  public void addPoint (String pointLabel, Integer pointValue) {
    xLabels.add(pointLabel);
    yValues.add(pointValue);
  }

  /**
   * Adds a "point" to the grapic/chart at a specified position in the list.
   *
   * @param index index at which the specified point is to be inserted
   * @param pointLabel the displayed label of the "point"
   * @param pointValue the value of the "point".
   */
  public void addPoint (int index, String pointLabel, Integer pointValue) {
    xLabels.add(index, pointLabel);
    yValues.add(index, pointValue);
  }

  /**
   * Return the HTML HEAD elements for the javascript files used by this
   * control.
   *
   * @see org.apache.click.Control#getHeadElements()
   *
   * @return the HTML HEAD elements for the javascript files used by this control.
   */
  @Override public final List<Element> getHeadElements (){
    if (headElements == null) {
      headElements = super.getHeadElements();
      headElements.addAll(getChartHeadElements());
    }

    JsScript script = new JsScript();
    script.setId(getId() + "_js_setup");

    if (!headElements.contains(script)) {
      script.setExecuteOnDomReady(true);

      HtmlStringBuffer buffer = new HtmlStringBuffer(512);
      String var = "g_" + getId();
      buffer.append("var ");
      buffer.append(var);
      buffer.append(" = new ");
      buffer.append(getJSChartType());
      buffer.append("(); ");

      for (int i = 0; i < xLabels.size(); i++) {
        String pointLabel = xLabels.get(i);
        Integer pointValue = yValues.get(i);
        buffer.append(var);
        buffer.append(".add('");
        buffer.append(pointLabel);
        buffer.append("',");
        buffer.append(pointValue);
        buffer.append("); ");
      }

      buffer.append(var);
      buffer.append(".render('");
      buffer.append(getId());
      buffer.append("','");
      buffer.append(getLabel());
      buffer.append("');\n");

      script.setContent(buffer.toString());
      headElements.add(script);
    }

    return headElements;
  }


  @Override public void setActionListener (ActionListener actionListener){}


  /**
   * Render the HTML representation of the chart.
   *
   * @see #toString()
   *
   * @param buffer the specified buffer to render the control's output to
   */
  @Override public void render (HtmlStringBuffer buffer){
    buffer.elementStart("div");
    buffer.appendAttribute("id", getId());
    buffer.appendAttribute("style", "overflow:auto; position:relative; height:" + getChartHeight() + "px; width:" + getChartWidth() + "px;");
    buffer.elementEnd();
  }

  /**
   * Return the HTML rendered chart.
   *
   * @return the HTML rendered chart string
   */
  @Override public String toString() {
    HtmlStringBuffer buffer = new HtmlStringBuffer(getControlSizeEst());
    render(buffer);
    return buffer.toString();
  }



  /**
   * Returns true, as javascript charts perform no server side logic.
   *
   * @return true
   */
  @Override public boolean onProcess (){ return true;}



  /**
   * Return the HTML HEAD elements.
   *
   * @return the HTML HEAD elements
   */
  protected abstract List<JsImport> getChartHeadElements();

  /**
   * Return the JavaScript Chart type.
   *
   * @return the JavaScript Chart type
   */
  protected abstract String getJSChartType();

}