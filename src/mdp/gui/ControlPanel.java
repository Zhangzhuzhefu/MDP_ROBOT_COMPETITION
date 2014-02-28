package mdp.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import mdp.algo.ArenaMap;
import mdp.simulation.Simulator;

public class ControlPanel extends Panel{
	private static final long serialVersionUID = 1L;
	
	private JButton jbRandomPath;
	private JButton jbshortestPath;
	
	private JButton algo1;
	private JButton algo2;
	private JButton jbExpolore;
	private JButton jbRun;
	
	
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
		jbRandomPath.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Simulator.updateRandomPath();
			}
		});
		
		jbshortestPath = new JButton("Shortest Path");
		jbshortestPath.setFont(new Font("Chalkduster", Font.PLAIN, 12));	
		jbshortestPath.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Simulator.updateShortestPath();
			}
		});

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
		
		algo1 = new JButton("Fld Fill");
		algo1.setFont(new Font("Chalkduster", Font.PLAIN, 12));
		algo1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Simulator.explore_floodFill();
			}
		});
		algo2 = new JButton("A*");
		algo2.setFont(new Font("Chalkduster", Font.PLAIN, 12));
		algo2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Simulator.explore_AStar();
			}
		});
		
		jbExpolore = new JButton("Explore");
		jbExpolore.setFont(new Font("Chalkduster", Font.PLAIN, 12));
		jbExpolore.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Simulator.explore();
			}
		});
		
		jbRun = new JButton("Run");
		jbRun.setFont(new Font("Chalkduster", Font.PLAIN, 12));
		jbRun.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Simulator.secondRun();
			}
		});
		
		JPanel panel21 = new JPanel(new GridLayout(1,2));
		panel21.add(algo1);
		panel21.add(algo2);
		
		panel2.add(lbPanel2);
		panel2.add(panel21);
		panel2.add(jbExpolore);
		panel2.add(jbRun);
		panel2.setLayout(gridLayout);
		panel2.setBorder(emptyBorder);
		panel2.setBackground(new Color(255,250,240));
		
		//Panel3
		lbPanel3 = new JLabel("Settings");
		lbPanel3.setFont(new Font("Chalkduster", Font.PLAIN, 12));
		lbPanel3.setHorizontalAlignment(SwingConstants.CENTER);
		
		mapSwitch = new JToggleButton("Map Display");
		mapSwitch.setFont(new Font("Chalkduster", Font.PLAIN, 12));
		mapSwitch.setOpaque(false);
		mapSwitch.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED)
		        {
					mapSwitch.setText("Arena Map");
					Simulator.simulatorMapPanel.updateMap(
							ArenaMap.actualMap);
		        }
		        else
		        {
		        	mapSwitch.setText("Robot Map");
		        	Simulator.simulatorMapPanel.updateMap(
		        			Simulator.robot.getMapKnowledgeBase().getArrayMap());
		        } 
			}
		});
		
		jbReset = new JButton("Reset");
		jbReset.setFont(new Font("Chalkduster", Font.PLAIN, 12));
		jbReset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(mapSwitch.isSelected()) mapSwitch.doClick();
				Simulator.reset();
			}
		});
		
		
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
