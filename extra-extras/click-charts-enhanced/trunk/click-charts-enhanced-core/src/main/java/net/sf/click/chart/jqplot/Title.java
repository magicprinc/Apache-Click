package net.sf.click.chart.jqplot;

import net.sf.click.chart.ChartOptions;
import net.sf.click.chart.Color;
import net.sf.click.chart.jqplot.renderer.DivTitleRenderer;
import net.sf.click.chart.jqplot.renderer.TitleRenderer;


public class Title extends ChartOptions {
	
	private String text = "";
	private boolean show = false;
	private String fontFamily = "";
	private int fontSize = 0;
	private String textAlign = "";
	private Color textColor = null;
	private TitleRenderer renderer = new DivTitleRenderer();
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
		add( "text", text );
	}
	public boolean isShow() {
		return show;
	}
	public void setShow(boolean show) {
		this.show = show;
		add( "show", show );
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
	public String getTextAlign() {
		return textAlign;
	}
	public void setTextAlign(String textAlign) {
		this.textAlign = textAlign;
		add( "textAlign", textAlign );
	}
	public Color getTextColor() {
		return textColor;
	}
	public void setTextColor(Color textColor) {
		this.textColor = textColor;
		add( "textColor", textColor );
	}
	public TitleRenderer getRenderer() {
		return renderer;
	}
	public void setRenderer(TitleRenderer renderer) {
		this.renderer = renderer;
		add( "renderer", renderer, "rendererOptions" );
	}
	public ChartOptions getRendererOptions() {
		add( "rendererOptions", renderer.getOptions() );
		return renderer.getOptions();
	}

}//class
