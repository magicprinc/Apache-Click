package net.sf.click.chart.test.jqplot;

import net.sf.click.chart.Data;
import net.sf.click.chart.jqplot.Axis;
import net.sf.click.chart.jqplot.JQPlotChart;
import net.sf.click.chart.jqplot.JQPlotDataSet;
import net.sf.click.chart.jqplot.Series;
import net.sf.click.chart.jqplot.renderer.AxisTickRenderer;
import net.sf.click.chart.jqplot.renderer.DateAxisRenderer;
import net.sf.click.chart.jqplot.renderer.MarkerRenderer;
import net.sf.click.chart.jqplot.renderer.MarkerRenderer.MarkerStyle;
import net.sf.click.chart.test.BorderPage;

/*

 */
public class JQPlotsDateAxisDemo extends BorderPage 
{
	private static final long serialVersionUID = 1L;

	public final String title = "JQPlots Date Axis Demo Page";
	
	public JQPlotChart lineChart = new JQPlotChart( "lineChart" );
	public JQPlotChart lineChart2 = new JQPlotChart( "lineChart2" );
	
	public JQPlotsDateAxisDemo()
	{
		lineChart.setDemoMode( true );
		lineChart2.setDemoMode( true );
		buildChart1();
		buildChart2();
	}
	
	private void buildChart1()
	{
		lineChart.setTitle( "Default Date Axis" );
		Axis xaxis = new Axis();
		xaxis.setRenderer( new DateAxisRenderer() );
		lineChart.getAxes().setXaxis(xaxis);
		
		Series series1 = new Series();
		series1.setLineWidth( 4 );
		MarkerRenderer mr = new MarkerRenderer();
		mr.setStyle(MarkerStyle.SQUARE );
		series1.setMarkerRenderer( mr );
		lineChart.add( series1 );
		
		JQPlotDataSet line1 = new JQPlotDataSet();
		line1.add( new Data( "2008-09-30",4 ) );
		line1.add( new Data( "2008-10-30",6.5 ) );
		line1.add( new Data( "2008-11-30", 5.7 ) );
		line1.add( new Data( "2008-12-30", 9 ) );
		line1.add( new Data( "2009-01-30", 8.2 ) );
		lineChart.add( line1 );
	}//met
	
	private void buildChart2()
	{
		lineChart2.setTitle( "Customized Date Axis" );
		Axis xaxis = new Axis();
		DateAxisRenderer r = new DateAxisRenderer( xaxis );
		
		//TODO check this out is tick renderer a property of series or r
		AxisTickRenderer atr = new AxisTickRenderer();
		r.setTickRenderer( atr );
		atr.setFormatString( "%b %#d, %y" );
		//TODO should support date
		//xaxis.setMin( 0d );
		//TODO should support date
		//xaxis.setTickInterval( 1d );
		
		lineChart2.getAxes().setXaxis(xaxis);
		
		Series series1 = new Series();
		series1.setLineWidth( 4 );
		MarkerRenderer mr = new MarkerRenderer();
		mr.setStyle(MarkerStyle.SQUARE );
		series1.setMarkerRenderer( mr );
		lineChart2.add( series1 );
		
		JQPlotDataSet line1 = new JQPlotDataSet();
		line1.add( new Data( "2008-09-30",4 ) );
		line1.add( new Data( "2008-10-30",6.5 ) );
		line1.add( new Data( "2008-11-30", 5.7 ) );
		line1.add( new Data( "2008-12-30", 9 ) );
		line1.add( new Data( "2009-01-30", 8.2 ) );
		lineChart2.add( line1 );
	}//met
	
	

}//class