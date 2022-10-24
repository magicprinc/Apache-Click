package net.sf.click.chart.test.jqplot;

import net.sf.click.chart.jqplot.Axis;
import net.sf.click.chart.jqplot.JQPlotChart;
import net.sf.click.chart.jqplot.JQPlotDataSet;
import net.sf.click.chart.jqplot.renderer.AxisTickRenderer;
import net.sf.click.chart.test.BorderPage;

/*

 */
public class JQPlotsAxisAutoScaleDemo extends BorderPage 
{
	private static final long serialVersionUID = 1L;

	public final String title = "JQPlots Axis Autoscale Demo Page";
	
	public JQPlotChart lineChart = new JQPlotChart( "lineChart" );
	public JQPlotChart lineChart2 = new JQPlotChart( "lineChart2" );
	public JQPlotChart lineChart3 = new JQPlotChart( "lineChart3" );
	public JQPlotChart lineChart4 = new JQPlotChart( "lineChart4" );
	
	public JQPlotsAxisAutoScaleDemo()
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
		JQPlotDataSet line1 = new JQPlotDataSet();
		line1.add(0.82, 1.5, 3.4, 4);
		lineChart.add( line1 );
	}//met
	
	private void buildChart2()
	{
		JQPlotDataSet line1 = new JQPlotDataSet();
		line1.add(0.82, 1.5, 3.4, 4);
		Axis yaxis = new Axis();
		lineChart2.getAxes().setYaxis( yaxis );
		lineChart2.add( line1 );
	}//met

	private void buildChart3()
	{
		Axis yaxis = new Axis();
		yaxis.setAutoscale( true );
		lineChart3.getAxes().setYaxis( yaxis );

		JQPlotDataSet line1 = new JQPlotDataSet();
		line1.add(0.82, 1.5, 3.4, 4);
		lineChart3.add( line1 );
	}//met
	
	private void buildChart4()
	{
		JQPlotDataSet line1 = new JQPlotDataSet();
		line1.add(0.82, 1.5, 3.4, 4);
		Axis yaxis = new Axis();
		((AxisTickRenderer)yaxis.getTickRenderer()).setFormatString("%.3f");
		yaxis.setAutoscale( true );
		lineChart4.getAxes().setYaxis( yaxis );
		lineChart4.add( line1 );
	}//met

}//class