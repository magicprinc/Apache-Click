package net.sf.click.chart.jqplot.renderer;

import java.util.ArrayList;
import java.util.List;

import net.sf.click.chart.Color;
import net.sf.click.chart.jqplot.Series;
import net.sf.click.chart.jqplot.plugins.Plugin;

/**
 * 
 * @author steff
 * 
 */
public class BubbleRenderer extends SeriesRenderer implements Plugin {

	public BubbleRenderer() {
		this( null );

	}//cons
	
	public BubbleRenderer( Series s) {
		super( "$.jqplot.BubbleRenderer", s );

	}//cons

	@Override
	public List<String> getListJavaScriptImport() {
		List<String> listJavaScriptImport = new ArrayList<String>();
		listJavaScriptImport.add("/jqplot/plugins/jqplot.bubbleRenderer.js");
		return listJavaScriptImport;
	}

	/** True to vary the color of each bubble in this series according to
	 *the seriesColors array.  False to set each bubble to the color
	 *specified on this series.  This has no effect if a css background color
	 *option is specified in the renderer css options.*/
	private boolean varyBubbleColors = true;
	/** True to scale the bubble radius based on plot size.
	 *False will use the radius value as provided as a raw pixel value for
	 *bubble radius.*/
	private boolean autoscaleBubbles = true;
	/** Multiplier the bubble size if autoscaleBubbles is true.*/
	private double autoscaleMultiplier = 1.0;
	/** Factor which decreases bubble size based on how many bubbles on on the chart.
	 *0 means no adjustment for number of bubbles.  Negative values will decrease
	 *size of bubbles as more bubbles are added.  Values between 0 and -0.2
	 *should work well.*/
	private double autoscalePointsFactor = -0.07;
	/** True to escape html in bubble label text.*/
	private boolean escapeHtml = true;
	/** True to highlight bubbles when moused over.
	 *This must be false to enable highlightMouseDown to highlight when clicking on a slice.*/
	private boolean highlightMouseOver = true;
	/** True to highlight when a mouse button is pressed over a bubble.
	 *This will be disabled if highlightMouseOver is true.*/
	private boolean highlightMouseDown = false;
	/** An array of colors to use when highlighting a slice.  Calculated automatically
	 *if not supplied.*/
	private Color[] highlightColors = new Color[0];
	/** Alpha transparency to apply to all bubbles in this series.*/
	private double bubbleAlpha = 1.0;
	/** Alpha transparency to apply when highlighting bubble.
	 *Set to value of bubbleAlpha by default.*/
	private Double highlightAlpha = null;
	/** True to color the bubbles with gradient fills instead of flat colors.
	 *NOT AVAILABLE IN IE due to lack of excanvas support for radial gradient fills.
	 *will be ignored in IE.*/
	private boolean bubbleGradients = false;
	/** True to show labels on bubbles (if any), false to not show.*/
	private boolean showLabels = true;
	/** array of [point index, radius] which will be sorted in descending order to plot 
	 *largest points below smaller points.*/
	private String[] radii = new String[0];
	private double maxRadius = 0d;
	/** index of the currenty highlighted point, if any*/
	private Integer _highlightedPoint = null;
	/** array of jQuery labels.*/
	private String[] labels = new String[0];
	private String[] bubbleCanvases = new String[0];
	public boolean isVaryBubbleColors() {
		return varyBubbleColors;
	}
	public void setVaryBubbleColors(boolean varyBubbleColors) {
		this.varyBubbleColors = varyBubbleColors;
		options.add( "varyBubbleColors", varyBubbleColors );
	}
	public boolean isAutoscaleBubbles() {
		return autoscaleBubbles;
	}
	public void setAutoscaleBubbles(boolean autoscaleBubbles) {
		this.autoscaleBubbles = autoscaleBubbles;
		options.add( "autoscaleBubbles", autoscaleBubbles );
	}
	public double getAutoscaleMultiplier() {
		return autoscaleMultiplier;
	}
	public void setAutoscaleMultiplier(double autoscaleMultiplier) {
		this.autoscaleMultiplier = autoscaleMultiplier;
		options.add( "autoscaleMultiplier", autoscaleMultiplier );
	}
	public double getAutoscalePointsFactor() {
		return autoscalePointsFactor;
	}
	public void setAutoscalePointsFactor(double autoscalePointsFactor) {
		this.autoscalePointsFactor = autoscalePointsFactor;
		options.add( "autoscalePointsFactor", autoscalePointsFactor );
	}
	public boolean isEscapeHtml() {
		return escapeHtml;
	}
	public void setEscapeHtml(boolean escapeHtml) {
		this.escapeHtml = escapeHtml;
		options.add( "escapeHtml", escapeHtml );
	}
	public boolean isHighlightMouseOver() {
		return highlightMouseOver;
	}
	public void setHighlightMouseOver(boolean highlightMouseOver) {
		this.highlightMouseOver = highlightMouseOver;
		options.add( "highlightMouseOver", highlightMouseOver );
	}
	public boolean isHighlightMouseDown() {
		return highlightMouseDown;
	}
	public void setHighlightMouseDown(boolean highlightMouseDown) {
		this.highlightMouseDown = highlightMouseDown;
		options.add( "highlightMouseDown", highlightMouseDown );
	}
	public Color[] getHighlightColors() {
		return highlightColors;
	}
	public void setHighlightColors(Color[] highlightColors) {
		this.highlightColors = highlightColors;
		options.add( "highlightColors", highlightColors );
	}
	public double getBubbleAlpha() {
		return bubbleAlpha;
	}
	public void setBubbleAlpha(double bubbleAlpha) {
		this.bubbleAlpha = bubbleAlpha;
		options.add( "bubbleAlpha", bubbleAlpha );
	}
	public Double getHighlightAlpha() {
		return highlightAlpha;
	}
	public void setHighlightAlpha(Double highlightAlpha) {
		this.highlightAlpha = highlightAlpha;
		options.add( "highlightAlpha", highlightAlpha );
	}
	public boolean isBubbleGradients() {
		return bubbleGradients;
	}
	public void setBubbleGradients(boolean bubbleGradients) {
		this.bubbleGradients = bubbleGradients;
		options.add( "bubbleGradients", bubbleGradients );
	}
	public boolean isShowLabels() {
		return showLabels;
	}
	public void setShowLabels(boolean showLabels) {
		this.showLabels = showLabels;
		options.add( "showLabels", showLabels );
	}
	public String[] getRadii() {
		return radii;
	}
	public void setRadii(String[] radii) {
		this.radii = radii;
		options.add( "radii", radii );
	}
	public double getMaxRadius() {
		return maxRadius;
	}
	public void setMaxRadius(double maxRadius) {
		this.maxRadius = maxRadius;
		options.add( "maxRadius", maxRadius );
	}
	public Integer getHighlightedPoint() {
		return _highlightedPoint;
	}
	public void setHighlightedPoint(Integer _highlightedPoint) {
		this._highlightedPoint = _highlightedPoint;
		options.add( "_highlightedPoint", _highlightedPoint );
	}
	public String[] getLabels() {
		return labels;
	}
	public void setLabels(String[] labels) {
		this.labels = labels;
		options.add( "labels", labels );
	}
	public String[] getBubbleCanvases() {
		return bubbleCanvases;
	}
	public void setBubbleCanvases(String[] bubbleCanvases) {
		this.bubbleCanvases = bubbleCanvases;
		options.add( "bubbleCanvases", bubbleCanvases );
	}

}//class
