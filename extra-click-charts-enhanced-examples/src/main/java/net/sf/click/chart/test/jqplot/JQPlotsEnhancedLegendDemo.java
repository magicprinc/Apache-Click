package net.sf.click.chart.test.jqplot;

import net.sf.click.chart.jqplot.Axis;
import net.sf.click.chart.jqplot.JQPlotChart;
import net.sf.click.chart.jqplot.JQPlotDataSet;
import net.sf.click.chart.jqplot.Legend;
import net.sf.click.chart.jqplot.Legend.Compass;
import net.sf.click.chart.jqplot.Series;
import net.sf.click.chart.jqplot.renderer.BarRenderer;
import net.sf.click.chart.jqplot.renderer.CategoryAxisRenderer;
import net.sf.click.chart.jqplot.renderer.EnhancedLegendRenderer;
import net.sf.click.chart.jqplot.renderer.PieRenderer;
import net.sf.click.chart.test.BorderPage;

/*

 */
public class JQPlotsEnhancedLegendDemo extends BorderPage 
{
	private static final long serialVersionUID = 1L;

	public final String title = "JQPlots Enhanced Legend Demo Page";

	public JQPlotChart lineChart = new JQPlotChart( "lineChart" );
	public JQPlotChart lineChart2 = new JQPlotChart( "lineChart2" );
	public JQPlotChart lineChart3 = new JQPlotChart( "lineChart3" );
	public JQPlotChart lineChart4 = new JQPlotChart( "lineChart4" );

	public JQPlotsEnhancedLegendDemo()
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
		lineChart.setStackSeries( true );

		Series series = new Series();
		lineChart.setSeriesDefaults( series );
		series.setFill( true );
		series.setShowMarker( false );

		Legend legend = new Legend();
		lineChart.setLegend( legend );
		EnhancedLegendRenderer elr = new EnhancedLegendRenderer(); 
		legend.setRenderer( elr );
		legend.setShow( true );
		legend.setLabels( new String[] {"Fog","Rain","Frost", "Sleet", "Hail","Snow"});
		elr.setNumberColumns( 3 );

		Axis xaxis = new Axis();
		lineChart.getAxes().setXaxis( xaxis );
		xaxis.setMin( 1d );
		xaxis.setMax( 4d );

		Axis yaxis = new Axis();
		lineChart.getAxes().setYaxis( yaxis );
		yaxis.setMin( 0d );
		yaxis.setMax( 35d );


		lineChart.getTitle().setText( "Stacked chart" );

		JQPlotDataSet line1 = new JQPlotDataSet();
		line1.add( 1,6,9,8 );

		JQPlotDataSet line2 = new JQPlotDataSet();
		line2.add( 4,3,1,2 );

		JQPlotDataSet line3 = new JQPlotDataSet();
		line3.add( 6,2,4,1 );

		JQPlotDataSet line4 = new JQPlotDataSet();
		line4.add( 1,2,3,4 );

		JQPlotDataSet line5 = new JQPlotDataSet();
		line5.add( 4,3,2,1 );

		JQPlotDataSet line6 = new JQPlotDataSet();
		line6.add( 8,2,6,3 );

