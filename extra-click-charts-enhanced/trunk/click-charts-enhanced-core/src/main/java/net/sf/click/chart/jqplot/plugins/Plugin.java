package net.sf.click.chart.jqplot.plugins;

import java.util.List;



/*
 * The Plugin interface represents a pluggable code module inside
 * the JQPlot framework. Up to now there are 2 kinds of plugins : 
 * Renderers plugins and ChartOptions plugins.<br>
 * Both plugins provide a name (the internal name of the plugin)
 * and a list of String representing the urls of the javascript source
 * file to import in a web page in ordre for the plugin to work.<BR>
 * @author Steff
 * @see net.sf.click.chart.jqplot.renderer.Renderer
 * @see net.sf.click.chart.jqplot.ChartOptions 
 */
public interface Plugin {

	/**
	 * The internal name of the plugin. Renderers provide 
	 * a javascript prototype constructor name. ChartOptions provide
	 * a prototype property name to be added to an object (generally the 
	 * main JQPlot ChartOptions)
	 * @return the internal name of the plugin.
	 */
	public String getName();
	
	/** 
	 * Provide the list of names of javascript files to import.
	 * The names provided are relative to the application contexte
	 * path. 
	 * @return the list of names of javascript files to import.
	 */
	public List<String> getListJavaScriptImport();

}//class
