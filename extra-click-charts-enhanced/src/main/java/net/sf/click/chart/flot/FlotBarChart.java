package net.sf.click.chart.flot;

import net.sf.click.chart.DataSet;
import org.apache.click.Context;
import org.apache.click.element.Element;
import org.apache.click.element.JsImport;
import org.apache.click.util.ClickUtils;

import java.util.ArrayList;
import java.util.List;

public class FlotBarChart extends FlotChart{
	private static final long serialVersionUID = 1081619562311039492L;


	public FlotBarChart() {
		this( "", "");
	}

	public FlotBarChart(String name, String label) {
		super(name, label);
		getSeries().getBars().setShow( true );
	}

	public FlotBarChart(String name) {
		this(name, name );
	}

	@Override
	public void add( FlotDataSeries s )
	{
		super.add( s );
		for( DataSet serie : listDataSet )
		{
			double barWidth = 0.9/ listDataSet.size();
			//((FlotSeries)serie).setBarWidth(barWidth);
		}//for
	}//met

	/**
	 * Return the HTML HEAD elements.
	 *
	 * @see JSChart#getChartHeadElements()
	 *
	 * @return the HTML HEAD elements
	 */
	@Override
	public List<Element> getChartHeadElements() {
		List<Element>  chartHeadElements = new ArrayList<>(3);
		Context context = Context.getThreadLocalContext();
		String versionIndicator = ClickUtils.getResourceVersionIndicator(context);

		chartHeadElements.add(new JsImport("/flot/jquery.js", versionIndicator));
		chartHeadElements.add(new JsImport("/flot/jquery.flot.js", versionIndicator));
		chartHeadElements.add(new JsImport("/flot/jquery.flot.orderBars.js", versionIndicator));

		return chartHeadElements;
	}

}