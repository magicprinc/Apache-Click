package net.sf.click.chart.jqplot;

import net.sf.click.chart.Data;
import net.sf.click.chart.jqplot.renderer.BarRenderer;
import net.sf.click.chart.jqplot.renderer.CategoryAxisRenderer;



/**
 * This class is an easy-tu-use wrapper to get a bar chart using
 * jqplot. It allows fast creation and rendering of a bar chart,
 * the data model is simple : simply add data through
 * the method add( String, double ) or the overloaded
 * add( String, double, Color ) to customize the color used
 * a given slice.
 * @author steff
 *
 */
public class BarChart extends JQPlotChart{
    private static final long serialVersionUID = 3351284859908519781L;

	private final Series series = new Series();
	private final BarRenderer br = new BarRenderer( series );

	private final Axis xaxis = new Axis();
	private final CategoryAxisRenderer car = new CategoryAxisRenderer( xaxis );
	private ChartDataProvider dataProvider = null;

	public BarChart( )
	{
		this( "", "" );
	}
	public BarChart(String name) {
		this( name, "" );
	}

	public BarChart(String name, String label) {
		super(name, label);

		setLabel( label );
		setSeriesDefaults( series );
		getLegend().setShow( true );
		getAxes().setXaxis(xaxis);
	}//cons

	@Override
	public void setLabel( String label )
	{
		super.setLabel( label );
		if( getTitle() != null )
			getTitle().setText( label );
	}//met

	@Override
	public boolean onProcess() {

		if( dataProvider == null )
			throw new RuntimeException( "DataProvider has not been set." );

		for( int serieIndex = 0; serieIndex < dataProvider.getSeriesCount(); serieIndex ++)
		{
			Series s = new Series( dataProvider.getSeriesName(serieIndex) );
			super.add( s );
		}//for

		String[] ticks = new String[ dataProvider.getXTickLabelCount() ];
		for( int x =0; x < ticks.length; x ++ )
			ticks[ x ] = dataProvider.getXTickLabel( x );
		xaxis.setTicks( ticks );

		for( int serieIndex = 0; serieIndex < dataProvider.getSeriesCount(); serieIndex ++)
		{
			JQPlotDataSet line = new JQPlotDataSet();
			for( int x =0; x < ticks.length; x ++ )
			{
			 line.add( new Data( x, dataProvider.getYValue(serieIndex, x)) );
			}//for
			add( line );
		}//for
		return super.onProcess();
	}//met

	/**
	 * Use a dataProvider instead.
	 */
	@Override	@Deprecated
	public void add(Series s) {
		super.add(s);
	}//met

	public void setDataProvider(ChartDataProvider dataProvider) {
		this.dataProvider = dataProvider;
	}

	public ChartDataProvider getDataProvider() {
		return dataProvider;
	}

	//delegates around the bar renderer
	public void setBarPadding(int barPadding) {
		br.setBarPadding(barPadding);
	}
	public void setBarMargin(int barMargin) {
		br.setBarMargin(barMargin);
	}
	public void setBarWidth(Integer barWidth) {
		br.setBarWidth(barWidth);
	}
	public void setShadowOffset(double shadowOffset) {
		br.setShadowOffset(shadowOffset);
	}
	public void setShadowDepth(int shadowDepth) {
		br.setShadowDepth(shadowDepth);
	}
	public void setShadowAlpha(double shadowAlpha) {
		br.setShadowAlpha(shadowAlpha);
	}
	public void setWaterfall(boolean waterfall) {
		br.setWaterfall(waterfall);
	}
	public void setVaryBarColor(boolean varyBarColor) {
		br.setVaryBarColor(varyBarColor);
	}

}//class