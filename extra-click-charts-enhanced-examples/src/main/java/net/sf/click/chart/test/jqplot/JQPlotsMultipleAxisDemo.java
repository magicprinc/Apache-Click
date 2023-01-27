package net.sf.click.chart.test.jqplot;

import net.sf.click.chart.jqplot.Axes;
import net.sf.click.chart.jqplot.Axis;
import net.sf.click.chart.jqplot.Grid;
import net.sf.click.chart.jqplot.JQPlotChart;
import net.sf.click.chart.jqplot.JQPlotDataSet;
import net.sf.click.chart.jqplot.Series;
import net.sf.click.chart.jqplot.Series.YAxis;
import net.sf.click.chart.test.BorderPage;

/*

 */
public class JQPlotsMultipleAxisDemo extends BorderPage 
{
	private static final long serialVersionUID = 1L;

	public final String title = "JQPlots Multiple Demo Page";
	
	public JQPlotChart lineChart = new JQPlotChart( "lineChart" );
	public JQPlotChart lineChart2 = new JQPlotChart( "lineChart2" );
	
	public JQPlotsMultipleAxisDemo()
	{
		lineChart.setDemoMode( true );
		lineChart2.setDemoMode( true );
		buildChart1();
		buildChart2();
	}
	
	private void buildChart1()
	{
		lineChart2.setTitle( "Default Multiple y axes" );
		lineChart.setWidth( 680 );
		lineChart.setHeight( 380 );
		JQPlotDataSet line1 = new JQPlotDataSet();
		line1.add(2, 3, 1, 4, 3);
		JQPlotDataSet line2 = new JQPlotDataSet();
		line2.add(1, 4, 3, 2, 2.5, 2);
		JQPlotDataSet line3 = new JQPlotDataSet();
		line3.add(14, 24, 18, 22);
		JQPlotDataSet line4 = new JQPlotDataSet();
		line4.add(102, 104, 153, 122, 138, 115);
		JQPlotDataSet line5 = new JQPlotDataSet();
		line5.add(843, 777, 754, 724, 722);
		lineChart.add( line1 );
		//lineChart.add( line2 );
		lineChart.add( line3 );
		lineChart.add( line4 );
		lineChart.add( line5 );
		Series s = new Series();
		lineChart.add( s );
		s = new Series();
		s.setYaxis( YAxis.Y2_AXIS );
		lineChart.add( s );
		s = new Series();
		s.setYaxis( YAxis.Y3_AXIS );
		lineChart.add( s );
		s = new Series();
		s.setYaxis( YAxis.Y4_AXIS );
		lineChart.add( s );
		s = new Series();
		s.setYaxis( YAxis.Y5_AXIS );
		lineChart.add( s );
		s = new Series();
		s.setYaxis( YAxis.Y6_AXIS );
		lineChart.add( s );
	}//met
	
	private void buildChart2()
	{
		lineChart2.setWidth( 680 );
		lineChart2.setHeight( 380 );
		lineChart2.setTitle( "Customized Multiple y axes" );
		JQPlotDataSet line1 = new JQPlotDataSet();
		line1.add(2, 3, 1, 4, 3);
		JQPlotDataSet line2 = new JQPlotDataSet();
		line2.add(1, 4, 3, 2, 2.5, 2);
		JQPlotDataSet line3 = new JQPlotDataSet();
		line3.add(14, 24, 18, 22);
		JQPlotDataSet line4 = new JQPlotDataSet();
		line4.add(102, 104, 153, 122, 138, 115);
		JQPlotDataSet line5 = new JQPlotDataSet();
		line5.add(843, 777, 754, 724, 722);
		lineChart2.add( line1 );
		lineChart2.add( line2 );
		lineChart2.add( line3 );
		lineChart2.add( line4 );
		lineChart2.add( line5 );
		Series s = new Series();
		lineChart2.add( s );
		s = new Series();
		s.setYaxis( YAxis.Y2_AXIS );
		lineChart2.add( s );
		s = new Series();
		s.setYaxis( YAxis.Y3_AXIS );
		lineChart2.add( s );
		s = new Series();
		s.setYaxis( YAxis.Y4_AXIS );
		lineChart2.add( s );
		s = new Series();
		s.setYaxis( YAxis.Y5_AXIS );
		lineChart2.add( s );
		s = new Series();
		s.setYaxis( YAxis.Y6_AXIS );
		lineChart2.add( s );
		
		Axes axes = lineChart2.getAxes();
		Axis axis = new Axis();
		axis.setPadMax( 2d );
		axes.setY2axis(axis);
		axis = new Axis();
		axis.setPadMax( 2.5d );
		axes.setY3axis(axis);
		axis = new Axis();
		axis.setPadMin( 2d );
		axes.setY4axis(axis);
		axis = new Axis();
		axis.setPadMin( 2.3d );
		axes.setY5axis(axis);
		
		axis = lineChart2.getAxesDefaults();
		axis.setUseSeriesColor( true );
		
		Grid grid = lineChart2.getGrid();
		grid.setGridLineWidth(1);
		grid.setBorderWidth(2.5);
		grid.setShadow( false );
	}//met

}//class