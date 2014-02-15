package mdp.algo;

import java.util.Stack;

public class PathFinder {
	
	int [][] map;
	Stack <Point> stack; //initialised as an empty stack Mark maze[here] as visited;
	
	public PathFinder() {

	}
	
	public boolean findRandomPath() {
		// find a path from (1, 1) to (m, m)
		boolean [][] visited;
		
		stack = new Stack<Point>();
		visited = new boolean[ArenaMap.MAXN+1][ArenaMap.MAXM+1];
		for (int i=0;i<ArenaMap.MAXN;i++)
			for (int j=0;j<ArenaMap.MAXM;j++)
				visited[i][j] = false;
		Point here = ArenaMap.START_POINT;
		while (!here.sameGridPoint(ArenaMap.END_POINT)){//ArenaMap.END_POINT)) {
			System.out.println("here at ("+here.gridX+","+here.gridY+")");
			visited[here.gridX][here.gridY] = true;
			int next = -1;
			for (int i = 0; i < 4; i++) {
				System.out.println("i: "+i+" next: "+next);
				if (here.getNeighbors(i) != null) {
					if (!visited[here.getNeighbors(i).gridX][here.getNeighbors(i).gridY]) {
						//System.out.println("not visited");
						if (map[here.gridX-1][here.gridY-1]==ArenaMap.EMP && 
							map[here.gridX-2][here.gridY-1]==ArenaMap.EMP &&
							map[here.gridX-1][here.gridY-2]==ArenaMap.EMP &&
							map[here.gridX-2][here.gridY-2]==ArenaMap.EMP) {
							next = i;
							break;
						}
					} else {
						//System.out.println("visited");
						
					}
				} else {
//					if (Config.debugOn)
//						System.out.println("here("+here.gridX+","+here.gridY+").getNeighbors("+i+") == null");
				}
			}
			if (next != -1) {
				stack.push(here); 
				
				here = here.getNeighbors(next);
			} else {
				if (stack.isEmpty())
					return false;
				here = stack.pop();
			}

		}
		stack.push(here);
		return true;
	}
	
	

	public int[][] getMap() {
		return map;
	}

	public void setMap(int[][] map) {
		this.map = map;
	}

	public Stack<Point> getPath() {
		return stack;
	}

}
