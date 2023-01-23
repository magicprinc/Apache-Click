package net.sf.click.examples.page.graph;

import net.sf.click.examples.page.BorderPage;
import net.sf.click.extras.graph.JSBarChart;

/**
 * Example usage of the JSBarChart.
 */
public class BarChart extends BorderPage {

    public JSBarChart chart = new JSBarChart("chart", "Bar Graph");

    /**
     * @see org.apache.click.Page#onInit()
     */
    @Override
    public void onInit() {
        super.onInit();

        chart.setChartHeight(300);
        chart.setChartWidth(400);

        chart.addPoint("1", new Integer(145));
        chart.addPoint("2", new Integer(0));
        chart.addPoint("3", new Integer(175));
        chart.addPoint("4", new Integer(130));
        chart.addPoint("5", new Integer(150));
        chart.addPoint("6", new Integer(175));
        chart.addPoint("7", new Integer(205));
        chart.addPoint("8", new Integer(125));
        chart.addPoint("9", new Integer(125));
        chart.addPoint("10", new Integer(135));
        chart.addPoint("11", new Integer(125));
    }

}