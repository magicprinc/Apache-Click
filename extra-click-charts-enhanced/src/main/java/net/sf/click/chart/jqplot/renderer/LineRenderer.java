package net.sf.click.chart.jqplot.renderer;

import net.sf.click.chart.jqplot.Series;

/**
 * 
 * @author steff
 *http://www.jqplot.com/docs/files/jqplot-lineRenderer-js.html
 */
public class LineRenderer extends SeriesRenderer {

	public LineRenderer() {
		this( null );
	}//cons
	
	public LineRenderer( Series s) {
		super( "$.jqplot.LineRenderer", s );
	}//cons
}//class
