package net.sf.click.chart.jqplot.plugins;

import java.util.ArrayList;
import java.util.List;

import net.sf.click.chart.ChartOptions;
import net.sf.click.chart.jqplot.Legend.Compass;

/**
 * Plugin class representing the cursor as displayed on the plot.
 * @author steff
 * http://www.jqplot.com/docs/files/plugins/jqplot-cursor-js.html
 */
public class Cursor extends ChartOptions implements ChartPlugin {

	private List<String> listJavaScriptImport = new ArrayList<String>();

	public Cursor() {

		//javascript imports
		listJavaScriptImport.add("/jqplot/plugins/jqplot.cursor.js");
	}//cons

	@Override 
	public String getName()
	{
		return "cursor";
	}//met

	@Override
	public List<String> getListJavaScriptImport() {
		return listJavaScriptImport;
	}//met

	public enum ConstrainZoom
	{
		X( "x" ),
		Y( "y" ),
		NONE( "none" );

		private String constrain;
		private ConstrainZoom( String s ) {
			this.constrain = s;			
		}//cons
		@Override
		public String toString() {
			return constrain;
		}//met
	}//enum

	/** CSS spec for cursor style*/
	private String style = "crosshair";
	/** wether to show the cursor or not.*/
	private boolean show = true;	
	/** show a cursor position tooltip near the cursor*/
	private boolean showTooltip = true;
	/** Tooltip follows the mouse, it is not at a fixed location.*/
	private boolean followMouse = false;	
	/** Where to position tooltip.*/
	private Compass tooltipLocation = Compass.SOUTH_EAST;
	/** Pixel offset of tooltip from the grid boudaries or cursor center.*/
	private int tooltipOffset = 6;	
	/** show the grid pixel coordinates of the mouse.*/
	private boolean showTooltipGridPosition = false;
	/** show the unit (data) coordinates of the mouse.*/
	private boolean showTooltipUnitPosition = true;
	/** Used with showVerticalLine to show intersecting data points in the tooltip.*/
	private boolean showTooltipDataPosition = false;	
	/** sprintf format string for the tooltip.*/
	private String tooltipFormatString = "%.4P, %.4P";	
	/** Use the x and y axes formatters to format the text in the tooltip.*/
	private boolean useAxesFormatters = true;	
	/** Show position for the specified axes.*/
	//TODO give it a proper type
	private String tooltipAxisGroups = null;	
	/** Enable plot zooming.*/
	private boolean zoom = false;
	/** Will reset plot zoom if single click on plot without drag.*/
	private boolean clickReset	= false;
	/** Will reset plot zoom if double click on plot without drag.*/
	private boolean dblClickReset = true;	
	/** draw a vertical line across the plot which follows the cursor.*/
	private boolean showVerticalLine = false;	
	/** draw a horizontal line across the plot which follows the cursor.*/
	private boolean showHorizontalLine = false;
	/** ‘none’, ‘x’ or ‘y’*/
	private ConstrainZoom constrainZoomTo = ConstrainZoom.NONE;	
	/** pixel distance from data point or marker to consider cursor lines intersecting with point.*/
	private int intersectionThreshold = 2;
	/** Replace the plot legend with an enhanced legend displaying intersection information.*/
	private boolean showCursorLegend = false;	
	/** Format string used in the cursor legend.*/
	//TODO give it a proper type. Do you have to get a new hierarchy ?
	private String cursorLegendFormatString	= "$.jqplot.Cursor.cursorLegendFormatString";

	public String getStyle() {
		return style;
	}//met

	public void setStyle(String style) {
		this.style = style;
	}//met

	public boolean isShow() {
		return show;
	}//met

	public void setShow(boolean show) {
		this.show = show;
		add( "show", show );
	}//met

	public boolean isShowTooltip() {
		return showTooltip;
	}//met

	public void setShowTooltip(boolean showTooltip) {
		this.showTooltip = showTooltip;
		add( "showTooltip", showTooltip );
	}//met

	public boolean isFollowMouse() {
		return followMouse;
	}//met

	public void setFollowMouse(boolean followMouse) {
		this.followMouse = followMouse;
		add( "followMouse", followMouse );
	}//met

	public Compass getTooltipLocation() {
		return tooltipLocation;
	}//met

	public void setTooltipLocation(Compass tooltipLocation) {
		this.tooltipLocation = tooltipLocation;
		add( "tooltipLocation", tooltipLocation.toString() );
	}//met

