package net.sf.click.chart.jqplot.renderer;

import net.sf.click.chart.Color;
import java.util.ArrayList;
import java.util.List;

import net.sf.click.chart.jqplot.Series;
import net.sf.click.chart.jqplot.plugins.Plugin;

/**
 * A renderer for ohlc charts. The renderer can be customized through its options.
 * @author steff
 * http://www.jqplot.com/docs/files/plugins/jqplot-ohlcRenderer-js.html
 */
public class OHLCRenderer extends SeriesRenderer implements Plugin {

	public OHLCRenderer() {
		this( null );
	}//cons
	
	public OHLCRenderer( Series s) {
		super( "$.jqplot.OHLCRenderer", s );
	}//cons

	@Override
	public List<String> getListJavaScriptImport() {
		List<String> listJavaScriptImport = new ArrayList<String>();
		//javascript imports
		listJavaScriptImport.add("/jqplot/plugins/jqplot.ohlcRenderer.js");
		return listJavaScriptImport;
	}//met

	/** true to render chart as candleStick. */
	private boolean candleStick = false;
	/** length of the line in pixels indicating open and close price. */
	private Double tickLength = null;
	/** width of the candlestick body in pixels. */
	private Double bodyWidth = null;
	/** color of the open price tick mark. */
	private Color openColor = null;
	/** color of the close price tick mark. */
	private Color closeColor = null;
	/** color of the hi-lo line thorugh the candlestick body. */
	private Color wickColor = null;
	/** true to render an “up” day (close price greater than open price) with a filled candlestick body.*/
	private boolean fillUpBody = false;
	/** true to render a “down” day (close price lower than open price) with a filled candlestick body.*/
	private boolean fillDownBody = true;
	/** Color of candlestick body of an “up” day. */
	private Color upBodyColor = null;
	/** Color of candlestick body on a “down” day. */
	private Color downBodyColor = null;
	/** true if is a hi-low-close chart (no open price). */
	private boolean hlc = false;
	/** Width of the hi-low line and open/close ticks.*/
	private double lineWidth = 1.5d;

	public boolean isCandleStick() {
		return candleStick;
	}//met
	public void setCandleStick(boolean candleStick) {
		this.candleStick = candleStick;
		options.add( "candleStick", candleStick );
	}//met
	public Double getTickLength() {
		return tickLength;
	}//met
	public void setTickLength(Double tickLength) {
		this.tickLength = tickLength;
		options.add( "tickLength", tickLength );
	}//met
	public Double getBodyWidth() {
		return bodyWidth;
	}//met
	public void setBodyWidth(Double bodyWidth) {
		this.bodyWidth = bodyWidth;
		options.add( "boyWidth", bodyWidth );
	}//met
	public Color getOpenColor() {
		return openColor;
	}//met
	public void setOpenColor(Color openColor) {
		this.openColor = openColor;
		options.add( "openColor", openColor );
	}//met
	public Color getCloseColor() {
		return closeColor;
	}//met
	public void setCloseColor(Color closeColor) {
		this.closeColor = closeColor;
		options.add( "closeColor", closeColor );
	}//met
	public Color getWickColor() {
		return wickColor;
	}//met
	public void setWickColor(Color wickColor) {
		this.wickColor = wickColor;
		options.add( "wickColor", wickColor );
	}//met
	public boolean isFillUpBody() {
		return fillUpBody;
	}//met
	public void setFillUpBody(boolean fillUpBody) {
		this.fillUpBody = fillUpBody;
		options.add( "fillUpBody", fillUpBody );
	}//met
	public boolean isFillDownBody() {
		return fillDownBody;
	}//met
	public void setFillDownBody(boolean fillDownBody) {
		this.fillDownBody = fillDownBody;
		options.add( "fillDownBody", fillDownBody );
	}//met
	public Color getUpBodyColor() {
		return upBodyColor;
	}//met
	public void setUpBodyColor(Color upBodyColor) {
		this.upBodyColor = upBodyColor;
		options.add( "upBodyColor", upBodyColor );
	}//met
	public Color getDownBodyColor() {
		return downBodyColor;
	}//met
	public void setDownBodyColor(Color downBodyColor) {
		this.downBodyColor = downBodyColor;
		options.add( "downBodyColor", downBodyColor );
	}//met
	public boolean isHlc() {
		return hlc;
	}//met
	public void setHlc(boolean hlc) {
		this.hlc = hlc;
		options.add( "hlc", hlc );
	}//met
	public double getLineWidth() {
		return lineWidth;
	}//met
	public void setLineWidth(double lineWidth) {
		this.lineWidth = lineWidth;
		options.add( "lineWidth", lineWidth );
	}//met
}//class
