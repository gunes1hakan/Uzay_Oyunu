package uzay_oyunu_;

public class Roket extends CarpisilabilirVarlik{
	private int x;		
	private int y;
	private int w;
	private int h;
	
	public Roket(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	@Override protected int getXDegeri(){ return x; }
	@Override protected int getYDegeri(){ return y; }
	@Override protected int getGenişlik(){ return (w > 0 ? w : 10); }
	@Override protected int getYukseklik(){ return (h > 0 ? h : 10); }
}

