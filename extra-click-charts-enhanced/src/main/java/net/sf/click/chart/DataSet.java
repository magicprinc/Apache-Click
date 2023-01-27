package net.sf.click.chart;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class DataSet extends ArrayList<Data<?,?>> {
	@Serial private static final long serialVersionUID = 2922020687950828807L;

	public DataSet(){
	}//met


	public void add( double x, double y )
	{
		Data<Double, Double> p = new Data<>(x, y);
		add( p );
	}//met

	public void add( int x, int y )
	{
		Data<Integer, Integer> p = new Data<>(x, y);
		add( p );
	}//met

	public void add( Object x, Object y )
	{
		Data<?,?> p = new Data(x,y);
		add( p );
	}//met

	public void add( double[] dTab )
	{
		int i=1;
		for( double d : dTab)
			add( i++, d );
	}//met

	public void add( double[][] dTab2D )
	{
		for( double[] dTab : dTab2D)
		{
			if( dTab.length != 2 )
				throw new RuntimeException( "Invalid length for inner array, must be 2"+ Arrays.toString(dTab));
			add( dTab[0], dTab[1] );
		}//for
	}//met

	public void add( Data<?,?>... pTab ){
		this.addAll(Arrays.asList(pTab));
	}//met

	public void add( double x, double y, double... varargs )
	{
		int i = 1;
		Data<Integer,Double> p0 = new Data<>(i++, x);
		add( p0 );
		Data<Integer, Double> p1 = new Data<>(i++, y);
		add( p1 );
		for( double d : varargs )
		{
			Data<Integer, Double> p = new Data<>(i++, d);
			add( p );
		}//for
	}//met

	public abstract String render();

}//class