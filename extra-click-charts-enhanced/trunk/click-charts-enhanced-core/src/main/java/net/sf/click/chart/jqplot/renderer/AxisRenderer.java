package net.sf.click.chart.jqplot.renderer;

import net.sf.click.chart.jqplot.Axis;

public class AxisRenderer extends AbstractRenderer {

	public AxisRenderer(String name) {
		super(name);
	}

	/**
	 * Handy shortcut to build a renderer and pass it 
	 * directly to a serie.
	 * @param name the name of the renderer.
	 * @param s the series that the renderer will be the renderer of.
	 */
	public AxisRenderer(String name, Axis x) {
		super(name);
		if( x != null )
			x.setRenderer( this );
	}
}
