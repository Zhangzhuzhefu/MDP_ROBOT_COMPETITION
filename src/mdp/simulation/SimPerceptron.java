package mdp.simulation;

import mdp.Config;
import mdp.algo.ArenaMap;
import mdp.algo.Direction;
import mdp.algo.Point;
import mdp.algo.Robot;
import mdp.algo.VirtualCommunicator;

public class SimPerceptron{

	protected VirtualCommunicator communicator;
	int[][] environment;
	private Robot robot;
	
	public SimPerceptron(Robot r) {
		robot = r;
		environment = r.getMapKnowledgeBase().getArrayMap();
	}
	
	public int perceptEnvironment(){
		perceptFront();
		perceptLeft();
		perceptRight();
		return 0;
	}

	public int perceptFront() {
		Point curLoc = robot.getCurrentLocation();
		int [] uInt =communicator.ultraSonic(); 
		int ulLeft =  uInt[0];
		//int ulCenter = uInt[1]; 
		int ulRight = uInt[2]; 
		
		switch (robot.direction.getDirection()) {
		case Direction.UP:
			int farFront = Math.min(Config.FrontSensorDetectDist, ulLeft);
			for (int i=0; i<farFront; i++){
				environment[curLoc.gridX-1][curLoc.gridY+i]=ArenaMap.EMP;
			}
			if (farFront==ulLeft){
				environment[curLoc.gridX-1][curLoc.gridY+ulLeft]=ArenaMap.OBS;
			}
			farFront = Math.min(Config.FrontSensorDetectDist, ulRight);
			for (int i=0; i<farFront; i++){
				environment[curLoc.gridX][curLoc.gridY+i]=ArenaMap.EMP;
			}
			if (farFront==ulRight){
				environment[curLoc.gridX][curLoc.gridY+ulRight]=ArenaMap.OBS;
			}
			break;
		case Direction.DOWN:

			break;
		case Direction.LEFT:

			break;
		case Direction.RIGHT:

			break;
		}
		return 0;
	}

	public int perceptLeft() {
		
		return 0;
	}

	public int perceptRight() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int[][] getEnvironment() {
		return environment;
	}
	
	public VirtualCommunicator getCommunicator() {
		return communicator;
	}

	public void setCommunicator(VirtualCommunicator communicator) {
		this.communicator = communicator;
	}

	//only for simulator
	public void setEnvironment(int[][] e) {
		environment = e;
	}
	
	public int percept(Point p) {
		return environment[p.gridX][p.gridY];
	}
	
	public int percept(int x, int y) {
		return environment[x][y];
	}

	

}
