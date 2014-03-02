package mdp.algo;


public class Direction {
	
	public static final int LEFT=0; 
	public static final int RIGHT=1; 
	public static final int BACK=2;  
	
	public static final int UP=3; 
	public static final int DOWN=4;   

	private double degree;
	
	public Direction(double d) {
		degree = d;
		while (degree<0) degree += 360;
	}
	public Direction(int d) {
		degree = (double) d;
		while (degree<0) degree += 360;
	}
	
	public void rotate(int dir){
		switch (dir) {
			
		case LEFT: 
			degree += 90;
			while (degree<0) degree += 360;
			break;
		case RIGHT:
			degree -= 90;
			while (degree<0) degree += 360;
			break;
		case BACK:
			degree += 180;
			while (degree<0) degree += 360;
			break;
			
		}
	}

	public int getDirection(){
		while (degree<0) degree += 360;
		if (degree%360==0) return RIGHT;
		else if (degree%360==90) return UP;
		else if (degree%360==270) return DOWN;
		else if (degree%360==180) return LEFT;
		else return -1;
	}
	
	public double getDegree() {
		return degree;
	}

	public void setDegree(double degree) {
		this.degree = degree;
		while (degree<0) this.degree += 360;
	}

}
