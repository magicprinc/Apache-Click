package net.sf.click.chart.test.jqplot;

import net.sf.click.chart.Color;
import net.sf.click.chart.jqplot.JQPlotChart;
import net.sf.click.chart.jqplot.JQPlotDataSet;
import net.sf.click.chart.jqplot.Series;
import net.sf.click.chart.jqplot.renderer.MeterGaugeRenderer;
import net.sf.click.chart.jqplot.renderer.MeterGaugeRenderer.LabelPosition;
import net.sf.click.chart.test.BorderPage;

/*

 */
public class JQPlotsMeterGaugeChartsDemo extends BorderPage 
{
	private static final long serialVersionUID = 1L;

	public final String title = "JQPlots Meter Gauge Charts Demo Page";
	
	public JQPlotChart lineChart = new JQPlotChart( "lineChart" );
	public JQPlotChart lineChart2 = new JQPlotChart( "lineChart2" );
	public JQPlotChart lineChart3 = new JQPlotChart( "lineChart3" );
	public JQPlotChart lineChart4 = new JQPlotChart( "lineChart4" );
	
	public JQPlotsMeterGaugeChartsDemo()
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
		lineChart.setWidth( 250 );
		lineChart.setHeight( 170 );
		lineChart.setSortData( false );
		lineChart.getTitle().setText( "Meter Gauge Test" );
		
		Series series = new Series();
		MeterGaugeRenderer mgr = new MeterGaugeRenderer( series );
		lineChart.setSeriesDefaults( series );
		mgr.setLabel( "MB/s" );
		
		JQPlotDataSet line1 = new JQPlotDataSet();
		line1.add( 0,1 );
		lineChart.add( line1 );
		
	}//met
	
	private void buildChart2()
	{
		lineChart2.setWidth( 210 );
		lineChart2.setHeight( 100 );
		lineChart2.setSortData( false );
		lineChart2.getTitle().setText( "Meter Gauge Test" );
		
		MeterGaugeRenderer mgr = new MeterGaugeRenderer();
		Series series = new Series();
		lineChart2.setSeriesDefaults( series );
		series.setRenderer( mgr );
		mgr.setIntervals( new Double[] {2d,3d,4d} );
		mgr.setIntervalColors( new Color[] { new Color( 0x66cc66), new Color( 0xE7E658), new Color( 0xcc6666) }  );
		mgr.setShowTickLabels( false );
		
		
		JQPlotDataSet line1 = new JQPlotDataSet();
		line1.add( 0,1 );
		lineChart2.add( line1 );
	}//met
	
	private void buildChart3() 
	{
		lineChart3.setWidth( 300 );
		lineChart3.setHeight( 180 );
		lineChart3.setSortData( false );
		lineChart3.getTitle().setText( "Meter Gauge Test" );
		
		MeterGaugeRenderer mgr = new MeterGaugeRenderer();
		Series series = new Series();
		lineChart3.setSeriesDefaults( series );
		series.setRenderer( mgr );
		mgr.setMin( 100 );
		mgr.setMax( 500 );
		mgr.setIntervals( new Double[] {200d,300d,400d, 500d} );
		mgr.setIntervalColors( new Color[] { new Color( 0x66cc66), new Color( 0x93b75f),new Color( 0xE7E658), new Color( 0xcc6666) }  );
		
		JQPlotDataSet line1 = new JQPlotDataSet();
		line1.add( 0,322 );
		lineChart3.add( line1 );
	}
	
	private void buildChart4() 
	{
		lineChart4.setWidth( 500 );
		lineChart4.setHeight( 300 );
		lineChart4.getTitle().setText( "Meter Gauge Test" );
		
		MeterGaugeRenderer mgr = new MeterGaugeRenderer();
		Series series = new Series();
		lineChart4.setSeriesDefaults( series );
		series.setRenderer( mgr );
		mgr.setLabel( "Metric Tons per Year" );
		mgr.setLabelPosition( LabelPosition.BOTTOM );
		mgr.setLabelHeightAdjust( -5 );
		mgr.setIntervalOuterRadius( 85d );
		mgr.setTicks( new Double[] {10000d,30000d,50000d, 70000d} );
		mgr.setIntervals( new Double[] {22000d,55000d,70000d} );
		mgr.setIntervalColors( new Color[] { new Color( 0x66cc66),new Color( 0xE7E658), new Color( 0xcc6666) }  );
		
		JQPlotDataSet line1 = new JQPlotDataSet();
		line1.add( 0,52200 );
		lineChart4.add( line1 );
	}
}//class
