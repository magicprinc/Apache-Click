package net.sf.click.chart.flot;

import net.sf.click.chart.Data;
import org.apache.click.Context;
import org.apache.click.element.Element;
import org.apache.click.element.JsImport;
import org.apache.click.util.ClickUtils;

import java.util.ArrayList;
import java.util.List;

public class FlotPieChart extends FlotChart {
    private static final long serialVersionUID = -6708109225323883537L;


	public FlotPieChart() {
		this( "", "" );
	}

	public FlotPieChart(String name, String label) {
		super(name, label);
		getSeries().getPies().setShow( true );
	}

	public FlotPieChart(String name) {
		this( name, name );
	}

	public void add( String label, int slice )
	{
		PieDataSeries s = new PieDataSeries(label);
		s.add(new PieData(slice, 0));
		this.listDataSet.add( s )  ;
	}//met

	private static class PieData extends Data
	{
		public PieData(Object x, Object y) {
			super(x, y);
		}

		@Override
		public String render() {
			return String.valueOf( x );
		}//met
	}//met

	private static class PieDataSeries extends FlotDataSeries {
	    private static final long serialVersionUID = -6875727619356824407L;

		public PieDataSeries(String label) {
			super( label );
		}

		@Override
		public String render()
		{
			this.options.add( "data", Integer.parseInt( get(0).render() ) );
			return this.options.render( 0 );
		}//met
	}//class

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
		chartHeadElements.add(new JsImport("/flot/jquery.flot.pie.js", versionIndicator));

		return chartHeadElements;
	}

}