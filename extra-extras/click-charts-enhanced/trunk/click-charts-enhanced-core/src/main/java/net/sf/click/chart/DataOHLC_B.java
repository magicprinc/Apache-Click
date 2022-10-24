package net.sf.click.chart;

public class DataOHLC_B<X,Y,H,L,C,B> extends Data<X,Y>{

	private H h;
	private L l;
	private C c;
	private B b;
	
	public DataOHLC_B(X x, Y y, H h, L l, C c, B b) {
		super(x, y);
		this.h = h;
		this.l = l;
		this.c = c;
		this.b = b;
	}

	@Deprecated
	public DataOHLC_B(X x, Y y) {
		this(x,y, null,null,null, null);
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

	public B getB() {
		return b;
	}

	public void setB(B b) {
		this.b = b;
	}
	
	@Override
	public String render() {
		return render(x,y,h,l,c,b);
	}//met

}//class
