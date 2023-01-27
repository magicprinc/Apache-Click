package net.sf.click.chart.jqplot.renderer;

import net.sf.click.chart.Color;
import net.sf.click.chart.jqplot.Series;
import net.sf.click.chart.jqplot.plugins.Plugin;

import java.util.ArrayList;
import java.util.List;

/**
 * A renderer for Bars charts. The renderer can be customized through its options.
 * @author steff
 * http://www.jqplot.com/docs/files/plugins/jqplot-barRenderer-js.html
 */
public class MeterGaugeRenderer extends SeriesRenderer implements Plugin {

	public MeterGaugeRenderer() {
		this( null );
	}//cons

	public MeterGaugeRenderer( Series s) {
		super( "$.jqplot.MeterGaugeRenderer", s );
	}//cons

	@Override
	public List<String> getListJavaScriptImport() {
		List<String> listJavaScriptImport = new ArrayList<String>();
		//javascript imports
		listJavaScriptImport.add("/jqplot/plugins/jqplot.meterGaugeRenderer.js");
		return listJavaScriptImport;
	}

	public enum LabelPosition
	{
		INSIDE( "inside" ),
		BOTTOM( "bottom" );

		private String postion;
		private LabelPosition( String s ) {
			this.postion = s;
		}//cons
		@Override
		public String toString() {
			return postion;
		}//met
	}//enum

        /** Outer diameter of the meterGauge, auto computed by default. */
        private Double  diameter = null;
        /** padding  between the meterGauge and plot edges, auto
        calculated by default.*/
        private Double  padding = null;
        /**offset of the shadow from the gauge ring and offset of
        each succesive stroke of the shadow from the last.*/
        private int shadowOffset = 2;
        /** transparency of the shadow (0 = transparent, 1 = opaque)*/
        private double shadowAlpha = 0.07;
        /** number of strokes to apply to the shadow,
        each stroke offset shadowOffset from the last.*/
        private int shadowDepth = 4;
        /** background color of the inside of the gauge.*/
        private Color background = new Color( 0xefefef );
        /** color of the outer ring, hub, and needle of the gauge.*/
        private Color ringColor = new Color( 0xBBC6D0 );
        /** needle color not implemented yet.*/
        private Color needleColor = new Color( 0xC3D3E5 );
        /** color of the tick marks around the gauge.*/
        private Color tickColor = new Color( 0x989898 );
        /** width of the ring around the gauge.  Auto computed by default.*/
        private Integer ringWidth = null;
        /** Minimum value on the gauge.  Auto computed by default*/
        private Integer min = null;
        /** Maximum value on the gauge. Auto computed by default*/
        private Integer max = null;
        /** Array of tick values. Auto computed by default.*/
        private Double[] ticks = new Double[0];
        /** true to show ticks around gauge.*/
        private boolean showTicks = true;
        /** true to show tick labels next to ticks.*/
        private boolean showTickLabels = true;
        /** A gauge label like 'kph' or 'Volts'*/
        private String label = null;
        /** Number of Pixels to offset the label up (-) or down (+) from its default position.*/
        private int labelHeightAdjust = 0;
        /** Where to position the label, either 'inside' or 'bottom'.*/
        private LabelPosition labelPosition = LabelPosition.INSIDE;
        /** Array of ranges to be drawn around the gauge.
         Array of form:
         > [value1, value2, ...]
         indicating the values for the first, second, ... intervals.*/
        private Double[] intervals = new Double[0];
        /** Array of colors to use for the intervals.*/
        private Color[] intervalColors = new Color[] {new Color ( 0x4bb2c ), new Color( 0xEAA228), new Color( 0xc5b47f), new Color( 0x579575), new Color( 0x839557), new Color( 0x958c12), new Color( 0x953579), new Color( 0x4b5de4), new Color( 0xd8b83f), new Color( 0xff5800), new Color( 0x0085cc), new Color( 0xc747a3), new Color( 0xcddf54), new Color( 0xFBD178), new Color( 0x26B4E3), new Color( 0xbd70c7 ) };
        /** Radius of the inner circle of the interval ring.*/
        private Double intervalInnerRadius =  null;
        /** Radius of the outer circle of the interval ring.*/
        private Double intervalOuterRadius = null;
        private MeterGaugeTickRenderer tickRenderer = new MeterGaugeTickRenderer();
        /** ticks spaced every 1, 2, 2.5, 5, 10, 20, .1, .2, .25, .5, etc.*/
        private double[] tickPositions = new double [] {1, 2, 2.5, 5, 10};
        /** Degrees between ticks.  This is a target number, if
        // incompatible span and ticks are supplied, a suitable
        // spacing close to this value will be computed.*/
        private int tickSpacing = 30;

