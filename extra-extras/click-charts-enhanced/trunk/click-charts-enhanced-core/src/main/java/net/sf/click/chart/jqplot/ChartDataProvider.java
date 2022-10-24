package net.sf.click.chart.jqplot;

public interface ChartDataProvider 
{
	int getSeriesCount();
	String getSeriesName( int serieIndex );


	int getXTickLabelCount();
	String getXTickLabel( int x );

	double getYValue( int serieIndex, int x );

}