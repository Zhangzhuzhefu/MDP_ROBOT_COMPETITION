package mdp.algo;


public class Point {
	
	public double positionX;
	public double positionY;
	public int gridX;
	public int gridY;
	public Point[] neighbors;
	public static final int UP = 0, DOWN = 1, LEFT = 2, RIGHT = 3; 
	
	public Point(double x, double y) {
		this.positionX = x;
		this.positionY = y;
		this.gridX = xToGrid();
		this.gridY = yToGrid();
		initNeighbors();
	}
	
	public Point(int a, int b) {
		this.positionX = xToPosition();
		this.positionY = yToPosition();
		this.gridX = a;
		this.gridY = b;
		initNeighbors();
	}
	
	private void initNeighbors(){
		neighbors = new Point[4];
	}
	
	public void setNeighbors(int i, Point p){
		neighbors[i] = p;
	}
	
	private int xToGrid() {
		return (int) (positionX / ArenaMap.GRID_LEN);
	}
	
	private int yToGrid() {
		return (int) (positionY / ArenaMap.GRID_LEN);
	}
	
	private double xToPosition() {
		return (double) (gridX * ArenaMap.GRID_LEN);
	}
	
	private double yToPosition() {
		return (double) (gridY * ArenaMap.GRID_LEN);
	}
	//TODO distance()
	
	//TODO equals()
	
	public Point scalarProduct(double a) {
		return new Point(a*positionX, a*positionY);
	}
	
	public Point normalize() {
		return scalarProduct(1 / vectorLength());
	}
	
	public Point sub(Point B) {
		return new Point(positionX-B.positionX, positionY-B.positionY);
	}
	
	public Point add(Point B) {
		return new Point(positionX+B.positionX, positionY+B.positionY);
	}
	
	public double vectorDotProduct(Point B) {
		return positionX*B.positionX+positionY*B.positionY;
	}
	
	public double vectorCrossProduct(Point B) {
		return positionX*B.positionY-positionY*B.positionX;
	}
	
	public double vectorLength() {
		return Math.sqrt(positionX*positionX+positionY*positionY);
	}

}