	public int getTooltipOffset() {
		return tooltipOffset;
	}//met

	public void setTooltipOffset(int tooltipOffset) {
		this.tooltipOffset = tooltipOffset;
		add( "tooltipOffset", tooltipOffset );
	}//met

	public boolean isShowTooltipGridPosition() {
		return showTooltipGridPosition;
	}//met

	public void setShowTooltipGridPosition(boolean showTooltipGridPosition) {
		this.showTooltipGridPosition = showTooltipGridPosition;
		add( "showTooltipGridPosition", showTooltipGridPosition );
	}//met

	public boolean isShowTooltipUnitPosition() {
		return showTooltipUnitPosition;
	}//met

	public void setShowTooltipUnitPosition(boolean showTooltipUnitPosition) {
		this.showTooltipUnitPosition = showTooltipUnitPosition;
		add( "showTooltipUnitPosition", showTooltipUnitPosition );
	}//met

	public boolean isShowTooltipDataPosition() {
		return showTooltipDataPosition;
	}//met

	public void setShowTooltipDataPosition(boolean showTooltipDataPosition) {
		this.showTooltipDataPosition = showTooltipDataPosition;
		add( "showTooltipDataPosition", showTooltipDataPosition );
	}//met

	public String getTooltipFormatString() {
		return tooltipFormatString;
	}//met

	public void setTooltipFormatString(String tooltipFormatString) {
		this.tooltipFormatString = tooltipFormatString;
		add( "tooltipFormatString", tooltipFormatString );
	}//met

	public boolean isUseAxesFormatters() {
		return useAxesFormatters;
	}//met

	public void setUseAxesFormatters(boolean useAxesFormatters) {
		this.useAxesFormatters = useAxesFormatters;
		add( "useAxesFormatters", useAxesFormatters );
	}//met

	public String getTooltipAxisGroups() {
		return tooltipAxisGroups;
	}//met

	public void setTooltipAxisGroups(String tooltipAxisGroups) {
		this.tooltipAxisGroups = tooltipAxisGroups;
		add( "tooltipAxisGroups", tooltipAxisGroups );
	}//met

	public boolean isZoom() {
		return zoom;
	}//met

	public void setZoom(boolean zoom) {
		this.zoom = zoom;
		add( "zoom", zoom );
	}//met

	public boolean isClickReset() {
		return clickReset;
	}//met

	public void setClickReset(boolean clickReset) {
		this.clickReset = clickReset;
		add( "clickReset", clickReset );
	}//met

	public boolean isDblClickReset() {
		return dblClickReset;
	}//met

	public void setDblClickReset(boolean dblClickReset) {
		this.dblClickReset = dblClickReset;
		add( "dblClickReset", dblClickReset );
	}//met

	public boolean isShowVerticalLine() {
		return showVerticalLine;
	}//met

	public void setShowVerticalLine(boolean showVerticalLine) {
		this.showVerticalLine = showVerticalLine;
		add( "showVerticalLine", showVerticalLine );
	}//met

	public boolean isShowHorizontalLine() {
		return showHorizontalLine;
	}//met

	public void setShowHorizontalLine(boolean showHorizontalLine) {
		this.showHorizontalLine = showHorizontalLine;
		add( "showHorizontalLine", showHorizontalLine );
	}//met

	public ConstrainZoom getConstrainZoomTo() {
		return constrainZoomTo;
	}//met

	public void setConstrainZoomTo(ConstrainZoom constrainZoomTo) {
		this.constrainZoomTo = constrainZoomTo;
		add( "constrainZoomTo", constrainZoomTo.toString() );
	}//met

	public int getIntersectionThreshold() {
		return intersectionThreshold;
	}//met

	public void setIntersectionThreshold(int intersectionThreshold) {
		this.intersectionThreshold = intersectionThreshold;
		add( "intersectionThreshold", intersectionThreshold );
	}//met

	public boolean isShowCursorLegend() {
		return showCursorLegend;
	}//met

	public void setShowCursorLegend(boolean showCursorLegend) {
		this.showCursorLegend = showCursorLegend;
		add( "showCursorLegend", showCursorLegend );
	}//met

	public String getCursorLegendFormatString() {
		return cursorLegendFormatString;
	}//met

	public void setCursorLegendFormatString(String cursorLegendFormatString) {
		this.cursorLegendFormatString = cursorLegendFormatString;
		add( "cursorLegendFormatString", cursorLegendFormatString );
	}//met
}//class
