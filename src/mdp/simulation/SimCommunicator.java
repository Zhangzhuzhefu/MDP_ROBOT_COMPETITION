package mdp.simulation;

import mdp.Config;
import mdp.algo.ArenaMap;
import mdp.algo.Point;
import mdp.algo.Robot;
import mdp.algo.VirtualCommunicator;

public class SimCommunicator extends VirtualCommunicator{
	int[][] actualMap;
	private Robot robot;
	private Point curLoc;
	
	public SimCommunicator() {
		actualMap = ArenaMap.actualMap;
		robot = Simulator.robot;
		curLoc = robot.getCurrentLocation();
	}

	@Override
	public int[] ultraSonic() {
		int[] detectInt = new int[3];
		int count = 0;
		//for(int i=0; i<Config.FrontSensorDetectDist;i++)
		
		//detectInt[0] = 
		return detectInt;
	}

	@Override
	public int leftSensor() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int rightSensor() {
		// TODO Auto-generated method stub
		return 0;
	}



}
