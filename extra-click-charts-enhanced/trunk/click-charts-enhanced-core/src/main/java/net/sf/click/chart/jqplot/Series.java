package net.sf.click.chart.jqplot;

import net.sf.click.chart.ChartOptions;
import net.sf.click.chart.Color;
import net.sf.click.chart.jqplot.plugins.SeriesPlugin;
import net.sf.click.chart.jqplot.renderer.LineRenderer;
import net.sf.click.chart.jqplot.renderer.MarkerRenderer;
import net.sf.click.chart.jqplot.renderer.Renderer;
import net.sf.click.chart.jqplot.renderer.SeriesRenderer;


public class Series extends ChartOptions {
	
	public Series() {
	}//met

	public Series( String label )
	{
		setLabel( label );
	}//met

	public static enum XAxis {
		X_AXIS("xaxis"),
		X2_AXIS("x2axis"),
		X3_AXIS("x3axis"),
		X4_AXIS("x4axis"),
		X5_AXIS("x5axis"),
		X6_AXIS("x6axis");
		
		private String s = null;
		
		private XAxis( String s )
		{ 
			this.s = s;
		}//cons
		
		@Override
		public String toString()
		{
			return s;
		}//met
	}//enum
	
	public static enum YAxis {
		Y_AXIS("yaxis"),
		Y2_AXIS("y2axis"),
		Y3_AXIS("y3axis"),
		Y4_AXIS("y4axis"),
		Y5_AXIS("y5axis"),
		Y6_AXIS("y6axis");
		
		private String s = null;
		
		private YAxis( String s )
		{ 
			this.s = s;
		}//cons
		
		@Override
		public String toString()
		{
			return s;
		}//met
	}//enum
	
	public static enum FillAxis {
		X("x"),
		Y("y");
		
		private String s = null;
		
		private FillAxis( String s )
		{ 
			this.s = s;
		}//cons
		
		@Override
		public String toString()
		{
			return s;
		}//met
	}//enum
	
	private boolean show = false;
	private XAxis xaxis = XAxis.X_AXIS;
	private YAxis yaxis = YAxis.Y_AXIS;
	private SeriesRenderer renderer = new LineRenderer();
	private String label = "";
	private boolean showLabel = true;
	private double borderWidth = 2.5;
	private Color color = Color.BLACK;
	private double lineWidth = 2.5d; 
	private boolean shadow = true;
	private double shadowAngle = 45d;
	private double shadowOffset = 1.25d;
	private int shadowDepth = 3;
	private double shadowAlpha = 0.1d;
	private boolean breakOnNull = false;
	private Renderer markerRenderer = new MarkerRenderer();
	private boolean showLine = true;
	private boolean showMarker = true;
	private int index = 0; 
	private boolean fill = false;
	private Color fillColor = null;
	private double fillAlpha = 0;
	private boolean fillAndStroke = false;
	private boolean disableStack = false;
	private int neighborThreashold = 4;
	private boolean fillToZero = false;
	private FillAxis fillAxis = FillAxis.X;
	private boolean useNegativeColor = false;
	
