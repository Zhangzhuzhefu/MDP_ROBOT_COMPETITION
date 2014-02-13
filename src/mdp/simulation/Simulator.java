package mdp.simulation;

import mdp.Config;
import mdp.algo.Explorer;
import mdp.algo.Robot;
import mdp.algo.RobotManager;
import mdp.gui.MainFrame;
import mdp.gui.MapPanel;

public class Simulator {

	MapPanel simulatorMap;
	SimPerceptron perceptronSim;
	RobotManager robotManager;
	Robot robot;
	Explorer explorer;
	
	public Simulator() {
		// GUI
		MainFrame mainFrame = new MainFrame();
		simulatorMap = mainFrame.getMap();

		// setup robot
		robotManager = new RobotManager();
		robot = robotManager.getRobot();

		// setup algo
		explorer = new Explorer();
		perceptronSim = new SimPerceptron();
		
		//test painting  *should be the knowledge base, not actual environment!
		simulatorMap.updateMap(robot.getMapKnowledgeBase().getArrayMap());
		
	}
	
	//TODO
	public void startSimulation() {
		robotManager.start();
		explore();
	}
	
	public void explore(){
		simulatorMap.updateRobot(Config.START_POINT);//test robot
	}
}
