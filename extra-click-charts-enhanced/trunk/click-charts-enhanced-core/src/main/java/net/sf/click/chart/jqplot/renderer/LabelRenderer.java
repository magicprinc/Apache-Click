package net.sf.click.chart.jqplot.renderer;

import net.sf.click.chart.jqplot.Axis;

//TODO it is not clear if they are options of the renderer or the axis itself or both
//donut,meter gauge and funnel seem to be coded in a different model where tick renderers are options of the axis renderer and not the axis itself as for usual core renderers.
public class LabelRenderer extends AbstractRenderer {

	public LabelRenderer(String name) {
		super(name);
	}

	/**
	 * Handy shortcut to build a renderer and pass it 
	 * directly to a serie.
	 * @param name the name of the renderer.
	 * @param s the series that the renderer will be the renderer of.
	 */
	public LabelRenderer(String name, Axis x) {
		super(name);
		if( x!= null )
			x.setLabelRenderer( this );
	}
	
}
