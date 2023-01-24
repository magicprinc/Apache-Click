package net.sf.click.examples.page.graph;

import lombok.Getter;
import net.sf.click.examples.page.BorderPage;
import net.sf.click.extras.graph.JSBarChart;
import net.sf.click.extras.graph.JSChart;

import java.io.Serial;

/**
 * Example usage of the JSBarChart.
 */
public class BarChart extends BorderPage {
  @Serial private static final long serialVersionUID = 1365390943431181780L;

  @Getter(onMethod_={@Override}) public final JSChart chart = new JSBarChart("chart", "Bar Graph");
}