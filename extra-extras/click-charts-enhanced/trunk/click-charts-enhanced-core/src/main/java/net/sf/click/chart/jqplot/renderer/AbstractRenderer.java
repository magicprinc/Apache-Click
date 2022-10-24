package net.sf.click.chart.jqplot.renderer;

import net.sf.click.chart.ChartOptions;

/**
 * This class represents core renderers as defined by JQPlot at 
 * http://www.jqplot.com/docs/files/jqplot-axisTickRenderer-js.html
 * @author steff
 *
 */
public class AbstractRenderer implements Renderer {

	protected String name;
	protected final ChartOptions options = new ChartOptions();

	public AbstractRenderer( String name )
	{
		this.name = name;
	}//cons
	
	/* (non-Javadoc)
	 * @see net.sf.click.chart.jqplot.renderer.Renderer#getName()
	 */
	@Override
	public String getName() {
		return name;
	}
	/* (non-Javadoc)
	 * @see net.sf.click.chart.jqplot.renderer.Renderer#getOptions()
	 */
	@Override
	public ChartOptions getOptions() {
		return options;
	}
}//class
