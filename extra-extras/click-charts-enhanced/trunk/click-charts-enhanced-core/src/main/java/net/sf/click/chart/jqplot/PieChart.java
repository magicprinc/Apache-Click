package net.sf.click.chart.jqplot;

import net.sf.click.chart.Data;
import net.sf.click.chart.jqplot.Legend.Compass;
import net.sf.click.chart.jqplot.renderer.PieRenderer;

/**
 * This class is an easy-tu-use wrapper to get a pie chart using
 * jqplot. It allows fast creation and rendering of a pie chart,
 * the data model is simple : simply data through 
 * the method add( String, double ) or the overloaded 
 * add( String, double, Color ) to customize the color used 
 * a given slice.
 * @author steff
 *
 */
public class PieChart extends JQPlotChart{

	Series series = new Series(); 
	PieRenderer pr = new PieRenderer( series );
	JQPlotDataSet data = new JQPlotDataSet();

	public PieChart( )
	{
		this( "", "" );
	}
	public PieChart(String name) {
		this( name, "" );
	}

	public PieChart(String name, String label) {
		super(name, label);

		setSeriesDefaults( series );
		pr.setSliceMargin( 8 );
		getTitle().setText( label );

		getLegend().setShow( true );
		getLegend().setLocation( Compass.NORTH_EAST );
		add( data );
	}//cons
	
	@Override
	public void setLabel( String label )
	{
		super.setLabel( label );
		if( getTitle() != null )
			getTitle().setText( label );
	}//met

	public void add( String sliceName, double sliceValue)
	{
		data.add( new Data<String, Double>( sliceName, sliceValue ) );
	}//met
	
	public void add( String sliceName, int sliceValue)
	{
		data.add( new Data<String, Integer>( sliceName, sliceValue ) );
	}//met
	
	//delegate to pie renderer
	public void setDiameter(Double diameter) {
		pr.setDiameter(diameter);
	}//met
	public void setPadding(int padding) {
		pr.setPadding(padding);
	}//met
	public void setSliceMargin(int sliceMargin) {
		pr.setSliceMargin(sliceMargin);
	}//met
	public void setFill(boolean fill) {
		pr.setFill(fill);
	}//met
	public void setShadow(boolean shadow) {
		pr.setShadow(shadow);
	}//met
	public void setShadowOffset(double shadowOffset) {
		pr.setShadowOffset(shadowOffset);
	}//met
	public void setShadowDepth(int shadowDepth) {
		pr.setShadowDepth(shadowDepth);
	}//met
	public void setShadowAlpha(double shadowAlpha) {
		pr.setShadowAlpha(shadowAlpha);
	}//met
	public void setLineWidth(int lineWidth) {
		pr.setLineWidth(lineWidth);
	}//met

	
}//class
