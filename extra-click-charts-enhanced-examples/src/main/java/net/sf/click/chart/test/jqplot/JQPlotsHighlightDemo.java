package net.sf.click.chart.test.jqplot;

import net.sf.click.chart.jqplot.Axis;
import net.sf.click.chart.jqplot.JQPlotChart;
import net.sf.click.chart.jqplot.JQPlotDataSet;
import net.sf.click.chart.jqplot.Legend.Compass;
import net.sf.click.chart.jqplot.plugins.Cursor;
import net.sf.click.chart.jqplot.plugins.Highlighter;
import net.sf.click.chart.jqplot.renderer.CanvasAxisTickRenderer;
import net.sf.click.chart.jqplot.renderer.DateAxisRenderer;
import net.sf.click.chart.test.BorderPage;

/*

 */
public class JQPlotsHighlightDemo extends BorderPage 
{
	private static final long serialVersionUID = 1L;

	public final String title = "JQPlots Data Point Highlighter and Cursor Tracking Demo Page";

	public JQPlotChart lineChart = new JQPlotChart( "lineChart" );
	public JQPlotChart lineChart2 = new JQPlotChart( "lineChart2" );

	public JQPlotsHighlightDemo()
	{
		lineChart.setDemoMode( true );
		lineChart2.setDemoMode( true );
		buildChart1();
		buildChart2();
	}

	private void buildChart1()
	{
		lineChart.getTitle().setText( "Data Point Highlighting" );

		Axis xaxis = new Axis();
		lineChart.getAxes().setXaxis( xaxis );
		xaxis.setRenderer( new DateAxisRenderer() );
		CanvasAxisTickRenderer catr = new CanvasAxisTickRenderer();
		xaxis.setTickRenderer( catr );
		catr.setFormatString( "%b %#d, %Y" );
		catr.setFontSize( 10 );
		catr.setFontFamily( "Tahoma" );
		catr.setAngle( -30 );

		Axis yaxis = new Axis();
		lineChart.getAxes().setYaxis( yaxis );
		catr = new CanvasAxisTickRenderer();
		yaxis.setTickRenderer( catr );
		catr.setFormatString( "$%.2f" );

		Highlighter h = new Highlighter();
		h.setSizeAdjust( 7.5d );
		lineChart.addPlugin( h );

		Cursor cursor = new Cursor();
		cursor.setShow( false );
		lineChart.addPlugin( cursor );

		JQPlotDataSet line1 = new JQPlotDataSet();
		line1.add( "23-May-08", 578.55 );
		line1.add( "20-Jun-08", 566.5 );
		line1.add( "25-Jul-08", 480.88 );
		line1.add( "22-Aug-08", 509.84 );
		line1.add( "26-Sep-08", 454.13);
		line1.add( "24-Oct-08", 379.75);
		line1.add( "21-Nov-08", 303);
		line1.add( "26-Dec-08", 308.56);
		line1.add( "23-Jan-09", 299.14);
		line1.add( "20-Feb-09", 346.51);
		line1.add( "20-Mar-09", 325.99);
		line1.add( "24-Apr-09", 386.15);

		lineChart.add( line1 );

	}//met
	
	private void buildChart2()
	{
		lineChart2.getTitle().setText( "Data Point Highlighting" );

		Axis xaxis = new Axis();
		lineChart2.getAxes().setXaxis( xaxis );
		xaxis.setRenderer( new DateAxisRenderer() );
		CanvasAxisTickRenderer catr = new CanvasAxisTickRenderer();
		xaxis.setTickRenderer( catr );
		catr.setFormatString( "%b %#d, %Y" );
		catr.setFontSize( 10 );
		catr.setFontFamily( "Tahoma" );
		catr.setAngle( -30 );

		Axis yaxis = new Axis();
		lineChart2.getAxes().setYaxis( yaxis );
		catr = new CanvasAxisTickRenderer();
		yaxis.setTickRenderer( catr );
		catr.setFormatString( "$%.2f" );

		Highlighter h = new Highlighter();
		h.setShow( false );
		lineChart2.addPlugin( h );

		Cursor cursor = new Cursor();
		cursor.setTooltipLocation( Compass.SOUTH_WEST );
		lineChart2.addPlugin( cursor );

		JQPlotDataSet line1 = new JQPlotDataSet();
		line1.add( "23-May-08", 578.55 );
		line1.add( "20-Jun-08", 566.5 );
		line1.add( "25-Jul-08", 480.88 );
		line1.add( "22-Aug-08", 509.84 );
		line1.add( "26-Sep-08", 454.13);
		line1.add( "24-Oct-08", 379.75);
		line1.add( "21-Nov-08", 303);
		line1.add( "26-Dec-08", 308.56);
		line1.add( "23-Jan-09", 299.14);
		line1.add( "20-Feb-09", 346.51);
		line1.add( "20-Mar-09", 325.99);
		line1.add( "24-Apr-09", 386.15);

		lineChart2.add( line1 );

	}//met

}//class
