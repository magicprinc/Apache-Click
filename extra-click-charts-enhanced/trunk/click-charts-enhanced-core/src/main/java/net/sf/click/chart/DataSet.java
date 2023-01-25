package net.sf.click.chart;

import java.util.ArrayList;

public abstract class DataSet extends ArrayList<Data<?,?>>
{
	private static final long serialVersionUID = 1L;

	public DataSet(){
	}//met
	
	
	public void add( double x, double y )
	{
		Data<Double, Double> p = new Data<Double, Double>(x,y);
		add( p );
	}//met
	
	public void add( int x, int y )
	{
		Data<Integer, Integer> p = new Data<Integer, Integer>(x,y);
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
				throw new RuntimeException( "Invalid length for inner array, must be 2"+dTab );
			add( dTab[0], dTab[1] );
		}//for
	}//met

	public void add( Data<?,?>... pTab )
	{
		for( Data<?,?> p : pTab )
			add( p );
	}//met

	public void add( double x, double y, double... varargs )
	{
		int i = 1;
		Data<Integer,Double> p0 = new Data<Integer,Double>( i++,x);
		add( p0 );
		Data<Integer, Double> p1 = new Data<Integer,Double>( i++,y);
		add( p1 );
		for( double d : varargs )
		{
			Data<Integer, Double> p = new Data<Integer,Double>( i++,d);
			add( p );
		}//for
	}//met

	public abstract String render();
	
}//class
