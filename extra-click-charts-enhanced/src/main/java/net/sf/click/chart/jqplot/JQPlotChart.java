package net.sf.click.chart.jqplot;

import net.sf.click.chart.Chart;
import net.sf.click.chart.ChartOptions;
import net.sf.click.chart.Color;
import net.sf.click.chart.DataSet;
import net.sf.click.chart.jqplot.plugins.ChartPlugin;
import org.apache.click.element.CssImport;
import org.apache.click.element.Element;
import org.apache.click.element.JsImport;
import org.apache.click.element.JsScript;
import org.apache.click.util.ClickUtils;
import org.apache.click.util.HtmlStringBuffer;

import java.util.ArrayList;
import java.util.List;

public class JQPlotChart extends Chart<JQPlotDataSet> {

	private static final long serialVersionUID = 1L;
	private Axes axes;
	private Legend legend;
	private Title title;
	private Grid grid;
	private List<Series> series = new ArrayList<Series>();
	private Axis axesDefaults;
	private Series seriesDefault;

	private String fontsize = "";
	private boolean sortData = true;
	private boolean stackSeries = false;
	private boolean drawIfHidden = false;


	public JQPlotChart() {
		this( "","");
	}

	public JQPlotChart(String name) {
		this( name, name );
	}

	public JQPlotChart(String name, String label) {
		super(name, label);
		setAxes( new Axes() );
		setLegend( new Legend() );
		setTitle( new Title() );
		setGrid( new Grid() );
		//setSeriesDefaults( new Series() );
		//others are plugins.
		options.add( "series", series );
	}

	public void add( Series s )
	{
		series.add( s );
	}//met

	public void remove( Series s )
	{
		series.remove( s );
	}//met

	public Axes getAxes() {
		return axes;
	}

	public void setAxes(Axes axes) {
		this.axes = axes;
		options.add( "axes", axes );
	}

	public Legend getLegend() {
		return legend;
	}

	public void setLegend(Legend legend) {
		this.legend = legend;
		options.add( "legend", legend );
	}

	public Title getTitle() {
		return title;
	}

	public void setTitle(Title title) {
		this.title = title;
		options.add( "title", title );
	}

	public void setTitle(String text) {
		this.title = new Title();
		title.setText(text);
		options.add( "title", title );
	}

	public Grid getGrid() {
		return grid;
	}

	public void setGrid(Grid grid) {
		this.grid = grid;
		options.add( "grid", grid );
	}

	public Axis getAxesDefaults() {
		if( axesDefaults == null )
			setAxesDefaults( new Axis() );
		return axesDefaults;
	}

	public void setAxesDefaults(Axis axesDefaults) {
		this.axesDefaults = axesDefaults;
		options.add( "axesDefaults", axesDefaults );
	}

	public Series getSeriesDefaults() {
		if( seriesDefault == null )
			setSeriesDefaults( new Series() );
		return seriesDefault;
	}

	public void setSeriesDefaults(Series series2) {
		this.seriesDefault = series2;
		options.add( "seriesDefaults", series2 );
	}

	public void addPlugin( ChartPlugin plugin )
	{
		if( !(plugin instanceof ChartOptions ) )
			throw new RuntimeException( "Only chart options can be plugged in." );
		options.add( plugin.getName(), (ChartOptions)plugin );
	}//met

	public String getFontsize() {
		return fontsize;
	}

	public void setFontsize(String fontsize) {
		this.fontsize = fontsize;
		options.add( "fontsize", fontsize );
	}

	public boolean isSortData() {
		return sortData;
	}

	public void setSortData(boolean sortData) {
		this.sortData = sortData;
		options.add( "sortData", sortData );
	}

	public boolean isStackSeries() {
		return stackSeries;
	}

	public void setStackSeries(boolean stackSeries) {
		this.stackSeries = stackSeries;
		options.add( "stackSeries", stackSeries );
	}

	public boolean isDrawIfHidden() {
		return drawIfHidden;
	}

	public void setDrawIfHidden(boolean drawIfHidden) {
		this.drawIfHidden = drawIfHidden;
		options.add( "drawIfHidden", drawIfHidden );
	}

	public void setSeriesColor( List<Color> listColors )
	{
		this.series.clear();
		for( Color c : listColors )
		{
			Series series = new Series();
			series.setColor( c );
			this.series.add( series );
		}//for
	}//met

	@Override
	public void render(HtmlStringBuffer buffer) {
		super.render(buffer);
		if( demoMode)
			buffer.append( "<p><i style='font-family:monospace, font-size:11pt'>Generated Code for "+getId()+" :</i><pre class='code' ></pre></p>" );
	}

	@Override
	public List<Element> getJsElements() {
		List<Element> listElem = new ArrayList<Element>();

		JsScript script = new JsScript();
		script.setAttribute("class", "code");
		script.setId(getId() + "_js_setup");

		if (!headElements.contains(script)) {
			HtmlStringBuffer buffer = new HtmlStringBuffer(512);
			buffer.append( "$(document).ready(function(){\n");
			buffer.append("$.jqplot.config.enablePlugins = true;\n");

			buffer.append("var options = "+getOptions().render( demoMode ? 0 : -100 ) +";\n");
			buffer.append("var data = [];\n");
			for( DataSet s : listDataSet )
			{
			        buffer.append("data.push(" + s.render() + ");\n");
			}//for
			buffer.append( "plot"+new java.util.Random().nextInt(10)+"=");
			buffer.append( "$.jqplot('"+getId()+"', data, options );" );
			buffer.append( "\n});");
			script.setContent(buffer.toString());
			listElem.add(script);
		}

		JsScript scriptDemo = new JsScript();
		scriptDemo.setId( "click-charts-demo-code");
		if (demoMode && !headElements.contains(scriptDemo)) {
			HtmlStringBuffer buffer = new HtmlStringBuffer(512);
			buffer.append("$(document).ready(function(){"+
	        "$('script.code').each(function(index) {"+
	        "    $('pre.code').eq(index).text($(this).html());"+
	        "});"+
	        "$('script.common').each(function(index) {"+
	        "    $('pre.common').eq(index).html($(this).html());"+
	        "});"+
	        "$(document).unload(function() {$('*').unbind(); });"+
			"});");

			scriptDemo.setContent(buffer.toString());
			listElem.add(scriptDemo);
		}//if
		return listElem;
	}//met

	/**
	 * Return the HTML HEAD elements.
	 * @see JSChart#getChartHeadElements()
	 * @return the HTML HEAD elements
	 */
	@Override
	public List<Element> getChartHeadElements() {
		List<Element>  chartHeadElements = new ArrayList<Element>(4);
		String versionIndicator = ClickUtils.getApplicationResourceVersionIndicator();

		chartHeadElements.add( new CssImport("/jqplot/jquery.jqplot.css" ) );
		chartHeadElements.add( new CssImport("/jqplot/examples/examples.css" ) );
		chartHeadElements.add(new JsImport("/jqplot/jquery-1.4.2.min.js", versionIndicator));
		chartHeadElements.add(new JsImport("/jqplot/jquery.jqplot.js", versionIndicator));

		for( String jsImport : getOptions().getListJavaScriptImport() )
			chartHeadElements.add(new JsImport( jsImport, versionIndicator));
		return chartHeadElements;
	}//met
}//class