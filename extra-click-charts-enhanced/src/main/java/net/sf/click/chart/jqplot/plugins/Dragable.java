package net.sf.click.chart.jqplot.plugins;

import net.sf.click.chart.ChartOptions;
import net.sf.click.chart.Color;
import net.sf.click.chart.jqplot.plugins.Cursor.ConstrainZoom;

import java.util.ArrayList;
import java.util.List;

/**
 * Plugin which will automatically compute and draw trendlines for plotted data.
 * @author steff
 * http://www.jqplot.com/docs/files/plugins/jqplot-trendline-js.html
 */
public class Dragable extends ChartOptions implements ChartPlugin {

	private List<String> listJavaScriptImport = new ArrayList<String>();

	public Dragable() {

		//javascript imports
		listJavaScriptImport.add("/jqplot/plugins/jqplot.dragable.js");
	}//cons

	@Override
	public String getName()
	{
		return "dragable";
	}//met

	@Override
	public List<String> getListJavaScriptImport() {
		return listJavaScriptImport;
	}//met

	/** Wether or not to show the trend line.*/
	private boolean show = true;
	/** CSS color spec for the dragged point (and adjacent line segment or bar).*/
	private Color color = null;
	/** Constrain dragging motion to an axis or to none. .*/
	private ConstrainZoom constrainTo = ConstrainZoom.NONE;

	public boolean isShow() {
		return show;
	}//met
	public void setShow(boolean show) {
		this.show = show;
		add( "show", show );
	}//met
	public Color getColor() {
		return color;
	}//met
	public void setColor(Color color) {
		this.color = color;
		add( "color", color );
	}//met
	public ConstrainZoom getConstrainZoom() {
		return constrainTo;
	}//met
	public void setConstrainZoom(ConstrainZoom renderer) {
		this.constrainTo = renderer;
		add( "constrainTo", constrainTo.toString() );
	}//met
}//class