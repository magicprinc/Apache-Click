package net.sf.click.chart.jqplot.plugins;

import net.sf.click.chart.ChartOptions;
import net.sf.click.chart.jqplot.Legend.Compass;
import net.sf.click.chart.jqplot.renderer.MarkerRenderer;
import net.sf.click.chart.jqplot.renderer.Renderer;

import java.util.ArrayList;
import java.util.List;

/**
 * Plugin which will highlight data points when they are moused over. The renderer can be customized through its options.
 * @author steff
 * http://www.jqplot.com/docs/files/plugins/jqplot-highlighter-js.html#$.jqplot.Highlighter.tooltipFade
 */
public class Highlighter extends ChartOptions implements ChartPlugin {

	private List<String> listJavaScriptImport = new ArrayList<String>();

	public Highlighter() {

		//javascript imports
		listJavaScriptImport.add("/jqplot/plugins/jqplot.highlighter.js");
	}//cons

	@Override
	public String getName()
	{
		return "highlighter";
	}//met

	@Override
	public List<String> getListJavaScriptImport() {
		return listJavaScriptImport;
	}//met

	public enum FadeSpeed
	{
		SLOW( "slow" ),
		FAST( "fast" ),
		DEF( "def" );

		private String speed;
		private FadeSpeed( String s ) {
			this.speed = s;
		}//met//cons
		@Override
		public String toString() {
			return speed;
		}//met
	}//enum

	public enum ToolTipAxes
	{
		X( "x" ),
		Y( "y" ),
		XY( "xy" ),
		YX( "yx" ),
		BOTH( "both" );

		private String axe;
		private ToolTipAxes( String s ) {
			this.axe = s;
		}//cons
		@Override
		public String toString() {
			return axe;
		}//met
	}//enum

	/** true to show the highlight.*/
	private boolean show = true;
	/** Renderer used to draw the marker of the highlighted point.*/
	private Renderer markerRenderer	= new MarkerRenderer();
	/** true to show the marker*/
	private boolean showMarker	= true;
	/** Pixels to add to the lineWidth of the highlight.*/
	double lineWidthAdjust	= 2.5d;
	/** Pixels to add to the overall size of the highlight.*/
	double sizeAdjust = 5;
	/** Show a tooltip with data point values.*/
	boolean showTooltip	= true;
	/** Where to position tooltip, ‘n’, ‘ne’, ‘e’, ‘se’, ‘s’, ‘sw’, ‘w’, ‘nw’*/
	private Compass tooltipLocation	= Compass.NORTH_WEST;
	/** true = fade in/out tooltip, flase = show/hide tooltip*/
	private boolean tooltipFade	= true;
	/** ‘slow’, ‘def’, ‘fast’, or number of milliseconds.*/
	private FadeSpeed tooltipFadeSpeed = FadeSpeed.FAST;
	/** Pixel offset of tooltip from the highlight.*/
	private int tooltipOffset =2;
	/** Which axes to display in tooltip, ‘x’, ‘y’ or ‘both’, ‘xy’ or ‘yx’ ‘both’ and ‘xy’ are equivalent, ‘yx’ reverses order of labels.*/
	private ToolTipAxes tooltipAxes	= ToolTipAxes.BOTH;
	/** Use the x and y axes formatters to format the text in the tooltip.*/
	private boolean useAxesFormatters = true;
	/** sprintf format string for the tooltip.*/
	private String tooltipFormatString	= "%.5P";
	/** alternative to tooltipFormatString will format the whole tooltip text, populating with x, y values as indicated by tooltipAxes option.*/
	private String formatString  = null;
	/** Number of y values to expect in the data point array.*/
	private int yvalues =1;


	public boolean isShow() {
		return show;
	}//met
	public void setShow(boolean show) {
		this.show = show;
		add( "show", show );
	}//met
	public Renderer getMarkerRenderer() {
		return markerRenderer;
	}//met
	public void setMarkerRenderer(Renderer markerRenderer) {
		this.markerRenderer = markerRenderer;
		//warning options don't seem to be used.
		add( "markerRenderer", markerRenderer,"markerRendererOptions" );
	}//met
	public boolean isShowMarker() {
		return showMarker;
	}//met
	public void setShowMarker(boolean showMarker) {
		this.showMarker = showMarker;
		add( "showMarker", showMarker );
	}//met
	public double getLineWidthAdjust() {
		return lineWidthAdjust;
	}//met
	public void setLineWidthAdjust(double lineWidthAdjust) {
		this.lineWidthAdjust = lineWidthAdjust;
		add( "lineWidthAdjust", lineWidthAdjust );
	}//met
	public double getSizeAdjust() {
		return sizeAdjust;
	}//met
	public void setSizeAdjust(double sizeAdjust) {
		this.sizeAdjust = sizeAdjust;
		add( "sizeAdjust", sizeAdjust );
	}//met
	public boolean isShowTooltip() {
		return showTooltip;
	}//met
	public void setShowTooltip(boolean showTooltip) {
		this.showTooltip = showTooltip;
		add( "showTooltip", showTooltip );
	}//met
	public Compass getTooltipLocation() {
		return tooltipLocation;
	}//met
	public void setTooltipLocation(Compass tooltipLocation) {
		this.tooltipLocation = tooltipLocation;
		add( "tooltipLocation", tooltipLocation.toString() );
	}//met
	public boolean isTooltipFade() {
		return tooltipFade;
	}//met
	public void setTooltipFade(boolean tooltipFade) {
		this.tooltipFade = tooltipFade;
		add( "tooltipFade", tooltipFade );
	}//met
	public FadeSpeed getTooltipFadeSpeed() {
		return tooltipFadeSpeed;
	}//met
	public void setTooltipFadeSpeed(FadeSpeed tooltipFadeSpeed) {
		this.tooltipFadeSpeed = tooltipFadeSpeed;
		add( "tooltipFadeSpeed", tooltipFadeSpeed.toString() );
	}//met
	public int getTooltipOffset() {
		return tooltipOffset;
	}//met
	public void setTooltipOffset(int tooltipOffset) {
		this.tooltipOffset = tooltipOffset;
		add( "tooltipOffset", tooltipOffset );
	}//met
	public ToolTipAxes getTooltipAxes() {
		return tooltipAxes;
	}//met
	public void setTooltipAxes(ToolTipAxes tooltipAxes) {
		this.tooltipAxes = tooltipAxes;
		add( "tooltipAxes", tooltipAxes.toString() );
	}//met
	public boolean isUseAxesFormatters() {
		return useAxesFormatters;
	}//met
	public void setUseAxesFormatters(boolean useAxesFormatters) {
		this.useAxesFormatters = useAxesFormatters;
		add( "useAxesFormatters", useAxesFormatters );
	}//met
	public String getTooltipFormatString() {
		return tooltipFormatString;
	}//met
	public void setTooltipFormatString(String tooltipFormatString) {
		this.tooltipFormatString = tooltipFormatString;
		add( "tooltipFormatString", tooltipFormatString );
	}//met
	public String getFormatString() {
		return formatString;
	}//met
	public void setFormatString(String formatString) {
		this.formatString = formatString;
		add( "formatString", formatString );
	}//met
	public int getYvalues() {
		return yvalues;
	}//met
	public void setYvalues(int yvalues) {
		this.yvalues = yvalues;
		add( "yvalues", yvalues );
	}//met
}//class