package net.sf.click.chart.test;

import net.sf.click.chart.test.jqplot.JQPlotsAxisAutoScaleDemo;
import net.sf.click.chart.test.jqplot.JQPlotsAxisLabelDemo;
import net.sf.click.chart.test.jqplot.JQPlotsBarChartsDemo;
import net.sf.click.chart.test.jqplot.JQPlotsBasicChartsDemo;
import net.sf.click.chart.test.jqplot.JQPlotsOHLCChartsDemo;
import net.sf.click.chart.test.jqplot.JQPlotsPieChartsDemo;
import org.apache.click.control.PageLink;

/*

 */
public class Index extends BorderPage
{
	private static final long serialVersionUID = 1L;

	public final String title = "Apache click charts enhanced, demo app";

	public PageLink linkPageFlot1 = new PageLink("linkPageFlot1","Flot demo 1",Index.class);
	public PageLink linkPageJQPlot1 = new PageLink("linkPageJQPlot1","JQPlot basic charts",JQPlotsBasicChartsDemo.class);
	public PageLink linkPageJQPlot2 = new PageLink("linkPageJQPlot2","JQPlot bar charts",JQPlotsBarChartsDemo.class);
	public PageLink linkPageJQPlot3 = new PageLink("linkPageJQPlot3","JQPlot axis autoscale",JQPlotsAxisAutoScaleDemo.class);
	public PageLink linkPageJQPlot4 = new PageLink("linkPageJQPlot4","JQPlot axis label",JQPlotsAxisLabelDemo.class);
	public PageLink linkPageJQPlot5 = new PageLink("linkPageJQPlot5","JQPlot pie charts",JQPlotsPieChartsDemo.class);
	public PageLink linkPageJQPlot6 = new PageLink("linkPageJQPlot6","JQPlot OHLC charts",JQPlotsOHLCChartsDemo.class);

}//class