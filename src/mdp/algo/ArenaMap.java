package mdp.algo;

import java.util.Arrays;

import mdp.Config;
import mdp.Utils;

public class ArenaMap {

	public static final int LENGTH = Config.MAZE_LENGTH, 
			WIDTH = Config.MAZE_WIDTH, 
			GRID_LEN = Config.GRID_LEN;
	public static final int MAXN = WIDTH / GRID_LEN + 2;
	public static final int MAXM = LENGTH / GRID_LEN + 2;
	public static final Point START_POINT = PointManager.getPoint(Config.startPointX, Config.startPointY);
	public static final Point END_POINT = PointManager.getPoint(Config.endPointX, Config.endPointY);
	
	public static final int UNKNOWN = 2, EMP = 0, OBS = 1, VWall = 3, UNSAFE=4;
	public static int[][] actualMap;
	static int[][] map;
	
	public ArenaMap() {
		map = new int[MAXN][MAXM];
		initializeMap();
		System.out.println("Printing robot knowledge base: ");
		Utils.printVirtualMap(map);
		if (Config.Simulator)
			ArenaMap.actualMap = Utils.loadMazeEnvironment(Config.mapFileName);
	}
	public void reset(){
		initializeMap();
		ArenaMap.actualMap = Utils.loadMazeEnvironment(Config.mapFileName);
	}
	public void initializeMap() {
		
		// set whole map status to be unknown
		for (int[] rows : map)
			Arrays.fill(rows, UNKNOWN);
		// set robot area empty
		int range = Config.TARGET_GRID_SIZE/ArenaMap.GRID_LEN;
		for (int i=1; i<=range; i++)
			for (int j=1; j<=range; j++)
				map[i][j] = EMP;
		
		/*//necessary? 
		for (int i=MAXN; i>=MAXM+1-range; i--)
			for (int j=MAXM; j>=MAXN+1-range; j--)
				map[i][j] = EMP;
		*/
		
		//set walls
		for (int i=0; i<MAXN; i++) {
			map[i][0] = OBS;
			map[i][MAXM-1] = OBS;
		}
		for (int i=0; i<MAXM; i++) {
			map[0][i] = OBS;
			map[MAXN-1][i] = OBS;
		}
		
	}
	
	public int[][] getArrayMap(){
		return map;
	}
	public void setArrayMap(int[][] map){
		ArenaMap.map=map;
	}
	
}
