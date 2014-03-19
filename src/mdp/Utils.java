package mdp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;

import mdp.algo.ArenaMap;

public class Utils {

	public static int[][] loadMazeEnvironment(String filename) {
		File file = new File(filename);
		int[][] map = new int[ArenaMap.MAXN][ArenaMap.MAXM];
		for (int[] rows : map)
			Arrays.fill(rows, ArenaMap.OBS);

		try {
			@SuppressWarnings("resource")
			Reader reader = new InputStreamReader(new FileInputStream(file));

			char tempchar;

			for (int i = 0; i < ArenaMap.MAXN - 2; i++)
				for (int j = 0; j < ArenaMap.MAXM - 2; j++) {
					tempchar = (char) reader.read();
					while (tempchar < '0')
						tempchar = (char) reader.read();
					map[i + 1][j + 1] = tempchar - '0';
					// map[i+1][j+2] = tempchar - '0';
					// map[i+2][j+1] = tempchar - '0';
					// map[i+2][j+2] = tempchar - '0';
				}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return map;
	}
	
	
	public static void printVirtualMap(int [][] map) {
		for (int j = map[0].length - 1; j >= 0; j--) {
			for (int i = 0; i < map.length; i++) {
					System.out.print(map[i][j]);
			}
			System.out.println();
		}
	}
	
	public static void printDistanceMap(int [][] map) {
		System.out.println("Ultils: Print DistanceMap:");
		for (int j = map[0].length - 1; j >= 0; j--) {
			for (int i = 0; i < map.length; i++) {
				if (map[i][j]>1000)
					System.out.print("\t*");
				else
					System.out.print("\t"+map[i][j]);
			}
			System.out.println();
		}
	}
	
	public static void printExplorationBitMap(int [][] map) {
		System.out.println("Ultils: print exploration bit stream:");
		System.out.println("11");
		for (int j = map[0].length - 2; j >= 1; j--) {
			for (int i = 1; i < map.length-1; i++) {
				System.out.print(map[i][j]==2?0:1);
			}
			System.out.println();
		}
		System.out.println("11");
		System.out.println(expBitsToHex(map));
		
		System.out.println("Ultils: print empty/obstacle bit stream:");
		for (int j = map[0].length - 2; j >= 1; j--) {
			for (int i = 1; i < map.length-1; i++) {
				if (map[i][j]!=2)
					System.out.print(map[i][j]==0?0:1);
			}
			System.out.println();
		}
		System.out.println(mapBitsToHex(map));
	}
	
	public static String expBitsToHex (int [][] map) {
		String result = "";
		String strMap = "11";
		for (int j = map[0].length - 2; j >= 1; j--) {
			for (int i = 1; i < map.length-1; i++) {
				strMap += map[i][j]==2?0:1;
			}
		}
		strMap += "11";
		
		String subStr;
		for (int i=0; i<(strMap.length()/4)+1; i++) {
			subStr = strMap.substring(i*4, Math.min((i+1)*4,strMap.length()));
			try{
				result += Long.toHexString(Integer.parseInt(subStr, 2));
			} catch (Exception e){
			}
		}
		return result; 
	}
	
	public static String mapBitsToHex (int [][] map) {
		String result="";
		String strMap = "";
		for (int j = map[0].length - 2; j >= 1; j--) {
			for (int i = 1; i < map.length-1; i++) {
				if (map[i][j]!=2)
				strMap += map[i][j]==0?0:1;
			}
		}
		System.out.print("Padding:");
		while (strMap.length()%4!=0){
			strMap += "1";
			System.out.print("1");
		}
		System.out.println();
		System.out.println(strMap);
		String subStr;
		for (int i=0; i<(strMap.length()/4)+1; i++) {
			subStr = strMap.substring(i*4, Math.min((i+1)*4,strMap.length()));
			try{
				result += Long.toHexString(Integer.parseInt(subStr, 2));
			} catch (Exception e){
			}
		}
		return result; 
	}

}
