package net.sf.click.chart.jqplot.renderer;

import java.util.ArrayList;
import java.util.List;

import net.sf.click.chart.Color;
import net.sf.click.chart.jqplot.plugins.Plugin;

/**
 * A renderer for Categories axies. The renderer can be customized through its options.
 * @author steff
 * http://www.jqplot.com/docs/files/plugins/jqplot-categoryAxisRenderer-js.html
 */
public class CanvasAxisLabelRenderer extends LabelRenderer implements Plugin {

	private List<String> listJavaScriptImport = new ArrayList<String>();

	public CanvasAxisLabelRenderer() {
		super( "$.jqplot.CanvasAxisLabelRenderer" );

		//javascript imports
		listJavaScriptImport.add("/jqplot/plugins/jqplot.canvasAxisLabelRenderer.js");
		listJavaScriptImport.add("/jqplot/plugins/jqplot.canvasTextRenderer.js");
	}//cons

	@Override
	public List<String> getListJavaScriptImport() {
		return listJavaScriptImport;
	}


	/** angle of text, measured clockwise from x axis.*/
	private double angle =0;
	/** wether or not to show the tick (mark and label).*/
	private boolean show = true;
	/** wether or not to show the label.*/
	private boolean showLabel = true;
	/** label for the axis.*/
	private String label = "";
	/** CSS spec for the font-family css attribute. */
	private String fontFamily = "";
	/** CSS spec for font size.*/
	private String fontSize = "11px";
	/** */
	private String fontWeight = "normal";
	/** 	Multiplier to condense or expand font width. */
	private double fontStretch = 1.0d;
	/** css spec for the color attribute.*/
	private Color textColor;
	/** true to turn on native canvas font support in Mozilla 3.5+ and Safari 4+. */
	private boolean enableFontSupport = false;
	/** Point to pixel scaling factor, used for computing height of bounding box around a label. */
	private Double pt2px = null;


	public double getAngle() {
		return angle;
	}//met
	public void setAngle(double angle) {
		this.angle = angle;
		options.add( "angle", angle );
	}//met
	public boolean isShow() {
		return show;
	}//met
	public void setShow(boolean show) {
		this.show = show;
		options.add( "show", show );
	}//met
	public boolean isShowLabel() {
		return showLabel;
	}//met
	public void setShowLabel(boolean showLabel) {
		this.showLabel = showLabel;
		options.add( "showLabel", showLabel );
	}//met
	public String getLabel() {
		return label;
	}//met
	public void setLabel(String label) {
		this.label = label;
		options.add( "label", label );
	}//met
	public String getFontFamily() {
		return fontFamily;
	}//met
	public void setFontFamily(String fontFamily) {
		this.fontFamily = fontFamily;
		options.add( "fontFamily", fontFamily );
	}//met
	public String getFontSize() {
		return fontSize;
	}//met
	public void setFontSize(String string) {
		this.fontSize = string;
		options.add( "fontSize", string );
	}//met
	public String getFontWeight() {
		return fontWeight;
	}//met
	public void setFontWeight(String fontWeight) {
		this.fontWeight = fontWeight;
		options.add( "fontWeight", fontWeight );
	}//met
	public double getFontStretch() {
		return fontStretch;
	}//met
	public void setFontStretch(double fontStretch) {
		this.fontStretch = fontStretch;
		options.add( "fontStretch", fontStretch );
	}//met
	public Color getTextColor() {
		return textColor;
	}//met
	public void setTextColor(Color textColor) {
		this.textColor = textColor;
		options.add( "textColor", textColor );
	}//met
	public boolean isEnableFontSupport() {
		return enableFontSupport;
	}//met
	public void setEnableFontSupport(boolean enableFontSupport) {
		this.enableFontSupport = enableFontSupport;
		options.add( "enableFontSupport", enableFontSupport );
	}//met
	public Double getPt2px() {
		return pt2px;
	}//met
	public void setPt2px(Double pt2px) {
		this.pt2px = pt2px;
		options.add( "pt2px", pt2px );
	}//met
}//class
