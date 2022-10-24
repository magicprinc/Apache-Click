package net.sf.click.chart.test.jqplot;

import net.sf.click.chart.jqplot.JQPlotChart;
import net.sf.click.chart.jqplot.JQPlotDataSet;
import net.sf.click.chart.jqplot.plugins.Dragable;
import net.sf.click.chart.jqplot.plugins.TrendLine;
import net.sf.click.chart.test.BorderPage;

/*

 */
public class JQPlotsDragDropTrendLineDemo extends BorderPage 
{
	private static final long serialVersionUID = 1L;

	public final String title = "JQPlots Dragable and Trend Line Demo Page";
	
	public JQPlotChart lineChart = new JQPlotChart( "lineChart" );
	
	public JQPlotsDragDropTrendLineDemo()
	{
		lineChart.setDemoMode( true );
		buildChart1();
	}
	
	private void buildChart1()
	{
		lineChart.getTitle().setText( "Dragable and Trend Line Example" );
		
		JQPlotDataSet line1 = new JQPlotDataSet();
		line1.add( 4, 25, 13, 22, 14, 17, 15 );
		lineChart.add( line1 );
		
		lineChart.addPlugin( new Dragable() );
		lineChart.addPlugin( new TrendLine() );
	}//met
	
}//class
