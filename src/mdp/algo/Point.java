package mdp.algo;

import mdp.Config;

public class Point {
	
	public double x;
	public double y;
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}
	//TODO distance()
	
	//TODO equals()
	
	public Point scalarProduct(double a) {
		return new Point(a*x, a*y);
	}
	
	public Point normalize() {
		return scalarProduct(1 / vectorLength());
	}
	
	public Point sub(Point B) {
		return new Point(x-B.x, y-B.y);
	}
	
	public Point add(Point B) {
		return new Point(x+B.x, y+B.y);
	}
	
	public double vectorDotProduct(Point B) {
		return x*B.x+y*B.y;
	}
	
	public double vectorCrossProduct(Point B) {
		return x*B.y-y*B.x;
	}
	
	public double vectorLength() {
		return Math.sqrt(x*x+y*y);
	}
	
	public int xToGrid() {
		return (int) (x / ArenaMap.GRID_LEN);
	}
	
	public int yToGrid() {
		return (int) (y / ArenaMap.GRID_LEN);
	}
}
