package net.sf.click.chart.jqplot.renderer;

import java.util.ArrayList;
import java.util.List;

import net.sf.click.chart.Color;
import net.sf.click.chart.jqplot.Series;
import net.sf.click.chart.jqplot.plugins.Plugin;
import net.sf.click.chart.jqplot.renderer.FunnelRenderer.DataLabel;

/**
 * 
 * @author steff
 * 
 */
public class DonutRenderer extends SeriesRenderer implements Plugin {

	public DonutRenderer() {
		this( null );
	}//cons
	
	public DonutRenderer( Series s) {
		super( "$.jqplot.DonutRenderer", s );
	}//cons


	@Override
	public List<String> getListJavaScriptImport() {
		List<String> listJavaScriptImport = new ArrayList<String>();
		//javascript imports
		listJavaScriptImport.add("/jqplot/plugins/jqplot.donutRenderer.js");
		return listJavaScriptImport;
	}

	/** Outer diameter of the donut, auto computed by default*/
	private Double diameter = null;
	/** Inner diameter of teh donut, auto calculated by default.
	 * If specified will override thickness value.*/
	private Double innerDiameter = null;
	/** thickness of the donut, auto computed by default
	 * Overridden by if innerDiameter is specified.*/
	private Double thickness = null;
	/** padding between the donut and plot edges, legend, etc.*/
	private int padding = 20;
	/** angular spacing between donut slices in degrees.*/
	private int sliceMargin = 0;
	/** pixel distance between rings, or multiple series in a donut plot.
	 * null will compute ringMargin based on sliceMargin.*/
	private Integer ringMargin = null;
	/** prop: fill
	 * true or false, wether to fil the slices.*/
	private boolean fill = true;
	/** offset of the shadow from the slice and offset of 
	 * each succesive stroke of the shadow from the last.*/
	private int shadowOffset = 2;
	/** transparency of the shadow (0 = transparent, 1 = opaque)*/
	private double shadowAlpha = 0.07;
	/** number of strokes to apply to the shadow, 
	 * each stroke offset shadowOffset from the last.*/
	private int shadowDepth = 5;
	/** True to highlight slice when moused over.
	 * This must be false to enable highlightMouseDown to highlight when clicking on a slice.*/
	private boolean highlightMouseOver = true;
	/** True to highlight when a mouse button is pressed over a slice.
	 * This will be disabled if highlightMouseOver is true.*/
	private boolean highlightMouseDown = false;
	/** an array of colors to use when highlighting a slice.*/
	private Color[] highlightColors = new Color[0];
	/** Either 'label', 'value', 'percent' or an array of labels to place on the pie slices.
	 * Defaults to percentage of each pie slice.*/
	private DataLabel dataLabels = DataLabel.PERCENT;
	/** true to show data labels on slices.*/
	private boolean showDataLabels = false;
	/** Format string for data labels.  If none, '%s' is used for "label" and for arrays, '%d' for value and '%d%%' for percentage.*/
	private String dataLabelFormatString = null;
	/** Threshhold in percentage (0 - 100) of pie area, below which no label will be displayed.
	 * This applies to all label types, not just to percentage labels.*/
	private int dataLabelThreshold = 3;
	/** A Multiplier (0-1) of the pie radius which controls position of label on slice.
	 * Increasing will slide label toward edge of pie, decreasing will slide label toward center of pie.*/
	private double dataLabelPositionFactor = 0.5;
	/** Number of pixels to slide the label away from (+) or toward (-) the center of the pie.*/
	private int dataLabelNudge = 0;
	/** Angle to start drawing donut in degrees.  
	 * According to orientation of canvas coordinate system:
	 * 0 = on the positive x axis
	 * -90 = on the positive y axis.
	 * 90 = on the negaive y axis.
	 * 180 or - 180 = on the negative x axis.*/
	private double startAngle = 0;
	private DonutTickRenderer tickRenderer = new DonutTickRenderer();
	/** Used as check for conditions where donut shouldn't be drawn.*/
	private boolean _drawData = true;
	public Double getDiameter() {
		return diameter;
	}
	public void setDiameter(Double diameter) {
		this.diameter = diameter;
		options.add( "diameter", diameter );
	}
	public Double getInnerDiameter() {
		return innerDiameter;
	}
	public void setInnerDiameter(Double innerDiameter) {
		this.innerDiameter = innerDiameter;
		options.add( "innerDiameter", innerDiameter );
	}
	public Double getThickness() {
		return thickness;
	}
	public void setThickness(Double thickness) {
		this.thickness = thickness;
		options.add( "thickness", thickness );
	}
	public int getPadding() {
		return padding;
	}
	public void setPadding(int padding) {
		this.padding = padding;
		options.add( "padding", padding );
	}
	public int getSliceMargin() {
		return sliceMargin;
	}
	public void setSliceMargin(int sliceMargin) {
		this.sliceMargin = sliceMargin;
		options.add( "sliceMargin", sliceMargin );
	}
	public Integer getRingMargin() {
		return ringMargin;
	}
	public void setRingMargin(Integer ringMargin) {
		this.ringMargin = ringMargin;
		options.add( "ringMargin", ringMargin );
	}
	public boolean isFill() {
		return fill;
	}
	public void setFill(boolean fill) {
		this.fill = fill;
		options.add( "fill", fill );
	}
	public int getShadowOffset() {
		return shadowOffset;
	}
	public void setShadowOffset(int shadowOffset) {
		this.shadowOffset = shadowOffset;
		options.add( "shadowOffset", shadowOffset );
	}
	public double getShadowAlpha() {
		return shadowAlpha;
	}
	public void setShadowAlpha(double shadowAlpha) {
		this.shadowAlpha = shadowAlpha;
		options.add( "shadowAlpha", shadowAlpha );
	}
	public int getShadowDepth() {
		return shadowDepth;
	}
	public void setShadowDepth(int shadowDepth) {
		this.shadowDepth = shadowDepth;
		options.add( "shadowDepth", shadowDepth );
	}
	public boolean isHighlightMouseOver() {
		return highlightMouseOver;
	}
	public void setHighlightMouseOver(boolean highlightMouseOver) {
		this.highlightMouseOver = highlightMouseOver;
		options.add( "highlightMouseOver", highlightMouseOver );
	}
	public boolean isHighlightMouseDown() {
		return highlightMouseDown;
	}
	public void setHighlightMouseDown(boolean highlightMouseDown) {
		this.highlightMouseDown = highlightMouseDown;
		options.add( "highlightMouseDown", highlightMouseDown );
	}
	public Color[] getHighlightColors() {
		return highlightColors;
	}
	public void setHighlightColors(Color[] highlightColors) {
		this.highlightColors = highlightColors;
		options.add( "highlightColors", highlightColors );
	}
	public DataLabel getDataLabels() {
		return dataLabels;
	}
	public void setDataLabels(DataLabel dataLabels) {
		this.dataLabels = dataLabels;
		options.add( "dataLabels", dataLabels.toString() );
	}
	public boolean isShowDataLabels() {
		return showDataLabels;
	}
	public void setShowDataLabels(boolean showDataLabels) {
		this.showDataLabels = showDataLabels;
		options.add( "showDataLabels", showDataLabels );
	}
	public String getDataLabelFormatString() {
		return dataLabelFormatString;
	}
	public void setDataLabelFormatString(String dataLabelFormatString) {
		this.dataLabelFormatString = dataLabelFormatString;
		options.add( "dataLabelFormatString", dataLabelFormatString );
	}
	public int getDataLabelThreshold() {
		return dataLabelThreshold;
	}
	public void setDataLabelThreshold(int dataLabelThreshold) {
		this.dataLabelThreshold = dataLabelThreshold;
		options.add( "dataLabelThreshold", dataLabelThreshold );
	}
	public double getDataLabelPositionFactor() {
		return dataLabelPositionFactor;
	}
	public void setDataLabelPositionFactor(double dataLabelPositionFactor) {
		this.dataLabelPositionFactor = dataLabelPositionFactor;
		options.add( "dataLabelPositionFactor", dataLabelPositionFactor );
	}
	public int getDataLabelNudge() {
		return dataLabelNudge;
	}
	public void setDataLabelNudge(int dataLabelNudge) {
		this.dataLabelNudge = dataLabelNudge;
		options.add( "dataLabelNudge", dataLabelNudge );
	}
	public double getStartAngle() {
		return startAngle;
	}
	public void setStartAngle(double startAngle) {
		this.startAngle = startAngle;
		options.add( "startAngle", startAngle );
	}
	
	public DonutTickRenderer getTickRenderer() {
		return tickRenderer;
	}
	public void setTickRenderer(DonutTickRenderer tickRenderer) {
		this.tickRenderer = tickRenderer;
		options.add( "tickRenderer", tickRenderer, "tickRendererOptions" );
	}
	public boolean isDrawData() {
		return _drawData;
	}
	public void setDrawData(boolean _drawData) {
		this._drawData = _drawData;
		options.add( "_drawData", _drawData );
	}
}//class
