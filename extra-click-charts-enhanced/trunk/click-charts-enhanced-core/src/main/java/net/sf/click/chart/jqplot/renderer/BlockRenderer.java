package net.sf.click.chart.jqplot.renderer;

import java.util.ArrayList;
import java.util.List;

import net.sf.click.chart.jqplot.Series;
import net.sf.click.chart.jqplot.plugins.Plugin;

/**
 * 
 * @author steff
 * 
 */
public class BlockRenderer extends SeriesRenderer implements Plugin {

	public BlockRenderer() {
		this( null );
	}//cons
	
	public BlockRenderer( Series s) {
		super( "$.jqplot.BlockRenderer", s );
	}//cons
	
	@Override
	public List<String> getListJavaScriptImport() {
		List<String> listJavaScriptImport = new ArrayList<String>();
		//javascript imports
		listJavaScriptImport.add("/jqplot/plugins/jqplot.blockRenderer.js");
		return listJavaScriptImport;
	}
	
}//class
