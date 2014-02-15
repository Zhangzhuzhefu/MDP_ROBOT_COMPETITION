package mdp.simulation;

import mdp.algo.ArenaMap;
import mdp.algo.Robot;
import mdp.algo.RobotManager;
import mdp.gui.MainFrame;
import mdp.gui.MapPanel;

public class Simulator {

	MapPanel simulatorMapPanel;
	RobotManager robotManager;
	Robot robot;
	
	
	
	public Simulator() {
		// GUI
		MainFrame mainFrame = new MainFrame();
		simulatorMapPanel = mainFrame.getMap();

		// setup robot
		robotManager = new RobotManager();
		robot = robotManager.getRobot();
		robot.setSensors(new SimPerceptron(simulatorMapPanel));
		
	}
	
	//TODO
	public void startSimulation() {
		
		explore();
		findPath();
		secondRun();
		robotManager.start();
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
	
	public void findPath(){
		simulatorMapPanel.updatePath(robot.generateShortestPath());
		//simulatorMapPanel.updatePath(robot.generateRandomPath());
	}
	
	public void secondRun(){
		
	}
	
	
}
