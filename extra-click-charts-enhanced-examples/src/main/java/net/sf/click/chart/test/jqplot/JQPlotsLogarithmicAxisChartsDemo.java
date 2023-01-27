package net.sf.click.chart.test.jqplot;

import net.sf.click.chart.jqplot.Axes;
import net.sf.click.chart.jqplot.Axis;
import net.sf.click.chart.jqplot.JQPlotChart;
import net.sf.click.chart.jqplot.JQPlotDataSet;
import net.sf.click.chart.jqplot.Legend;
import net.sf.click.chart.jqplot.Legend.Compass;
import net.sf.click.chart.jqplot.Series;
import net.sf.click.chart.jqplot.Series.YAxis;
import net.sf.click.chart.jqplot.renderer.LogAxisRenderer;
import net.sf.click.chart.jqplot.renderer.LogAxisRenderer.TickDistribution;
import net.sf.click.chart.test.BorderPage;

/*

 */
public class JQPlotsLogarithmicAxisChartsDemo extends BorderPage 
{
	private static final long serialVersionUID = 1L;

	public final String title = "JQPlots Logarithmic axes Demo Page";
	
	public JQPlotChart lineChart = new JQPlotChart( "lineChart" );
	public JQPlotChart lineChart2 = new JQPlotChart( "lineChart2" );
	public JQPlotChart lineChart3 = new JQPlotChart( "lineChart3" );
	public JQPlotChart lineChart4 = new JQPlotChart( "lineChart4" );
	
	public JQPlotsLogarithmicAxisChartsDemo()
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
		lineChart.setTitle( "Log Y Axis, Even Tick Distribution" );

		Legend legend = lineChart.getLegend();
		legend.setShow( true );
		legend.setLocation( Compass.NORTH_EAST );
		
		Series s = new Series();
		s.setLabel( "Declining line" );
		lineChart.add( s );
		
		Axes axes = lineChart.getAxes();
		Axis axis = new Axis();
		axis.setMin( 0d );
		axis.setMax( 5d );
		axes.setXaxis( axis );
		axis = new Axis();
		axis.setMin( 1d );
		axis.setMax( 64d );
		axis.setRenderer( new LogAxisRenderer() );
		axes.setYaxis( axis );
		
		JQPlotDataSet line1 = new JQPlotDataSet();
		line1.add(25, 12.5, 6.25, 3.125);
		lineChart.add( line1 );
	}//met
	
	private void buildChart2()
	{
		lineChart2.setTitle( "Log Y Axis, Even Tick Distribution" );

		Legend legend = lineChart2.getLegend();
		legend.setShow( true );
		legend.setLocation( Compass.NORTH_EAST );
		
		Series s = new Series();
		s.setLabel( "Declining line" );
		lineChart2.add( s );
		
		Axes axes = lineChart2.getAxes();
		Axis axis = new Axis();
		axis.setMin( 0d );
		axis.setMax( 5d );
		axes.setXaxis( axis );
		axis = new Axis();
		//TODO this should not be
		axis.add( "tickDistribution", "power" );
		LogAxisRenderer lar = new LogAxisRenderer( axis );
		//TODO this should be, but useless here
		lar.setTickDistribution( TickDistribution.POWER );
		axes.setYaxis( axis );
		
		JQPlotDataSet line1 = new JQPlotDataSet();
		line1.add(25, 12.5, 6.25, 3.125);
		lineChart2.add( line1 );
	}//met
	
	
	private void buildChart3()
	{
		lineChart3.setTitle( "Log Y Axis, Specifying Tick Values" );

		Legend legend = lineChart3.getLegend();
		legend.setShow( true );
		legend.setLocation( Compass.NORTH_EAST );
		
		Series s = new Series();
		s.setLabel( "Declining line" );
		lineChart3.add( s );
		
		Axes axes = lineChart3.getAxes();
		Axis axis = new Axis();
		axis.setMin( 0d );
		axis.setMax( 5d );
		axes.setXaxis( axis );
		axis = new Axis();
		axis.setTicks( new String[] {"1","2","4","8","16","32","64"});
		axis.setRenderer( new LogAxisRenderer() );
		axes.setYaxis( axis );
		
		JQPlotDataSet line1 = new JQPlotDataSet();
		line1.add(25, 12.5, 6.25, 3.125);
		lineChart3.add( line1 );
	}//met
	
	private void buildChart4()
	{
		lineChart4.setTitle( "Secondary Log Axis, Even Tick Distribution, Specify Min/Max" );

		Legend legend = lineChart4.getLegend();
		legend.setShow( true );
		legend.setLocation( Compass.NORTH_EAST );
		
		Series s = new Series();
		s.setLabel( "Rising line" );
		lineChart4.add( s );
		s = new Series();
		s.setLabel( "Declining line" );
		s.setYaxis( YAxis.Y2_AXIS );
		lineChart4.add( s );
		
		Axes axes = lineChart4.getAxes();
		Axis axis = new Axis();
		axis.setMin( 0d );
		axis.setMax( 5d );
		axes.setXaxis( axis );
		axis = new Axis();
		axis.setMin( 2d );
		axis.setMax( 30d );
		axis.setRenderer( new LogAxisRenderer() );
		axes.setY2axis( axis );
		
		JQPlotDataSet line1 = new JQPlotDataSet();
		line1.add( 1, 1 );
		line1.add( 2, 4 );
		line1.add( 3, 9 );
		line1.add( 4, 16 );
		JQPlotDataSet line2 = new JQPlotDataSet();
		line2.add(25, 12.5, 6.25, 3.125);
		lineChart4.add( line1 );
		lineChart4.add( line2 );
	}//met
}//class