        private Integer numberMinorTicks = null;
        /** Radius of the hub at the bottom center of gauge which the needle attaches to.
        // Auto computed by default*/
        private Double hubRadius = null;
        /** padding of the tick marks to the outer ring and the tick labels to marks.
        // Auto computed by default.*/
        private Integer tickPadding = null;
        /** Maximum thickness the needle.  Auto computed by default.*/
        private Integer needleThickness = null;
        /** Padding between needle and inner edge of the ring when the needle is at the min or max gauge value.*/
        private int needlePad = 6;
        /** True will stop needle just below/above the  min/max values if data is below/above min/max,
         as if the meter is "pegged".*/
        private boolean pegNeedle = true;

		public Double getDiameter() {
			return diameter;
		}
		public void setDiameter(Double diameter) {
			this.diameter = diameter;
			options.add( "diameter", diameter );
		}
		public Double getPadding() {
			return padding;
		}
		public void setPadding(Double padding) {
			this.padding = padding;
			options.add( "padding", padding);
		}
		public int getShadowOffset() {
			return shadowOffset;
		}
		public void setShadowOffset(int shadowOffset) {
			this.shadowOffset = shadowOffset;
			options.add( "shadowOffset", shadowOffset);
		}
		public double getShadowAlpha() {
			return shadowAlpha;
		}
		public void setShadowAlpha(double shadowAlpha) {
			this.shadowAlpha = shadowAlpha;
			options.add( "shadowAlpha", shadowAlpha);
		}
		public int getShadowDepth() {
			return shadowDepth;
		}
		public void setShadowDepth(int shadowDepth) {
			this.shadowDepth = shadowDepth;
			options.add( "shadowDepth", shadowDepth);
		}
		public Color getBackground() {
			return background;
		}
		public void setBackground(Color background) {
			this.background = background;
			options.add( "background", background);
		}
		public Color getRingColor() {
			return ringColor;
		}
		public void setRingColor(Color ringColor) {
			this.ringColor = ringColor;
			options.add( "ringColor", ringColor);
		}
		public Color getNeedleColor() {
			return needleColor;
		}
		public void setNeedleColor(Color needleColor) {
			this.needleColor = needleColor;
			options.add( "needleColor", needleColor);
		}
		public Color getTickColor() {
			return tickColor;
		}
		public void setTickColor(Color tickColor) {
			this.tickColor = tickColor;
			options.add( "tickColor", tickColor);
		}
		public Integer getRingWidth() {
			return ringWidth;
		}
		public void setRingWidth(Integer ringWidth) {
			this.ringWidth = ringWidth;
			options.add( "ringWidth", ringWidth);
		}
		public Integer getMin() {
			return min;
		}
		public void setMin(Integer min) {
			this.min = min;
			options.add( "min", min);
		}
		public Integer getMax() {
			return max;
		}
		public void setMax(Integer max) {
			this.max = max;
			options.add( "max", max);
		}
		public Double[] getTicks() {
			return ticks;
		}
		public void setTicks(Double[] ticks) {
			this.ticks = ticks;
			options.add( "ticks", ticks);
		}
		public boolean isShowTicks() {
			return showTicks;
		}
		public void setShowTicks(boolean showTicks) {
			this.showTicks = showTicks;
			options.add( "showTicks", showTicks);
		}
		public boolean isShowTickLabels() {
			return showTickLabels;
		}
		public void setShowTickLabels(boolean showTickLabels) {
			this.showTickLabels = showTickLabels;
			options.add( "showTickLabels", showTickLabels);
		}
		public String getLabel() {
			return label;
		}
		public void setLabel(String label) {
			this.label = label;
			options.add( "label", label);
		}
		public int getLabelHeightAdjust() {
			return labelHeightAdjust;
		}
		public void setLabelHeightAdjust(int labelHeightAdjust) {
			this.labelHeightAdjust = labelHeightAdjust;
			options.add( "labelHeightAdjust", labelHeightAdjust);
		}
		public LabelPosition getLabelPosition() {
			return labelPosition;
		}
		public void setLabelPosition(LabelPosition labelPosition) {
			this.labelPosition = labelPosition;
			options.add( "labelPosition", labelPosition.toString() );
		}
		public Double[] getIntervals() {
			return intervals;
		}
		public void setIntervals(Double[] intervals) {
			this.intervals = intervals;
			options.add( "intervals", intervals);
		}
		public Color[] getIntervalColors() {
			return intervalColors;
		}
		public void setIntervalColors(Color[] intervalColors) {
			this.intervalColors = intervalColors;
			options.add( "intervalColors", intervalColors);
		}
		public Double getIntervalInnerRadius() {
			return intervalInnerRadius;
		}
		public void setIntervalInnerRadius(Double intervalInnerRadius) {
			this.intervalInnerRadius = intervalInnerRadius;
			options.add( "intervalInnerRadius", intervalInnerRadius);
		}
		public Double getIntervalOuterRadius() {
			return intervalOuterRadius;
		}
		public void setIntervalOuterRadius(Double intervalOuterRadius) {
			this.intervalOuterRadius = intervalOuterRadius;
			options.add( "intervalOuterRadius", intervalOuterRadius);
		}
		public MeterGaugeTickRenderer getTickRenderer() {
			return tickRenderer;
		}
		public void setTickRenderer(MeterGaugeTickRenderer tickRenderer) {
			this.tickRenderer = tickRenderer;
			options.add( "tickRenderer", tickRenderer, "tickRendererOptions");
		}
		public double[] getTickPositions() {
			return tickPositions;
		}
		public void setTickPositions(double[] tickPositions) {
			this.tickPositions = tickPositions;
			options.add( "tickPositions", tickPositions);
		}
		public int getTickSpacing() {
			return tickSpacing;
		}
		public void setTickSpacing(int tickSpacing) {
			this.tickSpacing = tickSpacing;
			options.add( "tickSpacing", tickSpacing);
		}
		public Integer getNumberMinorTicks() {
			return numberMinorTicks;
		}
		public void setNumberMinorTicks(Integer numberMinorTicks) {
			this.numberMinorTicks = numberMinorTicks;
			options.add( "numberMinorTicks", numberMinorTicks);
		}
		public Double getHubRadius() {
			return hubRadius;
		}
		public void setHubRadius(Double hubRadius) {
			this.hubRadius = hubRadius;
			options.add( "hubRadius", hubRadius);
		}
		public Integer getTickPadding() {
			return tickPadding;
		}
		public void setTickPadding(Integer tickPadding) {
			this.tickPadding = tickPadding;
			options.add( "tickPadding", tickPadding);
		}
		public Integer getNeedleThickness() {
			return needleThickness;
		}
		public void setNeedleThickness(Integer needleThickness) {
			this.needleThickness = needleThickness;
			options.add( "needleThickness", needleThickness);
		}
		public int getNeedlePad() {
			return needlePad;
		}
		public void setNeedlePad(int needlePad) {
			this.needlePad = needlePad;
			options.add( "needlePad", needlePad);
		}
		public boolean isPegNeedle() {
			return pegNeedle;
		}
		public void setPegNeedle(boolean pegNeedle) {
			this.pegNeedle = pegNeedle;
			options.add( "pegNeedle", pegNeedle);
		}
}//class