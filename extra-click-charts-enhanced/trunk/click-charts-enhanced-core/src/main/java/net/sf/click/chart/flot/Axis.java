package net.sf.click.chart.flot;

import java.util.Map;

import net.sf.click.chart.ChartOptions;

public class Axis extends FlotChartOptions {
	public enum AxisMode
	{
		TIME( "time" );

		private String mode;
		private AxisMode( String s ) {
			this.mode = s;			
		}//cons
		@Override
		public String toString() {
			return mode;
		}//met
	}//enum
	
	private AxisMode mode;
    private Double min;
    private Double max;
    private Double autoscaleMargin;
    
    private Double labelWidth;
    private Double labelHeight;

    private String transform;
    private String inverseTransform;
    
    private Integer ticks;
    private Double tickSize;
    private Double minTickSize;
    private String tickFormatter;
    private Double tickDecimals;
    
	public AxisMode getMode() {
		return mode;
	}
	public void setMode(AxisMode mode) {
		this.mode = mode;
		add( "mode", mode.toString() );
	}
	public Double getMin() {
		return min;
	}
	public void setMin(Double min) {
		this.min = min;
		add( "min", min);
	}
	public Double getMax() {
		return max;
	}
	public void setMax(Double max) {
		this.max = max;
		add( "max", max);
	}
	public Double getAutoscaleMargin() {
		return autoscaleMargin;
	}
	public void setAutoscaleMargin(Double autoscaleMargin) {
		this.autoscaleMargin = autoscaleMargin;
		add( "autoscaleMargin", autoscaleMargin);
	}
	public Double getLabelWidth() {
		return labelWidth;
	}
	public void setLabelWidth(Double labelWidth) {
		this.labelWidth = labelWidth;
		add( "labelWidth", labelWidth);
	}
	public Double getLabelHeight() {
		return labelHeight;
	}
	public void setLabelHeight(Double labelHeight) {
		this.labelHeight = labelHeight;
		add( "labelHeight", labelHeight);
	}
	public String getTransform() {
		return transform;
	}
	public void setTransform(String transform) {
		this.transform = transform;
		add( "transform", transform);
	}
	public String getInverseTransform() {
		return inverseTransform;
	}
	public void setInverseTransform(String inverseTransform) {
		this.inverseTransform = inverseTransform;
		add( "inverseTransform", inverseTransform);
	}
	public Integer getTicks() {
		return ticks;
	}
	public void setTicks(Integer ticks) {
		this.ticks = ticks;
		add( "ticks", ticks);
	}
	
	public void setTicks( Map<Double, String> mapTicks ) {
		this.ticks = mapTicks.size();
		add( "ticks", mapTicks);
	}
	
	public Double getTickSize() {
		return tickSize;
	}
	public void setTickSize(Double tickSize) {
		this.tickSize = tickSize;
		add( "tickSize", tickSize);
	}
	public Double getMinTickSize() {
		return minTickSize;
	}
	public void setMinTickSize(Double minTickSize) {
		this.minTickSize = minTickSize;
		add( "minTickSize", minTickSize);
	}
	public String getTickFormatter() {
		return tickFormatter;
	}
	public void setTickFormatter(String tickFormatter) {
		this.tickFormatter = tickFormatter;
		add( "tickFormatter", tickFormatter);
	}
	public Double getTickDecimals() {
		return tickDecimals;
	}
	public void setTickDecimals(Double tickDecimals) {
		this.tickDecimals = tickDecimals;
		add( "tickDecimals", tickDecimals);
	}

    
}//met
