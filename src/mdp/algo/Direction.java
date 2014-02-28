package mdp.algo;


public class Direction {
	
	public static final int LEFT=0; 
	public static final int RIGHT=1; 
	public static final int BACK=2;  

	private double degree;
	
	public Direction(double d) {
		degree = d;
	}
	public Direction(int d) {
		degree = (double) d;
	}
	
	public void rotate(int dir){
		switch (dir) {
			
		case LEFT: 
			degree += 90;
			break;
		case RIGHT:
			degree -= 90;
			break;
			
		case BACK:
			degree += 180;
			break;
			
		}
	}

	public double getDegree() {
		return degree;
	}

	public void setDegree(double degree) {
		this.degree = degree;
	}

}
