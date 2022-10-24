package net.sf.click.chart.jqplot.renderer;

import net.sf.click.chart.jqplot.Title;

public class TitleRenderer extends AbstractRenderer {

	public TitleRenderer(String name) {
		super(name);
	}//cons

	/**
	 * Handy shortcut to build a renderer and pass it 
	 * directly to a serie.
	 * @param name the name of the renderer.
	 * @param s the series that the renderer will be the renderer of.
	 */
	public TitleRenderer(String name, Title x) {
		super(name);
		if( x!= null )
			x.setRenderer( this );
	}//cons
}//class
