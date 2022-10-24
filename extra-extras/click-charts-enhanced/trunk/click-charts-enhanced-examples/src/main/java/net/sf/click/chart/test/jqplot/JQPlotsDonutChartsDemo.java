package net.sf.click.chart.test.jqplot;

import net.sf.click.chart.jqplot.JQPlotChart;
import net.sf.click.chart.jqplot.JQPlotDataSet;
import net.sf.click.chart.jqplot.Series;
import net.sf.click.chart.jqplot.Legend.Compass;
import net.sf.click.chart.jqplot.renderer.DonutRenderer;
import net.sf.click.chart.test.BorderPage;

/*

 */
public class JQPlotsDonutChartsDemo extends BorderPage 
{
	private static final long serialVersionUID = 1L;

	public final String title = "JQPlots Funnel Charts Demo Page";
	
	public JQPlotChart lineChart = new JQPlotChart( "lineChart" );
	public JQPlotChart lineChart2 = new JQPlotChart( "lineChart2" );

	
	public JQPlotsDonutChartsDemo()
	{
		lineChart.setDemoMode( true );
		lineChart2.setDemoMode( true );
		buildChart1();
		buildChart2();
	}
	
	private void buildChart1()
	{
		lineChart.getTitle().setText( "2010. World population by contients" );
		
		DonutRenderer dr = new DonutRenderer();
		Series series = new Series();
		lineChart.setSeriesDefaults( series );
		series.setRenderer( dr );

		JQPlotDataSet line1 = new JQPlotDataSet();
		line1.add( "Africa", 1030 );
		line1.add( "Americas and the Caribbean", 929 );
		line1.add( "Asia", 4157 );
		line1.add( "Europe", 739 );
		line1.add( "Oceania", 35 );
		lineChart.add( line1 );
	}//met
	
	private void buildChart2()
	{
		lineChart2.getTitle().setText( "2010. World population (outer) and<br> wealth (inner) by contients" );
		
		Series series = new Series();
		lineChart2.setSeriesDefaults( series );
		DonutRenderer dr = new DonutRenderer( series );
		dr.setSliceMargin( 2 );
		dr.setInnerDiameter( 110d );
		dr.setStartAngle( -90 );
		dr.setHighlightMouseDown( true );
		dr.setShowDataLabels( true );
		dr.setShadowAlpha( 0.6 );
		
		lineChart2.getLegend().setShow( true );
		lineChart2.getLegend().setLocation( Compass.EAST );

		//TODO 
		//check legend placement property
		//lineChart2.getLegend().setPlacement( LegendPlacement.OUTSIDE );
		
		Series series1 = new Series( "Population" );
		lineChart2.add( series1 );

		Series series2 = new Series( "GDP");
		lineChart2.add( series2 );
		
		JQPlotDataSet line1 = new JQPlotDataSet();
		line1.add( "Africa", 1030 );
		line1.add( "Americas and the Caribbean", 929 );
		line1.add( "Asia", 4157 );
		line1.add( "Europe", 739 );
		line1.add( "Oceania", 35 );
		lineChart2.add( line1 );

		//source : http://www.geohive.com/charts/ec_gdp4.aspx
		JQPlotDataSet line2 = new JQPlotDataSet();
		line2.add( "Africa",  2092300 );
		line2.add( "Americas and the Caribbean", 12776478 + 4299879);
		line2.add( "Asia", 21504497 );
		line2.add( "Europe",  14244444 );
		line2.add( "Oceania",  737226);
		lineChart2.add( line2 );
	}//met
}//class
