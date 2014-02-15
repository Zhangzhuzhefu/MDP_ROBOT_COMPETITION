package mdp.algo;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Stack;

public class PathCalculator {
	
	final int infinity = Integer.MAX_VALUE/100;
	int [][] map;
	Point start = ArenaMap.START_POINT;
	Point destination = ArenaMap.END_POINT;
	int ArenaMapPointMAXN = ArenaMap.MAXN+1;
	int ArenaMapPointMAXM = ArenaMap.MAXM+1;
	Stack <Point> aRandomPath; //initialised as an empty stack Mark maze[here] as visited;
	Stack <Point> shortestPath;
	// the set of vertices whose shortest paths from the source node have
	// already been determined
	boolean[][] determinedPointSet;
	// an array of predecessors for each vertex
	Point[][] predecessors;
	// array of best estimates of
	int[][] distance;
	
	public PathCalculator() {
		aRandomPath = new Stack<Point>();
		shortestPath = new Stack<Point>();
		determinedPointSet = new boolean[ArenaMapPointMAXN][ArenaMapPointMAXM];
		predecessors = new Point[ArenaMapPointMAXN][ArenaMapPointMAXM];
		distance = new int[ArenaMapPointMAXN][ArenaMapPointMAXM];
	}
	
	public boolean findShortestPath(){
		
		//initialize
		Comparator<Point> comparator = new PointDistanceComparator();
		PriorityQueue<Point> queue = new PriorityQueue<Point>(ArenaMapPointMAXM*ArenaMapPointMAXN,comparator);
		
		for (int i=0;i<ArenaMapPointMAXN;i++) {
			for (int j=0;j<ArenaMapPointMAXM;j++) {
				determinedPointSet[i][j] = false;
				distance [i][j] = infinity;
				predecessors[i][j] = null;
			}
		}
		
		distance[start.gridX][start.gridY] = 0;
		
		//add all points into queue
		for (int i=0;i<ArenaMapPointMAXN;i++) {
			for (int j=0;j<ArenaMapPointMAXM;j++) {
				queue.add(PointManager.getPoint(i, j));
			}
		}
		
		while (!queue.isEmpty()){
			Point u = queue.remove();
			Point v ;
			determinedPointSet[u.gridX][u.gridY] = true;
			for (int i = 0; i < 4; i++) {
				v=u.getNeighbors(i);
				if (v!=null) {
					//System.out.println("distance["+u.gridX+"]["+u.gridY+"]+u.gridDistanceTo("+v.gridX+" "+v.gridY+")"+distance[u.gridX][u.gridY]);
					if (distance[v.gridX][v.gridY] > (distance[u.gridX][u.gridY]+u.gridDistanceTo(v))){
						if (map[v.gridX-1][v.gridY-1]==ArenaMap.EMP && 
							map[v.gridX-2][v.gridY-1]==ArenaMap.EMP &&
							map[v.gridX-1][v.gridY-2]==ArenaMap.EMP &&
							map[v.gridX-2][v.gridY-2]==ArenaMap.EMP){
						queue.remove(v);
						distance[v.gridX][v.gridY] = distance[u.gridX][u.gridY]+u.gridDistanceTo(v);
						predecessors[v.gridX][v.gridY] = u;
						queue.add(v);
						}
					}
				}
			}
		}
		
		Point a = destination;
		while (a!=start){
			shortestPath.push(a);
			a = predecessors[a.gridX][a.gridY];
		}
		shortestPath.push(a);
		if (shortestPath!=null)
			return true;
		else return false;
	}
	
	public boolean findRandomPath() {
		boolean [][] visited = new boolean[ArenaMapPointMAXN][ArenaMapPointMAXM];
		for (int i=0;i<ArenaMapPointMAXN;i++)
			for (int j=0;j<ArenaMapPointMAXM;j++)
				visited[i][j] = false;
		
		Point here = start;
		while (!here.sameGridPoint(destination)){//ArenaMap.END_POINT)) {
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
				aRandomPath.push(here);
				here = here.getNeighbors(next);
			} else {
				if (aRandomPath.isEmpty())
					return false;
				here = aRandomPath.pop();
			}

		}
		aRandomPath.push(here);
		
		//To reverse the randomPath stack
		Stack <Point> temp = new Stack<Point>();
		while (!aRandomPath.isEmpty()){
			temp.push(aRandomPath.pop());
		}
		aRandomPath = temp;
		
		return true;
	}
	
	

	public int[][] getMap() {
		return map;
	}

	public void setMap(int[][] map) {
		this.map = map;
	}

	public Stack<Point> getRandomPath() {
		return aRandomPath;
	}

	public Stack<Point> getShortestPath() {
		return shortestPath;
	}

	public class PointDistanceComparator implements Comparator<Point> {
		@Override
		public int compare(Point x, Point y) {
			if (distance[x.gridX][x.gridY] < distance[y.gridX][y.gridY]) {
				return -1;
			}
			if (distance[x.gridX][x.gridY] > distance[y.gridX][y.gridY]) {
				return 1;
			}
			return 0;
		}
	}
}
