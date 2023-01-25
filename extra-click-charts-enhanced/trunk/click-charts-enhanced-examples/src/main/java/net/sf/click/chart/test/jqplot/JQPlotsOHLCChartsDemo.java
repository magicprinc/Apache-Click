package net.sf.click.chart.test.jqplot;

import net.sf.click.chart.*;
import net.sf.click.chart.jqplot.Axis;
import net.sf.click.chart.jqplot.JQPlotChart;
import net.sf.click.chart.jqplot.JQPlotDataSet;
import net.sf.click.chart.jqplot.Legend.Compass;
import net.sf.click.chart.jqplot.Series;
import net.sf.click.chart.jqplot.plugins.Cursor;
import net.sf.click.chart.jqplot.plugins.Highlighter;
import net.sf.click.chart.jqplot.plugins.Highlighter.ToolTipAxes;
import net.sf.click.chart.jqplot.renderer.AxisTickRenderer;
import net.sf.click.chart.jqplot.renderer.DateAxisRenderer;
import net.sf.click.chart.jqplot.renderer.OHLCRenderer;
import net.sf.click.chart.test.BorderPage;

/*

 */
public class JQPlotsOHLCChartsDemo extends BorderPage 
{
	private static final long serialVersionUID = 1L;

	public final String title = "JQPlots OHLC Charts Demo Page";
	
	public JQPlotChart lineChart = new JQPlotChart( "lineChart" );
	public JQPlotChart lineChart2 = new JQPlotChart( "lineChart2" );

	public JQPlotsOHLCChartsDemo()
	{
		lineChart.setDemoMode( true );
		lineChart2.setDemoMode( true );

		
		buildChart1();
		buildChart2();

	}
	
	private void buildChart1()
	{
		Series series = new Series(); 
		series.setRenderer( new OHLCRenderer() );
		lineChart.add( series );
		
		lineChart.setAxesDefaults( new Axis() );
		
		
		Axis xaxis = new Axis();
		xaxis.setRenderer( new DateAxisRenderer() );
		((AxisTickRenderer)xaxis.getTickRenderer()).setFormatString( "%y.%m");
		lineChart.getAxes().setXaxis(xaxis);
		Axis yaxis = new Axis();
		((AxisTickRenderer)yaxis.getTickRenderer()).setFormatString( "%.2f");

		lineChart.getAxes().setXaxis(xaxis);
		lineChart.getAxes().setYaxis(yaxis);
		
		
		Highlighter highlighter = new Highlighter();
		highlighter.setShowMarker( false );
		highlighter.setYvalues( 4 );
		lineChart.addPlugin( highlighter );
		JQPlotDataSet line1 = new JQPlotDataSet();
		line1.add( new DataOHLC("07/06/09", 138.7, 139.68, 135.18, 135.4 ) );
		line1.add( new DataOHLC("06/29/09", 143.46, 144.66, 139.79, 140.02 ) );
		line1.add( new DataOHLC("06/22/09", 140.67, 143.56, 132.88, 142.44 ) );
		lineChart.add( line1 );
	}//met
	
	private void buildChart2()
	{
		Series series = new Series(); 
		OHLCRenderer r = new OHLCRenderer();
		r.setCandleStick( true );
		series.setRenderer( r );
		lineChart2.add( series );
		
		Axis def = new Axis();
		def.setLabel("");
		lineChart2.setAxesDefaults( def );
		
		lineChart2.getTitle().setText( "Chart" );
		
		
		Axis xaxis = new Axis();
		xaxis.setRenderer( new DateAxisRenderer() );
		((AxisTickRenderer)xaxis.getTickRenderer()).setFormatString( "%y.%m");
		lineChart.getAxes().setXaxis(xaxis);
		Axis yaxis = new Axis();
		((AxisTickRenderer)yaxis.getTickRenderer()).setFormatString( "%.2f");

		lineChart2.getAxes().setXaxis(xaxis);
		lineChart2.getAxes().setYaxis(yaxis);
		
		Highlighter highlighter = new Highlighter();
		highlighter.setShowMarker( true );
		highlighter.setYvalues( 4 );
		highlighter.setTooltipAxes( ToolTipAxes.XY );
		highlighter.setFormatString( "<table class=\"jqplot-highlighter\">"+
				"<tr><td>open:</td><td>%s</td></tr>"+
				"<tr><td>hi:</td><td>%s</td></tr>"+
				"<tr><td>low:</td><td>%s</td></tr>"+
				"<tr><td>close:</td><td>%s</td></tr>"+
				"</table>" );
		lineChart2.addPlugin( highlighter );
		
		Cursor cursor = new Cursor();
		cursor.setZoom( true );
		cursor.setTooltipOffset( 10 );
		cursor.setTooltipLocation( Compass.NORTH_WEST );
		lineChart2.addPlugin( cursor );
		
		JQPlotDataSet line1 = new JQPlotDataSet();
		line1.add( new DataOHLC("07/06/09", 138.7, 139.68, 135.18, 135.4 ) );
		line1.add( new DataOHLC("06/29/09", 143.46, 144.66, 139.79, 140.02 ) );
		line1.add( new DataOHLC("06/22/09", 140.67, 143.56, 132.88, 142.44 ) );
		lineChart2.add( line1 );
	}//met
	
	
}//class
