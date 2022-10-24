package net.sf.click.chart.flot;

import net.sf.click.chart.ChartOptions;
import net.sf.click.chart.Color;

public class FlotChartOptions extends ChartOptions {

	public FlotChartOptions()
	{}//cons

	public FlotChartOptions( ChartOptions toClone )
	{
		super( toClone );
	}//cons
	
	@Override
	public void add( String key, Color color ) {
		if( color == null )
			mapOptions.put( key, "null");
		else
			mapOptions.put( key, "\"#" + Integer.toHexString( color.getRGB() ) + "\"" );
	}//met
	
	@Override
	public void add( String key, Color[] colorTab ) {
		if( colorTab == null )
			mapOptions.put( key, "null");
		else 
		{
			String result = "{ colors : [";

			Color color = null;
			for( int i = 0; i < colorTab.length; i++ )
			{
				color = colorTab[ i ];
				result+= "\"#" + Integer.toHexString( color.getRGB() ).substring(2) + "\"";
				if( i != colorTab.length -1 )
					result += ", ";
			}//for
			result += "] }";
			mapOptions.put( key, result);
		}//else
	}//met
}//class