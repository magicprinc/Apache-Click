package net.sf.click.examples.page.graph;

import lombok.Getter;
import net.sf.click.examples.page.BorderPage;
import net.sf.click.extras.graph.JSChart;
import net.sf.click.extras.graph.JSLineChart;



/**
 * Example usage of the JSLineChart.
 */
public class LineChart extends BorderPage {
  private static final long serialVersionUID = 2062233782220924212L;

  @Getter(onMethod_={@Override}) public final JSChart chart = new JSLineChart("chart", "Line Graph");
}