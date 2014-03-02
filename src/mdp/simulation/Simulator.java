package mdp.simulation;

import mdp.Config;
import mdp.Utils;
import mdp.algo.ArenaMap;
import mdp.algo.Explorer;
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
		simPerceptron = new SimPerceptron(simulatorMapPanel);
		robot.setSensors(simPerceptron);
		
	}
	
	//TODO
	
	public void startSimulation() {
		ArenaMap.actualMap = Utils.loadMazeEnvironment(Config.mapFileName);
		simulatorMapPanel.updateRobot(ArenaMap.START_POINT,robot.getDirection());
		simulatorMapPanel.updateMap(robot.getMapKnowledgeBase().getArrayMap());
		
		//make sure perceptron is the same as knowledgebase
		simPerceptron.setEnvironment(robot.getMapKnowledgeBase().getArrayMap());
	}
	
	public static void explore(){
		//TODO
		//Explore share not talk to mapPanel. 
		//Instead, mapPanel share register an observer to Robots knowledge base
		simulatorMapPanel.updateRobot(ArenaMap.START_POINT,robot.getDirection());
		//update the full map to Robots knowledge base
		simPerceptron.setEnvironment(ArenaMap.actualMap.clone());
        //simPerceptron.setEnvironment(robot.getMapKnowledgeBase().getArrayMap());
		robot.updatePerceptronToKnowledgebase();
		
		//draw the map
		simulatorMapPanel.updateMap(robot.getMapKnowledgeBase().getArrayMap());
	}
	
	public static void explore_floodFill(){
		//TODO
		simulatorMapPanel.updateRobot(ArenaMap.START_POINT,robot.getDirection());
		robot.explore(Explorer.FLOODFILL);
        simPerceptron.setEnvironment(robot.getMapKnowledgeBase().getArrayMap());
		robot.updatePerceptronToKnowledgebase();
		simulatorMapPanel.updateMap(robot.getMapKnowledgeBase().getArrayMap());
	}
	public static void explore_AStar(){
		//TODO
		simulatorMapPanel.updateRobot(ArenaMap.START_POINT,robot.getDirection());
		robot.explore(Explorer.ASTAR);
        simPerceptron.setEnvironment(robot.getMapKnowledgeBase().getArrayMap());
		robot.updatePerceptronToKnowledgebase();
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
		ArenaMap.actualMap = Utils.loadMazeEnvironment(Config.mapFileName);
		robot.setDirectionDegree(0);
		simulatorMapPanel.updateRobot(ArenaMap.START_POINT,robot.getDirection());
		simulatorMapPanel.updatePath(robot.getRoute());
	}
	
	
}
