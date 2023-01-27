package net.sf.click.chart.test.jqplot;

import net.sf.click.chart.DataOHLC_B;
import net.sf.click.chart.jqplot.Axis;
import net.sf.click.chart.jqplot.JQPlotChart;
import net.sf.click.chart.jqplot.JQPlotDataSet;
import net.sf.click.chart.jqplot.Series;
import net.sf.click.chart.jqplot.renderer.BezierCurveRenderer;
import net.sf.click.chart.jqplot.renderer.DateAxisRenderer;
import net.sf.click.chart.test.BorderPage;

/*

 */
public class JQPlotsBezierChartsDemo extends BorderPage 
{
	private static final long serialVersionUID = 1L;

	public final String title = "JQPlots Bezier Charts Demo Page";
	
	public JQPlotChart lineChart = new JQPlotChart( "lineChart" );
	public JQPlotChart lineChart2 = new JQPlotChart( "lineChart2" );
	public JQPlotChart lineChart3 = new JQPlotChart( "lineChart3" );
	
	public JQPlotsBezierChartsDemo()
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
		lineChart.setSortData( false );
		lineChart.getTitle().setText( "Bubble Test" );
		
		BezierCurveRenderer bcr = new BezierCurveRenderer();
		Series series = new Series();
		lineChart.setSeriesDefaults( series );
		series.setRenderer( bcr );
		
		lineChart.getLegend().setShow( true );

		JQPlotDataSet line1 = new JQPlotDataSet();
		line1.add( 0,1 );
		line1.add( new DataOHLC_B(2,2,4,.5,6,0) );
		lineChart.add( line1 );
		
		JQPlotDataSet line2 = new JQPlotDataSet();
		line2.add( 0,5 );
		line2.add( new DataOHLC_B(2, 6, 5, 1, 6, .5) );
		lineChart.add( line2 );

		JQPlotDataSet line3 = new JQPlotDataSet();
		line3.add( 0,6 );
		line3.add( new DataOHLC_B( 3, 9, 4, 8, 6, 3 ) );
		lineChart.add( line3 );

		JQPlotDataSet line4 = new JQPlotDataSet();
		line4.add( 0,7 );
		line4.add( new DataOHLC_B( 2, 9, 4, 8, 6, 6 ) );
		lineChart.add( line4 );

		JQPlotDataSet line5 = new JQPlotDataSet();
		line5.add( 0,8 );
		line5.add( new DataOHLC_B( 3, 9, 4, 8, 6, 8 ) );
		lineChart.add( line5 );

	}//met
	
	private void buildChart2()
	{
		lineChart2.setSortData( false );
		lineChart2.getTitle().setText( "Bubble Test" );
		
		BezierCurveRenderer bcr = new BezierCurveRenderer();
		Series series = new Series();
		lineChart2.setSeriesDefaults( series );
		series.setRenderer( bcr );
		
		lineChart2.getLegend().setShow( true );

		JQPlotDataSet line1 = new JQPlotDataSet();
		line1.add( 0,1 );
		line1.add( 2,2 );
		line1.add( 4,.5 );
		line1.add( 6,0 );
		lineChart2.add( line1 );
		
		JQPlotDataSet line2 = new JQPlotDataSet();
		line2.add( 0,5 );
		line2.add( 2, 6 );
		line2.add( 5, 1 );
		line2.add( 6, .5 );
		lineChart2.add( line2 );

		JQPlotDataSet line3 = new JQPlotDataSet();
		line3.add( 0,6 );
		line3.add( 3, 9 );
		line3.add( 4, 8 );
		line3.add( 6, 3  );
		lineChart2.add( line3 );

		JQPlotDataSet line4 = new JQPlotDataSet();
		line4.add( 0,7 );
		line4.add( 2, 9 );
		line4.add( 4, 8 );
		line4.add( 6, 6  );
		lineChart2.add( line4 );

		JQPlotDataSet line5 = new JQPlotDataSet();
		line5.add( 0,8 );
		line5.add(  3, 9 );
		line5.add( 4, 8 );
		line5.add( 6, 8  );
		lineChart2.add( line5 );
	}//met
	
	private void buildChart3() 
	{
		lineChart3.setSortData( false );
		lineChart3.getTitle().setText( "Bubble Test" );
		
		Series series = new Series();
		BezierCurveRenderer bcr = new BezierCurveRenderer( series );
		lineChart3.setSeriesDefaults( series );

		lineChart3.getLegend().setShow( true );

		Axis axis = new Axis();
		lineChart3.getAxes().setXaxis( axis );
		axis.setNumberTicks( 4 );
		new DateAxisRenderer( axis );
		
		JQPlotDataSet line1 = new JQPlotDataSet();
		line1.add( "01/01/2010",1 );
		line1.add( "02/01/2010", 2 );
		line1.add( "03/01/2010", .5 );
		line1.add( "04/01/2010", 0 );
		lineChart3.add( line1 );
		
		JQPlotDataSet line2 = new JQPlotDataSet();
		line2.add( "01/01/2010", 5 );
		line2.add( "02/01/2010", 6 );
		line2.add( "03/01/2010", 1 );
		line2.add( "04/01/2010", .5 );
		lineChart3.add( line2 );

		JQPlotDataSet line3 = new JQPlotDataSet();
		line3.add( "01/01/2010", 6 );
		line3.add( "02/01/2010", 9);
		line3.add( "03/01/2010", 8 );
		line3.add( "04/01/2010", 3 );
		lineChart3.add( line3 );

		JQPlotDataSet line4 = new JQPlotDataSet();
		line4.add( "01/01/2010", 7 );
		line4.add( "02/01/2010", 9);
		line4.add( "03/01/2010", 8 );
		line4.add( "04/01/2010", 6 );
		lineChart3.add( line4 );

		JQPlotDataSet line5 = new JQPlotDataSet();
		line5.add( "01/01/2010", 8 );
		line5.add( "02/01/2010", 9);
		line5.add( "03/01/2010", 8 );
		line5.add( "04/01/2010", 8 );
		lineChart3.add( line5 );
		
	}
}//class
