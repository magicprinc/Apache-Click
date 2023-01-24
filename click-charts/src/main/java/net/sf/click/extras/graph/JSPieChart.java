package net.sf.click.extras.graph;

import lombok.val;
import org.apache.click.Context;
import org.apache.click.element.JsImport;
import org.apache.click.util.ClickUtils;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides a Pie Chart control based on JavaScript only.
 * <p/>
 * <table class='htmlHeader' cellspacing='10'>
 * <tr>
 * <td>
 * <img align='middle' hspace='2' src='pie-chart.png' title='Line Chart'/>
 * </td>
 * </tr>
 * </table>
 *
 * This control uses the <a href="http://www.walterzorn.com/jsgraphics">JSGraphics</a> library.
 */
public class JSPieChart extends JSChart {
  @Serial private static final long serialVersionUID = 7068788243174236654L;

  /**
   * Create a PieChart Control with no name defined.
   * <p/>
   * <b>Please note</b> the control's name must be defined before it is valid.
   */
  public JSPieChart () {}//new

  /**
   * Create a pie chart with the given name.
   *
   * @param name the button name
   */
  public JSPieChart(String name) {
    super.setName(name);
  }

  /**
   * Create a pie chart with the given name and label.
   *
   * @param name the name of the chart control
   * @param label the label of the chart that will be displayed
   */
  public JSPieChart(String name, String label) {
    super.setName(name);
    setLabel(label);
  }


  /**
   * Return the HTML HEAD elements.
   *
   * @see JSChart#getChartHeadElements()
   *
   * @return the HTML HEAD elements
   */
  @Override protected List<JsImport> getChartHeadElements() {
    val chartHeadElements = new ArrayList<JsImport>(3);
    Context context = Context.getThreadLocalContext();
    String versionIndicator = ClickUtils.getResourceVersionIndicator(context);

    chartHeadElements.add(new JsImport("/click/control.js", versionIndicator));
    chartHeadElements.add(new JsImport("/click/graph/jsgraph/wz_jsgraphics.js", versionIndicator));
    chartHeadElements.add(new JsImport("/click/graph/jsgraph/pie.js", versionIndicator));

    return chartHeadElements;
  }

  /**
   * Return the JavaScript Chart type.
   *
   * @see JSChart#getJSChartType()
   *
   * @return the JavaScript Chart type
   */
  @Override protected String getJSChartType (){ return "pie";}

}