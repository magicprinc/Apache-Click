package net.sf.click.chart;

public class DataHLC<X,Y,L,C> extends Data<X,Y>{

	private L l;
	private C c;
	
	public DataHLC(X x, Y y, L l, C c) {
		super(x, y);
		this.l = l;
		this.c = c;
	}

	@Deprecated
	public DataHLC(X x, Y y) {
		this(x,y,null,null);
	}//met

	public L getL() {
		return l;
	}

	public void setL(L l) {
		this.l = l;
	}

	public C getC() {
		return c;
	}

	public void setC(C c) {
		this.c = c;
	}

	@Override
	public String render() {
		return render(x,y,l,c);
	}//met

}//class
