package net.sf.click.chart.jqplot.renderer;

import net.sf.click.chart.jqplot.Axis;
import net.sf.click.chart.jqplot.plugins.Plugin;

import java.util.ArrayList;
import java.util.List;

/**
 * A renderer for Categories axies. The renderer can be customized through its options.
 * @author steff
 * http://www.jqplot.com/docs/files/plugins/jqplot-categoryAxisRenderer-js.html
 */
public class CategoryAxisRenderer extends AxisRenderer implements Plugin {

	public CategoryAxisRenderer() {
		this( null );
	}//cons

	public CategoryAxisRenderer( Axis axis ) {
		super( "$.jqplot.CategoryAxisRenderer",axis );
	}//cons

	@Override
	public List<String> getListJavaScriptImport() {
		List<String> listJavaScriptImport = new ArrayList<String>();
		//javascript imports
		listJavaScriptImport.add("/jqplot/plugins/jqplot.categoryAxisRenderer.js");
		return listJavaScriptImport;
	}

	/** A class of a rendering engine for creating the ticks labels displayed on the plot, See $.jqplot.AxisTickRenderer. */
	private Renderer tickRenderer = new AxisTickRenderer();

	public Renderer getTickRenderer() {
		return tickRenderer;
	}//met

	public void setTickRenderer(Renderer tickRenderer) {
		this.tickRenderer = tickRenderer;
		// TODO : I think this is wrong but should be right
		options.add( "tickRenderer", tickRenderer, "tickRendererOptions" );
	}//met
}//class