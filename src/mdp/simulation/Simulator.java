package mdp.simulation;

import mdp.Config;
import mdp.algo.Explorer;
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
	
	public Simulator() {
		// GUI
		MainFrame mainFrame = new MainFrame();
		simulatorMapPanel = mainFrame.getMap();

		// setup robot
		robotManager = new RobotManager();
		robot = robotManager.getRobot();

		// setup algo
		explorer = new Explorer();
		perceptronSim = new SimPerceptron();
		
		//test painting  *should be the knowledge base, not actual environment!
		simulatorMapPanel.updateMap(robot.getMapKnowledgeBase().getArrayMap());
		
	}
	
	//TODO
	public void startSimulation() {
		robotManager.start();
		explore();
	}
	
	public void explore(){
		//TODO
		//Explore share not talk to mapPanel. 
		//Instead, mapPanel share register an observer to Robots knowledgebase
		simulatorMapPanel.updateRobot(Config.START_POINT);//test robot
	}
}
