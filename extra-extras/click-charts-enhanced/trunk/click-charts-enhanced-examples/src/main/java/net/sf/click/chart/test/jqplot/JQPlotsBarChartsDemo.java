package net.sf.click.chart.test.jqplot;

import net.sf.click.chart.jqplot.Axis;
import net.sf.click.chart.jqplot.BarChart;
import net.sf.click.chart.jqplot.BasicChartDataProvider;
import net.sf.click.chart.jqplot.JQPlotChart;
import net.sf.click.chart.jqplot.JQPlotDataSet;
import net.sf.click.chart.jqplot.Legend.Compass;
import net.sf.click.chart.jqplot.Series;
import net.sf.click.chart.jqplot.renderer.BarRenderer;
import net.sf.click.chart.jqplot.renderer.CategoryAxisRenderer;
import net.sf.click.chart.jqplot.renderer.BarRenderer.BarDirection;
import net.sf.click.chart.test.BorderPage;

/*

 */
public class JQPlotsBarChartsDemo extends BorderPage 
{
	private static final long serialVersionUID = 1L;

	public final String title = "JQPlots Bar Charts Demo Page";
	
	public JQPlotChart lineChart = new JQPlotChart( "lineChart" );
	public BarChart barChart;
	public JQPlotChart lineChart3 = new JQPlotChart( "lineChart3" );
	
	public JQPlotsBarChartsDemo()
	{
		lineChart.setDemoMode( true );
		lineChart3.setDemoMode( true );
		buildChart1();
		buildChart2();
		buildChart3();
	}
	
	private void buildChart1()
	{
		Series series = new Series(); 
		series.setRenderer( new BarRenderer() );
		series.setLabel( "Profits" );
		
		Series series2 = new Series();
		series2.setRenderer( new BarRenderer() );
		series2.setLabel( "Expenses" );
		
		lineChart.getLegend().setShow( true );
		lineChart.getLegend().setLocation( Compass.NORTH_EAST);
		
		lineChart.getTitle().setText( "Bar Charts" );
		
		Axis xaxis = new Axis();
		xaxis.setRenderer( new CategoryAxisRenderer(  ) );
		Axis yaxis = new Axis();
		yaxis.setMin( 0d );
		lineChart.getAxes().setXaxis(xaxis);
		lineChart.getAxes().setYaxis(yaxis);
		lineChart.add( series );
		lineChart.add( series2 );
		JQPlotDataSet line1 = new JQPlotDataSet();
		line1.add( 1,1 );
		line1.add( 2,4 );
		line1.add( 3,9 );
		line1.add( 4,16 );
		JQPlotDataSet line2 = new JQPlotDataSet();
		line2.add( 1,25 );
		line2.add( 2,12.5 );
		line2.add( 3,6.25 );
		line2.add( 4,3.125 );
		lineChart.add( line1 );
		lineChart.add( line2 );
	}//met
	
	private void buildChart2()
	{
		barChart = new BarChart( "lineChart2" );
		barChart.setDemoMode( true );
		
		barChart.setLabel( "Bar Charts With Options" );
		barChart.setBarWidth( 20 );
		barChart.setBarPadding( 8 );

		barChart.getLegend().setXoffset( 55 );
		barChart.getLegend().setLocation( Compass.NORTH_EAST);

		BasicChartDataProvider sdp = new BasicChartDataProvider(3,4);
		barChart.setDataProvider( sdp );
		//TODO could be setSeriesNameProperty (column label)
		sdp.setListSeriesName("Profits", "Expenses", "Sales");
		//TODO could be setXProperties (column labels)
		sdp.setListXTickLabel( "1st Qtr","2nd Qtr", "3rd Qtr", "4th Qtr2" );
		//TODO could be addData( serieIndex, object );
		sdp.addData( 0,1,2,3,4 );
		sdp.addData( 1,25,12.25,6.25,3.125 );
		sdp.addData( 2,2,7,15,30 );

		/* would have been possible as well.
		double[][] data = new double[3][4];
		data[0][0] = 1; data[0][1] = 4;     data[0][2] = 9;    data[0][3] = 16;
		data[1][0] = 25;data[1][1] = 12.25; data[1][2] = 6.25; data[1][3] = 3.125;
		data[2][0] = 2; data[2][1] = 7;     data[2][2] = 15;   data[2][3] = 30;
		sdp.setData( data );
		*/
	}//met
	
	
	private void buildChart3()
	{
		Series df = new Series(); 
		df.setShadowAngle( 135d );
		
		BarRenderer r = new BarRenderer(); 
		r.setBarWidth( 15 );
		r.setBarPadding( 6 );
		r.setBarDirection( BarDirection.HORIZONTAL );
		df.setRenderer( r );
		
		
		Series series = new Series(); 
		series.setLabel( "Cats" );
		
		Series series2 = new Series();
		series2.setLabel( "Dogs" );

		lineChart3.getLegend().setShow( true );
		lineChart3.getLegend().setXoffset( 55 );
		lineChart3.getLegend().setLocation( Compass.NORTH_EAST);
		
		lineChart3.getTitle().setText( "Horizontally Oriented Bar Chart" );
		
		Axis yaxis = new Axis();
		yaxis.setRenderer( new CategoryAxisRenderer(  ) );
		yaxis.setTicks( new String[] {"Once","Tice", "Thrice", "More" } );
		lineChart3.getAxes().setYaxis(yaxis);
		Axis xaxis = new Axis();
		xaxis.setMin( 0d );
		lineChart3.getAxes().setXaxis(xaxis);
		lineChart3.add( series );
		lineChart3.add( series2 );
		lineChart3.setSeriesDefaults( df );
		
		JQPlotDataSet line1 = new JQPlotDataSet();
		line1.add( 1,1 );
		line1.add( 4,2 );
		line1.add( 9,3 );
		line1.add( 16,4 );
		JQPlotDataSet line2 = new JQPlotDataSet();
		line2.add( 25,1 );
		line2.add( 12.5,2 );
		line2.add( 6.25,3 );
		line2.add( 3.125,4 );
		lineChart3.add( line1 );
		lineChart3.add( line2 );
	}//met
}//class
