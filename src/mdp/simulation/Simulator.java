package mdp.simulation;

import mdp.algo.ArenaMap;
import mdp.algo.Explorer;
import mdp.algo.Robot;
import mdp.algo.RobotManager;
import mdp.gui.MainFrame;
import mdp.gui.MapPanel;

import java.io.IOException;

public class Simulator {

	public static MapPanel simulatorMapPanel;
	public static RobotManager robotManager;
	public static Robot robot;
	public static SimCommunicator simCommunicator;
	
	
	
	public Simulator(MainFrame mainFrame) throws IOException{
		//GUI
		simulatorMapPanel = mainFrame.getMap();
		
		// setup robot
		robotManager = new RobotManager();
		robot = robotManager.getRobot();
		simCommunicator = new SimCommunicator();
		robot.getSensors().setCommunicator(simCommunicator);
		
	}
	
	//TODO
	
	public void startSimulation() {
		simulatorMapPanel.updateRobot(ArenaMap.START_POINT,robot.getDirection());
		simulatorMapPanel.updateMap(robot.getMapKnowledgeBase().getArrayMap());
		
		//make sure perceptron is the same as knowledgebase
		 //robot.getSensors().setEnvironment(robot.getMapKnowledgeBase().getArrayMap());
	}
	
	public static void explore(){
		//TODO
		//Explore share not talk to mapPanel. 
		//Instead, mapPanel share register an observer to Robots knowledge base
		simulatorMapPanel.updateRobot(ArenaMap.START_POINT,robot.getDirection());
		//update the full map to Robots knowledge base
		robot.getSensors().setEnvironment(ArenaMap.actualMap.clone());
        //simPerceptron.setEnvironment(robot.getMapKnowledgeBase().getArrayMap());
		robot.updatePerceptronToKnowledgebase();
		
		//draw the map
		simulatorMapPanel.updateMap(robot.getMapKnowledgeBase().getArrayMap());
	}
	
	public static void explore_floodFill(){
		//TODO
		simulatorMapPanel.updateRobot(ArenaMap.START_POINT,robot.getDirection());
		robot.explore(Explorer.FLOODFILL);
	}
	public static void explore_FollowWall(){
		//TODO
		simulatorMapPanel.updateRobot(ArenaMap.START_POINT,robot.getDirection());
		robot.explore(Explorer.FLLWALL);
		//robot.getSensors().setEnvironment(robot.getMapKnowledgeBase().getArrayMap());
		//robot.updatePerceptronToKnowledgebase();
		//simulatorMapPanel.updateMap(robot.getMapKnowledgeBase().getArrayMap());
		//simulatorMapPanel.updatePath(robot.getRoute());
	}
	
	public static void updateRandomPath(){
		simulatorMapPanel.updatePath(robot.generateRandomPath());
	}
	
	public static void updateShortestPath(){
		simulatorMapPanel.updatePath(robot.generateShortestPath());
	}
	
	public static void updateFastestPath(){
		simulatorMapPanel.updatePath(robot.generateFastestPath());
	}
	
	public static void secondRun(){
		robotManager.robotRun();
	}
	
	public static void reset(){
		robot.reset();
		robot.setDirectionDegree(0);
		simulatorMapPanel.updateMap(robot.getMapKnowledgeBase().getArrayMap());
		simulatorMapPanel.updateRobot(ArenaMap.START_POINT,robot.getDirection());
		simulatorMapPanel.updatePath(robot.getRoute());
	}
	
	
}
