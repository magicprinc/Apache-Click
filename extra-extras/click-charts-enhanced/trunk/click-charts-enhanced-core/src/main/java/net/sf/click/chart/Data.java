package net.sf.click.chart;

public class Data<X,Y>{

	protected X x;
	protected Y y;
	
	public Data( X x, Y y )
	{
		this.x=x;
		this.y=y;
	}//cons

	public X getX() {
		return x;
	}

	public void setX(X x) {
		this.x = x;
	}

	public Y getY() {
		return y;
	}

	public void setY(Y y) {
		this.y = y;
	}
	
	public String render() {
		return render(x,y);
	}//met
	
	protected String render( Object o )
	{
		String res = "";
		if( o == null )
			res = "null";
		else if( o instanceof String )
			res = "'"+o+"'";
		else
			res = ""+o;
		return res;
	}//met
	
	protected String render( Object... oTab )
	{
		String res = "[";
		int count = 0;
		for( Object o : oTab )
		{
			res += render( o );
			if( count != oTab.length -1 )
				res += ',';
			count ++;
		}//for
		res += "]";
		return res;
	}//met
	
}//class
