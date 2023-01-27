package net.sf.click.chart.test.flot;

import net.sf.click.chart.flot.FlotPieChart;
import net.sf.click.chart.test.BorderPage;

/**
 * This example illustrates the flot example at
 * http://people.iola.dk/olau/flot/examples/basic.html
 * and
 * http://people.iola.dk/olau/flot/examples/graph-types.html
 */
public class FlotPieChartsDemo extends BorderPage
{
	private static final long serialVersionUID = 1L;

	public final String title = "Flot Pie Charts Demo Page not working...";

	public FlotPieChart lineChart = new FlotPieChart( "lineChart" );
	public FlotPieChart lineChart2 = new FlotPieChart( "lineChart2" );

	public FlotPieChartsDemo()
	{
		lineChart.setDemoMode( true );
		lineChart2.setDemoMode( true );
		buildChart1();
		buildChart2();
	}

	private void buildChart1()
	{
		lineChart.setWidth( 600 );
		lineChart.setHeight( 300 );

		//ease of data input
		lineChart.add( "frogs", 3 );
		lineChart.add( "buzzards", 7 );
		lineChart.add( "deer", 2 );
		lineChart.add( "turkeys", 6 );
		lineChart.add( "moles", 5 );
		lineChart.add( "ground hogs", 4 );
	}//met


	private void buildChart2()
	{
		lineChart2.setWidth( 600 );
		lineChart2.setHeight( 300 );

		lineChart2.getSeries().getPies().setRadius( 0.5d );
		//ease of data input
		lineChart2.add( "frogs", 3 );
		lineChart2.add( "buzzards", 7 );
		lineChart2.add( "deer", 2 );
		lineChart2.add( "turkeys", 6 );
		lineChart2.add( "moles", 5 );
		lineChart2.add( "ground hogs", 4 );
	}//met

}//class