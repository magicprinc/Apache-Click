package net.sf.click.chart.jqplot.renderer;

import net.sf.click.chart.jqplot.Grid;

public class GridRenderer extends AbstractRenderer {

	public GridRenderer(String name) {
		super(name);
	}

	/**
	 * Handy shortcut to build a renderer and pass it 
	 * directly to a serie.
	 * @param name the name of the renderer.
	 * @param g the grid that the renderer will be the renderer of.
	 */
	public GridRenderer(String name, Grid g) {
		super(name);
		if( g != null )
			g.setRenderer( this );
	}
}
