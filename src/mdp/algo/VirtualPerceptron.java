package mdp.algo;

public abstract class VirtualPerceptron {
	
	protected VirtualCommunicator communicator;
	
	public abstract int perceptFront();

	public abstract int perceptLeft();

	public abstract int perceptRight();

	public abstract int[][] getEnvironment();

	public VirtualCommunicator getCommunicator() {
		return communicator;
	}

	public void setCommunicator(VirtualCommunicator communicator) {
		this.communicator = communicator;
	}
	
}
