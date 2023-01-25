package net.sf.click.chart.jqplot;

import net.sf.click.chart.ChartOptions;
import net.sf.click.chart.Color;
import net.sf.click.chart.jqplot.renderer.LegendRenderer;


public class Legend extends ChartOptions {

	public static enum Compass {
		NORTH_WEST("nw"),
		NORTH("n"),
		NORTH_EAST("ne"),
		EAST("e"),
		SOUTH_EAST("se"),
		SOUTH("s"),
		SOUTH_WEST("sw"),
		WEST("w");
		
		private String s = null;
		
		private Compass( String s )
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
	private String[] labels = null;
	private Compass compass = Compass.NORTH_EAST;
	private int xoffset = 12;
	private int yoffset = 12;
	private String border = null;
	private Color textColor = null;
	private String fontFamily = "";
	private int fontSize = 0;
	private double rowSpacing = 0.5;
	private LegendRenderer renderer = null;
	private ChartOptions rendererOptions = new ChartOptions();
	private boolean predraw = true;
	
	public boolean isShow() {
		return show;
	}
	public void setShow(boolean show) {
		this.show = show;
		add( "show", show );
	}
	public Compass getLocation() {
		return compass;
	}
	
	public String[] getLabels() {
		return labels;
	}
	public void setLabels(String[] labels) {
		this.labels = labels;
		add( "labels", labels );
	}
	public void setLocation(Compass compass) {
		this.compass = compass;
		add( "location", compass.toString() );
	}
	public int getXoffset() {
		return xoffset;
	}
	public void setXoffset(int xoffset) {
		this.xoffset = xoffset;
		add( "xoffset", xoffset );
	}
	public int getYoffset() {
		return yoffset;
	}
	public void setYoffset(int yoffset) {
		this.yoffset = yoffset;
		add( "yoffset", yoffset );
	}
	public String getBorder() {
		return border;
	}
	public void setBorder(String border) {
		this.border = border;
		add( "border", border );
	}
	public Color getTextColor() {
		return textColor;
	}
	public void setTextColor(Color textColor) {
		this.textColor = textColor;
		add( "textColor", textColor );
	}
	public String getFontFamily() {
		return fontFamily;
	}
	public void setFontFamily(String fontFamily) {
		this.fontFamily = fontFamily;
		add( "fontFamily", fontFamily );
	}
	public int getFontSize() {
		return fontSize;
	}
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
		add( "fontSize", fontSize );
	}
	public double getRowSpacing() {
		return rowSpacing;
	}
	public void setRowSpacing(double rowSpacing) {
		this.rowSpacing = rowSpacing;
		add( "rowSpacing", rowSpacing );
	}
	
	public LegendRenderer getRenderer() {
		return renderer;
	}
	public void setRenderer(LegendRenderer renderer) {
		this.renderer = renderer;
		add( "renderer", renderer,"rendererOptions" );
	}
	public ChartOptions getRendererOptions() {
		return renderer.getOptions();
	}
	public boolean isPredraw() {
		return predraw;
	}
	public void setPredraw(boolean predraw) {
		this.predraw = predraw;
		add( "predraw", predraw );
	}
}//class

