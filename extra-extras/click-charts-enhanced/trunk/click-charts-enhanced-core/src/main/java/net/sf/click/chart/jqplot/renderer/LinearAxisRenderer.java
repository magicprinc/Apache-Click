package net.sf.click.chart.jqplot.renderer;

import net.sf.click.chart.jqplot.Axis;

/**
 * A renderer for Axis Ticks. The renderer can be customized through its options.
 * @author steff
 * http://www.jqplot.com/docs/files/jqplot-linearAxisRenderer-js.html
 */
public class LinearAxisRenderer extends AxisRenderer {

	public LinearAxisRenderer() {
		this( null );
	}//cons 
	
	public LinearAxisRenderer( Axis axis ) {
		super( "$.jqplot.LineRenderer", axis );
	}//cons
}//class
