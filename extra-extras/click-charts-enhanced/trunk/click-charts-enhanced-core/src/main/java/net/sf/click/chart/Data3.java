package net.sf.click.chart;

public class Data3<X,Y,L> extends Data<X,Y>{

	private L l;
	
	public Data3(X x, Y y, L l) {
		super(x, y);
		this.l = l;
	}

	@Deprecated
	public Data3(X x, Y y) {
		this(x,y,null);
	}//met

	public L getL() {
		return l;
	}

	public void setL(L l) {
		this.l = l;
	}

	@Override
	public String render() {
		return render(x,y,l);
	}//met

}//class
