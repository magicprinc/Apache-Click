package net.sf.click.chart.jqplot.renderer;

import java.util.ArrayList;
import java.util.List;

import net.sf.click.chart.Color;
import net.sf.click.chart.jqplot.Series;
import net.sf.click.chart.jqplot.plugins.Plugin;

/**
 * A renderer for Bars charts. The renderer can be customized through its options.
 * @author steff
 * http://www.jqplot.com/docs/files/plugins/jqplot-barRenderer-js.html
 */
public class FunnelRenderer extends SeriesRenderer implements Plugin {

	public FunnelRenderer() {
		this( null );
	}//cons
	
	public FunnelRenderer( Series s) {
		super( "$.jqplot.FunnelRenderer" ,s );
	}//cons

	@Override
	public List<String> getListJavaScriptImport() {
		List<String> listJavaScriptImport = new ArrayList<String>();
		//javascript imports
		listJavaScriptImport.add("/jqplot/plugins/jqplot.funnelRenderer.js");
		return listJavaScriptImport;
	}

	public enum DataLabel
	{
		LABEL( "label" ),
		PERCENT( "percent" ),
		VALUE( "value" );

		private String type;
		private DataLabel( String s ) {
			this.type = s;			
		}//cons
		@Override
		public String toString() {
			return type;
		}//met
	}//enum

	/** ...*/
	private boolean show = true;
	/** padding between the funnel and plot edges, legend, etc.*/
	private String padding = "{top: 20, right: 20, bottom: 20, left: 20}";
	/** spacing between funnel sections in pixels.*/
	private int sectionMargin = 6;
	/** true or false, wether to fill the areas.*/
	private boolean fill = true;
	/** offset of the shadow from the area and offset of 
         each succesive stroke of the shadow from the last.*/
	private int shadowOffset = 2;
	/** transparency of the shadow (0 = transparent, 1 = opaque)*/
	private double shadowAlpha = 0.07;
	/** number of strokes to apply to the shadow, 
         each stroke offset shadowOffset from the last.*/
	private int shadowDepth = 5;
	/** True to highlight area when moused over.
         This must be false to enable highlightMouseDown to highlight when clicking on a area.*/
	private boolean highlightMouseOver = true;
	/** True to highlight when a mouse button is pressed over a area.
         This will be disabled if highlightMouseOver is true.*/
	private boolean highlightMouseDown = false;
	/** array of colors to use when highlighting an area.*/
	private Color[] highlightColors = new Color[ 0 ];
	/** The ratio of the width of the top of the funnel to the bottom.
         a ratio of 0 will make an upside down pyramid.*/ 
	private double widthRatio = 0.2;
	/** width of line if areas are stroked and not filled.*/
	private int lineWidth = 2;
	/** Either 'label', 'value', 'percent' or an array of labels to place on the pie slices.
         Defaults to percentage of each pie slice.*/
	private DataLabel dataLabels = DataLabel.PERCENT;
	/** true to show data labels on slices.*/
	private boolean showDataLabels = false;
	/** Format string for data labels.  If none, '%s' is used for "label" and for arrays, '%d' for value and '%d%%' for percentage.*/
	private String dataLabelFormatString = null;
	/** Threshhold in percentage (0 - 100) of pie area, below which no label will be displayed.
         This applies to all label types, not just to percentage labels.*/
	private int dataLabelThreshold = 3;

	private FunnelTickRenderer tickRenderer = new FunnelTickRenderer();

	public boolean isShow() {
		return show;
	}

	public void setShow(boolean show) {
		this.show = show;
		options.add( "show", show );
	}

	public String getPadding() {
		return padding;
	}

	public void setPadding(String padding) {
		this.padding = padding;
		options.add( "padding", padding );
	}

	public int getSectionMargin() {
		return sectionMargin;
	}

	public void setSectionMargin(int sectionMargin) {
		this.sectionMargin = sectionMargin;
		options.add( "sectionMargin", sectionMargin );
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

	public double getWidthRatio() {
		return widthRatio;
	}

	public void setWidthRatio(double widthRatio) {
		this.widthRatio = widthRatio;
		options.add( "widthRatio", widthRatio );
	}

	public int getLineWidth() {
		return lineWidth;
	}

	public void setLineWidth(int lineWidth) {
		this.lineWidth = lineWidth;
		options.add( "lineWidth", lineWidth );
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

	public FunnelTickRenderer getTickRenderer() {
		return tickRenderer;
	}

	public void setTickRenderer(FunnelTickRenderer tickRenderer) {
		this.tickRenderer = tickRenderer;
		options.add( "tickRenderer", tickRenderer, "tickRendererOptions" );
	}
}//class
