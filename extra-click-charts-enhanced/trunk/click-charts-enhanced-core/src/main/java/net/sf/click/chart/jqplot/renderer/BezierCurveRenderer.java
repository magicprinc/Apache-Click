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
public class BezierCurveRenderer extends SeriesRenderer implements Plugin {

	private List<String> listJavaScriptImport = new ArrayList<String>();
	
	public BezierCurveRenderer() {
		this( null );
	}//cons
	
	public BezierCurveRenderer( Series s ) {
		super( "$.jqplot.BezierCurveRenderer", s);
		
		//javascript imports
		listJavaScriptImport.add("/jqplot/plugins/jqplot.bezierCurveRenderer.js");
	}//cons

	@Override
	public List<String> getListJavaScriptImport() {
		return listJavaScriptImport;
	}
}//class
