package net.sf.click.chart.flot;

import net.sf.click.chart.ChartOptions;
import net.sf.click.chart.Color;
import net.sf.click.chart.DataSet;


public class FlotDataSeries extends DataSet
{
	private static final long serialVersionUID = 1L;
	private String label;
	private Color color;
	protected ChartOptions options = new FlotChartOptions();

	private LinesOptions lines;
	private PointsOptions points;
	private BarsOptions bars;

	public FlotDataSeries(){
	}//met

	public FlotDataSeries( String label )
	{
		setLabel( label );
	}//met

	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
		options.add( "label", label );
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
		options.add( "color", color );
	}

	public LinesOptions getLines() {
		if( lines == null )
			setLines( new LinesOptions() );
		return lines;
	}

	public void setLines(LinesOptions lines) {
		this.lines = lines;
		options.add( "lines", lines );
	}

	public BarsOptions getBars() {
		if( bars == null )
			setBars( new BarsOptions() );
		return bars;
	}

	public void setBars(BarsOptions bars) {
		this.bars = bars;
		options.add( "bars", bars );
	}

	public PointsOptions getPoints() {
		if( points == null )
			setPoints( new PointsOptions() );
		return points;
	}

	public void setPoints(PointsOptions points) {
		this.points = points;
		options.add( "points", points );
	}
	@Override
	public String render()
	{
		options.add( "data", this );
		return options.render( 0 );
	}//met

}//met