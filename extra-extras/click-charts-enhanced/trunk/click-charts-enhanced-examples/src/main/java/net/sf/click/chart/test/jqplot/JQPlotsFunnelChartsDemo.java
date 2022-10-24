package net.sf.click.chart.test.jqplot;

import net.sf.click.chart.jqplot.JQPlotChart;
import net.sf.click.chart.jqplot.JQPlotDataSet;
import net.sf.click.chart.jqplot.Series;
import net.sf.click.chart.jqplot.Legend.Compass;
import net.sf.click.chart.jqplot.renderer.FunnelRenderer;
import net.sf.click.chart.test.BorderPage;

/*

 */
public class JQPlotsFunnelChartsDemo extends BorderPage 
{
	private static final long serialVersionUID = 1L;

	public final String title = "JQPlots Funnel Charts Demo Page";
	
	public JQPlotChart lineChart = new JQPlotChart( "lineChart" );
	public JQPlotChart lineChart2 = new JQPlotChart( "lineChart2" );
	public JQPlotChart lineChart3 = new JQPlotChart( "lineChart3" );
	
	public JQPlotsFunnelChartsDemo()
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
		lineChart.getTitle().setText( "Basic Funnel Test" );
		
		FunnelRenderer fr = new FunnelRenderer();
		Series series = new Series();
		lineChart.setSeriesDefaults( series );
		series.setRenderer( fr );

		JQPlotDataSet line1 = new JQPlotDataSet();
		line1.add( "Sony", 7 );
		line1.add( "Saumsung", 13 );
		line1.add( "LG", 14 );
		line1.add( "Vizio", 5 );
		lineChart.add( line1 );
	}//met
	
	private void buildChart2()
	{
		lineChart2.getTitle().setText( "Basic Funnel Test" );
		
		Series series = new Series();
		FunnelRenderer fr = new FunnelRenderer( series );
		lineChart2.setSeriesDefaults( series );
		fr.setWidthRatio( 0.5 );
		fr.setSectionMargin( 0 );
		
		lineChart2.getLegend().setShow( true );
		lineChart2.getLegend().setLocation( Compass.EAST );

		JQPlotDataSet line1 = new JQPlotDataSet();
		line1.add( "Sony", 7 );
		line1.add( "Saumsung", 13 );
		line1.add( "LG", 14 );
		line1.add( "Vizio", 5 );
		lineChart2.add( line1 );
	}//met
	
	private void buildChart3()
	{
		lineChart3.getTitle().setText( "Basic Funnel Test" );
		
		Series series = new Series();
		FunnelRenderer fr = new FunnelRenderer();
		lineChart3.setSeriesDefaults( series );
		fr.setWidthRatio( 0.2 );
		fr.setSectionMargin( 0 );
		//TODO
		//check this highlightMouseDown: true

		
		lineChart3.getLegend().setShow( true );
		lineChart3.getLegend().setLocation( Compass.EAST );
		//TODO 
		//check this  placement: 'outside'

		JQPlotDataSet line1 = new JQPlotDataSet();
		line1.add( "Sony", 7 );
		line1.add( "Saumsung", 13 );
		line1.add( "LG", 14 );
		line1.add( "Vizio", 5 );
		lineChart3.add( line1 );
	}//met
}//class
