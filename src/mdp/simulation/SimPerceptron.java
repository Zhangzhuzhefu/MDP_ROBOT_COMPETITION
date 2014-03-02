package mdp.simulation;

import mdp.algo.Point;
import mdp.algo.VirtualPerceptron;
import mdp.gui.MapPanel;

public class SimPerceptron extends VirtualPerceptron{
	//protected VirtualCommunicator communicator;
	int[][] environment;
	
	
	public SimPerceptron (MapPanel p){
		super();
		communicator = new SimCommunicator(p);
	}
	
	@Override
	public int perceptFront() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int perceptLeft() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int perceptRight() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public int percept(Point p) {
		return environment[p.gridX][p.gridY];
	}
	
	public int percept(int x, int y) {
		return environment[x][y];
	}

	@Override
	public int[][] getEnvironment() {
		return environment;
	}

	//only for simulator
	public void setEnvironment(int [][] e) {
		environment = e;
	}

	
}
