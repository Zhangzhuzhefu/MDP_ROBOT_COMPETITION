package mdp.gui;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import mdp.Config;

public class MainFrame extends JFrame{
	
	private static final long serialVersionUID = 1L;
	MapPanel map;
	int[] position = new int[4];
	
	public MainFrame() {
		//initialization 
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		position = Config.FRAME_LOCATION;
		setBounds(position[0],position[1],position[2],position[3]);
		map = new MapPanel();
		add(map);
		setVisible(true);
	}

	public MapPanel getMap() {
		return map;
	}

}
