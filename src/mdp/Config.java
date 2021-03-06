package mdp;


public class Config {
	//simulation test file
	public static String mapFileName = "testMap.in";
	//public static String mapFileName = "shtest&fartestMap.in";
	//public static String mapFileName = "emptyMap.in";
	//public static String mapFileName = "zheMap.in";
	public static String background = "black_dark.jpg";
	public static String robotFileName = "Wall.E.png";
	public static boolean trackingOn = false;
	public static boolean printBitMapStreamOn = false;
	public static boolean debugOn = true;
	public static boolean Simulator = false;

	//environment variables
	public static final int MAZE_LENGTH = 200, MAZE_WIDTH = 150, GRID_LEN = 10;
	public static final int START_GRID_SIZE = 30;
	public static final int END_GRID_SIZE = 30;
	public static final int EPS = 2;//cm
	public static boolean twoBytwo = false;
	
	//simulation variables
	public static final double PERCENTAGEOFEXPLORATION = 1;
	public static final int PANEL_GRID_SIZE = 35;
	public static final int[] FRAME_LOCATION ={250, 100, 850, 1050};
	public static final int[] FRAME_SIZE ={900,1050};
	public static final long EXP_TIMER_MIN =8;
	public static final long EXP_TIMER_SEC =1;

	//Robot
	public static final int TIMEOUT = 12000;

	public static final int robotStepsPersecond = 50;
	public static final int robotWaitingTime = 1000/robotStepsPersecond;
	public static final double startPointX = 30.0;
	public static final double startPointY = 30.0;
	public static final double endPointX = 160.0;
	public static final double endPointY = 210.0;
	public static final double endPointX3by3 = 150.0;
	public static final double endPointY3by3 = 200.0;
	public static final int LongInfraredDetectDist = 2;
	public static final int LeftInfraRedDetectDist = 2;
    public static final int InfraRedDetectDist = 2;
	//public static final double RobotStartingDirection = 0;//right
	public static final double RobotStartingDirection = 0;

    //Rapsberry Pi
    public static final String host = "192.168.4.4";
    public static final int port = 4014;
}
