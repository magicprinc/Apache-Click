package net.sf.click.chart.test.jqplot;

import net.sf.click.chart.jqplot.Axis;
import net.sf.click.chart.jqplot.JQPlotChart;
import net.sf.click.chart.jqplot.JQPlotDataSet;
import net.sf.click.chart.jqplot.Series;
import net.sf.click.chart.jqplot.renderer.CanvasAxisLabelRenderer;
import net.sf.click.chart.test.BorderPage;

/*

 */
public class JQPlotsAxisLabelDemo extends BorderPage 
{
	private static final long serialVersionUID = 1L;

	public final String title = "JQPlots Axis Label Demo Page";
	
	public JQPlotChart lineChart = new JQPlotChart( "lineChart" );
	public JQPlotChart lineChart2 = new JQPlotChart( "lineChart2" );
	public JQPlotChart lineChart3 = new JQPlotChart( "lineChart3" );
	
	public JQPlotsAxisLabelDemo()
	{
		lineChart.setDemoMode( true );
		lineChart2.setDemoMode( true );
		lineChart3.setDemoMode( true );
		buildChart1();
		buildChart2();
		buildChart3();
	}
	
	private void buildChart1()
	{
		Axis xaxis = new Axis();
		xaxis.setLabel( "Angle (radians)" );
		xaxis.setAutoscale( true );
		lineChart.getAxes().setXaxis(xaxis);
		
		Axis yaxis = new Axis();
		yaxis.setLabel( "Cosine" );
		yaxis.setAutoscale( true );
		lineChart.getAxes().setYaxis(yaxis);

		Series series1 = new Series();
		series1.setShowMarker( false );
		lineChart.add( series1 );
		
		JQPlotDataSet line1 = new JQPlotDataSet();
		for( double i=0; i< 2* Math.PI; i+=0.1 )
			line1.add( i, Math.cos( i ) );
		lineChart.add( line1 );
	}//met
	
	private void buildChart2()
	{
		Axis xaxis = new Axis();
		xaxis.setLabel( "Angle (radians)" );
		xaxis.setAutoscale( true );
		xaxis.setLabelRenderer( new CanvasAxisLabelRenderer() );
		lineChart2.getAxes().setXaxis(xaxis);
		
		Axis yaxis = new Axis();
		yaxis.setLabel( "Cosine" );
		yaxis.setAutoscale( true );
		yaxis.setLabelRenderer( new CanvasAxisLabelRenderer() );
		lineChart2.getAxes().setYaxis(yaxis);

		Series series1 = new Series();
		series1.setShowMarker( false );
		lineChart2.add( series1 );
		
		JQPlotDataSet line1 = new JQPlotDataSet();
		for( double i=0; i< 2* Math.PI; i+=0.1 )
			line1.add( i, Math.cos( i ) );
		lineChart2.add( line1 );
	}//met

	private void buildChart3()
	{
		Axis xaxis = new Axis();
		xaxis.setLabel( "Angle (radians)" );
		xaxis.setAutoscale( true );
		CanvasAxisLabelRenderer calr = new CanvasAxisLabelRenderer();
		xaxis.setLabelRenderer( calr );
		calr.setEnableFontSupport( true );
		calr.setFontFamily( "Georgia");
		calr.setFontSize( "12pt" );
		lineChart3.getAxes().setXaxis(xaxis);
		
		Axis yaxis = new Axis();
		yaxis.setLabel( "Cosine" );
		yaxis.setAutoscale( true );
		calr = new CanvasAxisLabelRenderer();
		yaxis.setLabelRenderer( calr );
		calr.setEnableFontSupport( true );
		calr.setFontFamily( "Georgia");
		calr.setFontSize( "12pt" );
		lineChart3.getAxes().setYaxis(yaxis);

		Series series1 = new Series();
		series1.setShowMarker( false );
		lineChart3.add( series1 );
		
		JQPlotDataSet line1 = new JQPlotDataSet();
		for( double i=0; i< 2* Math.PI; i+=0.1 )
			line1.add( i, Math.cos( i ) );
		lineChart3.add( line1 );
	}//met
	
	

}//class