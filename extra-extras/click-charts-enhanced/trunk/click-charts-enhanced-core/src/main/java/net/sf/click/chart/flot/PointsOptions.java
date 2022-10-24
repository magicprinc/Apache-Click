package net.sf.click.chart.flot;


public class PointsOptions extends LinesPointsBarsOptions {

    private double radius;
    
	public double getRadius() {
		return radius;
	}
	public void setRadius(double radius) {
		this.radius = radius;
		add( "radius", radius );
	}
}//met
