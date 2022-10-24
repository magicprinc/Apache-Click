package net.sf.click.chart.flot;

import net.sf.click.chart.Color;

public class Grid extends FlotChartOptions {
    private boolean show;
    private boolean aboveData;
    private Color color;
    private Color[] backgroundColor;
    private Color tickColor;
    private int labelMargin;
    //TODO markings ?
    private String markings;
    private double borderWidth;
    private Color borderColor;
    private boolean clickable;
    private boolean hoverable;
    private boolean autoHighlight;
    private double mouseActiveRadius;
	public boolean isShow() {
		return show;
	}
	public void setShow(boolean show) {
		this.show = show;
		add( "show", show );
	}
	public boolean isAboveData() {
		return aboveData;
	}
	public void setAboveData(boolean aboveData) {
		this.aboveData = aboveData;
		add( "aboveData", aboveData );
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
		add( "color", color );
	}
	public Color[] getBackgroundColor() {
		return backgroundColor;
	}
	public void setBackgroundColor(Color[] backgroundColor) {
		this.backgroundColor = backgroundColor;
		add( "backgroundColor", backgroundColor );
	}
	
	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = new Color[] {backgroundColor };
		add( "backgroundColor", backgroundColor );
	}

	public Color getTickColor() {
		return tickColor;
	}
	public void setTickColor(Color tickColor) {
		this.tickColor = tickColor;
		add( "tickColor", tickColor );
	}
	public int getLabelMargin() {
		return labelMargin;
	}
	public void setLabelMargin(int labelMargin) {
		this.labelMargin = labelMargin;
		add( "labelMargin", labelMargin );
	}
	public String getMarkings() {
		return markings;
	}
	public void setMarkings(String markings) {
		this.markings = markings;
		add( "markings", markings );
	}
	public double getBorderWidth() {
		return borderWidth;
	}
	public void setBorderWidth(double borderWidth) {
		this.borderWidth = borderWidth;
		add( "borderWidth", borderWidth );
	}
	public Color getBorderColor() {
		return borderColor;
	}
	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
		add( "borderColor", borderColor );
	}
	public boolean isClickable() {
		return clickable;
	}
	public void setClickable(boolean clickable) {
		this.clickable = clickable;
		add( "clickable", clickable );
	}
	public boolean isHoverable() {
		return hoverable;
	}
	public void setHoverable(boolean hoverable) {
		this.hoverable = hoverable;
		add( "hoverable", hoverable );
	}
	public boolean isAutoHighlight() {
		return autoHighlight;
	}
	public void setAutoHighlight(boolean autoHighlight) {
		this.autoHighlight = autoHighlight;
		add( "autoHighlight", autoHighlight );
	}
	public double getMouseActiveRadius() {
		return mouseActiveRadius;
	}
	public void setMouseActiveRadius(double mouseActiveRadius) {
		this.mouseActiveRadius = mouseActiveRadius;
		add( "mouseActiveRadius", mouseActiveRadius );
	}
    
    

}
