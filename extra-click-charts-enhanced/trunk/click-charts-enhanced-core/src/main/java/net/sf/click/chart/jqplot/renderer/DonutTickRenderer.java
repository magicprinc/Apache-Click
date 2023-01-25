package net.sf.click.chart.jqplot.renderer;

import java.util.ArrayList;
import java.util.List;

import net.sf.click.chart.jqplot.plugins.Plugin;

/**
 * 
 * @author steff
 * 
 */
public class DonutTickRenderer extends TickRenderer implements Plugin {

	public DonutTickRenderer() {
		super( "$.jqplot.DonutTickRenderer" );
	}//cons
	
	@Override
	public List<String> getListJavaScriptImport() {
		List<String> listJavaScriptImport = new ArrayList<String>();
		//javascript imports
		listJavaScriptImport.add("/jqplot/plugins/jqplot.donutRenderer.js");
		return listJavaScriptImport;
	}
}//class
