package net.sf.click.chart.test.jqplot;

import net.sf.click.chart.jqplot.Axis;
import net.sf.click.chart.jqplot.JQPlotChart;
import net.sf.click.chart.jqplot.JQPlotDataSet;
import net.sf.click.chart.jqplot.Series;
import net.sf.click.chart.jqplot.Series.XAxis;
import net.sf.click.chart.jqplot.Series.YAxis;
import net.sf.click.chart.jqplot.renderer.BarRenderer;
import net.sf.click.chart.jqplot.renderer.CanvasAxisTickRenderer;
import net.sf.click.chart.jqplot.renderer.CanvasAxisTickRenderer.LabelPosition;
import net.sf.click.chart.jqplot.renderer.CategoryAxisRenderer;
import net.sf.click.chart.test.BorderPage;

/*

 */
public class JQPlotsRotatedAxisLabelDemo extends BorderPage
{
	private static final long serialVersionUID = 1L;

	public final String title = "JQPlots Charts with Rotated Axis Text Demo Page";

	public JQPlotChart lineChart = new JQPlotChart( "lineChart" );
	public JQPlotChart lineChart2 = new JQPlotChart( "lineChart2" );
	public JQPlotChart lineChart3 = new JQPlotChart( "lineChart3" );
	public JQPlotChart lineChart4 = new JQPlotChart( "lineChart4" );

	public JQPlotsRotatedAxisLabelDemo()
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
		lineChart.setTitle( "Concern vs. Occurrance" );

		Series series1 = new Series();
		BarRenderer mr = new BarRenderer();
		series1.setRenderer( mr );
		lineChart.add( series1 );

		Axis axis = new Axis();
		lineChart.setAxesDefaults( axis );
		CanvasAxisTickRenderer catr = new CanvasAxisTickRenderer();
		axis.setTickRenderer( catr );
		catr.setAngle( -30 );
		catr.setFontSize( 10 );

		Axis xaxis = new Axis();
		lineChart.getAxes().setXaxis( xaxis );
		xaxis.setRenderer( new CategoryAxisRenderer() );

		Axis yaxis = new Axis();
		lineChart.getAxes().setYaxis( yaxis );
		yaxis.setAutoscale( true );


