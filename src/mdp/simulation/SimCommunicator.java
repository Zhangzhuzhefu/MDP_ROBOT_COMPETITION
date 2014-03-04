package mdp.simulation;

import mdp.Config;
import mdp.algo.ArenaMap;
import mdp.algo.Direction;
import mdp.algo.Point;
import mdp.algo.Robot;
import mdp.algo.VirtualCommunicator;

public class SimCommunicator extends VirtualCommunicator{
	
	private Robot robot;
	
	
	public SimCommunicator() {
		robot = Simulator.robot;
	}

	@Override
	public int[] ultraSonic() {
		int[] detectInt = new int[3];
		int[][] actualMap;
		actualMap = ArenaMap.actualMap;
		Point curLoc = robot.getCurrentLocation();
		Direction dir = robot.getDirection();
		for (int i=0; i<3; i++)
			detectInt[i] = Config.FrontSensorDetectDist;
		switch (dir.getDirection()) {
		case Direction.UP:
			for(int i=0; i<Config.FrontSensorDetectDist;i++){
				if (actualMap[curLoc.gridX-1-1][curLoc.gridY+i]==ArenaMap.OBS) {
					detectInt[0] = i;
					break;
				}
			}
			for(int i=0; i<Config.FrontSensorDetectDist;i++){
				if (actualMap[curLoc.gridX-1][curLoc.gridY+i]==ArenaMap.OBS) {
					detectInt[2] = i;
					break;
				}
			}
			break;
		case Direction.DOWN:
			for(int i=0; i<Config.FrontSensorDetectDist;i++){
				if (actualMap[curLoc.gridX-1][curLoc.gridY-3-i]==ArenaMap.OBS) {
					detectInt[0] = i;
					break;
				}
			}
			for(int i=0; i<Config.FrontSensorDetectDist;i++){
				if (actualMap[curLoc.gridX-2][curLoc.gridY-3-i]==ArenaMap.OBS) {
					detectInt[2] = i;
					break;
				}
			}
			break;
		case Direction.LEFT:
			for(int i=0; i<Config.FrontSensorDetectDist;i++){
				if (actualMap[curLoc.gridX-2-i][curLoc.gridY-1-1]==ArenaMap.OBS) {
					detectInt[0] = i;
					break;
				}
			}
			for(int i=0; i<Config.FrontSensorDetectDist;i++){
				if (actualMap[curLoc.gridX-2-i][curLoc.gridY-1]==ArenaMap.OBS) {
					detectInt[2] = i;
					break;
				}
			}
			break;
		case Direction.RIGHT:
			for(int i=0; i<Config.FrontSensorDetectDist;i++){
				if (actualMap[curLoc.gridX+i][curLoc.gridY-1]==ArenaMap.OBS) {
					detectInt[0] = i;
					break;
				}
			}
			for(int i=0; i<Config.FrontSensorDetectDist;i++){
				if (actualMap[curLoc.gridX+i][curLoc.gridY-1-1]==ArenaMap.OBS) {
					detectInt[2] = i;
					break;
				}
			}
			break;
		}
		return detectInt;
	}

	@Override
	public int leftSensor() {
		int detectInt;
		int[][] actualMap;
		actualMap = ArenaMap.actualMap;
		Point curLoc = robot.getCurrentLocation();
		Direction dir = robot.getDirection();
		detectInt = Config.SideSensorDetectDist;
		switch (dir.getDirection()) {
		case Direction.UP:
			for(int i=0; i<Config.SideSensorDetectDist;i++){
				if (actualMap[curLoc.gridX-3-i][curLoc.gridY-1]==ArenaMap.OBS) {
					detectInt = i;
					break;
				}
			}
			break;
		case Direction.DOWN:
			for(int i=0; i<Config.SideSensorDetectDist;i++){
				if (actualMap[curLoc.gridX+i][curLoc.gridY-2]==ArenaMap.OBS) {
					detectInt = i;
					break;
				}
			}
			break;
		case Direction.LEFT:
			for(int i=0; i<Config.SideSensorDetectDist;i++){
				if (actualMap[curLoc.gridX-2][curLoc.gridY-3-i]==ArenaMap.OBS) {
					detectInt = i;
					break;
				}
			}
			break;
		case Direction.RIGHT:
			for(int i=0; i<Config.SideSensorDetectDist;i++){
				if (actualMap[curLoc.gridX-1][curLoc.gridY+i]==ArenaMap.OBS) {
					detectInt = i;
					break;
				}
			}
			break;
		}		
		return detectInt;
	}

	@Override
	public int rightSensor() {
		int detectInt;
		int[][] actualMap;
		actualMap = ArenaMap.actualMap;
		Point curLoc = robot.getCurrentLocation();
		Direction dir = robot.getDirection();
		detectInt = Config.SideSensorDetectDist;
		switch (dir.getDirection()) {
		case Direction.UP:
			for(int i=0; i<Config.SideSensorDetectDist;i++){
				if (actualMap[curLoc.gridX+i][curLoc.gridY-1]==ArenaMap.OBS) {
					detectInt = i;
					break;
				}
			}
			break;
		case Direction.DOWN:
			for(int i=0; i<Config.SideSensorDetectDist;i++){
				if (actualMap[curLoc.gridX-3-i][curLoc.gridY-2]==ArenaMap.OBS) {
					detectInt = i;
					break;
				}
			}
			break;
		case Direction.LEFT:
			for(int i=0; i<Config.SideSensorDetectDist;i++){
				if (actualMap[curLoc.gridX-2][curLoc.gridY+i]==ArenaMap.OBS) {
					detectInt = i;
					break;
				}
			}
			break;
		case Direction.RIGHT:
			for(int i=0; i<Config.SideSensorDetectDist;i++){
				if (actualMap[curLoc.gridX-1][curLoc.gridY-3-i]==ArenaMap.OBS) {
					detectInt = i;
					break;
				}
			}
			break;
		}		
		return detectInt;
	}



}