	public boolean isShow() {
		return show;
	}
	public void setShow(boolean show) {
		this.show = show;
		add( "show", show );
	}
	public XAxis getXaxis() {
		return xaxis;
	}
	public void setXaxis(XAxis xaxis) {
		this.xaxis = xaxis;
		add( "xaxis", xaxis.toString() );
	}
	public YAxis getYaxis() {
		return yaxis;
	}
	public void setYaxis(YAxis yaxis) {
		this.yaxis = yaxis;
		add( "yaxis", yaxis.toString() );
	}
	public SeriesRenderer getRenderer() {
		return renderer;
	}
	public void setRenderer(SeriesRenderer renderer) {
		this.renderer = renderer;
		add( "renderer", renderer, "rendererOptions" );
	}
	public ChartOptions getRendererOptions() {
		add( "rendererOptions", renderer.getOptions() );
		return renderer.getOptions();
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
		add( "label", label );
	}
	public boolean isShowLabel() {
		return showLabel;
	}
	public void setShowLabel(boolean showLabel) {
		this.showLabel = showLabel;
		add( "showLabel", showLabel );
	}
	public double getBorderWidth() {
		return borderWidth;
	}
	public void setBorderWidth(double borderWidth) {
		this.borderWidth = borderWidth;
		add( "borderWidth", borderWidth );
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
		add( "color", color );
	}
	public double getLineWidth() {
		return lineWidth;
	}//met
	public void setLineWidth(double lineWidth) {
		this.lineWidth = lineWidth;
		add( "lineWidth", lineWidth );
	}//met
	public boolean isShadow() {
		return shadow;
	}
	public void setShadow(boolean shadow) {
		this.shadow = shadow;
		add( "shadow", shadow );
	}
	public double getShadowAngle() {
		return shadowAngle;
	}
	public void setShadowAngle(double shadowAngle) {
		this.shadowAngle = shadowAngle;
		add( "shadowAngle", shadowAngle );
	}
	public double getShadowOffset() {
		return shadowOffset;
	}
	public void setShadowOffset(double shadowOffset) {
		this.shadowOffset = shadowOffset;
		add( "shadowOffset", shadowOffset );
	}
	public int getShadowDepth() {
		return shadowDepth;
	}
	public void setShadowDepth(int shadowDepth) {
		this.shadowDepth = shadowDepth;
		add( "shadowDepth", shadowDepth );
	}
	public double getShadowAlpha() {
		return shadowAlpha;
	}
	public void setShadowAlpha(double shadowAlpha) {
		this.shadowAlpha = shadowAlpha;
		add( "shadowAlpha", shadowAlpha );
	}
	public boolean isBreakOnNull() {
		return breakOnNull;
	}
	public void setBreakOnNull(boolean breakOnNull) {
		this.breakOnNull = breakOnNull;
		add( "breakOnNull", breakOnNull );
	}
	public Renderer getMarkerRenderer() {
		return markerRenderer;
	}
	public void setMarkerRenderer(Renderer markerRenderer) {
		this.markerRenderer = markerRenderer;
		add( "markerRenderer", markerRenderer, "markerOptions" );
	}
	public ChartOptions getMarkerOptions() {
		add( "markerOptions", markerRenderer.getOptions() );
		return markerRenderer.getOptions();
	}
	public boolean isShowLine() {
		return showLine;
	}
	public void setShowLine(boolean showLine) {
		this.showLine = showLine;
		add( "showLine", showLine );
	}
	public boolean isShowMarker() {
		return showMarker;
	}
	public void setShowMarker(boolean showMarker) {
		this.showMarker = showMarker;
		add( "showMarker", showMarker );
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
		add( "index", index );
	}
	public boolean isFill() {
		return fill;
	}
	public void setFill(boolean fill) {
		this.fill = fill;
		add( "fill", fill );
	}
	public Color getFillColor() {
		return fillColor;
	}
	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
		add( "fillColor", fillColor );
	}
	public double getFillAlpha() {
		return fillAlpha;
	}
	public void setFillAlpha(double fillAlpha) {
		this.fillAlpha = fillAlpha;
		add( "fillAlpha", fillAlpha );
	}
	public boolean isFillAndStroke() {
		return fillAndStroke;
	}
	public void setFillAndStroke(boolean fillAndStroke) {
		this.fillAndStroke = fillAndStroke;
		add( "fillAndStroke", fillAndStroke );
	}
	public boolean isDisableStack() {
		return disableStack;
	}
	public void setDisableStack(boolean disableStack) {
		this.disableStack = disableStack;
		add( "disableStack", disableStack );
	}
	public int getNeighborThreashold() {
		return neighborThreashold;
	}
	public void setNeighborThreashold(int neighborThreashold) {
		this.neighborThreashold = neighborThreashold;
		add( "neighborThreashold", neighborThreashold );
	}
	public boolean isFillToZero() {
		return fillToZero;
	}
	public void setFillToZero(boolean fillToZero) {
		this.fillToZero = fillToZero;
		add( "fillToZero", fillToZero );
	}
	public FillAxis getFillAxis() {
		return fillAxis;
	}
	public void setFillAxis(FillAxis fillAxis) {
		this.fillAxis = fillAxis;
		add( "fillAxis", fillAxis.toString() );
	}
	public boolean isUseNegativeColor() {
		return useNegativeColor;
	}
	public void setUseNegativeColor(boolean useNegativeColor) {
		this.useNegativeColor = useNegativeColor;
		add( "useNegativeColor", useNegativeColor );
	}
	
	public void add( SeriesPlugin plugin ) {
		add( plugin.getName(), plugin );
	}//met

	@Override
	public boolean needsRendering()
	{
		return true;
	}//met
	
}//class
