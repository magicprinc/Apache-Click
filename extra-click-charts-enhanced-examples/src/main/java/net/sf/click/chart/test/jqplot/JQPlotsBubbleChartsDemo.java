package net.sf.click.chart.test.jqplot;

import lombok.val;
import net.sf.click.chart.DataHLC;
import net.sf.click.chart.jqplot.Axis;
import net.sf.click.chart.jqplot.JQPlotChart;
import net.sf.click.chart.jqplot.JQPlotDataSet;
import net.sf.click.chart.jqplot.Series;
import net.sf.click.chart.jqplot.renderer.BubbleRenderer;
import net.sf.click.chart.test.BorderPage;

import java.util.concurrent.ThreadLocalRandom;

/*

 */
public class JQPlotsBubbleChartsDemo extends BorderPage {
    private static final long serialVersionUID = -5466784369082249346L;

	public final String title = "JQPlots Bubble Charts Demo Page";

	public JQPlotChart lineChart = new JQPlotChart( "lineChart" );

	public JQPlotsBubbleChartsDemo()
	{
		lineChart.setDemoMode( true );
		buildChart1();
	}

	private void buildChart1()
	{
		lineChart.setSortData( false );
		lineChart.getTitle().setText( "Bubble Test" );

		Series series = new Series();
		series.setShadow( true );
		series.setShadowAlpha( 0.05 );

		BubbleRenderer br = new BubbleRenderer( series );
		br.setAutoscalePointsFactor( -0.15 );
		br.setBubbleAlpha( 0.6 );
		br.setHighlightAlpha( 0.8 );

		lineChart.setSeriesDefaults( series );
		//TODO : ask for details
		/*highlightMouseOver = false;
		highlightMouseDown
		highlightMouseColor*/

		Axis axis = new Axis();
		lineChart.setAxesDefaults( axis );

		String[] makes = new String[] {"Acura", "Alfa Romeo", "AM General", "Aston Martin Lagonda Ltd.", "Audi", "BMW", "Bugatti", "Buick", "Cadillac", "Chevrolet", "Citroen", "DaimlerChrysler Corporation", "Daewoo Motor Co.", "Delorean Motor Company", "Dodge", "Ferrari", "Fiat", "Ford Motor Company", "General Motors", "GMC", "Holden", "Honda", "Hummer", "Hyundai", "Infiniti", "Isuzu", "Jaguar Cars", "Jeep", "Jensen Motors", "Kia", "Laforza", "Lamborghini", "Lancia", "Land Rover", "Lincoln", "Lotus Cars", "Lexus", "Maserati", "Mazda", "Mercedes-Benz", "Mercury", "MG", "Minelli", "The Mini Cooper", "Mistubishi", "Morgan Motor Co.", "Mosler Automotive", "Nissan", "NUMMI ", "Oldsmobile", "Opel", "Packard", "Panoz", "Peugeot", "Pontiac", "Porsche", "Proton", "PSA Peugeot Citroen", "Renault", "Rolls-Royce", "Rover Cars", "Saab", "Saturn", "Shelby American", "Skoda", "Spectre Cars", "Studebaker Motor Company", "Subaru", "Suzuki", "Th!nk", "Toyota", "Toyota Motor Manufacturing - Georgetown, KY", "TVR", "Vauxhall", "Volkswagen", "Volvo", "Zimmer Motor Cars"};

		JQPlotDataSet line1 = new JQPlotDataSet();
		val r = ThreadLocalRandom.current();
		//TODO DataHLC is not a good name as it can used here.
		for( int i =0; i< 7; i++ )
			line1.add( new DataHLC(r.nextInt( 50 ), r.nextInt( 150 ), 400+r.nextInt(900), makes[i]) );
		lineChart.add( line1 );
	}//met
}//class