package net.sf.click.chart.jqplot;

import net.sf.click.chart.ChartOptions;
import net.sf.click.chart.Color;
import net.sf.click.chart.jqplot.renderer.AxisLabelRenderer;
import net.sf.click.chart.jqplot.renderer.AxisRenderer;
import net.sf.click.chart.jqplot.renderer.AxisTickRenderer;
import net.sf.click.chart.jqplot.renderer.LabelRenderer;
import net.sf.click.chart.jqplot.renderer.LinearAxisRenderer;
import net.sf.click.chart.jqplot.renderer.TickRenderer;

import java.util.Map;

//TODO should we have a more generic base class Double/Date...
public class Axis extends ChartOptions{
	private boolean show = false;
	private TickRenderer tickRenderer = new AxisTickRenderer();
	private LabelRenderer labelRenderer = new AxisLabelRenderer();
	private String label = null;
	private boolean showLabel = true;
	private Double min = null;
	private Double max = null;
	private boolean autoscale = false;
	private double pad = 1.2d;
	private Double padMin = null;
	private Double padMax = null;
	private String[] ticks = null;
	private Integer numberTicks = null;
	private Double tickInterval = null;
	private AxisRenderer renderer = new LinearAxisRenderer();
	private boolean showTicks = true;
	private boolean showTickMarks = true;
	private boolean showMinorTick = true;
	private boolean useSeriesColor = false;
	private Integer borderWidth = null;
	private Color borderColor = null;
	private Boolean syncTicks = null;
	private int tickSpacing = 75;

	public boolean isShow() {
		return show;
	}
	public void setShow(boolean show) {
		this.show = show;
		add( "show", show );
	}
	public TickRenderer getTickRenderer() {
		return tickRenderer;
	}
	public void setTickRenderer(TickRenderer tickRenderer) {
		this.tickRenderer = tickRenderer;
		add( "tickRenderer", tickRenderer, "tickOptions" );
	}
	public ChartOptions getTickOptions() {
		add( "tickOptions", tickRenderer.getOptions() );
		return tickRenderer.getOptions();
	}
	public LabelRenderer getLabelRenderer() {
		return labelRenderer;
	}
	public void setLabelRenderer(LabelRenderer labelRenderer) {
		this.labelRenderer = labelRenderer;
		add( "labelRenderer", labelRenderer, "labelOptions" );
	}
	public ChartOptions getLabelOptions() {
		add( "labelOptions", labelRenderer.getOptions() );
		return labelRenderer.getOptions();
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
		add( "label", label );
	}
	public boolean isShowLabel() {
		return showLabel;
	}
	public void setShowLabel(boolean showLabel) {
		this.showLabel = showLabel;
		add( "showLabel", showLabel );
	}
	public Double getMin() {
		return min;
	}
	public void setMin(Double min) {
		this.min = min;
		add( "min", min );
	}
	public Double getMax() {
		return max;
	}
	public void setMax(Double max) {
		this.max = max;
		add( "max", max );
	}
	public boolean isAutoscale() {
		return autoscale;
	}
	public void setAutoscale(boolean autoscale) {
		this.autoscale = autoscale;
		add( "autoscale", autoscale );
	}
	public double getPad() {
		return pad;
	}
	public void setPad(double pad) {
		this.pad = pad;
		add( "pad", pad );
	}
	public Double getPadMin() {
		return padMin;
	}
	public void setPadMin(Double padMin) {
		this.padMin = padMin;
		add( "padMin", padMin );
	}
	public Double getPadMax() {
		return padMax;
	}
	public void setPadMax(Double padMax) {
		this.padMax = padMax;
		add( "padMax", padMax );
	}
	public String[] getTicks() {
		return ticks;
	}
	public void setTicks(String[] ticks) {
		this.ticks = ticks;
		add( "ticks", ticks );
	}
	public void setTicks( Map<Integer, String> tickMap) {
		//this.ticks = ticks;
		add( "ticks", tickMap );
	}
	public Integer getNumberTicks() {
		return numberTicks;
	}
	public void setNumberTicks(Integer numberTicks) {
		this.numberTicks = numberTicks;
		add( "numberTicks", numberTicks );
	}
	public Double getTickInterval() {
		return tickInterval;
	}
	public void setTickInterval(Double tickInterval) {
		this.tickInterval = tickInterval;
		add( "tickInterval", tickInterval );
	}
	public AxisRenderer getRenderer() {
		return renderer;
	}
	public void setRenderer(AxisRenderer renderer) {
		this.renderer = renderer;
		add( "renderer", renderer, "rendererOptions" );
	}
	public ChartOptions getRendererOptions() {
		add( "rendererOptions", renderer.getOptions() );
		return renderer.getOptions();
	}

	public boolean isShowTicks() {
		return showTicks;
	}
	public void setShowTicks(boolean showTicks) {
		this.showTicks = showTicks;
		add( "showTicks", showTicks );
	}
	public boolean isShowTickMarks() {
		return showTickMarks;
	}
	public void setShowTickMarks(boolean showTickMarks) {
		this.showTickMarks = showTickMarks;
		add( "showTickMarks", showTickMarks );
	}
	public boolean isShowMinorTick() {
		return showMinorTick;
	}
	public void setShowMinorTick(boolean showMinorTick) {
		this.showMinorTick = showMinorTick;
		add( "showMinorTick", showMinorTick );
	}
	public boolean isUseSeriesColor() {
		return useSeriesColor;
	}
	public void setUseSeriesColor(boolean useSeriesColor) {
		this.useSeriesColor = useSeriesColor;
		add( "useSeriesColor", useSeriesColor );
	}
	public Integer getBorderWidth() {
		return borderWidth;
	}
	public void setBorderWidth(Integer borderWidth) {
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
	public Boolean getSyncTicks() {
		return syncTicks;
	}
	public void setSyncTicks(Boolean syncTicks) {
		this.syncTicks = syncTicks;
		add( "syncTicks", syncTicks );
	}
	public int getTickSpacing() {
		return tickSpacing;
	}
	public void setTickSpacing(int tickSpacing) {
		this.tickSpacing = tickSpacing;
		add( "tickSpacing", tickSpacing );
	}


}//class