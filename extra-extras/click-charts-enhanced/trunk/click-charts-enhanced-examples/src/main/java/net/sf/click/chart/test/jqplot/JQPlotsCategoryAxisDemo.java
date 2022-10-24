package net.sf.click.chart.test.jqplot;

import net.sf.click.chart.jqplot.Axis;
import net.sf.click.chart.jqplot.JQPlotChart;
import net.sf.click.chart.jqplot.JQPlotDataSet;
import net.sf.click.chart.jqplot.Series;
import net.sf.click.chart.jqplot.renderer.CategoryAxisRenderer;
import net.sf.click.chart.jqplot.renderer.MarkerRenderer;
import net.sf.click.chart.jqplot.renderer.MarkerRenderer.MarkerStyle;
import net.sf.click.chart.test.BorderPage;

/*

 */
public class JQPlotsCategoryAxisDemo extends BorderPage 
{
	private static final long serialVersionUID = 1L;

	public final String title = "JQPlots Category Axis Demo Page";
	
	public JQPlotChart lineChart = new JQPlotChart( "lineChart" );
	public JQPlotChart lineChart2 = new JQPlotChart( "lineChart2" );
	public JQPlotChart lineChart3 = new JQPlotChart( "lineChart3" );
	
	public JQPlotsCategoryAxisDemo()
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
		lineChart.setTitle( "Default Category X Axis" );
		Axis xaxis = new Axis();
		xaxis.setRenderer( new CategoryAxisRenderer() );
		lineChart.getAxes().setXaxis(xaxis);
		
		Series series1 = new Series();
		MarkerRenderer mr = new MarkerRenderer();
		mr.setStyle( MarkerStyle.SQUARE );
		series1.setMarkerRenderer( mr );
		series1.setLineWidth( 4 );
		lineChart.add( series1 );
		
		JQPlotDataSet line1 = new JQPlotDataSet();
		line1.add( 4, 25, 13, 22, 14, 17, 15 );
		lineChart.add( line1 );
	}//met
	
	private void buildChart2()
	{
		lineChart2.setTitle( "Customized Category X Axis" );
		Axis xaxis = new Axis();
		lineChart2.getAxes().setXaxis(xaxis);
		xaxis.setRenderer( new CategoryAxisRenderer() );
		xaxis.setTicks( new String[] {"uno","dos","tres","cuatro","cinco","seis","siete"} );
		
		Series series1 = new Series();
		MarkerRenderer mr = new MarkerRenderer();
		mr.setStyle( MarkerStyle.SQUARE );
		series1.setMarkerRenderer( mr );
		series1.setLineWidth( 4 );
		lineChart2.add( series1 );
		
		JQPlotDataSet line1 = new JQPlotDataSet();
		line1.add( 4, 25, 13, 22, 14, 17, 15 );
		lineChart2.add( line1 );
	}//met

	private void buildChart3()
	{
		lineChart3.setTitle( "Customized Category X Axis by Series Data Specificaiton" );
		Axis xaxis = new Axis();
		lineChart3.getAxes().setXaxis(xaxis);
		xaxis.setRenderer( new CategoryAxisRenderer() );
		
		Series series1 = new Series();
		MarkerRenderer mr = new MarkerRenderer();
		mr.setStyle( MarkerStyle.SQUARE );
		series1.setMarkerRenderer( mr );
		series1.setLineWidth( 4 );
		lineChart3.add( series1 );
		
		JQPlotDataSet line1 = new JQPlotDataSet();
		line1.add( "uno", 4 );
		line1.add("dos", 25 );
		line1.add( "tre", 13 );
		line1.add( "cuatro", 22 ) ;
		line1.add( "cinco", 14 ) ;
		line1.add( "seis", 17 ) ;
		line1.add( "siete", 15 );
		lineChart3.add( line1 );
	}//met
	
	

}//class