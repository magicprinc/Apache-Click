package net.sf.click.chart.test.flot;

import net.sf.click.chart.Color;
import net.sf.click.chart.Data;
import net.sf.click.chart.flot.FlotChart;
import net.sf.click.chart.flot.FlotDataSeries;
import net.sf.click.chart.test.BorderPage;

import java.util.HashMap;
import java.util.Map;

/**
 * This example illustrates the flot example at
 * http://people.iola.dk/olau/flot/examples/basic.html
 * and
 * http://people.iola.dk/olau/flot/examples/graph-types.html
 */
public class FlotBasicChartsDemo extends BorderPage
{
	private static final long serialVersionUID = 1L;

	public final String title = "Flot Basic Charts Demo Page";

	public FlotChart lineChart = new FlotChart( "lineChart" );
	public FlotChart lineChart2 = new FlotChart( "lineChart2" );
	public FlotChart lineChart3 = new FlotChart( "lineChart3" );

	public FlotBasicChartsDemo()
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
		lineChart.setWidth( 600 );
		lineChart.setHeight( 300 );

		FlotDataSeries line1 = new FlotDataSeries();
		for (double i = 0; i < 14; i += 0.5)
			line1.add(i, Math.sin(i) );

		FlotDataSeries line2 = new FlotDataSeries();
		line2.add( 0, 3 );
		line2.add( 4, 8 );
		line2.add( 8, 5 );
		line2.add( 9, 13 );

		FlotDataSeries line3 = new FlotDataSeries();
		line3.add( 0, 12 );
		line3.add( 4, 12 );
		line3.add( (Data)null );
		line3.add( 7, 12.5 );
		line3.add( 12, 12.5 );

		lineChart.add( line1 );
		lineChart.add( line2 );
		lineChart.add( line3 );
	}//met


	private void buildChart2()
	{
		lineChart2.setWidth( 600 );
		lineChart2.setHeight( 300 );

		FlotDataSeries line1 = new FlotDataSeries();
		for (double i = 0; i < 14; i += 0.5)
			line1.add(i, Math.sin(i) );
		line1.getLines().setShow(true);
		line1.getLines().setFill(true);;

		FlotDataSeries line2 = new FlotDataSeries();
		line2.add( 0, 3 );
		line2.add( 4, 8 );
		line2.add( 8, 5 );
		line2.add( 9, 13 );
		line2.getBars().setShow( true );

		FlotDataSeries line3 = new FlotDataSeries();
		for (double i = 0; i < 14; i += 0.5)
			line3.add(i, Math.cos(i) );
		line3.getPoints().setShow( true );

		FlotDataSeries line4 = new FlotDataSeries();
		for (double i = 0; i < 14; i += 0.1)
			line4.add(i, Math.sqrt(i*10) );
		line4.getLines().setShow(true);

		FlotDataSeries line5 = new FlotDataSeries();
		for (double i = 0; i < 14; i += 0.5)
			line5.add(i, Math.sqrt(i) );
		line5.getLines().setShow(true);
		line5.getPoints().setShow( true );

		FlotDataSeries line6 = new FlotDataSeries();
		for (double i = 0; i < 14; i += 0.5)
			line6.add(i, Math.sqrt(2*i + Math.sin(i)+5) );
		line6.getLines().setShow(true);
		line6.getLines().setSteps(true);

		lineChart2.add( line1 );
		lineChart2.add( line2 );
		lineChart2.add( line3 );
		lineChart2.add( line4 );
		lineChart2.add( line5 );
		lineChart2.add( line6 );
	}//met

	private void buildChart3()
	{
		lineChart3.setWidth( 600 );
		lineChart3.setHeight( 300 );

		lineChart3.getSeries().getLines().setShow(true);
		lineChart3.getSeries().getPoints().setShow(true);

		lineChart3.getYaxis().setMin( -2d );
		lineChart3.getYaxis().setMax( 2d );
		lineChart3.getYaxis().setTicks( 10 );

		Map<Double, String> mapTicks = new HashMap<Double, String>();
		mapTicks.put( 0d, "0");
		mapTicks.put( Math.PI/2, "\u03c0/2");
		mapTicks.put( Math.PI, "\u03c0");
		mapTicks.put( 3*Math.PI/2, "3\u03c0/2");
		mapTicks.put( 2*Math.PI, "2\u03c0");
		lineChart3.getXaxis().setTicks( mapTicks );

		lineChart3.getGrid().setBackgroundColor( new Color[] { new Color( 0xffffff ), new Color( 0xeeeeee ) } );

		FlotDataSeries line1 = new FlotDataSeries();
		for (double i = 0; i < 2*Math.PI; i += 0.25)
			line1.add(i, Math.sin(i) );
		line1.setLabel( "sin(x)" );

		FlotDataSeries line2 = new FlotDataSeries();
		for (double i = 0; i < 2*Math.PI; i += 0.25)
			line2.add( i, Math.cos( i ) );
		line2.setLabel( "cos(x)" );

		FlotDataSeries line3 = new FlotDataSeries();
		for (double i = 0; i < 2*Math.PI; i += 0.1)
			line3.add(i, Math.tan(i) );
		line3.setLabel( "tan(x)" );

		lineChart3.add( line1 );
		lineChart3.add( line2 );
		lineChart3.add( line3 );
	}//met
}//class