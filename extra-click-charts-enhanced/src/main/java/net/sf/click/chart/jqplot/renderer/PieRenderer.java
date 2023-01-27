package net.sf.click.chart.jqplot.renderer;

import net.sf.click.chart.jqplot.Series;
import net.sf.click.chart.jqplot.plugins.Plugin;

import java.util.ArrayList;
import java.util.List;

/**
 * A renderer for pie charts. The renderer can be customized through its options.
 * @author steff
 * http://www.jqplot.com/docs/files/plugins/jqplot-pieRenderer-js.html
 */
public class PieRenderer extends SeriesRenderer implements Plugin {

	public PieRenderer() {
		this( null );
	}//cons

	public PieRenderer( Series s) {
		super( "$.jqplot.PieRenderer", s );
	}//cons

	@Override
	public List<String> getListJavaScriptImport() {
		List<String> listJavaScriptImport = new ArrayList<String>();
		//javascript imports
		listJavaScriptImport.add("/jqplot/plugins/jqplot.pieRenderer.js");
		return listJavaScriptImport;
	}

		/** diameter of the pie, auto computed by default*/
		private Double diameter = null;
		/** padding between the pie and plot edges, legend, etc.*/
		private int padding = 20;
		/** pixels spacing between pie slices.*/
		private int sliceMargin = 0;
		/** true or false, wether to fill the slices.*/
		private boolean fill = true;
		/** true or false, wether to use shadows.*/
		private boolean shadow = true;
		/** offset of the shadow from the slice and offset of each succesive stroke of the shadow from the last.*/
		private double shadowOffset = 2d;
		/** transparency of the shadow (0 = transparent, 1 = opaque)*/
		private double shadowAlpha = 0.07d;
		/** number of strokes to apply to the shadow, each stroke offset shadowOffset from the last.*/
		private int shadowDepth = 5;
		/** pixels spacing between pie slices.*/
		private int lineWidth = 2;

		// --- methods --

		public double getShadowOffset() {
			return shadowOffset;
		}//met
		public Double getDiameter() {
			return diameter;
		}//met
		public void setDiameter(Double diameter) {
			this.diameter = diameter;
			options.add( "diameter", diameter );
		}//met
		public int getPadding() {
			return padding;
		}//met
		public void setPadding(int padding) {
			this.padding = padding;
			options.add( "padding", padding );
		}//met
		public int getSliceMargin() {
			return sliceMargin;
		}//met
		public void setSliceMargin(int sliceMargin) {
			this.sliceMargin = sliceMargin;
			options.add( "sliceMargin", sliceMargin );
		}//met
		public boolean isFill() {
			return fill;
		}//met
		public void setFill(boolean fill) {
			this.fill = fill;
			options.add( "fill", fill );
		}//met
		public boolean isShadow() {
			return shadow;
		}//met
		public void setShadow(boolean shadow) {
			this.shadow = shadow;
			options.add( "shadow", shadow );
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
		public int getLineWidth() {
			return lineWidth;
		}
		public void setLineWidth(int lineWidth) {
			this.lineWidth = lineWidth;
			options.add( "lineWidth", lineWidth );
		}
}//class