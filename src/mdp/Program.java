package mdp;

import mdp.competition.Competition;
import mdp.gui.MainFrame;
import mdp.simulation.Simulator;

import java.io.IOException;


public class Program {
	
	public static MainFrame mainFrame;
	public static Simulator simulator;
    public static Competition competition;
	
	public static void main(String[] args) throws IOException{
		// GUI
		mainFrame = new MainFrame();
		if (Config.Competition){
            competition = new Competition(mainFrame);
            competition.startRunning();
        } else {
		    simulator = new Simulator(mainFrame);
		    simulator.startSimulation();}
	}
}
