package edu.cu.cs.Sombra.DomTree;

public class PhantomFeature {
	
	public int x;
	
	public int y;
	
	public int width;
	
	public int height;
	
	public PhantomFeature(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
	}
	
	public double distance(PhantomFeature pf) {
		return (x - pf.x) * (x - pf.x) + (y - pf.y) * (y - pf.y);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
