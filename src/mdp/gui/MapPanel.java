package mdp.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import mdp.Config;
import mdp.Utils;
import mdp.algo.ArenaMap;
import mdp.algo.Direction;
import mdp.algo.Point;

public class MapPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	int[][] map;
	Point robotPosition;
	Direction direction;
	ArrayList<Point> path;
	public static int GRID_SIZE = Config.PANEL_GRID_SIZE;

	private BufferedImage RobotImage;

	public MapPanel() {
		try {
			RobotImage = ImageIO.read(new File(Config.robotFileName));
		} catch (IOException ex) {
			// handle exception... whatever, nobody cares my code anyway
		}
	}

	public void updateMap(int[][] matrix) {
		map = matrix;
		this.revalidate();
		this.repaint();
	}

	public void updateRobot(Point robotPoint,Direction direction) {
		robotPosition = robotPoint;
		this.direction = direction;  
		this.revalidate();
		this.repaint();
	}

	/*public void updatePath(ArrayList<Point> path) {
		if (path == null)
			return;
		this.path = new ArrayList<Point>();
		this.path.addAll(path);
		this.revalidate();
		this.repaint();
	}*/
	
	public void updatePath(Stack<Point> path) {
		if (path == null)
			return;
		this.path = new ArrayList<Point>();
		
		this.path.addAll(path);
		this.revalidate();
		this.repaint();
	}

	@Override 
	public void paint(Graphics g) {
		Graphics2D ga = (Graphics2D) g;
		if (Config.trackingOn)
			System.out.println("visualizing...");

		if (this.map != null) {
			int height = GRID_SIZE;
			int width = GRID_SIZE;
			
			if (Config.trackingOn) {
				System.out.print("map.length: " + map.length + "    ");
				System.out.println("map.width: " + map[0].length);
			}
			//Utils.printVirtualMap(map);

			// ConvertMap mapimage = new ConvertMap(map);
			for (int i = 0; i < map.length; i++) {
				for (int j = 0; j < map[0].length; j++) {
					if (Config.trackingOn) System.out.print(map[i][j]);
					switch (map[i][j]) {
					case ArenaMap.OBS:
						ga.setPaint(Color.black);
						ga.fillRect((i + 1) * GRID_SIZE, (map[0].length-j) * GRID_SIZE, width, height);
						ga.setPaint(Color.white);
						ga.drawRect((i + 1) * GRID_SIZE, (map[0].length-j) * GRID_SIZE, width, height);
						break;
					case ArenaMap.UNKNOWN:
						ga.setPaint(Color.GRAY);
						ga.fillRect((i + 1) * GRID_SIZE, (map[0].length-j) * GRID_SIZE, width, height);
						ga.setPaint(Color.black);
						ga.drawRect((i + 1) * GRID_SIZE, (map[0].length-j) * GRID_SIZE, width, height);
						break;
					case ArenaMap.VWall:
						ga.setPaint(Color.blue);
						ga.fillRect((i + 1) * GRID_SIZE, (map[0].length-j) * GRID_SIZE, width, height);
						ga.setPaint(Color.black);
						ga.drawRect((i + 1) * GRID_SIZE, (map[0].length-j) * GRID_SIZE, width, height);
						break;
                    case ArenaMap.UNSAFE:
                        ga.setPaint(Color.red);
                        ga.fillRect((i + 1) * GRID_SIZE, (map[0].length-j) * GRID_SIZE, width, height);
                        ga.setPaint(Color.black);
                        ga.drawRect((i + 1) * GRID_SIZE, (map[0].length-j) * GRID_SIZE, width, height);
                        break;
					default:
						ga.setPaint(Color.cyan);
						ga.fillRect((i + 1) * GRID_SIZE, (map[0].length-j) * GRID_SIZE, width, height);
						ga.setPaint(Color.black);
						ga.drawRect((i + 1) * GRID_SIZE, (map[0].length-j) * GRID_SIZE, width, height);
						break;

					}

				}
				if (Config.trackingOn) System.out.println();
			}
			
			if (Config.printBitMapStreamOn) Utils.printExplorationBitMap(map);
		}

		if (Config.twoBytwo) {
			if (this.path != null) {
				if (path.size() > 2) {
					for (int k = 0; k < path.size() - 1; k++) {
						int firstpointx = map[0].length - path.get(k).gridY + 1;
						int firstpointy = path.get(k).gridX - 1;
						int secondpointx = map[0].length - path.get(k + 1).gridY + 1;
						int secondpointy = path.get(k + 1).gridX - 1;

						ga.setPaint(new Color(139, 131, 134));
						//ga.setPaint(Color.red);
						ga.setStroke(new BasicStroke(5));
						ga.drawLine((int) ((firstpointy + 1) * GRID_SIZE),
								(int) ((firstpointx + 1) * GRID_SIZE),
								(int) ((secondpointy + 1) * GRID_SIZE),
								(int) ((secondpointx + 1) * GRID_SIZE));

					}
				}
			}

			if (this.robotPosition != null) {
				double x = robotPosition.gridX ;
				double y = map[0].length - robotPosition.gridY + 2;
				double d = direction.getDegree();

				AffineTransform at = new AffineTransform();
				at.translate((int) ((x) * GRID_SIZE), (int) ((y) * GRID_SIZE));
				at.rotate(-(d + 90) / 180 * Math.PI); // rotation should changed
														// accordingly to the
														// direction
				at.scale(2 * (double) GRID_SIZE / RobotImage.getHeight(), 2
						* (double) GRID_SIZE / RobotImage.getWidth());
				at.translate(-RobotImage.getWidth() / 2,
						-RobotImage.getHeight() / 2);

				((Graphics2D) g).drawImage(RobotImage, at, null);
			}
		} else {
			if (this.path != null) {
				if (path.size() > 2) {
					for (int k = 0; k < path.size() - 1; k++) {
						double firstpointx = map[0].length - path.get(k).gridY
								+ 1 - 0.5;
						double firstpointy = path.get(k).gridX - 1 + 0.5;
						double secondpointx = map[0].length
								- path.get(k + 1).gridY + 1 - 0.5;
						double secondpointy = path.get(k + 1).gridX - 1 + 0.5;

						ga.setPaint(new Color(139, 131, 134));
						ga.setStroke(new BasicStroke(5));
						ga.drawLine((int) ((firstpointy + 1) * GRID_SIZE),
								(int) ((firstpointx + 1) * GRID_SIZE),
								(int) ((secondpointy + 1) * GRID_SIZE),
								(int) ((secondpointx + 1) * GRID_SIZE));

					}
				}
			}

			if (this.robotPosition != null) {
				double x = robotPosition.gridX + 0.5;
				double y = map[0].length - robotPosition.gridY + 1.5;
				double d = direction.getDegree();

				AffineTransform at = new AffineTransform();
				at.translate((int) ((x) * GRID_SIZE), (int) ((y) * GRID_SIZE));
				at.rotate(-(d + 90) / 180 * Math.PI); // rotation should changed
														// accordingly to the
														// direction
				at.scale(2 * (double) GRID_SIZE / RobotImage.getHeight(), 2
						* (double) GRID_SIZE / RobotImage.getWidth());
				at.translate(-RobotImage.getWidth() / 2,
						-RobotImage.getHeight() / 2);

				((Graphics2D) g).drawImage(RobotImage, at, null);
			}
		}

	}

}
