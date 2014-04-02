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
	private boolean updateFlag;
	
	public SimPerceptron(Robot r) {
		updateFlag = true;
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
		int ulCenter = uInt[1]; 
		int ulRight = uInt[2]; 
		int farFront;
		int frontWay;
		if(updateFlag){
		switch (robot.getDirection().getDirection()) {
		case Direction.UP:
			farFront = Math.min(Config.InfraRedDetectDist, ulLeft);
			if (Config.twoBytwo) {
				for (int i = 0; i < farFront; i++) {
					environment[curLoc.gridX - 1 - 1][curLoc.gridY + i] = ArenaMap.EMP;
				}
				if (farFront < Config.InfraRedDetectDist) {
					environment[curLoc.gridX - 1 - 1][curLoc.gridY + ulLeft] = ArenaMap.OBS;
				}
				farFront = Math.min(Config.InfraRedDetectDist, ulRight);
				for (int i = 0; i < farFront; i++) {
					environment[curLoc.gridX - 1][curLoc.gridY + i] = ArenaMap.EMP;
				}
				if (farFront < Config.InfraRedDetectDist) {
					environment[curLoc.gridX - 1][curLoc.gridY + farFront] = ArenaMap.OBS;
				}
			} else {
				boolean breakFlag = false;
                frontWay = ArenaMap.MAXM-2 - robot.getCurrentLocation().gridY;
				farFront = Math.min(frontWay,Math.min(Config.InfraRedDetectDist, ulLeft));
				for (int i = 0; i < farFront; i++) {
					if (environment[curLoc.gridX - 1 - 1][curLoc.gridY + i+1] == ArenaMap.UNKNOWN)
					environment[curLoc.gridX - 1 - 1][curLoc.gridY + i+1] = ArenaMap.EMP;
					else if(environment[curLoc.gridX - 1 - 1][curLoc.gridY + i+1] == ArenaMap.OBS) {
						breakFlag = true;
						break;
					}
				}
				if (farFront < Config.InfraRedDetectDist && !breakFlag) {
					if (environment[curLoc.gridX - 1 - 1][curLoc.gridY + farFront+1] == ArenaMap.UNKNOWN)
					environment[curLoc.gridX - 1 - 1][curLoc.gridY + farFront+1] = ArenaMap.OBS;
				}
				breakFlag = false;
				frontWay = ArenaMap.MAXM-2 - robot.getCurrentLocation().gridY;

                farFront = Math.min(frontWay,Math.min(Config.InfraRedDetectDist, ulCenter));
				for (int i = 0; i < farFront; i++) {
					if (environment[curLoc.gridX - 1][curLoc.gridY + i+1] == ArenaMap.UNKNOWN)
					environment[curLoc.gridX - 1][curLoc.gridY + i+1] = ArenaMap.EMP;
					else if(environment[curLoc.gridX - 1][curLoc.gridY + i+1] == ArenaMap.OBS) {
						breakFlag = true;
						break;
					}
				}
				if (farFront < Config.InfraRedDetectDist && !breakFlag) {
					if (environment[curLoc.gridX - 1][curLoc.gridY + farFront+1] == ArenaMap.UNKNOWN)
					environment[curLoc.gridX - 1][curLoc.gridY + farFront+1] = ArenaMap.OBS;
				}
				breakFlag = false;
                frontWay = ArenaMap.MAXM-2 - robot.getCurrentLocation().gridY;
				farFront = Math.min(frontWay,Math.min(Config.InfraRedDetectDist, ulRight));
				for (int i = 0; i < farFront; i++) {
					if (environment[curLoc.gridX][curLoc.gridY + i+1] == ArenaMap.UNKNOWN)
					environment[curLoc.gridX][curLoc.gridY + i+1] = ArenaMap.EMP;
					else if(environment[curLoc.gridX][curLoc.gridY + i+1] == ArenaMap.OBS) {
						breakFlag = true;
						break;
					}
				}
				if (farFront < Config.InfraRedDetectDist && !breakFlag) {
					if (environment[curLoc.gridX][curLoc.gridY + farFront+1] == ArenaMap.UNKNOWN)
					environment[curLoc.gridX][curLoc.gridY + farFront+1] = ArenaMap.OBS;
				}
			}
			break;
		case Direction.DOWN:
			farFront = Math.min(Config.InfraRedDetectDist, ulLeft);
			if (Config.twoBytwo) {
				for (int i = 0; i < farFront; i++) {
					environment[curLoc.gridX - 1][curLoc.gridY - i - 3] = ArenaMap.EMP;
				}
				if (farFront < Config.InfraRedDetectDist) {
					environment[curLoc.gridX - 1][curLoc.gridY - ulLeft - 3] = ArenaMap.OBS;
				}
				farFront = Math.min(Config.InfraRedDetectDist, ulRight);
				for (int i = 0; i < farFront; i++) {
					environment[curLoc.gridX - 2][curLoc.gridY - i - 3] = ArenaMap.EMP;
				}
				if (farFront < Config.InfraRedDetectDist) {
					environment[curLoc.gridX - 2][curLoc.gridY - ulRight - 3] = ArenaMap.OBS;
				}
			} else {
				boolean breakFlag = false;
                frontWay = robot.getCurrentLocation().gridY - 3;
                farFront = Math.min(frontWay,Math.min(Config.InfraRedDetectDist, ulLeft));
				for (int i = 0; i < farFront; i++) {
					if (environment[curLoc.gridX - 0][curLoc.gridY - i - 3] == ArenaMap.UNKNOWN)
					environment[curLoc.gridX - 0][curLoc.gridY - i - 3] = ArenaMap.EMP;
					else if(environment[curLoc.gridX - 0][curLoc.gridY - i - 3] == ArenaMap.OBS) {
						breakFlag = true;
						break;
					}
				}
				if (farFront < Config.InfraRedDetectDist && !breakFlag) {
					if (environment[curLoc.gridX - 0][curLoc.gridY - farFront - 3] == ArenaMap.UNKNOWN)
					environment[curLoc.gridX - 0][curLoc.gridY - farFront - 3] = ArenaMap.OBS;
				}
				breakFlag = false;
				frontWay = robot.getCurrentLocation().gridY - 3;

                farFront = Math.min(frontWay,Math.min(Config.InfraRedDetectDist, ulCenter));
				for (int i = 0; i < farFront; i++) {
					if (environment[curLoc.gridX - 1][curLoc.gridY - i - 3] == ArenaMap.UNKNOWN)
					environment[curLoc.gridX - 1][curLoc.gridY - i - 3] = ArenaMap.EMP;
					else if(environment[curLoc.gridX - 1][curLoc.gridY - i - 3] == ArenaMap.OBS) {
						breakFlag = true;
						break;
					}
				}
				if (farFront < Config.InfraRedDetectDist && !breakFlag) {
					if (environment[curLoc.gridX - 1][curLoc.gridY - farFront - 3] == ArenaMap.UNKNOWN)
					environment[curLoc.gridX - 1][curLoc.gridY - farFront - 3] = ArenaMap.OBS;
				}
				breakFlag = false;
                frontWay = robot.getCurrentLocation().gridY - 3;
				farFront = Math.min(frontWay,Math.min(Config.InfraRedDetectDist, ulRight));
				for (int i = 0; i < farFront; i++) {
					if (environment[curLoc.gridX - 2][curLoc.gridY - i - 3] == ArenaMap.UNKNOWN)
					environment[curLoc.gridX - 2][curLoc.gridY - i - 3] = ArenaMap.EMP;
					else if(environment[curLoc.gridX - 2][curLoc.gridY - i - 3] == ArenaMap.OBS) {
						breakFlag = true;
						break;
					}
				}
				if (farFront < Config.InfraRedDetectDist && !breakFlag) {
					if (environment[curLoc.gridX - 2][curLoc.gridY - farFront - 3] == ArenaMap.UNKNOWN)
					environment[curLoc.gridX - 2][curLoc.gridY - farFront - 3] = ArenaMap.OBS;
				}
			}
			break;
		case Direction.LEFT:
			farFront = Math.min(Config.InfraRedDetectDist, ulLeft);
			if (Config.twoBytwo) {
				for (int i = 0; i < farFront; i++) {
					environment[curLoc.gridX - 2 - i][curLoc.gridY - 1 - 1] = ArenaMap.EMP;
				}
				if (farFront < Config.InfraRedDetectDist) {
					environment[curLoc.gridX - 2 - ulLeft][curLoc.gridY - 1 - 1] = ArenaMap.OBS;
				}
				farFront = Math.min(Config.InfraRedDetectDist, ulRight);
				for (int i = 0; i < farFront; i++) {
					environment[curLoc.gridX - 2 - i][curLoc.gridY - 1] = ArenaMap.EMP;
				}
				if (farFront < Config.InfraRedDetectDist) {
					environment[curLoc.gridX - 2 - ulRight][curLoc.gridY - 1] = ArenaMap.OBS;
				}
			} else {
				boolean breakFlag = false;
                frontWay = robot.getCurrentLocation().gridX - 3;
                farFront = Math.min(frontWay,Math.min(Config.InfraRedDetectDist,ulLeft));
				for (int i = 0; i < farFront; i++) {
					if (environment[curLoc.gridX - 2 - i][curLoc.gridY - 1 - 1] == ArenaMap.UNKNOWN)
					environment[curLoc.gridX - 2 - i][curLoc.gridY - 1 - 1] = ArenaMap.EMP;
					else if(environment[curLoc.gridX - 2 - i][curLoc.gridY - 1 - 1] == ArenaMap.OBS) {
						breakFlag = true;
						break;
					}
				}
				if (farFront < Config.InfraRedDetectDist && !breakFlag) {
					if (environment[curLoc.gridX - 2 - farFront][curLoc.gridY - 1 - 1] == ArenaMap.UNKNOWN)
					environment[curLoc.gridX - 2 - farFront][curLoc.gridY - 1 - 1] = ArenaMap.OBS;
				}
				breakFlag = false;
				frontWay = robot.getCurrentLocation().gridX - 3;

                farFront = Math.min(frontWay,Math.min(Config.InfraRedDetectDist, ulCenter));
				for (int i = 0; i < farFront; i++) {
					if (environment[curLoc.gridX - 2 - i][curLoc.gridY - 1] == ArenaMap.UNKNOWN)
					environment[curLoc.gridX - 2 - i][curLoc.gridY - 1] = ArenaMap.EMP;
					else if(environment[curLoc.gridX - 2 - i][curLoc.gridY - 1] == ArenaMap.OBS) {
						breakFlag = true;
						break;
					}
					
				}
				if (farFront < Config.InfraRedDetectDist && !breakFlag) {
					if (environment[curLoc.gridX - 2 - farFront][curLoc.gridY - 1] == ArenaMap.UNKNOWN)
					environment[curLoc.gridX - 2 - farFront][curLoc.gridY - 1] = ArenaMap.OBS;
				}
				breakFlag = false;
                frontWay = robot.getCurrentLocation().gridX - 3;
				farFront = Math.min(frontWay,Math.min(Config.InfraRedDetectDist, ulRight));
				for (int i = 0; i < farFront; i++) {
					if (environment[curLoc.gridX - 2 - i][curLoc.gridY] == ArenaMap.UNKNOWN)
					environment[curLoc.gridX - 2 - i][curLoc.gridY] = ArenaMap.EMP;
					else if(environment[curLoc.gridX - 2 - i][curLoc.gridY] == ArenaMap.OBS) {
						breakFlag = true;
						break;
					}
				}
				if (farFront < Config.InfraRedDetectDist && !breakFlag) {
					if (environment[curLoc.gridX - 2 - farFront][curLoc.gridY] == ArenaMap.UNKNOWN)
					environment[curLoc.gridX - 2 - farFront][curLoc.gridY] = ArenaMap.OBS;
				}
			}
			break;
		case Direction.RIGHT:
			farFront = Math.min(Config.InfraRedDetectDist, ulLeft);
			if (Config.twoBytwo) {
				for (int i = 0; i < farFront; i++) {
					environment[curLoc.gridX + i][curLoc.gridY - 1] = ArenaMap.EMP;
				}
				if (farFront < Config.InfraRedDetectDist) {
					environment[curLoc.gridX + ulLeft][curLoc.gridY - 1] = ArenaMap.OBS;
				}
				farFront = Math.min(Config.InfraRedDetectDist, ulRight);
				for (int i = 0; i < farFront; i++) {
					environment[curLoc.gridX + i][curLoc.gridY - 1 - 1] = ArenaMap.EMP;
				}
				if (farFront < Config.InfraRedDetectDist) {
					environment[curLoc.gridX + ulRight][curLoc.gridY - 1 - 1] = ArenaMap.OBS;
				}
			} else {
				boolean breakFlag = false;
                frontWay = ArenaMap.MAXN-2 - robot.getCurrentLocation().gridX;
                farFront = Math.min(frontWay,Math.min(Config.InfraRedDetectDist,ulLeft));
				for (int i = 0; i < farFront; i++) {
					if (environment[curLoc.gridX + i + 1][curLoc.gridY] == ArenaMap.UNKNOWN)
					environment[curLoc.gridX + i + 1][curLoc.gridY] = ArenaMap.EMP;
					else if(environment[curLoc.gridX + i + 1][curLoc.gridY] == ArenaMap.OBS) {
						breakFlag = true;
						break;
					}
				}
				if (farFront < Config.InfraRedDetectDist && !breakFlag) {
					if (environment[curLoc.gridX + farFront + 1][curLoc.gridY] == ArenaMap.UNKNOWN)
					environment[curLoc.gridX + farFront + 1][curLoc.gridY] = ArenaMap.OBS;
				}
				breakFlag = false;
				frontWay = ArenaMap.MAXN-2 - robot.getCurrentLocation().gridX;

                farFront = Math.min(frontWay,Math.min(Config.InfraRedDetectDist, ulCenter));
				for (int i = 0; i < farFront; i++) {
					if (environment[curLoc.gridX + i + 1][curLoc.gridY - 1] == ArenaMap.UNKNOWN)
					environment[curLoc.gridX + i + 1][curLoc.gridY - 1] = ArenaMap.EMP;
					else if(environment[curLoc.gridX + i + 1][curLoc.gridY - 1] == ArenaMap.OBS) {
						breakFlag = true;
						break;
					}
				}
				if (farFront < Config.InfraRedDetectDist && !breakFlag) {
					if (environment[curLoc.gridX + farFront + 1][curLoc.gridY - 1] == ArenaMap.UNKNOWN)
					environment[curLoc.gridX + farFront + 1][curLoc.gridY - 1] = ArenaMap.OBS;
				}
				breakFlag = false;
                frontWay = ArenaMap.MAXN-2 - robot.getCurrentLocation().gridX;
				farFront = Math.min(frontWay,Math.min(Config.InfraRedDetectDist, ulRight));
				for (int i = 0; i < farFront; i++) {
					if (environment[curLoc.gridX + i + 1][curLoc.gridY - 1 - 1 ] == ArenaMap.UNKNOWN)
					environment[curLoc.gridX + i + 1][curLoc.gridY - 1 - 1 ] = ArenaMap.EMP;
					else if(environment[curLoc.gridX + i + 1][curLoc.gridY - 1 - 1 ] == ArenaMap.OBS) {
						breakFlag = true;
						break;
					}
				}
				if (farFront < Config.InfraRedDetectDist && !breakFlag) {
					if (environment[curLoc.gridX + farFront + 1][curLoc.gridY - 1 - 1] == ArenaMap.UNKNOWN)
					environment[curLoc.gridX + farFront + 1][curLoc.gridY - 1 - 1] = ArenaMap.OBS;
				}
			}
			break;
		}
		}
		return 0;
	}

	public int perceptLeft() {
		Point curLoc = robot.getCurrentLocation();
		int uInt =communicator.leftSensor(); 
		int farFront;
        int frontWay;
        if(updateFlag){
		switch (robot.getDirection().getDirection()) {
		case Direction.UP:
			farFront = Math.min(Config.InfraRedDetectDist, uInt);
			if (Config.twoBytwo) {
				for (int i = 0; i < farFront; i++) {
					environment[curLoc.gridX - 1 - 1 - 1 - i][curLoc.gridY - 1] = ArenaMap.EMP;
				}
				if (farFront < Config.InfraRedDetectDist) {
					environment[curLoc.gridX - 1 - 1 - 1 - uInt][curLoc.gridY - 1] = ArenaMap.OBS;
				}
			}
			else {
				boolean breakFlag = false;
                frontWay = robot.getCurrentLocation().gridX - 3;
                farFront = Math.min(frontWay,Math.min(Config.InfraRedDetectDist,uInt));
				for (int i = 0; i < farFront; i++) {
					if (environment[curLoc.gridX - 1 - 1 - 1 - i][curLoc.gridY] == ArenaMap.UNKNOWN)
					environment[curLoc.gridX - 1 - 1 - 1 - i][curLoc.gridY] = ArenaMap.EMP;
					else if(environment[curLoc.gridX - 1 - 1 - 1 - i][curLoc.gridY] == ArenaMap.OBS) {
						breakFlag = true;
						break;
					}
				}
				if (farFront < Config.InfraRedDetectDist && !breakFlag) {
					if (environment[curLoc.gridX - 1 - 1 - 1 - farFront][curLoc.gridY] == ArenaMap.UNKNOWN)
					environment[curLoc.gridX - 1 - 1 - 1 - farFront][curLoc.gridY] = ArenaMap.OBS;
				}
			}
			break;
		case Direction.DOWN:
			farFront = Math.min(Config.InfraRedDetectDist, uInt);
			if (Config.twoBytwo) {
				for (int i = 0; i < farFront; i++) {
					environment[curLoc.gridX + i][curLoc.gridY - 2] = ArenaMap.EMP;
				}
				if (farFront < Config.InfraRedDetectDist) {
					environment[curLoc.gridX + uInt][curLoc.gridY - 2] = ArenaMap.OBS;
				}
			} else {
				boolean breakFlag = false;
                frontWay = ArenaMap.MAXN-2 - robot.getCurrentLocation().gridX;
                farFront = Math.min(frontWay,Math.min(Config.InfraRedDetectDist, uInt));
				for (int i = 0; i < farFront; i++) {
					if (environment[curLoc.gridX + i + 1][curLoc.gridY - 2] == ArenaMap.UNKNOWN)
					environment[curLoc.gridX + i + 1][curLoc.gridY - 2] = ArenaMap.EMP;
					else if(environment[curLoc.gridX + i + 1][curLoc.gridY - 2] == ArenaMap.OBS) {
						breakFlag = true;
						break;
					}
				}
				if (farFront < Config.InfraRedDetectDist && !breakFlag) {
					if (environment[curLoc.gridX + farFront + 1][curLoc.gridY - 2]  == ArenaMap.UNKNOWN)
					environment[curLoc.gridX + farFront + 1][curLoc.gridY - 2] = ArenaMap.OBS;
				}
			}
			break;
		case Direction.LEFT:
			farFront = Math.min(Config.InfraRedDetectDist, uInt);
			if (Config.twoBytwo) {
				for (int i = 0; i < farFront; i++) {
					environment[curLoc.gridX - 2][curLoc.gridY - 3 - i] = ArenaMap.EMP;
				}
				if (farFront < Config.InfraRedDetectDist) {
					environment[curLoc.gridX - 2][curLoc.gridY - 3 - uInt] = ArenaMap.OBS;
				}
			} else {
				boolean breakFlag = false;
                frontWay = robot.getCurrentLocation().gridY - 3;
                farFront = Math.min(frontWay,Math.min(Config.InfraRedDetectDist, uInt));
				for (int i = 0; i < farFront; i++) {
					if (environment[curLoc.gridX - 2][curLoc.gridY - 3 - i]  == ArenaMap.UNKNOWN)
						environment[curLoc.gridX - 2][curLoc.gridY - 3 - i] = ArenaMap.EMP;
					else if(environment[curLoc.gridX - 2][curLoc.gridY - 3 - i] == ArenaMap.OBS) {
						breakFlag = true;
						break;
					}
				}
				if (farFront < Config.InfraRedDetectDist && !breakFlag) {
					if (environment[curLoc.gridX - 2][curLoc.gridY - 3 - farFront] == ArenaMap.UNKNOWN)
					environment[curLoc.gridX - 2][curLoc.gridY - 3 - farFront] = ArenaMap.OBS;
				}

			}
			break;
		case Direction.RIGHT:
			if (Config.twoBytwo) {
				farFront = Math.min(Config.InfraRedDetectDist, uInt);
				for (int i = 0; i < farFront; i++) {
					environment[curLoc.gridX - 1][curLoc.gridY + i] = ArenaMap.EMP;
				}
				if (farFront < Config.InfraRedDetectDist) {
					environment[curLoc.gridX - 1][curLoc.gridY + uInt] = ArenaMap.OBS;
				}
			} else {
				boolean breakFlag = false;
                frontWay = ArenaMap.MAXM-2 - robot.getCurrentLocation().gridY;
                farFront = Math.min(frontWay,Math.min(Config.InfraRedDetectDist, uInt));
				for (int i = 0; i < farFront; i++) {
					if (environment[curLoc.gridX ][curLoc.gridY + i + 1] == ArenaMap.UNKNOWN)
						environment[curLoc.gridX ][curLoc.gridY + i + 1] = ArenaMap.EMP;
					else if(environment[curLoc.gridX ][curLoc.gridY + i + 1] == ArenaMap.OBS) {
						breakFlag = true;
						break;
					}
				}
				if (farFront < Config.InfraRedDetectDist && !breakFlag) {
					if (environment[curLoc.gridX ][curLoc.gridY + farFront + 1] == ArenaMap.UNKNOWN)
					environment[curLoc.gridX ][curLoc.gridY + farFront + 1] = ArenaMap.OBS;
				}
			}
			break;
		}
        }
		return 0;
	}

	public int perceptRight() {
		Point curLoc = robot.getCurrentLocation();
		int uInt =communicator.rightSensor(); 
		int farFront;
		int frontWay;
		if(updateFlag){
		switch (robot.getDirection().getDirection()) {
		case Direction.UP:
			farFront = Math.min(Config.LongInfraredDetectDist, uInt);
			if (Config.twoBytwo) {
				for (int i = 0; i < farFront; i++) {
					environment[curLoc.gridX + i][curLoc.gridY - 1] = ArenaMap.EMP;
				}
				if (farFront < Config.LongInfraredDetectDist) {
					environment[curLoc.gridX + uInt][curLoc.gridY - 1] = ArenaMap.OBS;
				}
			} else {
				boolean breakFlag = false;
                frontWay = ArenaMap.MAXN-2 - robot.getCurrentLocation().gridX;

                farFront = Math.min(frontWay,Math.min(Config.LongInfraredDetectDist, uInt));
				for (int i = 0; i < farFront; i++) {
					if (environment[curLoc.gridX + i + 1][curLoc.gridY] == ArenaMap.UNKNOWN)
					environment[curLoc.gridX + i + 1][curLoc.gridY] = ArenaMap.EMP;
					else if(environment[curLoc.gridX + i + 1][curLoc.gridY] == ArenaMap.OBS) {
						breakFlag = true;
						break;
					}
				}
				if (farFront < Config.LongInfraredDetectDist  && !breakFlag) {
					if (environment[curLoc.gridX + farFront + 1][curLoc.gridY] == ArenaMap.UNKNOWN)
					environment[curLoc.gridX + farFront + 1][curLoc.gridY] = ArenaMap.OBS;
				}
			}
			break;
		case Direction.DOWN:
			farFront = Math.min(Config.InfraRedDetectDist, uInt);
			if (Config.twoBytwo) {
				for (int i = 0; i < farFront; i++) {
					environment[curLoc.gridX - 3 - i][curLoc.gridY - 2] = ArenaMap.EMP;
				}
				if (farFront < Config.InfraRedDetectDist) {
					environment[curLoc.gridX - 3 - uInt][curLoc.gridY - 2] = ArenaMap.OBS;
				}
			} else {
				boolean breakFlag = false;
                frontWay = robot.getCurrentLocation().gridX - 3;

                farFront = Math.min(frontWay,Math.min(Config.LongInfraredDetectDist, uInt));
				for (int i = 0; i < farFront; i++) {
					if (environment[curLoc.gridX - 3 - i][curLoc.gridY - 2] == ArenaMap.UNKNOWN)
					environment[curLoc.gridX - 3 - i][curLoc.gridY - 2] = ArenaMap.EMP;
					else if(environment[curLoc.gridX - 3 - i][curLoc.gridY - 2] == ArenaMap.OBS) {
						breakFlag = true;
						break;
					}
				}
				if (farFront < Config.LongInfraredDetectDist  && !breakFlag) {
					if (environment[curLoc.gridX - 3 - farFront][curLoc.gridY - 2] == ArenaMap.UNKNOWN)
					environment[curLoc.gridX - 3 - farFront][curLoc.gridY - 2] = ArenaMap.OBS;
				}
			}
			break;
		case Direction.LEFT:
			if (Config.twoBytwo) {
				farFront = Math.min(Config.InfraRedDetectDist, uInt);
				for (int i = 0; i < farFront; i++) {
					environment[curLoc.gridX - 2][curLoc.gridY + i] = ArenaMap.EMP;
				}
				if (farFront < Config.InfraRedDetectDist) {
					environment[curLoc.gridX - 2][curLoc.gridY + uInt] = ArenaMap.OBS;
				}
			} else {
				boolean breakFlag = false;
                frontWay = ArenaMap.MAXM-2 - robot.getCurrentLocation().gridY;

                farFront = Math.min(frontWay,Math.min(Config.LongInfraredDetectDist, uInt));
				for (int i = 0; i < farFront; i++) {
					if (environment[curLoc.gridX - 2][curLoc.gridY + i + 1] == ArenaMap.UNKNOWN)
					environment[curLoc.gridX - 2][curLoc.gridY + i + 1] = ArenaMap.EMP;
					else if(environment[curLoc.gridX - 2][curLoc.gridY + i + 1] == ArenaMap.OBS) {
						breakFlag = true;
						break;
					}
				}
				if (farFront < Config.LongInfraredDetectDist  && !breakFlag) {
					if (environment[curLoc.gridX - 2][curLoc.gridY + farFront + 1] == ArenaMap.UNKNOWN)
					environment[curLoc.gridX - 2][curLoc.gridY + farFront + 1] = ArenaMap.OBS;
				}
			}
			break;
		case Direction.RIGHT:
			if (Config.twoBytwo) {
				farFront = Math.min(Config.InfraRedDetectDist, uInt);
				for (int i = 0; i < farFront; i++) {
					environment[curLoc.gridX - 1][curLoc.gridY - 3 - i] = ArenaMap.EMP;
				}
				if (farFront < Config.InfraRedDetectDist) {
					environment[curLoc.gridX - 1][curLoc.gridY - 3 - uInt] = ArenaMap.OBS;
				}
			} else {
				boolean breakFlag = false;
                frontWay = robot.getCurrentLocation().gridY - 3;
                farFront = Math.min(frontWay,Math.min(Config.LongInfraredDetectDist, uInt));
				for (int i = 0; i < farFront; i++) {
					if (environment[curLoc.gridX][curLoc.gridY - 3 - i] == ArenaMap.UNKNOWN)
					environment[curLoc.gridX][curLoc.gridY - 3 - i] = ArenaMap.EMP;
					else if(environment[curLoc.gridX][curLoc.gridY - 3 - i] == ArenaMap.OBS) {
						breakFlag = true;
						break;
					}
				}
				if (farFront < Config.LongInfraredDetectDist  && !breakFlag) {
					if (environment[curLoc.gridX][curLoc.gridY - 3 - farFront] == ArenaMap.UNKNOWN)
					environment[curLoc.gridX][curLoc.gridY - 3 - farFront] = ArenaMap.OBS;
				}
			}
			break;
		}
		}
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

	public boolean isUpdateFlag() {
		return updateFlag;
	}

	public void setUpdateFlag(boolean updateFlag) {
		this.updateFlag = updateFlag;
	}

	

}
