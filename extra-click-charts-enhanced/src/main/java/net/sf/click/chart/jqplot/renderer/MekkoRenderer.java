package net.sf.click.chart.jqplot.renderer;

import net.sf.click.chart.jqplot.Series;
import net.sf.click.chart.jqplot.plugins.Plugin;

import java.util.ArrayList;
import java.util.List;

/**
 * Render for Mekk charts.
 * @author steff
 * http://www.jqplot.com/docs/files/plugins/jqplot-mekkoRenderer-js.html
 */
public class MekkoRenderer extends SeriesRenderer implements Plugin {

	public MekkoRenderer() {
		this( null );
	}//cons

	public MekkoRenderer( Series s) {
		super( "$.jqplot.MekkoRenderer", s );
	}//cons

	@Override
	public List<String> getListJavaScriptImport() {
		List<String> listJavaScriptImport = new ArrayList<String>();
		//javascript imports
		listJavaScriptImport.add("/jqplot/plugins/jqplot.mekkoRenderer.js");
		return listJavaScriptImport;
	}

}//class