package net.sf.click.chart.flot;

import java.util.ArrayList;
import java.util.List;


import net.sf.click.chart.*;

import org.apache.click.Context;
import org.apache.click.element.Element;
import org.apache.click.element.JsImport;
import org.apache.click.element.JsScript;
import org.apache.click.util.ClickUtils;
import org.apache.click.util.HtmlStringBuffer;

public class FlotBarChart extends FlotChart{

	
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
		List<Element>  chartHeadElements = new ArrayList<Element>(3);
		Context context = getContext();
		String versionIndicator = ClickUtils.getResourceVersionIndicator(context);

		chartHeadElements.add(new JsImport("/flot/jquery.js", versionIndicator));
		chartHeadElements.add(new JsImport("/flot/jquery.flot.js", versionIndicator));
		chartHeadElements.add(new JsImport("/flot/jquery.flot.orderBars.js", versionIndicator));

		return chartHeadElements;
	}

}
