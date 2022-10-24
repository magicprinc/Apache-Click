package net.sf.click.chart.jqplot.renderer;

import java.util.ArrayList;
import java.util.List;


import net.sf.click.chart.jqplot.Series;
import net.sf.click.chart.jqplot.plugins.Plugin;

/**
 * A renderer for Bars charts. The renderer can be customized through its ((BarRendererOptions) options).
 * @author steff
 * http://www.jqplot.com/docs/files/plugins/jqplot-barRenderer-js.html
 */
public class BarRenderer extends SeriesRenderer implements Plugin {

	public BarRenderer() {
		this( null );
	}//cons
	
	public BarRenderer( Series s ) {
		super( "$.jqplot.BarRenderer",s );
	}//cons

	@Override
	public List<String> getListJavaScriptImport() {
		List<String> listJavaScriptImport = new ArrayList<String>();
		//javascript imports
		listJavaScriptImport.add("/jqplot/plugins/jqplot.barRenderer.js");
		return listJavaScriptImport;
	}


	public enum BarDirection
	{
		HORIZONTAL( "horizontal" ),
		VERTICAL( "vertical" );

		private String direction;
		private BarDirection( String s ) {
			this.direction = s;			
		}//cons
		@Override
		public String toString() {
			return direction;
		}//met
	}//enum


	/** Number of pixels between adjacent bars at the same axis value.*/
	private int barPadding = 8;
	/** Number of pixels between groups of bars at adjacent axis values.*/
	private int barMargin = 10;
	/** ‘vertical’ = up and down bars, ‘horizontal’ = side to side bars*/
	private BarDirection barDirection = BarDirection.HORIZONTAL;
	/** Width of the bar in pixels (auto by devaul).  */
	private Integer barWidth = 10;
	/** offset of the shadow from the slice and offset of each succesive stroke of the shadow from the last.*/
	private double shadowOffset = 2d;
	/** number of strokes to apply to the shadow, each stroke offset shadowOffset from the last.*/
	private int shadowDepth = 5;
	/** transparency of the shadow (0 = transparent, 1 = opaque)*/
	private double shadowAlpha = 0.08d;
	/** true to enable waterfall plot.*/
	private boolean waterfall = false;
	/** true to color each bar separately.*/
	private boolean varyBarColor = false;

	// --- methods --
	public int getBarPadding() {
		return barPadding;
	}//met
	public void setBarPadding(int barPadding) {
		this.barPadding = barPadding;
		options.add( "barPadding", barPadding );
	}//met
	public int getBarMargin() {
		return barMargin;
	}//met
	public void setBarMargin(int barMargin) {
		this.barMargin = barMargin;
		options.add( "barMargin", barMargin );
	}//met
	public BarDirection getBarDirection() {
		return barDirection;
	}//met
	public void setBarDirection(BarDirection barDirection) {
		this.barDirection = barDirection;
		options.add( "barDirection", barDirection.toString() );
	}//met
	public Integer getBarWidth() {
		return barWidth;
	}//met
	public void setBarWidth(Integer barWidth) {
		this.barWidth = barWidth;
		options.add( "barWidth", barWidth );
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
	public boolean isWaterfall() {
		return waterfall;
	}//met
	public void setWaterfall(boolean waterfall) {
		this.waterfall = waterfall;
		options.add( "waterfall", waterfall );
	}//met
	public boolean isVaryBarColor() {
		return varyBarColor;
	}//met
	public void setVaryBarColor(boolean varyBarColor) {
		this.varyBarColor = varyBarColor;
		options.add( "varyBarColor", varyBarColor );
	}//met
}//class
