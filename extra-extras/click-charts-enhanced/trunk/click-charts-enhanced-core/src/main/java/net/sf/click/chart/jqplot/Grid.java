package net.sf.click.chart.jqplot;

import net.sf.click.chart.ChartOptions;
import net.sf.click.chart.Color;
import net.sf.click.chart.jqplot.renderer.CanvasGridRenderer;
import net.sf.click.chart.jqplot.renderer.GridRenderer;


public class Grid extends ChartOptions {
	private boolean drawGridLines = true;
	private Color gridLineColor = null;
	private double gridLineWidth = 1.0d;
	private Color background = null;
	private Color borderColor = null;
	private double borderWidth = 2.0d;
	private boolean shadow = true;
	private double shadowAngle = 45d;
	private double shadowOffset = 1.25d;
	private int shadowDepth = 3;
	private double shadowAlpha = 0.1d;
	private GridRenderer renderer = new CanvasGridRenderer();
	public boolean isDrawGridLines() {
		return drawGridLines;
	}
	public void setDrawGridLines(boolean drawGridLines) {
		this.drawGridLines = drawGridLines;
		add( "drawGridLines", drawGridLines );
	}
	public Color getGridLineColor() {
		return gridLineColor;
	}
	public void setGridLineColor(Color gridLineColor) {
		this.gridLineColor = gridLineColor;
		add( "gridLineColor", gridLineColor );
	}
	public double getGridLineWidth() {
		return gridLineWidth;
	}
	public void setGridLineWidth(double gridLineWidth) {
		this.gridLineWidth = gridLineWidth;
		add( "gridLineWidth", gridLineWidth );
	}
	public Color getBackground() {
		return background;
	}
	public void setBackground(Color background) {
		this.background = background;
		add( "background", background );
	}
	public Color getBorderColor() {
		return borderColor;
	}
	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
		add( "borderColor", borderColor );
	}
	public double getBorderWidth() {
		return borderWidth;
	}
	public void setBorderWidth(double borderWidth) {
		this.borderWidth = borderWidth;
		add( "borderWidth", borderWidth );
	}
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
	public GridRenderer getRenderer() {
		return renderer;
	}
	public void setRenderer(GridRenderer renderer) {
		this.renderer = renderer;
		add( "renderer", renderer, "rendererOptions" );
	}
	public ChartOptions getRendererOptions() {
		add( "rendererOptions", renderer.getOptions() );
		return renderer.getOptions();
	}
	
}//class
