package net.sf.click.chart.jqplot.renderer;

import net.sf.click.chart.jqplot.Series;

public class SeriesRenderer extends AbstractRenderer {

	/**
	 * Handy shortcut to build a renderer and pass it 
	 * directly to a serie.
	 * @param name the name of the renderer.
	 * @param s the series that the renderer will be the renderer of.
	 */
	public SeriesRenderer(String name, Series s) {
		super(name);
		if( s!= null )
			s.setRenderer( this );
	}
}
