package net.sf.click.chart;

import net.sf.click.chart.jqplot.plugins.Plugin;
import net.sf.click.chart.jqplot.renderer.AbstractRenderer;
import net.sf.click.chart.jqplot.renderer.Renderer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class ChartOptions {


	/** the map (sorted) of options. */
	//tree map is used, more predictible for unit tests
	protected Map<String, Object> mapOptions = new TreeMap<String, Object>();

	public ChartOptions()
	{}//cons

	public ChartOptions( ChartOptions toClone )
	{
		this.mapOptions = new TreeMap<String, Object>( toClone.mapOptions );
	}//cons

	public boolean needsRendering()
	{
		boolean res = false;
		for( String key : mapOptions.keySet() )
		{
			Object value = mapOptions.get( key );
			if( value instanceof ChartOptions && res == false )
				res = ((ChartOptions) value).needsRendering();
			else
				res = true;
		}//for
		return res;
	}//met

	public List<String> getListJavaScriptImport() {
		List<String> listJavaScriptImport = new ArrayList<String>();
		for( Object o : mapOptions.values() )
		{
			if( o instanceof Plugin )
			{
				listJavaScriptImport.addAll( ((Plugin)o).getListJavaScriptImport() );
			}//if
			if( o instanceof ChartOptions )
			{
				listJavaScriptImport.addAll( ((ChartOptions)o).getListJavaScriptImport() );
			}//if
			if( o instanceof List )
			{
				for( ChartOptions option : (List<ChartOptions>)o )
					listJavaScriptImport.addAll( option.getListJavaScriptImport() );
			}//if
		}//for
		return listJavaScriptImport;
	}//met

	public void add( String key, Renderer renderer, String optionKey ) {
		mapOptions.put( key, renderer );
		mapOptions.put( optionKey, renderer.getOptions() );
	}//met

	public void add( String key, ChartOptions option ) {
		mapOptions.put( key, option );
	}//met

	protected void add( String key, Plugin p ) {
		mapOptions.put( key, p );
	}//met

	public void add( String key, List<? extends ChartOptions> listOption ) {
		mapOptions.put( key, listOption );
	}//met

	public void add( String key, Color color ) {
		if( color == null )
			mapOptions.put( key, "null");
		else
			mapOptions.put( key, "'rgb("+color.getRed()+','+color.getGreen()+','+color.getBlue()+")'");
	}//met

	public void add( String key, Color[] colorTab ) {
		if( colorTab == null )
			mapOptions.put( key, "null");
		else
		{
			String result = "[";

			Color color = null;
			for( int i = 0; i < colorTab.length; i++ )
			{
				color = colorTab[ i ];
				result+= "'rgb("+color.getRed()+','+color.getGreen()+','+color.getBlue()+")'";
				if( i != colorTab.length -1 )
					result += ", ";
			}//for
			result += "]";
			mapOptions.put( key, result);
		}//else
	}//met

	public void add( String key, Number[] numberTab ) {
		if( numberTab == null )
			mapOptions.put( key, "null");
		else
		{
			String result = "[";

			Number number = null;
			for( int i = 0; i < numberTab.length; i++ )
			{
				number = numberTab[ i ];
				result+= number;
				if( i != numberTab.length -1 )
					result += ", ";
			}//for
			result += "]";
			mapOptions.put( key, result);
		}//else
	}//met

	public void add( String key, double[] numberTab ) {
		if( numberTab == null )
			mapOptions.put( key, "null");
		else
		{
			String result = "[";

			double number = 0;
			for( int i = 0; i < numberTab.length; i++ )
			{
				number = numberTab[ i ];
				result+= number;
				if( i != numberTab.length -1 )
					result += ", ";
			}//for
			result += "]";
			mapOptions.put( key, result);
		}//else
	}//met

	public void add( String key, String value ) {
		if( value == null )
			mapOptions.put( key, "null" );
		else
			mapOptions.put( key, "'"+value+"'");
	}//met

	/** This is for flot. */
	public void add( String key, DataSet ds ) {
		if( ds == null )
			mapOptions.put( key, "null" );
		else
		{
			StringBuffer buffer = new StringBuffer();
			buffer.append( "[" );
			Iterator<Data<?,?>> iter = ds.iterator();
			while( iter.hasNext() )
			{
				Data<?,?> p = iter.next();
				if( p != null )
					buffer.append( p.render() );
				else
					buffer.append( "null");
				if( iter.hasNext() )
					buffer.append( " , " );
			}//for
			buffer.append( "]" );
			mapOptions.put( key, buffer.toString() );
		}//else
	}//met

	public void add( String key, String[] value ) {
		if( value == null )
			mapOptions.put( key, "null" );
		else
		{
			String result = "[";

			for( int i = 0; i < value.length-1; i++ )
				result+= "'"+value[i]+"',";
			result+= "'"+value[ value.length-1]+"']";
			mapOptions.put( key, result);
		}//else
	}//met

	public void add( String key, Map<?, ?> map )
	{
		String result = "[";
		int i=0;
		for( Object k : map.keySet() )
		{

			result+= "[";
			if( k instanceof String )
				result += "'"+k+"'";
			else
				result += ""+k;
			result +=",";
			if( map.get( k ) instanceof String )
				result += "'"+map.get( k )+"'";
			else
				result += ""+map.get( k );

			result += "]";
			if( i != map.size() -1 )
				result += ',';
			i++;
		}//for
		result += "]";
		mapOptions.put( key, result);
	}//met

	public void add( String key, Boolean value ) {
		if( value == null )
			mapOptions.put( key, "null" );
		else
			mapOptions.put( key, String.valueOf( value ) );
	}//met

	public void add( String key, Number value ) {
		if( value == null )
			mapOptions.put( key, "null" );
		else
			mapOptions.put( key, String.valueOf( value ) );
	}//met

	private void indent( StringBuffer buffer, int indentLevel )
	{
		if( indentLevel <= -1   )
			return;
		buffer.append( '\n' );
		for( int i = 0; i<4*(indentLevel); i++ )
			buffer.append( ' ' );
	}//met

	public String render(int indentLevel )
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append( '{' );
		indent( buffer, indentLevel );

		boolean written = false;
		Iterator<String> iter = mapOptions.keySet().iterator();
		while( iter.hasNext() )
		{
			String key = iter.next();
			Object value = mapOptions.get(key);

			if( value == null )
			{
				if( written )
				{
					buffer.append( ',');
					indent( buffer, indentLevel );
				}//if
				buffer.append( key + ": null" );
				written = true;
			}//if
			else if( value instanceof String )
			{
				if( written )
				{
					buffer.append( ',');
					indent( buffer, indentLevel );
				}//if
				buffer.append( key + ": "+ value );
				written = true;
			}//else
			else if( value instanceof AbstractRenderer )
			{
				if( written )
				{
					buffer.append( ',');
					indent( buffer, indentLevel );
				}//if
				buffer.append( key + ": "+ ((Renderer)value).getName() );
				written = true;
			}//else
			else if( value instanceof ChartOptions )
			{
				if( ((ChartOptions) value).needsRendering() )
				{
					if( written )
					{
						buffer.append( ',');
						indent( buffer, indentLevel );
					}//if
					buffer.append( key + ": "+ ((ChartOptions) value).render(indentLevel+1) );
					written = true;
				}//if

			}//if
			else if( value instanceof List )
			{
				List<ChartOptions> l = (List<ChartOptions>) value;

				if( written )
				{
					buffer.append( ',');
					//indent( buffer, indentLevel );
				}//if
				buffer.append( key+": [");
				Iterator<ChartOptions> iter2 = l.iterator();
				while( iter2.hasNext() )
				{
					ChartOptions option = iter2.next();
					if( option.needsRendering() )
						buffer.append( option.render(-1) );
					if( iter2.hasNext() )
						buffer.append( ", " );
				}//for
				buffer.append( "]");
				written = true;
			}//if




		}//while
		indent( buffer, indentLevel-1 );
		buffer.append( '}' );
		return buffer.toString();
	}//met
}//class