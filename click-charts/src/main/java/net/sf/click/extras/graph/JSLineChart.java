package net.sf.click.extras.graph;

import lombok.val;
import org.apache.click.Context;
import org.apache.click.element.JsImport;
import org.apache.click.util.ClickUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Provides a Line Chart control based on JavaScript only.
 *
 * <table class='htmlHeader' cellspacing='10'>
 * <tr>
 * <td>
 * <img align='middle' hspace='2' src='line-chart.png' title='Line Chart'/>
 * </td>
 * </tr>
 * </table>
 *
 * This control uses the <a href="http://www.walterzorn.com/jsgraphics">JSGraphics</a> library.
 */
public class JSLineChart extends JSChart {
  private static final long serialVersionUID = -8193246280314625136L;

  /**
   * Create a line chart with no name defined.
   * <p/>
   * <b>Please note</b> the control's name must be defined before it is valid.
   */
  public JSLineChart (){}//new

  /**
   * Create a line chart with the given name.
   *
   * @param name the button name
   */
  public JSLineChart(String name) {
    super.setName(name);
  }

  /**
   * Create a line chart with the given name and label.
   *
   * @param name the name of the chart control
   * @param label the label of the chart that will be displayed
   */
  public JSLineChart(String name, String label) {
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
    chartHeadElements.add(new JsImport("/click/graph/jsgraph/line.js", versionIndicator));

    return chartHeadElements;
  }

  /**
   * Return the JavaScript Chart type.
   *
   * @see JSChart#getJSChartType()
   *
   * @return the JavaScript Chart type
   */
  @Override protected String getJSChartType(){ return "line_graph";}
}