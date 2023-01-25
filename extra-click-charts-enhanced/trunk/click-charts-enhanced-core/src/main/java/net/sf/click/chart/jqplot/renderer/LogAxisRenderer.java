package net.sf.click.chart.jqplot.renderer;

import java.util.ArrayList;
import java.util.List;

import net.sf.click.chart.jqplot.Axis;
import net.sf.click.chart.jqplot.plugins.Plugin;

/**
 * A renderer for logarithmic axies. The renderer can be customized through its options.
 * @author steff
 * http://www.jqplot.com/docs/files/plugins/jqplot-logAxisRenderer-js.html

 */
public class LogAxisRenderer extends AxisRenderer implements Plugin {

	public LogAxisRenderer() {
		this( null );
	}//cons
	
	public LogAxisRenderer( Axis axis ) {
		super( "$.jqplot.LogAxisRenderer", axis );
	}//cons

	@Override
	public List<String> getListJavaScriptImport() {
		List<String> listJavaScriptImport = new ArrayList<String>();
		//javascript imports
		listJavaScriptImport.add("/jqplot/plugins/jqplot.logAxisRenderer.js");
		return listJavaScriptImport;
	}

	/** A class of a rendering engine for creating the ticks labels displayed on the plot, See $.jqplot.AxisTickRenderer. */
	private Renderer tickRenderer = new AxisTickRenderer();

	public Renderer getTickRenderer() {
		return tickRenderer;
	}//met

	public void setTickRenderer(Renderer tickRenderer) {
		this.tickRenderer = tickRenderer;
		options.add( "tickRenderer", tickRenderer.getName() );
	}//met
	
	public enum TickDistribution
	{
		EVEN( "even" ),
		POWER( "power" );

		private String distribution;
		private TickDistribution( String s ) {
			this.distribution = s;			
		}//cons
		@Override
		public String toString() {
			return distribution;
		}//met
	}//enum
	
	//TODO check that with chris . Should we add the options to the father ?? beurk 
	private int base = 10;
	//TODO check that with chris
	private TickDistribution tickDistribution = TickDistribution.EVEN;

	public int getBase() {
		return base;
	}

	public void setBase(int base) {
		this.base = base;
		options.add( "base", base );
	}

	public TickDistribution getTickDistribution() {
		return tickDistribution;
	}

	public void setTickDistribution(TickDistribution tickDistribution) {
		this.tickDistribution = tickDistribution;
		options.add( "tickDistribution", tickDistribution.toString() );
	}
	
	
}//class
