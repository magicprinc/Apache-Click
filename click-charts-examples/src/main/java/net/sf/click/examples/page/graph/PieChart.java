package net.sf.click.examples.page.graph;

import lombok.Getter;
import net.sf.click.examples.page.BorderPage;
import net.sf.click.extras.graph.JSChart;
import net.sf.click.extras.graph.JSPieChart;



/**
 * Example usage of the JSPieChart.
 */
public class PieChart extends BorderPage {
  private static final long serialVersionUID = 5535695657993103994L;

  @Getter(onMethod_={@Override}) public final JSChart chart = new JSPieChart("chart", "Pie Graph");

  @Override protected void fillChartWithDataSeries () {
    chart.addPoint("Jan", 10);
    chart.addPoint("Feb", 350);
    chart.addPoint("Mar", 150);
    chart.addPoint("Apr", 120);
    chart.addPoint("May", 315);
    chart.addPoint("Jun", 415);
    chart.addPoint("Jul", 315);
  }
}