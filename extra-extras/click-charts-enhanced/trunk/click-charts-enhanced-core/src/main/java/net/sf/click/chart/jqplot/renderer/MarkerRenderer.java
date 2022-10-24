package net.sf.click.chart.jqplot.renderer;


import net.sf.click.chart.Color;
import net.sf.click.chart.jqplot.Series;

/**
 * A renderer for Markers. The renderer can be customized through its options.
 * @author steff
 *http://www.jqplot.com/docs/files/jqplot-markerRenderer-js.html
 */
public class MarkerRenderer extends SeriesRenderer {

	public MarkerRenderer() {
		this( null );
	}//cons
	
	public MarkerRenderer( Series s) {
		super( "$.jqplot.MarkerRenderer", null );
		if( s!= null )
			s.setMarkerRenderer( this );
	}//cons


	/** Represents the set of alternatives for the style properties of 
	 * MarkerRendererOptions. */
	public enum MarkerStyle
	{
		DIAMOND( "diamond" ),
		SQUARE( "square" ),
		CIRCLE( "circle" ),
		X( "x" ),
		PLUS( "plus" ),
		DASH( "dash" ),
		FILLED_DIAMOND( "filledDiamond" ),
		FILLED_CIRCLE( "filledCircle" ),
		FILLED_SQUARE( "filledSquare");

		private String style;
		private MarkerStyle( String s ) {
			this.style = s;			
		}//cons
		@Override
		public String toString() {
			return style;
		}//met
	}//enum

	/** wether or not to show the marker.*/
	private boolean show = true;
	/** One of diamond, circle, square, x, plus, dash, filledDiamond, filledCircle, filledSquare.*/
	private MarkerStyle style = MarkerStyle.FILLED_CIRCLE;
	/** size of the line for non-filled markers.*/
	private int lineWidth = 2;
	/** Size of the marker (diameter or circle, length of edge of square, etc.)*/
	private int size = 4;
	/** color of marker. */
	private Color color = null;
	/** wether or not to draw a shadow on the line*/
	private boolean shadow = true;
	/** Shadow angle in degrees*/
	private double shadowAngle = 45d;
	/** Shadow offset from line in pixels*/
	private double shadowOffset = 1d;
	/** Number of times shadow is stroked, each stroke offset shadowOffset from the last.*/
	private int shadowDepth = 3;
	/** Alpha channel transparency of shadow. */
	private double shadowAlpha = 0.07d;
	/** Renderer that will draws the shadows on the marker.*/
	private Renderer shadowRenderer = new ShadowRenderer();
	/** Renderer that will draw the marker. */
	private Renderer shapeRenderer = new ShapeRenderer();

	//---methods
	public boolean isShow() {
		return show;
	}//met
	public void setShow(boolean show) {
		this.show = show;
		options.add( "show", show );
	}//met
	public MarkerStyle getStyle() {
		return style;
	}//met
	public void setStyle(MarkerStyle style) {
		this.style = style;
		options.add( "style", style.toString() );
	}//met
	public int getLineWidth() {
		return lineWidth;
	}//met
	public void setLineWidth(int lineWidth) {
		this.lineWidth = lineWidth;
		options.add( "lineWidth", lineWidth );
	}//met
	public int getSize() {
		return size;
	}//met
	public void setSize(int size) {
		this.size = size;
		options.add( "size", size );
	}//met
	public Color getColor() {
		return color;
	}//met
	public void setColor(Color color) {
		this.color = color;
		options.add( "color", color );
	}//met
	public boolean isShadow() {
		return shadow;
	}//met
	public void setShadow(boolean shadow) {
		this.shadow = shadow;
		options.add( "shadow", shadow );
	}//met
	public double getShadowAngle() {
		return shadowAngle;
	}//met
	public void setShadowAngle(double shadowAngle) {
		this.shadowAngle = shadowAngle;
		options.add( "shadowAngle", shadowAngle );
	}//met
	public double getShadowOffset() {
		return shadowOffset;
	}//met
	public void setShadowOffset(double shadowOffset) {
		this.shadowOffset = shadowOffset;
		options.add( "shadowOffset", shadowOffset );
	}//met
	public int getShadowDepth() {
		return shadowDepth;
	}//met
	public void setShadowDepth(int shadowDepth) {
		this.shadowDepth = shadowDepth;
		options.add( "shadowDepth", shadowDepth );
	}//met
	public double getShadowAlpha() {
		return shadowAlpha;
	}//met
	public void setShadowAlpha(double shadowAlpha) {
		this.shadowAlpha = shadowAlpha;
		options.add( "shadowAlpha", shadowAlpha );
	}//met
	public Renderer getShadowRenderer() {
		return shadowRenderer;
	}//met
	public void setShadowRenderer(Renderer shadowRenderer) {
		this.shadowRenderer = shadowRenderer;
		options.add( "shadowRenderer", shadowRenderer.getName() );
	}//met
	public Renderer getShapRenderer() {
		return shapeRenderer;
	}//met
	public void setShapRenderer(Renderer shapRenderer) {
		this.shapeRenderer = shapRenderer;
		options.add( "shapeRenderer", shapRenderer.getName() );
	}//met
}//class
