package net.sf.click.examples.page.graph;

import net.sf.click.examples.page.BorderPage;
import net.sf.click.extras.graph.JSPieChart;

/**
 * Example usage of the JSPieChart.
 */
public class PieChart extends BorderPage {

    public JSPieChart chart = new JSPieChart("chart", "Pie Graph");

    /**
     * @see org.apache.click.Page#onInit()
     */
    @Override
    public void onInit() {
        super.onInit();

        chart.setChartHeight(350);
        chart.setChartWidth(380);

        chart.addPoint("Jan", new Integer(100));
        chart.addPoint("Feb", new Integer(200));
        chart.addPoint("Mar", new Integer(150));
        chart.addPoint("Apr", new Integer(120));
        chart.addPoint("May", new Integer(315));
        chart.addPoint("Jun", new Integer(415));
        chart.addPoint("Jul", new Integer(315));
    }
}
