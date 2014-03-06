package mdp;


public class Config {
	//simulation test file
	public static String mapFileName = "zheMap.in";
	public static String background = "black_dark.jpg";
	public static String robotFileName = "Wall.E.png";
	public static boolean trackingOn = false;
	public static boolean debugOn = true;
	public static boolean Simulator = true;
	
	//environment variables
	public static final int MAZE_LENGTH = 200, MAZE_WIDTH = 150, GRID_LEN = 10;
	public static final int TARGET_GRID_SIZE = 20;
	public static final int EPS = 2;//cm
	
	//simulation variables
	public static final double PERCENTAGEOFEXPLORATION = 0.88;
	public static final int PANEL_GRID_SIZE = 35;
	public static final int[] FRAME_LOCATION ={250, 100, 850, 1050};
	public static final int[] FRAME_SIZE ={900,1050};

	//Robot
	public static final int robotStepsPersecond = 4;
	public static final int robotWaitingTime = 1000/robotStepsPersecond;
	public static final double startPointX = 30.0;
	public static final double startPointY = 30.0;
	public static final double endPointX = 160.0;
	public static final double endPointY = 210.0;
	public static final int FrontSensorDetectDist = 3;
	public static final int SideSensorDetectDist = 3;
	
}
