package mdp.simulation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;

import mdp.Config;
import mdp.algo.ArenaMap;
import mdp.algo.Point;
import mdp.algo.VirtualPerceptron;

public class SimPerceptron extends VirtualPerceptron{
	int[][] environment;
	
	public SimPerceptron (){
		super();
		environment = loadMazeEnvironment(Config.mapFileName);
	}
	
	@SuppressWarnings("resource")
	int [][] loadMazeEnvironment(String filename) {
		File file = new File(filename);
		int[][] map = new int[ArenaMap.MAXN][ArenaMap.MAXM];
		for (int[] rows : map)
			Arrays.fill(rows, ArenaMap.OBS);
		
		try {
			Reader reader = new InputStreamReader(new FileInputStream(file));
		
		char tempchar;
			
		for (int i=0; i<ArenaMap.MAXN-2; i++)
			for (int j=0; j<ArenaMap.MAXM-2; j++) {
				tempchar = (char) reader.read();
				while (tempchar < '0')
					tempchar = (char) reader.read();
				map[i+1][j+1] = tempchar - '0';
//				map[i+1][j+2] = tempchar - '0';
//				map[i+2][j+1] = tempchar - '0';
//				map[i+2][j+2] = tempchar - '0';
			}
		
		System.out.println("Print environment:");
		for (int i=0; i<ArenaMap.MAXN; i++) {
			for (int j=0; j<ArenaMap.MAXM; j++)
				System.out.print(map[i][j]);
			System.out.println();
		}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	@Override
	public int perceptFront() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int perceptLeft() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int perceptRight() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public int percept(Point p) {
		return environment[p.gridX][p.gridY];
	}
	
	public int percept(int x, int y) {
		return environment[x][y];
	}

	@Override
	public int[][] getEnvironment() {
		return environment;
	}

	
}
