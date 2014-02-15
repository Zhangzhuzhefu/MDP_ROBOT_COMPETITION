package mdp.competition;

import mdp.algo.VirtualPerceptron;

public class Perceptron extends VirtualPerceptron{

	public Perceptron() {
		super();
		communicator = new Communicator();
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

	@Override
	public int[][] getEnvironment() {
		return null;
	}

	

}
