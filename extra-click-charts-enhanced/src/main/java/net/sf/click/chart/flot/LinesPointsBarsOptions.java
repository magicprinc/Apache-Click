package net.sf.click.chart.flot;

import net.sf.click.chart.Color;

public abstract class LinesPointsBarsOptions extends FlotChartOptions {

	private boolean show;
    private int lineWidth;
    private boolean fill;
    private Color fillColor;
    private boolean steps;
    
	public boolean isShow() {
		return show;
	}
	public void setShow(boolean show) {
		this.show = show;
		add( "show", show );
	}
	public int getLineWidth() {
		return lineWidth;
	}
	public void setLineWidth(int lineWidth) {
		this.lineWidth = lineWidth;
		add( "lineWidth", lineWidth );
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
	public boolean getSteps() {
		return steps;
	}
	public void setSteps(boolean steps) {
		this.steps = steps;
		add( "steps", steps );
	}

    
}//met
