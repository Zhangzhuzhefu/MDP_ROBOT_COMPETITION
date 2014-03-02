package mdp.competition;

import mdp.algo.Point;
import mdp.algo.VirtualCommunicator;

public class Perceptron{

	protected VirtualCommunicator communicator;
	static int[][] environment;
	
	public Perceptron() {
	}

	public int perceptFront() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int perceptLeft() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int perceptRight() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int[][] getEnvironment() {
		return environment;
	}
	
	public VirtualCommunicator getCommunicator() {
		return communicator;
	}

	public void setCommunicator(VirtualCommunicator communicator) {
		this.communicator = communicator;
	}

	//only for simulator
	public static void setEnvironment(int[][] e) {
		environment = e;
	}
	
	public int percept(Point p) {
		return environment[p.gridX][p.gridY];
	}
	
	public int percept(int x, int y) {
		return environment[x][y];
	}

	

}
