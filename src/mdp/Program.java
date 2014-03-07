package mdp;

import mdp.gui.MainFrame;
import mdp.simulation.Simulator;

import java.io.IOException;


public class Program {
	
	public static MainFrame mainFrame;
	public static Simulator simulator;
	
	public static void main(String[] args) throws IOException{
		// GUI
		mainFrame = new MainFrame();
		
		simulator = new Simulator(mainFrame);
		simulator.startSimulation();
	}
}
