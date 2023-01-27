package net.sf.click.chart.flot;


public class PiesOptions extends LinesPointsBarsOptions {

    private double radius;
    private double innerRadius;
    private boolean show;
    
	public boolean isShow() {
		return show;
	}
	public void setShow(boolean show) {
		this.show = show;
		add( "show", show );
	}
	
	public double getRadius() {
		return radius;
	}
	public void setRadius(double radius) {
		this.radius = radius;
		add( "radius", radius );
	}
	public double getInnerRadius() {
		return innerRadius;
	}
	public void setInnerRadius(double innerRadius) {
		this.innerRadius = innerRadius;
		add( "innerRadius", innerRadius );
	}
	
	
}//met
