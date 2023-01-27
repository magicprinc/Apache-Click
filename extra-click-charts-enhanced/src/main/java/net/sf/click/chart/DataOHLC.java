package net.sf.click.chart;

public class DataOHLC<X,Y,H,L,C> extends Data<X,Y>{

	private H h;
	private L l;
	private C c;
	
	public DataOHLC(X x, Y y, H h, L l, C c) {
		super(x, y);
		this.h = h;
		this.l = l;
		this.c = c;
	}

	@Deprecated
	public DataOHLC(X x, Y y) {
		this(x,y, null,null,null);
	}//met

	public H getH() {
		return h;
	}

	public void setH(H h) {
		this.h = h;
	}

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
		return render(x,y,h,l,c);
	}//met
}//class
