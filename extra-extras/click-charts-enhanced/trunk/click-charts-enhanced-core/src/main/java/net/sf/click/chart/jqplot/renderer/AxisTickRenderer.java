package net.sf.click.chart.jqplot.renderer;


import net.sf.click.chart.Color;
import net.sf.click.chart.jqplot.Axis;

/**
 * A renderer for Axis Ticks. The renderer can be customized through its options.
 * @author steff
 * http://www.jqplot.com/docs/files/jqplot-axisTickRenderer-js.html
 */
public class AxisTickRenderer extends TickRenderer {

	public AxisTickRenderer() {
		this( null );
	}//cons
	
	public AxisTickRenderer( Axis x) {
		super( "$.jqplot.AxisTickRenderer",x );
	}//cons

	public enum MarkLocation
	{
		INSIDE( "inside" ),
		OUTSIDE( "outside" ),
		CROSS( "cross" );

		private String location;
		private MarkLocation( String s ) {
			this.location = s;			
		}//cons
		@Override
		public String toString() {
			return location;
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
	/** Length of the tick beyond the grid in pixels. */
	private int size = 4;
	/** Length of the tick marks in pixels. */
	private int markSize = 16;
	/** wether or not to show the tick (mark and label). */
	private boolean show = true;
	/** wether or not to show the label.*/
	private boolean showabel = true;
	/** A class of a formatter for the tick text. */
	private String formatter = "$.jqplot.DefaultTickFormatter";
	/** 	string passed to the formatter.*/
	private String formatString = "";
	/** css spec for the font-family css attribute.*/
	private String fontFamily = "";
	/** css spec for the font-size css attribute.*/
	private String fontSize = "";
	/** css spec for the color attribute.*/
	private Color textColor = null;


	public MarkLocation getMark() {
		return mark;
	}//met
	public void setMark(MarkLocation mark) {
		this.mark = mark;
		options.add( "mark", mark.toString() );
	}//met
	public boolean isShowMark() {
		return showMark;
	}
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
	public int getSize() {
		return size;
	}//met
	public void setSize(int size) {
		this.size = size;
		options.add( "size", size );
	}//met
	public int getMarkSize() {
		return markSize;
	}//met
	public void setMarkSize(int markSize) {
		this.markSize = markSize;
		options.add( "markSize", markSize );
	}//met
	public boolean isShow() {
		return show;
	}//met
	public void setShow(boolean show) {
		this.show = show;
		options.add( "show", show );
	}//met
	public boolean isShowabel() {
		return showabel;
	}//met
	public void setShowabel(boolean showabel) {
		this.showabel = showabel;
		options.add( "showabel", showabel );
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
	public String getFontFamily() {
		return fontFamily;
	}//met
	public void setFontFamily(String fontFamily) {
		this.fontFamily = fontFamily;
		options.add( "fontFamily", fontFamily );
	}//met
	public String getFontSize() {
		return fontSize;
	}//met
	public void setFontSize(String fontSize) {
		this.fontSize = fontSize;
		options.add( "fontSize", fontSize );
	}//met
	public Color getTextColor() {
		return textColor;
	}//met
	public void setTextColor(Color textColor) {
		this.textColor = textColor;
		options.add( "textColor", textColor );
	}//met
}//class
