package net.sf.click.chart.test.jqplot;

import net.sf.click.chart.jqplot.Axis;
import net.sf.click.chart.jqplot.JQPlotChart;
import net.sf.click.chart.jqplot.JQPlotDataSet;
import net.sf.click.chart.jqplot.Legend.Compass;
import net.sf.click.chart.jqplot.Series;
import net.sf.click.chart.jqplot.renderer.AxisTickRenderer;
import net.sf.click.chart.jqplot.renderer.BarRenderer;
import net.sf.click.chart.jqplot.renderer.BarRenderer.BarDirection;
import net.sf.click.chart.jqplot.renderer.CategoryAxisRenderer;
import net.sf.click.chart.test.BorderPage;

/*

 */
public class JQPlotsStackedDemo extends BorderPage
{
	private static final long serialVersionUID = 1L;

	public final String title = "JQPlots Stacked Bar and Line Charts Demo Page";

	public JQPlotChart lineChart = new JQPlotChart( "lineChart" );
	public JQPlotChart lineChart2 = new JQPlotChart( "lineChart2" );
	public JQPlotChart lineChart3 = new JQPlotChart( "lineChart3" );

	public JQPlotsStackedDemo()
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
		lineChart.setTitle( "Unit Revenues: Acme Traps Division" );
		lineChart.setStackSeries( true );

		lineChart.getLegend().setShow( true );
		lineChart.getLegend().setLocation( Compass.NORTH_WEST);

		Series series = new Series();
		lineChart.setSeriesDefaults( series );
		BarRenderer br = new BarRenderer();
		series.setRenderer( br );
		br.setBarWidth( 50 );

		Series series1 = new Series();
		series1.setLabel( "1st Qtr" );
		lineChart.add( series1 );

		Series series2 = new Series();
		series2.setLabel( "2nd Qtr" );
		lineChart.add( series2 );

		Axis xaxis = new Axis();
		xaxis.setRenderer( new CategoryAxisRenderer() );
		xaxis.setTicks( new String[] {"Red","Blue","Green","Yellow"} );
		lineChart.getAxes().setXaxis(xaxis);

		Axis yaxis = new Axis();
		yaxis.setMin( 0d );
		yaxis.setMax( 20d );
		yaxis.setNumberTicks( 5 );
		AxisTickRenderer atr = new AxisTickRenderer();
		yaxis.setTickRenderer( atr );
		atr.setFormatString( "$%.2f" );
		lineChart.getAxes().setYaxis(yaxis);

		JQPlotDataSet line1 = new JQPlotDataSet();
		line1.add( 1,4 );
		line1.add( 2,2 );
		line1.add( 3,9 );
		line1.add( 4,16 );
		JQPlotDataSet line2 = new JQPlotDataSet();
		line2.add( 1,3 );
		line2.add( 2,7.5 );
		line2.add( 3,6.25 );
		line2.add( 4,3.125 );
		lineChart.add( line1 );
		lineChart.add( line2 );
	}//met

	private void buildChart2()
	{
		lineChart2.setTitle( "Unit Sales: Acme Decoy Division" );
		lineChart2.setStackSeries( true );

		lineChart2.getLegend().setShow( true );
		lineChart2.getLegend().setLocation( Compass.SOUTH_EAST);

		Series series = new Series();
		lineChart2.setSeriesDefaults( series );
		BarRenderer br = new BarRenderer( series );
		series.setShadowAngle( 135 );
		br.setBarWidth( 40 );
		br.setBarDirection( BarDirection.HORIZONTAL );

		Series series1 = new Series();
		series1.setLabel( "Noisy" );
		lineChart2.add( series1 );

		Series series2 = new Series();
		series2.setLabel( "Quiet" );
		lineChart2.add( series2 );

		Axis yaxis = new Axis();
		yaxis.setRenderer( new CategoryAxisRenderer() );
		yaxis.setTicks( new String[] {"Q1","Q2","Q3","Q4"} );
		lineChart2.getAxes().setYaxis(yaxis);

		Axis xaxis = new Axis();
		xaxis.setMin( 0d );
		xaxis.setMax( 20d );
		xaxis.setNumberTicks( 5 );
		lineChart2.getAxes().setXaxis(xaxis);

		JQPlotDataSet line1 = new JQPlotDataSet();
		line1.add( 4,1 );
		line1.add( 2,2 );
		line1.add( 9,3 );
		line1.add( 16,4 );
		JQPlotDataSet line2 = new JQPlotDataSet();
		line2.add( 3,1 );
		line2.add( 7,2 );
		line2.add( 6.25,3 );
		line2.add( 3.125,4 );
		lineChart2.add( line1 );
		lineChart2.add( line2 );
	}//met


	private void buildChart3()
	{
		lineChart3.setTitle( " Acme Company Unit Sales" );
		lineChart3.setStackSeries( true );

		lineChart3.getLegend().setShow( true );
		lineChart3.getLegend().setLocation( Compass.NORTH_WEST);

		Series series = new Series();
		lineChart3.setSeriesDefaults( series );
		series.setFill( true );
		series.setShowMarker( false );

		Series series1 = new Series();
		series1.setLabel( "Traps Division" );
		lineChart3.add( series1 );

		Series series2 = new Series();
		series2.setLabel( "Decoy Division" );
		lineChart3.add( series2 );

		Axis xaxis = new Axis();
		AxisTickRenderer atr = new AxisTickRenderer( xaxis );
		xaxis.setTicks( new String[] {"2006","2007","2008","2009"} );
		atr.setFormatString( "%d" );
		lineChart3.getAxes().setXaxis(xaxis);

		Axis yaxis = new Axis();
		yaxis.setMin( 0d );
		yaxis.setMax( 20d );
		yaxis.setNumberTicks( 5 );
		lineChart3.getAxes().setYaxis(yaxis);

		JQPlotDataSet line1 = new JQPlotDataSet();
		line1.add( 2006,4 );
		line1.add( 2007,2 );
		line1.add( 2008,9 );
		line1.add( 2009,16 );
		JQPlotDataSet line2 = new JQPlotDataSet();
		line2.add( 2006,3 );
		line2.add( 2007,7.5 );
		line2.add( 2008,6.25 );
		line2.add( 2009,3.125 );
		lineChart3.add( line1 );
		lineChart3.add( line2 );
	}//met
}//class