package mdp.simulation;

import java.util.Stack;

import mdp.algo.ArenaMap;
import mdp.algo.Explorer;
import mdp.algo.PathFinder;
import mdp.algo.Point;
import mdp.algo.Robot;
import mdp.algo.RobotManager;
import mdp.gui.MainFrame;
import mdp.gui.MapPanel;

public class Simulator {

	MapPanel simulatorMapPanel;
	SimPerceptron perceptronSim;
	RobotManager robotManager;
	Robot robot;
	Explorer explorer;
	PathFinder pathFinder;
	
	public Simulator() {
		// GUI
		MainFrame mainFrame = new MainFrame();
		simulatorMapPanel = mainFrame.getMap();

		// setup robot
		robotManager = new RobotManager();
		robot = robotManager.getRobot();
		robot.setSensors(new SimPerceptron());

		// setup algo
		explorer = new Explorer();
		perceptronSim = new SimPerceptron();
		pathFinder = new PathFinder();
		
	}
	
	//TODO
	public void startSimulation() {
		robotManager.start();
		explore();
		generateShortestPath();
		secondRun();
	}
	
	public void explore(){
		//TODO
		//Explore share not talk to mapPanel. 
		//Instead, mapPanel share register an observer to Robots knowledgebase
		simulatorMapPanel.updateRobot(ArenaMap.START_POINT);
		/*
		 * test painting  *should be the knowledge base, not actual environment!
		*/
		//update the full map to Robots knowledge base
		robot.updatePerceptronToKnowledgebase();
		//draw the map
		simulatorMapPanel.updateMap(robot.getMapKnowledgeBase().getArrayMap());
	}
	
	public void generateShortestPath(){
		pathFinder.setMap(robot.getMapKnowledgeBase().getArrayMap());
		System.out.println("Finding Path: " + pathFinder.findRandomPath());
		Stack <Point> path = pathFinder.getPath();
		simulatorMapPanel.updatePath(path);
	}
	
	public void secondRun(){
		
	}
	
	
}
