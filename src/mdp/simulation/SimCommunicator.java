package mdp.simulation;

import mdp.algo.VirtualCommunicator;
import mdp.gui.MapPanel;

public class SimCommunicator extends VirtualCommunicator{

	public MapPanel mapPanel;
	
	public SimCommunicator(MapPanel p) {
		mapPanel  = p;
	}

	@Override
	public void updatePath() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateRobot() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateMap() {
		// TODO Auto-generated method stub
		
	}

	public MapPanel getMapPanel() {
		return mapPanel;
	}

	public void setMapPanel(MapPanel mapPanel) {
		this.mapPanel = mapPanel;
	}
	
	

}
