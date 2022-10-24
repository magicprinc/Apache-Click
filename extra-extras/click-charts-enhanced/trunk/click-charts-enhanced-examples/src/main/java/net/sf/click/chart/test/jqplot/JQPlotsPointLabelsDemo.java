package net.sf.click.chart.test.jqplot;

import net.sf.click.chart.Data3;
import net.sf.click.chart.jqplot.Axis;
import net.sf.click.chart.jqplot.JQPlotChart;
import net.sf.click.chart.jqplot.JQPlotDataSet;
import net.sf.click.chart.jqplot.Series;
import net.sf.click.chart.jqplot.Legend.Compass;
import net.sf.click.chart.jqplot.plugins.PointLabels;
import net.sf.click.chart.jqplot.renderer.BarRenderer;
import net.sf.click.chart.jqplot.renderer.CategoryAxisRenderer;
import net.sf.click.chart.test.BorderPage;

/*

 */
public class JQPlotsPointLabelsDemo extends BorderPage 
{
	private static final long serialVersionUID = 1L;

	public final String title = "JQPlots Data Point Label Demo Page";
	
	public JQPlotChart lineChart = new JQPlotChart( "lineChart" );
	public JQPlotChart lineChart2 = new JQPlotChart( "lineChart2" );
	public JQPlotChart lineChart3 = new JQPlotChart( "lineChart3" );
	public JQPlotChart lineChart4 = new JQPlotChart( "lineChart4" );
	public JQPlotChart lineChart5 = new JQPlotChart( "lineChart5" );
	
	public JQPlotsPointLabelsDemo()
	{
		lineChart.setDemoMode( true );
		lineChart2.setDemoMode( true );
		lineChart3.setDemoMode( true );
		lineChart4.setDemoMode( true );
		lineChart5.setDemoMode( true );
		buildChart1();
		buildChart2();
		buildChart3();
		buildChart4();
		buildChart5();
	}
	
	private void buildChart1()
	{
		lineChart.getTitle().setText( "Chart with Point Labels" );
		
		Series series = new Series();
		series.setShowMarker( false );
		lineChart.setSeriesDefaults( series );
		PointLabels pl = new PointLabels();
		lineChart.addPlugin( pl );
		
		Axis axis = new Axis();
		axis.setPad( 1.3d );
		lineChart.setAxesDefaults( axis );
		
		
		JQPlotDataSet line1 = new JQPlotDataSet();
		line1.add( 14, 32, 41, 44, 40, 47, 53, 67 );
		lineChart.add( line1 );
	}//met
	
	private void buildChart2()
	{
		lineChart2.getTitle().setText( "Point Labels From Extra Series Data" );
		
		Series series = new Series();
		series.setShowMarker( false );
		lineChart2.setSeriesDefaults( series );
		PointLabels pl = new PointLabels();
		pl.setLocation( Compass.SOUTH );
		pl.setYpadding( 3 );
		//TODO review the plugin mechanism : there seem to be different 
		//kinds of plugin
		//lineChart2.addPlugin( pl );
		series.add( pl );
		
		Axis axis = new Axis();
		axis.setPad( 1.3d );
		lineChart2.getAxes().setYaxis( axis );
		
		
		JQPlotDataSet line1 = new JQPlotDataSet();
		line1.add( new Data3(-12,7,null) );
		line1.add( new Data3(-3,14,null ) );
		line1.add( new Data3(2,-1, "low" ) );
		line1.add( new Data3(7,-1,"low") );
		line1.add( new Data3(11,11,null) );
		line1.add( new Data3(13,-1,"low") );
		lineChart2.add( line1 );
	}//met
	
	
	private void buildChart3()
	{
		lineChart3.getTitle().setText( "Bar Chart with Point Labels" );
		
		Series series = new Series();
		series.setRenderer( new BarRenderer() );
		lineChart3.setSeriesDefaults( series );
		
		Series series1 = new Series();
		
		series1.setLabel( "Declining line" );
		lineChart3.add( series1 );
		PointLabels pl = new PointLabels();
		series1.add( pl );
		pl.setLabels( new String[] {"fourteen", "thirty two", "fourty one", "fourty four", "fourty"} );
		
		Axis xaxis = new Axis();
		xaxis.setRenderer( new CategoryAxisRenderer() );
		lineChart3.getAxes().setXaxis( xaxis );
		Axis yaxis = new Axis();
		yaxis.setPadMax( 1.3d );
		lineChart3.getAxes().setYaxis( yaxis );
		
		JQPlotDataSet line1 = new JQPlotDataSet();
		line1.add( 1,14 );
		line1.add( 2,32 );
		line1.add( 3,41 );
		line1.add( 4,44 );
		line1.add( 5,40 );
		lineChart3.add( line1 );
	}//met
	
	private void buildChart4()
	{
		lineChart4.getTitle().setText( "Stacked Bar Chart with Cumulative Point Labels" );
		
		lineChart4.setStackSeries( true );
		
		Series series = new Series();
		lineChart4.setSeriesDefaults( series );
		BarRenderer br = new BarRenderer();
		series.setRenderer( br );
		br.setBarMargin( 25 );
		PointLabels pl = new PointLabels();
		series.add( pl );
		//TODO tell chris the snapshot no good, labels everywhere
		pl.setStackedValue( true );
		
		Axis xaxis = new Axis();
		xaxis.setRenderer( new CategoryAxisRenderer() );
		lineChart4.getAxes().setXaxis( xaxis );

		Axis yaxis = new Axis();
		yaxis.setTicks( new String[] {"0","20","40","60","80"} );
		lineChart4.getAxes().setYaxis( yaxis );
		
		JQPlotDataSet line1 = new JQPlotDataSet();
		line1.add( 14, 32, 41, 44, 40, 37, 29 );
		lineChart4.add( line1 );
		
		JQPlotDataSet line2 = new JQPlotDataSet();
		line2.add( 7, 12, 15, 17, 20, 27, 39 );
		lineChart4.add( line2 );

	}//met
	
	private void buildChart5() {
		lineChart5.getTitle().setText( "Chart5 with Point Labels" );
		
		Series series = new Series();
		series.setShowMarker( false );
		lineChart5.setSeriesDefaults( series );
		PointLabels pl = new PointLabels();
		pl.setEdgeTolerance( 5 );
		lineChart5.addPlugin( pl );
		
		Axis axis = new Axis();
		axis.setMin( 3d );
		lineChart5.getAxes().setXaxis( axis );
		
		
		JQPlotDataSet line1 = new JQPlotDataSet();
		line1.add( 14, 32, 41, 44, 40, 47, 53, 67 );
		lineChart5.add( line1 );		
	}

}//class
