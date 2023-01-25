package net.sf.click.chart.flot;

import java.util.ArrayList;
import java.util.List;

import net.sf.click.chart.Chart;
import net.sf.click.chart.DataSet;

import org.apache.click.element.Element;
import org.apache.click.element.JsImport;
import org.apache.click.element.JsScript;
import org.apache.click.util.ClickUtils;
import org.apache.click.util.HtmlStringBuffer;

/**
 * This class is the basic class in the flot chart hierarchies. 
 * It shall provide full support for all flot drawings and is subclassed to enable ready-to-use subclasses. 
 * @author steff
 */
public class FlotChart extends Chart<FlotDataSeries> {

	private static final long serialVersionUID = 1L;

	private Series series;
	private Axis xaxis;
	private Axis x2axis;
	private Axis yaxis;
	private Axis y2axis;
	private Grid grid;

	public FlotChart() {
		this( "" , "" );
	}


	public FlotChart(String name, String label) {
		super(name, label);
		this.options = new FlotChartOptions( this.options );
	}


	public FlotChart(String name) {
		this( name , name );
	}
	
	public Series getSeries() {
		if( series == null )
			setSeries( new Series() );
		return series;
	}

	public void setSeries(Series series) {
		this.series = series;
		options.add( "series", series );
	}

	public Axis getXaxis() {
		if( xaxis == null )
			setXaxis( new Axis() );
		return xaxis;
	}

	public void setXaxis(Axis xaxis) {
		this.xaxis = xaxis;
		options.add( "xaxis", xaxis );
	}

	public Axis getX2axis() {
		if( x2axis == null )
			setX2axis( new Axis() );
		return x2axis;
	}

	public void setX2axis(Axis x2axis) {
		this.x2axis = x2axis;
		options.add( "x2axis", x2axis );
	}

	public Axis getYaxis() {
		if( yaxis == null )
			setYaxis( new Axis() );
		return yaxis;
	}

	public void setYaxis(Axis yaxis) {
		this.yaxis = yaxis;
		options.add( "yaxis", yaxis );
	}

	public Axis getY2axis() {
		if( y2axis == null )
			setY2axis( new Axis() );
		return y2axis;
	}

	public void setY2axis(Axis y2axis) {
		this.y2axis = y2axis;
		options.add( "y2axis", y2axis );
	}

	public Grid getGrid() {
		if( grid == null )
			setGrid( new Grid() );
		return grid;
	}


	public void setGrid(Grid grid) {
		this.grid = grid;
		options.add( "grid", grid );
	}

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

		//here goes the main javascript elements to call flot api
		if (!headElements.contains(script)) {
			HtmlStringBuffer buffer = new HtmlStringBuffer(512);
			
			buffer.append( "$(document).ready(function(){\n");

			buffer.append("var series = [];");
			for( DataSet s : listDataSet )
			{
			        buffer.append("series.push(" + s.render() + ");\n");			        
			}//for
			buffer.append("var options =" ); 
			buffer.append( options.render( 0 ) );
			//{ series: { bars: { show: true } } }
			buffer.append(";\n");
			buffer.append( "jQuery.plot($('#"+getId()+"'), series, options);" );
			buffer.append( "\n});");
			
			script.setContent(buffer.toString());
			listElem.add(script);
		}
		
		//here some javascript code to allow for javascript code exposure.
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
	}
	
	/**
	 * Return the HTML HEAD elements.
	 *
	 * @see JSChart#getChartHeadElements()
	 *
	 * @return the HTML HEAD elements
	 */
	@Override
	public List<Element> getChartHeadElements() {
		List<Element>  chartHeadElements = new ArrayList<Element>(2);
		String versionIndicator = ClickUtils.getApplicationResourceVersionIndicator();

		//TODO here we got a problem houston, flot uses an older version of jquery
		chartHeadElements.add(new JsImport("/flot/jquery.js", versionIndicator));
		chartHeadElements.add(new JsImport("/flot/jquery.flot.js", versionIndicator));

		return chartHeadElements;
	}

}//class
