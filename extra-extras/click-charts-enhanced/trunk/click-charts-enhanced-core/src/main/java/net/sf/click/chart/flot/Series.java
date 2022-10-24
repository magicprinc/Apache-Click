package net.sf.click.chart.flot;


public class Series extends FlotChartOptions {
	private LinesOptions lines;
	private PointsOptions points;
	private BarsOptions bars;
	private PiesOptions pies;
	
	public LinesOptions getLines() {
		if( lines == null )
			setLines( new LinesOptions() );
		return lines;
	}
	
	public void setLines(LinesOptions lines) {
		this.lines = lines;
		add( "lines", lines );
	}

	public BarsOptions getBars() {
		if( bars == null )
			setBars( new BarsOptions() );
		return bars;
	}

	public void setBars(BarsOptions bars) {
		this.bars = bars;
		add( "bars", bars );
	}
	
	public PointsOptions getPoints() {
		if( points == null )
			setPoints( new PointsOptions() );
		return points;
	}

	public void setPoints(PointsOptions points) {
		this.points = points;
		add( "points", points );
	}

	public PiesOptions getPies() {
		if( pies == null )
			setPies( new PiesOptions() );
		return pies;
	}

	public void setPies(PiesOptions pies) {
		this.pies = pies;
		add( "pies", pies );
	}
	
}//class
