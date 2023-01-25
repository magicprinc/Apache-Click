package net.sf.click.chart.jqplot.renderer;

import net.sf.click.chart.Color;

/**
 * 
 * @author steff
 *http://www.jqplot.com/docs/files/jqplot-shapeRenderer-js.html
 */
public class ShapeRenderer extends AbstractRenderer {

	public ShapeRenderer() {
		super( "$.jqplot.ShapeRenderer" );
	}//cons

	/** How line segments of the shadow are joined.*/
	private String lineJoin = "miter";
	/** how ends of the shadow line are rendered.*/
	private String lineCap = "round";
	/** whether to fill the shape.*/
	private boolean fill = false;
	/** wether the shadow is an arc or not.*/
	private boolean isarc = false;
	/** true to draw shape as a filled rectangle.*/
	private boolean fillRect = false;
	/** true to draw shape as a stroked rectangle.*/
	private boolean strokeRect = false;
	/** true to cear a rectangle.*/
	private boolean clearRect = false;
	/** css color spec for the stoke style*/
	private Color strokeStyle;
	/** css color spec for the fill style.*/
	private Color fillStyle;
	public String getLineJoin() {
		return lineJoin;
	}//met
	public void setLineJoin(String lineJoin) {
		this.lineJoin = lineJoin;
		options.add( "lineJoin", lineJoin );
	}//met
	public String getLineCap() {
		return lineCap;
	}//met
	public void setLineCap(String lineCap) {
		this.lineCap = lineCap;
		options.add( "lineCap", lineCap );
	}//met
	public boolean isFill() {
		return fill;
	}//met
	public void setFill(boolean fill) {
		this.fill = fill;
		options.add( "fill", fill );
	}//met
	public boolean isIsarc() {
		return isarc;
	}//met
	public void setIsarc(boolean isarc) {
		this.isarc = isarc;
		options.add( "isarc", isarc );
	}//met
	public boolean isFillRect() {
		return fillRect;
	}//met
	public void setFillRect(boolean fillRect) {
		this.fillRect = fillRect;
		options.add( "fillRect", fillRect );
	}//met
	public boolean isStrokeRect() {
		return strokeRect;
	}//met
	public void setStrokeRect(boolean strokeRect) {
		this.strokeRect = strokeRect;
		options.add( "strokeRect", strokeRect );
	}//met
	public boolean isClearRect() {
		return clearRect;
	}//met
	public void setClearRect(boolean clearRect) {
		this.clearRect = clearRect;
		options.add( "clearRect", clearRect );
	}//met
	public Color getStrokeStyle() {
		return strokeStyle;
	}//met
	public void setStrokeStyle(Color strokeStyle) {
		this.strokeStyle = strokeStyle;
		options.add( "strokeStyle", strokeStyle );
	}//met
	public Color getFillStyle() {
		return fillStyle;
	}//met
	public void setFillStyle(Color fillStyle) {
		this.fillStyle = fillStyle;
		options.add( "fillStyle", fillStyle );
	}//inner class
}//class
