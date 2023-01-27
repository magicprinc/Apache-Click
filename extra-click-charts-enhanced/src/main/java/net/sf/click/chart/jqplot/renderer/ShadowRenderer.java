package net.sf.click.chart.jqplot.renderer;

/**
 * 
 * @author steff
 *http://www.jqplot.com/docs/files/jqplot-shadowRenderer-js.html
 */
public class ShadowRenderer extends AbstractRenderer {

	public ShadowRenderer() {
		super("$.jqplot.ShadowRenderer");
	}//cons

	/** Angle of the shadow in degrees. */
	private double angle = 45d;
	/** Pixel offset at the given shadow angle of each shadow stroke from the last stroke.*/
	private int offset =1;
	/** alpha transparency of shadow stroke. */
	private double alpha = 0.07d;
	/** size of the line for non-filled markers.*/
	private int lineWidth = 2;
	/** How line segments of the shadow are joined.*/
	private String lineJoin = "miter";
	/** how ends of the shadow line are rendered.*/
	private String lineCap = "round";
	/** whether to fill the shape.*/
	/** whether to fill the shape.*/
	private boolean fill = false;
	/** how many times the shadow is stroked. */
	private int depth = 3;
	/** wether the shadow is an arc or not.*/
	private boolean isarc = false;


	public double getAngle() {
		return angle;
	}//met
	public void setAngle(double angle) {
		this.angle = angle;
		options.add( "angle", angle );
	}//met
	public int getOffset() {
		return offset;
	}//met
	public void setOffset(int offset) {
		this.offset = offset;
		options.add( "offset", offset );
	}//met
	public double getAlpha() {
		return alpha;
	}//met
	public void setAlpha(double alpha) {
		this.alpha = alpha;
		options.add( "alpha", alpha );
	}//met
	public int getLineWidth() {
		return lineWidth;
	}//met
	public void setLineWidth(int lineWidth) {
		this.lineWidth = lineWidth;
		options.add( "lineWidth", lineWidth );
	}//met
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
	public int getDepth() {
		return depth;
	}//met
	public void setDepth(int depth) {
		this.depth = depth;
		options.add( "depth", depth );
	}//met
	public boolean isIsarc() {
		return isarc;
	}//met
	public void setIsarc(boolean isarc) {
		this.isarc = isarc;
		options.add( "isarc", isarc );
	}//met
}//class