		JQPlotDataSet line1 = new JQPlotDataSet();
		line1.add( "Cup Holder Pinion Bob",7 );
		line1.add( "Generic Fog Lamp",9 );
		line1.add( "HDTV Receiver",15 );
		line1.add( "8 Track Control Module",12 );
		line1.add( "Sludge Pump Fourier Modulator",3 );
		line1.add( "Transcender/Spice Rac",6 );
		line1.add( "Hair Spray Danger Indicator",18 );
		lineChart.add( line1 );
	}//met

	private void buildChart2()
	{
		lineChart2.setTitle( "Concern vs. Occurrance and custom font" );

		Series series1 = new Series();
		BarRenderer mr = new BarRenderer();
		series1.setRenderer( mr );
		lineChart2.add( series1 );

		Axis axis = new Axis();
		lineChart2.setAxesDefaults( axis );
		CanvasAxisTickRenderer catr = new CanvasAxisTickRenderer( axis );
		catr.setAngle( -30 );
		catr.setFontSize( 10 );
		catr.setEnableFontSupport( true );
		catr.setFontFamily( "Georgia" );

		Axis xaxis = new Axis();
		lineChart2.getAxes().setXaxis( xaxis );
		xaxis.setRenderer( new CategoryAxisRenderer() );

		Axis yaxis = new Axis();
		lineChart2.getAxes().setYaxis( yaxis );
		yaxis.setAutoscale( true );


		JQPlotDataSet line1 = new JQPlotDataSet();
		line1.add( "Cup Holder Pinion Bob",7 );
		line1.add( "Generic Fog Lamp",9 );
		line1.add( "HDTV Receiver",15 );
		line1.add( "8 Track Control Module",12 );
		line1.add( "Sludge Pump Fourier Modulator",3 );
		line1.add( "Transcender/Spice Rac",6 );
		line1.add( "Hair Spray Danger Indicator",18 );
		lineChart2.add( line1 );
	}//met

	private void buildChart3()
	{
		lineChart3.setTitle( "rotated secondary axis labels" );

		Series series1 = new Series();
		BarRenderer mr = new BarRenderer();
		series1.setRenderer( mr );
		lineChart3.add( series1 );


		Series series2 = new Series();
		series2.setXaxis( XAxis.X2_AXIS );
		series2.setYaxis( YAxis.Y2_AXIS );
		lineChart3.add( series2 );


		Axis axis = new Axis();
		lineChart3.setAxesDefaults( axis );
		CanvasAxisTickRenderer catr = new CanvasAxisTickRenderer( axis );
		catr.setAngle( 30 );

		Axis xaxis = new Axis();
		lineChart3.getAxes().setXaxis( xaxis );
		lineChart3.getAxes().setX2axis( xaxis );
		xaxis.setRenderer( new CategoryAxisRenderer() );

		Axis yaxis = new Axis();
		lineChart3.getAxes().setYaxis( yaxis );
		lineChart3.getAxes().setY2axis( yaxis );
		yaxis.setAutoscale( true );


		JQPlotDataSet line1 = new JQPlotDataSet();
		line1.add( "Cup Holder Pinion Bob",7 );
		line1.add( "Generic Fog Lamp",9 );
		line1.add( "HDTV Receiver",15 );
		line1.add( "8 Track Control Module",12 );
		line1.add( "Sludge Pump Fourier Modulator",3 );
		line1.add( "Transcender/Spice Rac",6 );
		line1.add( "Hair Spray Danger Indicator",18 );
		lineChart3.add( line1 );

		JQPlotDataSet line2 = new JQPlotDataSet();
		line2.add( "Nickle",28 );
		line2.add( "Aluminium",13 );
		line2.add( "Xenon",54 );
		line2.add( "Silver",47 );
		line2.add( "Sulfer",16 );
		line2.add( "Silicon",14 );
		line2.add( "Vanadium",23 );
		lineChart3.add( line2 );

	}//met

	private void buildChart4() {
		lineChart4.setTitle( "Concern vs. Occurrance and custom positioning" );

		Series series1 = new Series();
		BarRenderer mr = new BarRenderer();
		series1.setRenderer( mr );
		lineChart4.add( series1 );

		Axis axis = new Axis();
		lineChart4.setAxesDefaults( axis );
		CanvasAxisTickRenderer catr = new CanvasAxisTickRenderer();
		axis.setTickRenderer( catr );
		catr.setAngle( -30 );
		catr.setFontSize( 10 );
		catr.setEnableFontSupport( true );
		catr.setFontFamily( "Georgia" );

		Axis xaxis = new Axis();
		lineChart4.getAxes().setXaxis( xaxis );
		xaxis.setRenderer( new CategoryAxisRenderer() );
		catr = new CanvasAxisTickRenderer();
		xaxis.setTickRenderer( catr );
		catr.setLabelPosition( LabelPosition.MIDDLE );



		Axis yaxis = new Axis();
		lineChart4.getAxes().setYaxis( yaxis );
		yaxis.setAutoscale( true );
		catr = new CanvasAxisTickRenderer();
		yaxis.setTickRenderer( catr );
		catr.setLabelPosition( LabelPosition.START );


		JQPlotDataSet line1 = new JQPlotDataSet();
		line1.add( "Cup Holder Pinion Bob",7 );
		line1.add( "Generic Fog Lamp",9 );
		line1.add( "HDTV Receiver",15 );
		line1.add( "8 Track Control Module",12 );
		line1.add( "Sludge Pump Fourier Modulator",3 );
		line1.add( "Transcender/Spice Rac",6 );
		line1.add( "Hair Spray Danger Indicator",18 );
		lineChart4.add( line1 );
	}

}//class