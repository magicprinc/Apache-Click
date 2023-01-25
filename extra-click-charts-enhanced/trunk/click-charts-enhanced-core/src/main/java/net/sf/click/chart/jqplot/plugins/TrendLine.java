package net.sf.click.chart.jqplot.plugins;

import java.util.ArrayList;
import java.util.List;

import net.sf.click.chart.ChartOptions;
import net.sf.click.chart.Color;
import net.sf.click.chart.jqplot.renderer.LineRenderer;
import net.sf.click.chart.jqplot.renderer.MarkerRenderer;
import net.sf.click.chart.jqplot.renderer.Renderer;

/**
 * Plugin which will automatically compute and draw trendlines for plotted data.
 * @author steff
 * http://www.jqplot.com/docs/files/plugins/jqplot-trendline-js.html
 */
public class TrendLine extends ChartOptions implements ChartPlugin {
	
	private List<String> listJavaScriptImport = new ArrayList<String>();

	public TrendLine() {

		//javascript imports
		listJavaScriptImport.add("/jqplot/plugins/jqplot.trendline.js");
	}//cons

	@Override 
	public String getName()
	{
		return "trendLine";
	}//met

	@Override
	public List<String> getListJavaScriptImport() {
		return listJavaScriptImport;
	}//met
	
	public static enum LineType
	{
		EXP( "exp" ),
		EXPONENTIAL( "exponential" ),
		LINEAR( "linear" );

		private String type;
		private LineType( String s ) {
			this.type = s;			
		}//cons
		@Override
		public String toString() {
			return type;
		}//met
	}//enum

	/** Wether or not to show the trend line.*/
	private boolean show = true;	
	/** CSS color spec for the trend line.*/
	private Color color = new Color( 0x666666 );
	/** Renderer to use to draw the trend line.*/
	private Renderer renderer = new LineRenderer();	
	/** Label for the trend line to use in the legend.*/
	private String label = "";	
	/** Either ‘exponential’, ‘exp’, or ‘linear’.*/
	private LineType type = LineType.LINEAR;
	/** true or false, wether or not to show the shadow.*/
	private boolean shadow = true;
	/** Renderer to use to draw markers on the line.*/
	private Renderer markerRenderer	= new MarkerRenderer();
	/** Width of the trend line.*/
	private double lineWidth = 1.5;
	/** Angle of the shadow on the trend line.*/
	private int shadowAngle	= 45;
	/** pixel offset for each stroke of the shadow.*/
	private double shadowOffset	= 1.0d;
	/** Alpha transparency of the shadow.*/
	private double shadowAlpha = 0.07;
	/** number of strokes to make of the shadow.*/
	private int shadowDepth	= 3;
	
	public boolean isShow() {
		return show;
	}//met
	public void setShow(boolean show) {
		this.show = show;
		add( "show", show );
	}//met
	public Color getColor() {
		return color;
	}//met
	public void setColor(Color color) {
		this.color = color;
		add( "color", color );
	}//met
	public Renderer getRenderer() {
		return renderer;
	}//met
	public void setRenderer(Renderer renderer) {
		this.renderer = renderer;
		//TODO : i think this i wrong
		add( "renderer", renderer, "rendererOptions" );
	}//met
	public String getLabel() {
		return label;
	}//met
	public void setLabel(String label) {
		this.label = label;
		add( "label", label );
	}//met
	public LineType getType() {
		return type;
	}//met
	public void setType(LineType type) {
		this.type = type;
		add( "type", type.toString() );
	}//met
	public boolean isShadow() {
		return shadow;
	}//met
	public void setShadow(boolean shadow) {
		this.shadow = shadow;
		add( "shadow", shadow );
	}//met
	public Renderer getMarkerRenderer() {
		return markerRenderer;
	}//met
	public void setMarkerRenderer(Renderer markerRenderer) {
		this.markerRenderer = markerRenderer;
		//TODO : i think this is wrong too
		add( "markerRenderer", markerRenderer, "markerRendererOptions" );
	}//met
	public double getLineWidth() {
		return lineWidth;
	}//met
	public void setLineWidth(double lineWidth) {
		this.lineWidth = lineWidth;
		add( "lineWidth", lineWidth );
	}//met
	public int getShadowAngle() {
		return shadowAngle;
	}//met
	public void setShadowAngle(int shadowAngle) {
		this.shadowAngle = shadowAngle;
		add( "shadowAngle", shadowAngle );
	}//met
	public double getShadowOffset() {
		return shadowOffset;
	}//met
	public void setShadowOffset(double shadowOffset) {
		this.shadowOffset = shadowOffset;
		add( "shadowOffset", shadowOffset );
	}//met
	public double getShadowAlpha() {
		return shadowAlpha;
	}//met
	public void setShadowAlpha(double shadowAlpha) {
		this.shadowAlpha = shadowAlpha;
		add( "shadowAlpha", shadowAlpha );
	}//met
	public int getShadowDepth() {
		return shadowDepth;
	}//met
	public void setShadowDepth(int shadowDepth) {
		this.shadowDepth = shadowDepth;
		add( "shadowDepth", shadowDepth );
	}//met
}//class
