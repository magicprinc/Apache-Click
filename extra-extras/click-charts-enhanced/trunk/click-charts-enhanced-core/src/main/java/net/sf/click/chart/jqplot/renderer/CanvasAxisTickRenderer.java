package net.sf.click.chart.jqplot.renderer;

import java.util.ArrayList;
import java.util.List;

import net.sf.click.chart.Color;
import net.sf.click.chart.jqplot.Axis;
import net.sf.click.chart.jqplot.plugins.Plugin;
import net.sf.click.chart.jqplot.renderer.AxisTickRenderer.MarkLocation;

/**
 * A renderer for Categories axies. The renderer can be customized through its options.
 * @author steff
 * http://www.jqplot.com/docs/files/plugins/jqplot-canvasAxisTickRenderer-js.html
 */
public class CanvasAxisTickRenderer extends TickRenderer implements Plugin {

	public CanvasAxisTickRenderer() {
		this( null );
	}//cons
	
	public CanvasAxisTickRenderer( Axis x ) {
		super( "$.jqplot.CanvasAxisTickRenderer", x );
	}//cons


	@Override
	public List<String> getListJavaScriptImport() {
		List<String> listJavaScriptImport = new ArrayList<String>();
		//javascript imports
		listJavaScriptImport.add("/jqplot/plugins/jqplot.canvasAxisTickRenderer.js");
		listJavaScriptImport.add("/jqplot/plugins/jqplot.canvasTextRenderer.js");
		return listJavaScriptImport;
	}

	public enum LabelPosition
	{
		AUTO( "inside" ),
		START( "start" ),
		MIDDLE( "middle" ),
		END( "end" );

		private String position;
		private LabelPosition( String s ) {
			this.position = s;			
		}//cons
		@Override
		public String toString() {
			return position;
		}//met
	}//enum

	/** tick mark on the axis. */
	private MarkLocation mark = MarkLocation.OUTSIDE;
	/** wether or not to show the mark on the axis.*/
	private boolean showMark = true;
	/** wether or not to draw the gridline on the grid at this tick.*/
	private boolean showGridline = true;
	/** if this is a minor tick.*/
	private boolean isMinorTick = false;
	/** angle of text, measured clockwise from x axis.*/
	private double angle =0;
	/** Length of the tick marks in pixels. */
	private int markSize = 16;
	/** wether or not to show the tick (mark and label). */
	private boolean show = true;
	/** wether or not to show the label.*/
	private boolean showLabel = true;
	/** ‘auto’, ‘start’, ‘middle’ or ‘end’. */
	private LabelPosition labelPosition = LabelPosition.AUTO;
	/** A class of a formatter for the tick text. */
	private String formatter = "$.jqplot.DefaultTickFormatter";
	/** 	string passed to the formatter.*/
	private String formatString = "";
	/** css spec for the font-family css attribute.*/
	private String fontFamily = "";
	/** css spec for the font-size css attribute.*/
	private int fontSize = 11;
	/** CSS spec for fontWeight*/
	private String fontWeight = "normal";
	/** 	Multiplier to condense or expand font width. */
	private double fontStretch = 1.0d;
	/** css spec for the color attribute.*/
	private Color textColor = null;
	/** true to turn on native canvas font support in Mozilla 3.5+ and Safari 4+. */
	private boolean enableFontSupport = false;
	/** Point to pixel scaling factor, used for computing height of bounding box around a label. */
	private Double pt2px = null;


	public double getAngle() {
		return angle;
	}//met
	public void setAngle(double angle) {
		this.angle = angle;
		options.add( "angle", angle );
	}//met
	public boolean isShow() {
		return show;
	}//met
	public void setShow(boolean show) {
		this.show = show;
		options.add( "show", show );
	}//met
	public boolean isShowLabel() {
		return showLabel;
	}//met
	public void setShowLabel(boolean showLabel) {
		this.showLabel = showLabel;
		options.add( "showLabel", showLabel );
	}//met
	public String getFontFamily() {
		return fontFamily;
	}//met
	public void setFontFamily(String fontFamily) {
		this.fontFamily = fontFamily;
		options.add( "fontFamily", fontFamily );
	}//met
	public int getFontSize() {
		return fontSize;
	}//met
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
		options.add( "fontSize", fontSize );
	}//met
	public String getFontWeight() {
		return fontWeight;
	}//met
	public void setFontWeight(String fontWeight) {
		this.fontWeight = fontWeight;
		options.add( "fontWeight", fontWeight );
	}//met
	public double getFontStretch() {
		return fontStretch;
	}//met
	public void setFontStretch(double fontStretch) {
		this.fontStretch = fontStretch;
		options.add( "fontStretch", fontStretch );
	}//met
	public Color getTextColor() {
		return textColor;
	}//met
	public void setTextColor(Color textColor) {
		this.textColor = textColor;
		options.add( "textColor", textColor );
	}//met
	public boolean isEnableFontSupport() {
		return enableFontSupport;
	}//met
	public void setEnableFontSupport(boolean enableFontSupport) {
		this.enableFontSupport = enableFontSupport;
		options.add( "enableFontSupport", enableFontSupport );
	}//met
	public Double getPt2px() {
		return pt2px;
	}//met
	public void setPt2px(Double pt2px) {
		this.pt2px = pt2px;
		options.add( "pt2px", pt2px );
	}//met
	public MarkLocation getMark() {
		return mark;
	}//met
	public void setMark(MarkLocation mark) {
		this.mark = mark;
		options.add( "mark", mark.toString() );
	}//met
	public boolean isShowMark() {
		return showMark;
	}//met
	public void setShowMark(boolean showMark) {
		this.showMark = showMark;
		options.add( "showMark", showMark );
	}//met
	public boolean isShowGridline() {
		return showGridline;
	}//met
	public void setShowGridline(boolean showGridline) {
		this.showGridline = showGridline;
		options.add( "showGridline", showGridline );
	}//met
	public boolean isMinorTick() {
		return isMinorTick;
	}//met
	public void setMinorTick(boolean isMinorTick) {
		this.isMinorTick = isMinorTick;
		options.add( "isMinorTick", isMinorTick );
	}//met
	public int getMarkSize() {
		return markSize;
	}//met
	public void setMarkSize(int markSize) {
		this.markSize = markSize;
		options.add( "markSize", markSize );
	}//met
	public LabelPosition getLabelPosition() {
		return labelPosition;
	}//met
	public void setLabelPosition(LabelPosition labelPosition) {
		this.labelPosition = labelPosition;
		options.add( "labelPosition", labelPosition.toString() );
	}//met
	public String getFormatter() {
		return formatter;
	}//met
	public void setFormatter(String formatter) {
		this.formatter = formatter;
		options.add( "formatter", formatter );
	}//met
	public String getFormatString() {
		return formatString;
	}//met
	public void setFormatString(String formatString) {
		this.formatString = formatString;
		options.add( "formatString", formatString );
	}//met
}//class
