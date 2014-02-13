package mdp.simulation;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import mdp.Config;

public class MainFrame extends JFrame{
	
	private static final long serialVersionUID = 1L;
	Map map;
	int[] position = new int[4];
	
	public MainFrame() {
		//initialization 
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		position = Config.FRAME_LOCATION;
		setBounds(position[0],position[1],position[2],position[3]);
		map = new Map();
		add(map);
		setVisible(true);
	}

	public Map getMap() {
		return map;
	}

}
