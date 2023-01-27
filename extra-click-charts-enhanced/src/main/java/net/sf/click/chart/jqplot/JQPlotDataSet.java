package net.sf.click.chart.jqplot;

import net.sf.click.chart.Data;
import net.sf.click.chart.DataSet;

import java.util.Iterator;

public class JQPlotDataSet extends DataSet
{
	private static final long serialVersionUID = 1L;

	public JQPlotDataSet(){
	}//met

	@Override
	public String render()
	{
		StringBuffer buffer = new StringBuffer();

		buffer.append( "[" );
		Iterator<Data<?,?>> iter = this.iterator();
		//int count = 0;
		while( iter.hasNext() )
		{
			Data<?,?> p = iter.next();
			buffer.append( p.render() );
			if( iter.hasNext() )
			{
				buffer.append( " , " );
				/*
				if( buffer.length() - count > 80 )
				{
					buffer.append( "\n" );
					count+=80;
				}//if*/
			}//if
		}//for
		buffer.append( "]" );
		return buffer.toString();
	}//met
}//met