package mdp.gui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import mdp.Config;

public class MainFrame extends JFrame{
	
	private static final long serialVersionUID = 1L;
	MapPanel map;
	ControlPanel control;
	int[] position = new int[4];
	
	public MainFrame() {
		//initialization 
		setResizable(true);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("MDP Control Center");
		position = Config.FRAME_LOCATION;
		setSize(1050,700);
		setLocationRelativeTo(null);

		//setLayout(new BorderLayout());
		
		map = new MapPanel();
		map.setOpaque(false);
		add(map,BorderLayout.CENTER);
		
		control = new ControlPanel();
		control.setBackground(new Color(255,255,255));
		add(control,BorderLayout.EAST);
		
		
		//pack ();
		setVisible(true);
	}

	public MapPanel getMap() {
		return map;
	}

}
