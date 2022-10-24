package net.sf.click.chart.jqplot.renderer;

import java.util.ArrayList;
import java.util.List;

import net.sf.click.chart.jqplot.plugins.Plugin;

/**
 * A renderer for Categories axies. The renderer can be customized through its options.
 * @author steff
 * http://www.jqplot.com/docs/files/plugins/jqplot-canvasAxisTickRenderer-js.html
 */
public class MeterGaugeTickRenderer extends TickRenderer implements Plugin {

	public MeterGaugeTickRenderer() {
		super( "$.jqplot.MeterGaugeTickRenderer" );
	}//cons
	
	@Override
	public List<String> getListJavaScriptImport() {
		List<String> listJavaScriptImport = new ArrayList<String>();
		//javascript imports
		listJavaScriptImport.add("/jqplot/plugins/jqplot.meterGaugeRenderer.js");
		return listJavaScriptImport;
	}
}//class
