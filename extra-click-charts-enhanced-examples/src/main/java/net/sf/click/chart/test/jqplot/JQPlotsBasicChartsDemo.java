package net.sf.click.chart.test.jqplot;

import net.sf.click.chart.Color;
import net.sf.click.chart.jqplot.Axis;
import net.sf.click.chart.jqplot.Grid;
import net.sf.click.chart.jqplot.JQPlotChart;
import net.sf.click.chart.jqplot.JQPlotDataSet;
import net.sf.click.chart.jqplot.Series;
import net.sf.click.chart.jqplot.renderer.AxisTickRenderer;
import net.sf.click.chart.jqplot.renderer.MarkerRenderer;
import net.sf.click.chart.jqplot.renderer.MarkerRenderer.MarkerStyle;
import net.sf.click.chart.test.BorderPage;

import java.util.HashMap;

/*

 */
public class JQPlotsBasicChartsDemo extends BorderPage
{
	private static final long serialVersionUID = 1L;

	public final String title = "JQPlots Basic Charts Demo Page";

	public JQPlotChart lineChart = new JQPlotChart( "lineChart" );
	public JQPlotChart lineChart2 = new JQPlotChart( "lineChart2" );
	public JQPlotChart lineChart3 = new JQPlotChart( "lineChart3" );
	public JQPlotChart lineChart4 = new JQPlotChart( "lineChart4" );

	public JQPlotsBasicChartsDemo()
	{
		lineChart.setDemoMode( true );
		lineChart2.setDemoMode( true );
		lineChart3.setDemoMode( true );
		lineChart4.setDemoMode( true );
		buildChart1();
		buildChart2();
		buildChart3();
		buildChart4();
	}

	private void buildChart1()
	{
		Series series = new Series();
		series.setLineWidth(2);
		((MarkerRenderer)series.getMarkerRenderer()).setStyle( MarkerStyle.DIAMOND );

		Series series2 = new Series();
		series2.setShowLine( false );
		((MarkerRenderer)series2.getMarkerRenderer()).setStyle( MarkerStyle.X );
		((MarkerRenderer)series2.getMarkerRenderer()).setSize( 7 );

		Series series3 = new Series();
		((MarkerRenderer)series3.getMarkerRenderer()).setStyle( MarkerStyle.CIRCLE );

		Series series4 = new Series();
		series4.setShowLine( true );
		series4.setLineWidth(5);
		((MarkerRenderer)series4.getMarkerRenderer()).setStyle( MarkerStyle.FILLED_SQUARE );
		((MarkerRenderer)series4.getMarkerRenderer()).setSize( 14 );

		lineChart.getTitle().setText( "Line Style Options" );

		lineChart.add( series );
		lineChart.add( series2 );
		lineChart.add( series3 );
		lineChart.add( series4 );

		JQPlotDataSet line1 = new JQPlotDataSet();
		for( double i=0; i< 2* Math.PI; i+=0.4 )
			line1.add( i, Math.cos( i ) );

		JQPlotDataSet line2 = new JQPlotDataSet();
		for( double i=0; i< 2* Math.PI; i+=0.4 )
			line2.add( i, 2*Math.sin( i - 0.8 ) );

		JQPlotDataSet line3 = new JQPlotDataSet();
		for( double i=0; i< 2* Math.PI; i+=0.4 )
			line3.add( i, 2.5d + Math.pow( i/4,2 ) );

		JQPlotDataSet line4 = new JQPlotDataSet();
		for( double i=0; i< 2* Math.PI; i+=0.4 )
			line4.add( i, -2.5d - Math.pow( i/4,2 ) );

		lineChart.add( line1 );
		lineChart.add( line2 );
		lineChart.add( line3 );
		lineChart.add( line4 );
	}//met

	private void buildChart2()
	{
		Series series = new Series();
		series.setShowMarker( false );
		series.setLineWidth( 5 );
		series.setShadowAngle( 0 );
		series.setShadowOffset( 1.5d );
		series.setShadowAlpha( 0.08 );
		series.setShadowDepth( 6 );

		lineChart2.getTitle().setText( "Shadow Options" );

		lineChart2.add( series );

		JQPlotDataSet line1 = new JQPlotDataSet();
		for( double i=0; i< 2* Math.PI; i+=0.1 )
			line1.add( i, Math.cos( i ) );
		lineChart2.add( line1 );
	}//met


	private void buildChart3()
	{
		Series series = new Series();
		MarkerRenderer mr = new MarkerRenderer( series );
		series.setLabel( "Rising line" );
		series.setShowLine( false );
		mr.setStyle( MarkerStyle.SQUARE);

		Series series2 = new Series( "Declining line" );

		Series series3 = new Series( "Zig Zag line" );
		series3.setLineWidth( 5 );
		series3.setShowMarker( false );

		lineChart3.getLegend().setShow( true );

		lineChart3.getTitle().setText( "Mixed Data Input Formats" );

		lineChart3.add( series );
		lineChart3.add( series2 );
		lineChart3.add( series3 );

		JQPlotDataSet line1 = new JQPlotDataSet();
		line1.add( 1,1 );
		line1.add( 1.5,2.25 );
		line1.add( 2,4 );
		line1.add( 2.5,6.25 );
		line1.add( 3,9 );
		line1.add( 3.5,12.25 );
		line1.add( 4,16 );
		JQPlotDataSet line2 = new JQPlotDataSet();
		line2.add( 25,17.5,12.25,8.6,6.0,4.2,2.9 );
		JQPlotDataSet line3 = new JQPlotDataSet();
		line3.add( new double[][] {{1,4},{2,25},{3,13},{4,22},{5,14},{6,17},{7,15}} );

		lineChart3.add( line1 );
		lineChart3.add( line2 );
		lineChart3.add( line3 );
	}//met

	private void buildChart4()
	{
		Series series = new Series( "RisingLine" );
		((MarkerRenderer)series.getMarkerRenderer()).setStyle( MarkerStyle.SQUARE);

		Series series2 = new Series( "DecliningLine" );

		lineChart4.getLegend().setShow( true );

		lineChart4.getTitle().setText( "Customized Axes Ticks" );

		Axis xaxis = new Axis();
		HashMap<Integer, String> map = new HashMap<Integer, String>();
		map.put( 0, "zero" );
		map.put( 1, "one" );
		map.put( 2, "two" );
		map.put( 3, "three" );
		map.put( 4, "four" );
		map.put( 5, "five" );
		xaxis.setTicks( map );
		lineChart4.getAxes().setXaxis(xaxis);
		Axis yaxis = new Axis();
		yaxis.setTicks( new String[] {"-5","0","5","10","15","20","25","30"} );
		((AxisTickRenderer)yaxis.getTickRenderer()).setFormatString("%d");
		lineChart4.getAxes().setYaxis(yaxis);
		Grid grid = new Grid();
		grid.setBackground( new Color( 0xf3f3f3 ) );
		grid.setGridLineColor( new Color( 0xaccf9b ) );
		lineChart4.setGrid( grid );
		lineChart4.add( series );
		lineChart4.add( series2 );

		JQPlotDataSet line1 = new JQPlotDataSet();
		line1.add( 1,1 );
		line1.add( 1.5,2.25 );
		line1.add( 2,4 );
		line1.add( 2.5,6.25 );
		line1.add( 3,9 );
		line1.add( 3.5,12.25 );
		line1.add( 4,16 );
		JQPlotDataSet line2 = new JQPlotDataSet();
		line2.add( 25,12.25,6.25,3.125 );
		lineChart4.add( line1 );
		lineChart4.add( line2 );
	}//met
}//class