package mdp.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Panel;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class ControlPanel extends Panel{
	private static final long serialVersionUID = 1L;
	
	private JButton jbRandomPath;
	private JButton jbshortestPath;
	
	private JButton jbExpolore;
	
	
	private JToggleButton mapSwitch;
	private JButton jbReset;
	
	private JLabel lbPanel1;
	private JLabel lbPanel2;
	private JLabel lbPanel3;
	
	
	
	
	public ControlPanel() {
		
		setLayout(new GridLayout(3,1));
		JPanel panel1 = new JPanel();
		JPanel panel2 = new JPanel();
		JPanel panel3 = new JPanel();
		EmptyBorder emptyBorder = new EmptyBorder(0,8,5,8);
		GridLayout gridLayout = new GridLayout(4,1);
		
		//Panel1
		lbPanel1 = new JLabel("map controller");
		lbPanel1.setFont(new Font("Chalkduster", Font.PLAIN, 12));
		lbPanel1.setHorizontalAlignment(SwingConstants.CENTER);
		
		jbRandomPath = new JButton("Random Path");
		jbRandomPath.setFont(new Font("Chalkduster", Font.PLAIN, 12));
		
		jbshortestPath = new JButton("Shortest Path");
		jbshortestPath.setFont(new Font("Chalkduster", Font.PLAIN, 12));		

		panel1.add(lbPanel1);
		panel1.add(jbRandomPath);
		panel1.add(jbshortestPath);
		panel1.setLayout(gridLayout);
		panel1.setBorder(emptyBorder);
		panel1.setBackground(new Color(245,255,250));
		
		//Panel2
		lbPanel2 = new JLabel("Rob controller");
		lbPanel2.setFont(new Font("Chalkduster", Font.PLAIN, 12));
		lbPanel2.setHorizontalAlignment(SwingConstants.CENTER);
		
		jbExpolore = new JButton("Explore");
		jbExpolore.setFont(new Font("Chalkduster", Font.PLAIN, 12));	
		
		panel2.add(lbPanel2);
		panel2.add(jbExpolore);
		panel2.setLayout(gridLayout);
		panel2.setBorder(emptyBorder);
		panel2.setBackground(new Color(255,250,240));
		
		//Panel3
		lbPanel3 = new JLabel("Settings");
		lbPanel3.setFont(new Font("Chalkduster", Font.PLAIN, 12));
		lbPanel3.setHorizontalAlignment(SwingConstants.CENTER);
		
		mapSwitch = new JToggleButton("Map Display");
		mapSwitch.setFont(new Font("Chalkduster", Font.PLAIN, 12));
		
		jbReset = new JButton("Reset");
		jbReset.setFont(new Font("Chalkduster", Font.PLAIN, 12));	
		
		
		panel3.add(lbPanel3);
		panel3.add(mapSwitch);
		panel3.add(jbReset);
		panel3.setLayout(gridLayout);
		panel3.setBorder(emptyBorder);
		panel3.setBackground(new Color(240,248,255));
		
		add(panel1);
		add(panel2);
		add(panel3);
		
	}

}
