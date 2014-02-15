package mdp.algo;

import mdp.Config;

public class PointManager {

	private static Point[][] gridPoints;
	
	private static Point[][]getGridPoints(){
		if (gridPoints==null) {
			
			if (Config.debugOn) System.out.println("Initializing Points...");
			
			int numTotalXPoints = ArenaMap.MAXN+1;
			int numTotalYPoints = ArenaMap.MAXM+1;
			gridPoints = new Point[numTotalXPoints][numTotalYPoints];
			
			//new points
			for (int i=0; i<numTotalXPoints;i++) {
				for (int j=0; j<numTotalYPoints;j++){
					gridPoints[i][j] = new Point(i,j);
					//if (i==3 && j == 3) System.out.println ("(3,3): "+gridPoints[i][j] );
				}
			}
			//set lower neighbors 
			for (int i=0; i<ArenaMap.MAXN;i++) {
				for (int j=0; j<numTotalYPoints;j++){
					gridPoints[i][j].setNeighbors(Point.DOWN,gridPoints[i+1][j]); 
					//System.out.print(gridPoints[i][j].getNeighbors(Point.DOWN).gridX);
				}//System.out.println();
			}
			//set upper neighbors 
			for (int i=1; i<numTotalXPoints;i++) {
				for (int j=0; j<numTotalYPoints;j++){
					gridPoints[i][j].setNeighbors(Point.UP,gridPoints[i-1][j]); 
				}
			}
			//set left neighbors 
			for (int i=0; i<numTotalXPoints;i++) {
				for (int j=1; j<numTotalYPoints;j++){
					gridPoints[i][j].setNeighbors(Point.LEFT,gridPoints[i][j-1]); 
				}
			}
			//set right neighbors 
			for (int i=0; i<numTotalXPoints;i++) {
				for (int j=0; j<ArenaMap.MAXM;j++){
					gridPoints[i][j].setNeighbors(Point.RIGHT,gridPoints[i][j+1]); 
				}
			}
			
			
		}
		return gridPoints;
	}
	
	public static Point getPoint(int i, int j){
		return getGridPoints()[i][j];
	}
	
	public static Point getPoint(double x, double y){
		int i, j;
		i = (int) (x / ArenaMap.GRID_LEN);
		j = (int) (y / ArenaMap.GRID_LEN);
		return getGridPoints()[i][j];
	}

}