		lineChart.add( line1 );
		lineChart.add( line2 );
		lineChart.add( line3 );
		lineChart.add( line4 );
		lineChart.add( line5 );
		lineChart.add( line6 );
	}//met

	private void buildChart2()
	{
		lineChart2.setStackSeries( true );

		Series series = new Series();
		lineChart2.setSeriesDefaults( series );
		series.setRenderer( new BarRenderer() );

		Legend legend = new Legend();
		lineChart2.setLegend( legend );
		EnhancedLegendRenderer elr = new EnhancedLegendRenderer( legend ); 
		legend.setShow( true );
		legend.setLabels( new String[] {"Fog","Rain","Frost", "Sleet", "Hail","Snow"});
		elr.setNumberColumns( 1 );
		//TODO check this with a speed
		elr.setSeriesToggleSpeed( 900 );
		elr.setDisableIEFading( false );

		//TODO Check the placement property
		//legend.setPlacement( LegendPlacement.OUTSIDE );
		//TODO Check the shrink property
		//legend.setShrinkGrid( true );

		Axis xaxis = new Axis();
		lineChart2.getAxes().setXaxis( xaxis );
		xaxis.setRenderer( new CategoryAxisRenderer() );

		Axis yaxis = new Axis();
		lineChart2.getAxes().setYaxis( yaxis );
		yaxis.setMin( 0d );
		yaxis.setMax( 35d );

		lineChart2.getTitle().setText( "Bars Chart" );

		JQPlotDataSet line1 = new JQPlotDataSet();
		line1.add( 1,6,9,8 );

		JQPlotDataSet line2 = new JQPlotDataSet();
		line2.add( 4,3,1,2 );

		JQPlotDataSet line3 = new JQPlotDataSet();
		line3.add( 6,2,4,1 );

		JQPlotDataSet line4 = new JQPlotDataSet();
		line4.add( 1,2,3,4 );

		JQPlotDataSet line5 = new JQPlotDataSet();
		line5.add( 4,3,2,1 );

		JQPlotDataSet line6 = new JQPlotDataSet();
		line6.add( 8,2,6,3 );

		lineChart2.add( line1 );
		lineChart2.add( line2 );
		lineChart2.add( line3 );
		lineChart2.add( line4 );
		lineChart2.add( line5 );
		lineChart2.add( line6 );
	}//met


	private void buildChart3()
	{
		lineChart3.setStackSeries( true );

		Series series = new Series();
		lineChart3.setSeriesDefaults( series );
		series.setFill( false );
		series.setShowMarker( false );

		Legend legend = new Legend();
		lineChart3.setLegend( legend );
		EnhancedLegendRenderer elr = new EnhancedLegendRenderer( legend ); 
		legend.setShow( true );
		legend.setLabels( new String[] {"Fog","Rain","Frost", "Sleet", "Hail","Snow"});
		//TODO check the showSwatches property
		//legend.setShowSwatches( false );
		elr.setNumberRows( 3 );
		legend.setLocation( Compass.SOUTH );
		//TODO check the placement property
		//legend.setPlacement( LegendPlacement.OUTSIDE );

		Axis xaxis = new Axis();
		lineChart3.getAxes().setXaxis( xaxis );
		xaxis.setMin( 1d );
		xaxis.setMax( 4d );
		
		//TODO check the martgin top property
		//legend.setMarginTop (30 );

		Axis yaxis = new Axis();
		lineChart3.getAxes().setYaxis( yaxis );
		yaxis.setMin( 0d );
		yaxis.setMax( 35d );

		lineChart3.getTitle().setText( "line Charts" );

		JQPlotDataSet line1 = new JQPlotDataSet();
		line1.add( 1,6,9,8 );

		JQPlotDataSet line2 = new JQPlotDataSet();
		line2.add( 4,3,1,2 );

		JQPlotDataSet line3 = new JQPlotDataSet();
		line3.add( 6,2,4,1 );

		JQPlotDataSet line4 = new JQPlotDataSet();
		line4.add( 1,2,3,4 );

		JQPlotDataSet line5 = new JQPlotDataSet();
		line5.add( 4,3,2,1 );

		JQPlotDataSet line6 = new JQPlotDataSet();
		line6.add( 8,2,6,3 );

		lineChart3.add( line1 );
		lineChart3.add( line2 );
		lineChart3.add( line3 );
		lineChart3.add( line4 );
		lineChart3.add( line5 );
		lineChart3.add( line6 );
	}//met

	private void buildChart4()
	{
		lineChart4.setStackSeries( true );

		Series series = new Series();
		lineChart4.setSeriesDefaults( series );
		series.setRenderer( new PieRenderer() );

		Legend legend = new Legend();
		lineChart4.setLegend( legend );
		EnhancedLegendRenderer elr = new EnhancedLegendRenderer( legend ); 
		legend.setShow( true );
		elr.setNumberColumns( 2 );

		lineChart4.getTitle().setText( "Pie Chart" );

		JQPlotDataSet line1 = new JQPlotDataSet();
		line1.add( "A",25 );
		line1.add( "B",14 );
		line1.add( "C",7 );
		line1.add( "D",13 );
		line1.add( "E",11 );
		line1.add( "F", 35 );

		lineChart4.add( line1 );
	}//met
}//class
