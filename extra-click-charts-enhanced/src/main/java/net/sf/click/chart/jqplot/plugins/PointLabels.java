package net.sf.click.chart.jqplot.plugins;

import net.sf.click.chart.ChartOptions;
import net.sf.click.chart.jqplot.Legend.Compass;

import java.util.ArrayList;
import java.util.List;

/**
 * Plugin for putting labels at the data points.
 * @author steff
 * http://www.jqplot.com/docs/files/plugins/jqplot-pointLabels-js.html
 */
public class PointLabels extends ChartOptions implements ChartPlugin, SeriesPlugin {

	private List<String> listJavaScriptImport = new ArrayList<String>();

	public PointLabels() {

		//javascript imports
		listJavaScriptImport.add("/jqplot/plugins/jqplot.pointLabels.js");
	}//cons

	@Override
	public String getName()
	{
		return "pointLabels";
	}//met

	@Override
	public List<String> getListJavaScriptImport() {
		return listJavaScriptImport;
	}//met

	/** show the labels or not.*/
	private boolean show = true;
	/** compass location where to position the label around the point.*/
	private Compass location = Compass.NORTH;
	/** true to use labels within data point arrays.*/
	private boolean labelsFromSeries = false;
	/** array index for location of labels within data point arrays.*/
	private String seriesLabelIndex = null;
	/** */
	private String[] labels = null;
	/** true to display value as stacked in a stacked plot.*/
	private boolean stackedValue = false;
	/** vertical padding in pixels between point and label*/
	private int ypadding = 6;
	/** */
	private int xpadding = 6;
	/** true to escape html entities in the labels.*/
	private boolean escapeHTML = true;
	/** Number of pixels that the label must be away from an axis boundary in order to be drawn.*/
	private int edgeTolerance = 0;
	/** true to not show a label for a value which is 0.*/
	private boolean hideZeros = false;

	public boolean isShow() {
		return show;
	}

	public void setShow(boolean show) {
		this.show = show;
		add( "show", show );
	}

	public Compass getLocation() {
		return location;
	}

	public void setLocation(Compass location) {
		this.location = location;
		add( "location", location.toString() );
	}

	public boolean isLabelsFromSeries() {
		return labelsFromSeries;
	}

	public void setLabelsFromSeries(boolean labelsFromSeries) {
		this.labelsFromSeries = labelsFromSeries;
		add( "labelsFromSeries", labelsFromSeries );
	}

	public String getSeriesLabelIndex() {
		return seriesLabelIndex;
	}

	public void setSeriesLabelIndex(String seriesLabelIndex) {
		this.seriesLabelIndex = seriesLabelIndex;
		add( "seriesLabelIndex", seriesLabelIndex );
	}

	public String[] getLabels() {
		return labels;
	}

	public void setLabels(String[] labels) {
		this.labels = labels;
		add( "labels", labels );
	}

	public boolean isStackedValue() {
		return stackedValue;
	}

	public void setStackedValue(boolean stackedValue) {
		this.stackedValue = stackedValue;
		add( "stackedValue", stackedValue );
	}

	public int getYpadding() {
		return ypadding;
	}

	public void setYpadding(int ypadding) {
		this.ypadding = ypadding;
		add( "ypadding", ypadding );
	}

	public int getXpadding() {
		return xpadding;
	}

	public void setXpadding(int xpadding) {
		this.xpadding = xpadding;
		add( "xpadding", xpadding );
	}

	public boolean isEscapeHTML() {
		return escapeHTML;
	}

	public void setEscapeHTML(boolean escapeHTML) {
		this.escapeHTML = escapeHTML;
		add( "escapeHTML", escapeHTML );
	}

	public int getEdgeTolerance() {
		return edgeTolerance;
	}

	public void setEdgeTolerance(int edgeTolerance) {
		this.edgeTolerance = edgeTolerance;
		add( "edgeTolerance", edgeTolerance );
	}

	public boolean isHideZeros() {
		return hideZeros;
	}

	public void setHideZeros(boolean hideZeros) {
		this.hideZeros = hideZeros;
		add( "hideZeros", hideZeros );
	}
}//class