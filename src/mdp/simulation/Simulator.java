package mdp.simulation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;

import mdp.Config;
import mdp.algo.ArenaMap;
import mdp.algo.Robot;
import mdp.algo.RobotManager;
import mdp.gui.MainFrame;
import mdp.gui.MapPanel;

public class Simulator {

	public static MapPanel simulatorMapPanel;
	public static RobotManager robotManager;
	public static Robot robot;
	public static SimPerceptron simPerceptron; 
	
	
	
	public Simulator(MainFrame mainFrame) {
		//GUI
		simulatorMapPanel = mainFrame.getMap();
		
		// setup robot
		robotManager = new RobotManager();
		robot = robotManager.getRobot();
		SimPerceptron sp = new SimPerceptron(simulatorMapPanel);
		robot.setSensors(sp);
		
	}
	
	//TODO
	
	public void startSimulation() {
		ArenaMap.actualMap = loadMazeEnvironment(Config.mapFileName);
		simulatorMapPanel.updateRobot(ArenaMap.START_POINT);
		simulatorMapPanel.updateMap(robot.getMapKnowledgeBase().getArrayMap());
		simPerceptron = (SimPerceptron) robot.getSensors();
		simPerceptron.setEnvironment(robot.getMapKnowledgeBase().getArrayMap());
		//explore();
	}
	
	public static void explore(){
		//TODO
		//Explore share not talk to mapPanel. 
		//Instead, mapPanel share register an observer to Robots knowledge base
		simulatorMapPanel.updateRobot(ArenaMap.START_POINT);
		/*
		 * test painting  *should be the knowledge base, not actual environment!
		*/
		//update the full map to Robots knowledge base
		simPerceptron.setEnvironment(ArenaMap.actualMap.clone());
		robot.updatePerceptronToKnowledgebase();
		//draw the map
		simulatorMapPanel.updateMap(robot.getMapKnowledgeBase().getArrayMap());
	}
	
	public static void updateRandomPath(){
		simulatorMapPanel.updatePath(robot.generateRandomPath());
	}
	
	public static void updateShortestPath(){
		simulatorMapPanel.updatePath(robot.generateShortestPath());
	}
	
	public static void secondRun(){
		robotManager.robotRun();
	}
	
	public static void reset(){
		robot.reset();
		simulatorMapPanel.updateRobot(ArenaMap.START_POINT);
		simulatorMapPanel.updatePath(robot.getRoute());
		ArenaMap.actualMap = loadMazeEnvironment(Config.mapFileName);
	}
	
	static int [][] loadMazeEnvironment(String filename) {
		File file = new File(filename);
		int[][] map = new int[ArenaMap.MAXN][ArenaMap.MAXM];
		for (int[] rows : map)
			Arrays.fill(rows, ArenaMap.OBS);
		
		try {
			@SuppressWarnings("resource")
			Reader reader = new InputStreamReader(new FileInputStream(file));
		
		char tempchar;
			
		for (int i=0; i<ArenaMap.MAXN-2; i++)
			for (int j=0; j<ArenaMap.MAXM-2; j++) {
				tempchar = (char) reader.read();
				while (tempchar < '0')
					tempchar = (char) reader.read();
				map[i+1][j+1] = tempchar - '0';
//				map[i+1][j+2] = tempchar - '0';
//				map[i+2][j+1] = tempchar - '0';
//				map[i+2][j+2] = tempchar - '0';
			}
		
		System.out.println("Print environment:");
		for (int i=0; i<ArenaMap.MAXN; i++) {
			for (int j=0; j<ArenaMap.MAXM; j++)
				System.out.print(map[i][j]);
			System.out.println();
		}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	
}
