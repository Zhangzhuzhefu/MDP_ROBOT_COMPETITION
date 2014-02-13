package mdp.simulation;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import mdp.Config;
import mdp.algo.ArenaMap;
import mdp.algo.Point;

public class Map extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	int[][] map;
	Point robotPosition;
	ArrayList<Point> path;
	public static int GRID_SIZE = Config.PANEL_GRID_SIZE;

	private BufferedImage image;

	public Map() {

	}

	public void updateMap(int[][] matrix) {
		map = matrix;
		this.revalidate();
		this.repaint();
	}

	public void updateRobot(Point robotPoint) {
		robotPosition = robotPoint;
		this.revalidate();
		this.repaint();
	}

	public void updatePath(ArrayList<Point> path) {
		if (path == null)
			return;
		this.path = new ArrayList<Point>();
		this.path.add(robotPosition);
		this.path.addAll(path);
		this.revalidate();
		this.repaint();
	}

	@Override 
	public void paint(Graphics g) {
		Graphics2D ga = (Graphics2D) g;
		System.out.println("visualizing...");

		if (this.map != null) {
			int height = GRID_SIZE;
			int width = GRID_SIZE;

			System.out.print("map.length: "+map.length+"    ");
			System.out.println("map.width: "+map[0].length);

			// ConvertMap mapimage = new ConvertMap(map);
			for (int i = 0; i < map.length; i++) {
				for (int j = 0; j < map[0].length; j++) {
					System.out.print(map[i][j]);
					switch (map[i][j]) {
					case ArenaMap.OBS:
						ga.setPaint(Color.black);
						ga.fillRect((j + 1) * GRID_SIZE, (i + 1) * GRID_SIZE,
								width, height);
						ga.setPaint(Color.white);
						ga.drawRect((j + 1) * GRID_SIZE, (i + 1) * GRID_SIZE,
								width, height);
						break;
					case ArenaMap.UNKNOWN:
						ga.setPaint(Color.GRAY);
						ga.fillRect((j + 1) * GRID_SIZE, (i + 1) * GRID_SIZE,
								width, height);
						ga.setPaint(Color.black);
						ga.drawRect((j + 1) * GRID_SIZE, (i + 1) * GRID_SIZE,
								width, height);
						break;
					case ArenaMap.VWall:
						ga.setPaint(Color.cyan);
						ga.fillRect((j + 1) * GRID_SIZE, (i + 1) * GRID_SIZE,
								width, height);
						ga.setPaint(Color.black);
						ga.drawRect((j + 1) * GRID_SIZE, (i + 1) * GRID_SIZE,
								width, height);
						break;
					default:
						ga.setPaint(Color.blue);
						ga.fillRect((j + 1) * GRID_SIZE, (i + 1) * GRID_SIZE,
								width, height);
						ga.setPaint(Color.black);
						ga.drawRect((j + 1) * GRID_SIZE, (i + 1) * GRID_SIZE,
								width, height);
						ga.setPaint(Color.BLUE);
						break;

					}

				}
				System.out.println();
			}
		}

		if (this.path != null) {

			for (int k = 0; k < path.size() - 1; k++) {
				int firstpointx = path.get(k).xToGrid();
				int firstpointy = path.get(k).yToGrid();
				int secondpointx = path.get(k + 1).xToGrid();
				int secondpointy = path.get(k + 1).yToGrid();

				ga.setPaint(Color.red);
				ga.setStroke(new BasicStroke(5));
				ga.drawLine((int) ((firstpointy + 1.5) * GRID_SIZE),
						(int) ((firstpointx + 1.5) * GRID_SIZE),
						(int) ((secondpointy + 1.5) * GRID_SIZE),
						(int) ((secondpointx + 1.5) * GRID_SIZE));

			}

		}

		if (this.robotPosition != null) {
			int x = robotPosition.xToGrid();
			int y = robotPosition.yToGrid();

			try {
				image = ImageIO.read(new File("droidedited.png"));
			} catch (IOException ex) {
				// handle exception... whatever, nobody cares my code anyway
			}
			g.drawImage(image, (int) ((y - 0.5) * GRID_SIZE),
					(int) ((x - 0.5) * GRID_SIZE), 5 * GRID_SIZE,
					6 * GRID_SIZE, null);

		}

	}

}
