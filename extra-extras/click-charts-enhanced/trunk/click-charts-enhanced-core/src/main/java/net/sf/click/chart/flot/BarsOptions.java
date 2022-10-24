package net.sf.click.chart.flot;


public class BarsOptions extends LinesPointsBarsOptions {

    private double barWidth;
    private BarAlign align;
    private boolean horizontal;
    
	public enum BarAlign
	{
		CENTER( "center" ),
		LEFT( "left" );

		private String align;
		private BarAlign( String s ) {
			this.align = s;			
		}//cons
		@Override
		public String toString() {
			return align;
		}//met
	}//enum
    
	public double getBarWidth() {
		return barWidth;
	}
	public void setBarWidth(double barWidth) {
		this.barWidth = barWidth;
		add( "barWidth", barWidth );
	}
	public BarAlign getAlign() {
		return align;
	}
	public void setAlign(BarAlign align) {
		this.align = align;
		add( "align", align.toString() );
	}
	public boolean isHorizontal() {
		return horizontal;
	}
	public void setHorizontal(boolean horizontal) {
		this.horizontal = horizontal;
		add( "horizontal", horizontal );
	}
}//met
