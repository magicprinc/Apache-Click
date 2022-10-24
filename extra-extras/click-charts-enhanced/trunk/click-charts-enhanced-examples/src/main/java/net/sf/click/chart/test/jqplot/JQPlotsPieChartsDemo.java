package net.sf.click.chart.test.jqplot;

import net.sf.click.chart.*;
import net.sf.click.chart.jqplot.JQPlotChart;
import net.sf.click.chart.jqplot.JQPlotDataSet;
import net.sf.click.chart.jqplot.Legend.Compass;
import net.sf.click.chart.jqplot.PieChart;
import net.sf.click.chart.jqplot.Series;
import net.sf.click.chart.jqplot.renderer.PieRenderer;
import net.sf.click.chart.test.BorderPage;

/*

 */
public class JQPlotsPieChartsDemo extends BorderPage 
{
	private static final long serialVersionUID = 1L;

	public final String title = "JQPlots Pie Charts Demo Page";
	
	public JQPlotChart lineChart = new JQPlotChart( "lineChart" );
	public JQPlotChart lineChart2 = new JQPlotChart( "lineChart2" );
	public JQPlotChart lineChart3 = new JQPlotChart( "lineChart3" );
	public PieChart pieChart;
	
	public JQPlotsPieChartsDemo()
	{
		lineChart.setDemoMode( true );
		lineChart2.setDemoMode( true );
		lineChart3.setDemoMode( true );
		
		buildChart1();
		buildChart2();
		buildChart3();
		buildChart4();
	}
	
	private void buildChart1()
	{
		Series series = new Series(); 
		series.setRenderer( new PieRenderer() );
		
		lineChart.getTitle().setText( "Default Pie Chart" );
		
		lineChart.setSeriesDefaults( series );
		
		JQPlotDataSet line1 = new JQPlotDataSet();
		line1.add( new Data<String, Integer>( "frogs", 3 ) );
		line1.add( new Data<String, Integer>( "buzzards", 7 ) );
		line1.add( new Data<String, Double>( "deer", 2.5 ) );
		line1.add( new Data<String, Integer>( "turkeys", 6 ) );
		line1.add( new Data<String, Integer>( "moles", 5 ) );
		line1.add( new Data<String, Integer>( "ground hogs", 4 ) );
		lineChart.add( line1 );
	}//met
	
	private void buildChart2()
	{
		Series series = new Series(); 
		PieRenderer pr = new PieRenderer( series );
		pr.setSliceMargin( 8 );
		lineChart2.getTitle().setText( "Pie Chart with Legend and sliceMargin" );
		
		lineChart2.setSeriesDefaults( series );
		lineChart2.getLegend().setShow( true );
		
		JQPlotDataSet line1 = new JQPlotDataSet();
		line1.add( new Data<String, Integer>( "frogs", 3 ) );
		line1.add( new Data<String, Integer>( "buzzards", 7 ) );
		line1.add( new Data<String, Double>( "deer", 2.5 ) );
		line1.add( new Data<String, Integer>( "turkeys", 6 ) );
		line1.add( new Data<String, Integer>( "moles", 5 ) );
		line1.add( new Data<String, Integer>( "ground hogs", 4 ) );
		lineChart2.add( line1 );
	}//met
	
	
	private void buildChart3()
	{
		Series series = new Series(); 
		PieRenderer pr = new PieRenderer( series );
		pr.setSliceMargin( 8 );
		pr.setFill( false );
		pr.setShadow( false );
		pr.setLineWidth( 5 );
		lineChart3.getTitle().setText( "Pie Chart with Legend and sliceMargin" );
		
		lineChart3.setSeriesDefaults( series );
		lineChart3.getLegend().setShow( true );
		lineChart3.getLegend().setLocation( Compass.WEST );
		
		JQPlotDataSet line1 = new JQPlotDataSet();
		line1.add( new Data<String, Integer>( "frogs", 3 ) );
		line1.add( new Data<String, Integer>( "buzzards", 7 ) );
		line1.add( new Data<String, Double>( "deer", 2.5 ) );
		line1.add( new Data<String, Integer>( "turkeys", 6 ) );
		line1.add( new Data<String, Integer>( "moles", 5 ) );
		line1.add( new Data<String, Integer>( "ground hogs", 4 ) );
		lineChart3.add( line1 );
	}//met
	
	private void buildChart4()
	{
		pieChart = new PieChart( "lineChart4" );
		pieChart.setDemoMode( true );
		//you're gonna love that class. 
		//good morning everyone
		pieChart.setLabel("Pie Chart with Legend and sliceMargin");
			
		//easy to use shortcuts
		pieChart.setSliceMargin( 8 );
		pieChart.setFill( false );
		pieChart.setShadow( false );
		pieChart.setLineWidth( 5 );
		pieChart.setDiameter( 100d );

		//power of inheritance, the pie chart still is highly 
		//customizable
		pieChart.getLegend().setLocation( Compass.WEST );
		
		//ease of data input
		pieChart.add( "frogs", 3 );
		pieChart.add( "buzzards", 7 );
		pieChart.add( "deer", 2.5 );
		pieChart.add( "turkeys", 6 );
		pieChart.add( "moles", 5 );
		pieChart.add( "ground hogs", 4 );
	}//met
}//class
