package net.sf.click.chart.jqplot.renderer;

import java.util.ArrayList;
import java.util.List;

import net.sf.click.chart.jqplot.Axis;
import net.sf.click.chart.jqplot.plugins.Plugin;

/**
 * An axis renderer for a Mekko chart.  Should be used with a Mekko chart where the mekkoRenderer is used on the series.  Displays the Y axis as a range from 0 to 1 (0 to 100%) and the x axis with a tick for each series scaled to the sum of all the y values.
 * @author steff
 * http://www.jqplot.com/docs/files/plugins/jqplot-mekkoAxisRenderer-js.html
 */
public class MekkoAxisRenderer extends AxisRenderer implements Plugin {

	public MekkoAxisRenderer() {
		this( null );
	}//cons
	
	public MekkoAxisRenderer( Axis axis ) {
		super( "$.jqplot.MekkoAxisRenderer", axis );
	}//cons

	@Override
	public List<String> getListJavaScriptImport() {
		List<String> listJavaScriptImport = new ArrayList<String>();
		//javascript imports
		listJavaScriptImport.add("/jqplot/plugins/jqplot.mekkoAxisRenderer.js");
		return listJavaScriptImport;
	}

	public enum TickMode
	{
		BAR( "bar" ),
		EVEN( "even" );

		private String mode;
		private TickMode( String s ) {
			this.mode = s;			
		}//cons
		@Override
		public String toString() {
			return mode;
		}//met
	}//enum

	/** How to space the ticks on the axis. */
	private TickMode tickMode = TickMode.EVEN;
	/**  renderer to use to draw labels under each bar.*/
	private Renderer barLabelRenderer = new AxisTickRenderer();
	/** array of labels to put under each bar.*/
	private String barLabels = "[]";
	/** options object to pass to the bar label renderer.*/
	private String barLabelOptions = "{}";

	public Renderer getBarLabelRenderer() {
		return barLabelRenderer;
	}//met

	public void setBarLabelRenderer(Renderer barLabelRenderer) {
		this.barLabelRenderer = barLabelRenderer;
		options.add( "barLabelRenderer", barLabelRenderer.getName() );
	}//met

	public TickMode getTickMode() {
		return tickMode;
	}

	public void setTickMode(TickMode tickMode) {
		this.tickMode = tickMode;
		options.add( "tickMode", tickMode.toString() );
	}

	public String getBarLabels() {
		return barLabels;
	}

	public void setBarLabels(String barLabels) {
		this.barLabels = barLabels;
		options.add( "barLabels", barLabels );
	}

	public String getBarLabelOptions() {
		return barLabelOptions;
	}

	public void setBarLabelOptions(String barLabelOptions) {
		this.barLabelOptions = barLabelOptions;
		options.add( "barLabelOptions", barLabelOptions );
	}
}//class
