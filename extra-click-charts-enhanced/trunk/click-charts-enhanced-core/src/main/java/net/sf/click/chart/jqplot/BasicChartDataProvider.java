package net.sf.click.chart.jqplot;

import java.util.List;

public class BasicChartDataProvider implements ChartDataProvider
{
	private String[] listSeriesName = null;
	private String[] listXTickLabel = null;
	
	private double[][] data = null;
	
	public BasicChartDataProvider(int seriesCount, int xTickCount) {
		listSeriesName = new String[ seriesCount ];
		listXTickLabel = new String[ xTickCount ];
		data = new double[ seriesCount ][ xTickCount ];
	}//cons

	@Override
	public int getSeriesCount() {
		return listSeriesName.length;
	}//met

	@Override
	public String getSeriesName(int serieIndex) {
		return listSeriesName[ serieIndex ];
	}//met

	@Override
	public int getXTickLabelCount() {
		return listXTickLabel.length;
	}//met

	@Override
	public String getXTickLabel(int x) {
		return listXTickLabel[ x ];
	}//met

	@Override
	public double getYValue(int serieIndex, int x) {
		if( data == null )
			throw new RuntimeException( "Data has not been set." );
		return data[ serieIndex ][ x ];
	}//met

	public String[] getListSeriesName() {
		return listSeriesName;
	}//met

	public void setListSeriesName(List<String> listSeriesName) {
		if( listSeriesName.size() > this.listSeriesName.length )
			throw new RuntimeException( "the size of the list exceeds seriesNameCount given at construction time :"+this.listSeriesName.length );
		listSeriesName.toArray( this.listSeriesName );
	}//met
	
	public void setListSeriesName( String...strings ) {
		if( strings.length > this.listSeriesName.length )
			throw new RuntimeException( "the size of the list exceeds seriesNameCount given at construction time :"+this.listSeriesName.length );
		this.listSeriesName = strings;
	}//met

	public String[] getListXTickLabel() {
		return listXTickLabel;
	}//met

	public void setListXTickLabel(List<String> listXTickLabel) {
		if( listXTickLabel.size() > this.listXTickLabel.length )
			throw new RuntimeException( "the size of the list exceeds seriesNameCount given at construction time :"+this.listXTickLabel.length );
		listXTickLabel.toArray( this.listXTickLabel );
	}//met
	
	public void setListXTickLabel( String...strings ) {
		if( strings.length > this.listXTickLabel.length )
			throw new RuntimeException( "the size of the list exceeds seriesNameCount given at construction time :"+this.listXTickLabel.length );
		this.listXTickLabel = strings;
	}//met

	public double[][] getData() {
		return data;
	}//met

	public void setData(double[][] data) {
		this.data = data;
	}//met
	
	public void addData( int serieIndex, double... values )
	{
		if( serieIndex > listSeriesName.length )
			throw new RuntimeException( "serieIndex exceeds seriesNameCount given at construction time :"+this.listSeriesName.length );
		if( values.length > listXTickLabel.length )
			throw new RuntimeException( "number of values exceeds xTickCount given at construction time :"+this.listXTickLabel.length );

		this.data[ serieIndex ] = values;
	}//met
}