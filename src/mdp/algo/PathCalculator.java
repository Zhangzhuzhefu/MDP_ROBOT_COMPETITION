package mdp.algo;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Stack;

import mdp.Config;

public class PathCalculator {
	
	final int infinity = Integer.MAX_VALUE/100;
	int [][] map;
	Point start = ArenaMap.START_POINT;
	Point destination = ArenaMap.END_POINT;
	int ArenaMapPointMAXN = ArenaMap.MAXN+1;
	int ArenaMapPointMAXM = ArenaMap.MAXM+1;
	Stack <Point> aRandomPath; //initialised as an empty stack Mark maze[here] as visited;
	Stack <Point> shortestPath;
	Stack <Point> fastestPath;
	Stack <Point> fastestPathBacktoStart;
	// the set of vertices whose shortest paths from the source node have
	// already been determined
	boolean[][] determinedPointSet;
	// an array of predecessors for each vertex
	Point[][] predecessors;
	// array of best estimates of
	int[][] distance;
	
	public PathCalculator() {
		if (!Config.twoBytwo) {
			destination = ArenaMap.END_POINT3by3;
		}
		aRandomPath = new Stack<Point>();
		shortestPath = new Stack<Point>();
		fastestPath = new Stack<Point>();
		determinedPointSet = new boolean[ArenaMapPointMAXN][ArenaMapPointMAXM];
		predecessors = new Point[ArenaMapPointMAXN][ArenaMapPointMAXM];
		distance = new int[ArenaMapPointMAXN][ArenaMapPointMAXM];
	}
	
	public boolean findFastestPath(Point des){
		
		//if (validatePath(shortestPath)) return true;
		
		//initialize
		fastestPath.clear();
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
			int preDir = -9;
			int nextDir = -9;
			int turningPenalty = 0;
			determinedPointSet[u.gridX][u.gridY] = true;
			for (int i = 0; i < 4; i++) {
				v = u.getNeighbors(i);
				if (v != null) {
					int xDiff, yDiff;
					xDiff = v.gridX - u.gridX;
					yDiff = v.gridY - u.gridY;
					if (xDiff>0) {
						nextDir = Direction.RIGHT;
					} else if (xDiff<0) {
						nextDir = Direction.LEFT;
					} else if (yDiff>0) {
						nextDir = Direction.UP;
					} else if (yDiff<0) {
						nextDir = Direction.DOWN;
					}
					Point t = predecessors[u.gridX][u.gridY];
					if (t != null) {
						xDiff = u.gridX - t.gridX;
						yDiff = u.gridY - t.gridY;
						if (xDiff>0) {
							preDir = Direction.RIGHT;
						} else if (xDiff<0) {
							preDir = Direction.LEFT;
						} else if (yDiff>0) {
							preDir = Direction.UP;
						} else if (yDiff<0) {
							preDir = Direction.DOWN;
						} else {
						}
						if (nextDir != -9 && preDir != -9) {
							if (nextDir != preDir) {
								turningPenalty = 1;
							} else {
								turningPenalty = 0;
							}
							System.out.println(u.gridX+" "+u.gridY+" "+preDir+" " +nextDir +" "+turningPenalty);
						}
					}
					
					// System.out.println("distance["+u.gridX+"]["+u.gridY+"]+u.gridDistanceTo("+v.gridX+" "+v.gridY+")"+distance[u.gridX][u.gridY]);
					if (distance[v.gridX][v.gridY] 
							> (distance[u.gridX][u.gridY] 
							+ u.gridDistanceTo(v) 
							+ turningPenalty)) {
						if (v.robotMovable(map)) {
							queue.remove(v);
							distance[v.gridX][v.gridY] = distance[u.gridX][u.gridY]
									+ u.gridDistanceTo(v)+turningPenalty;
							predecessors[v.gridX][v.gridY] = u;
							queue.add(v);
						}
					} else if (distance[v.gridX][v.gridY] == 
							(distance[u.gridX][u.gridY] + u.gridDistanceTo(v))){
						
					}
				}
			}
		}
		Point a;
		if (des==null) {
			a = destination;
		} else {
			a = des;
		}
		while (a!=null && a != start) {
			if (Config.debugOn)
				System.out.println("predecessors to stack "
						+ predecessors.length);
			fastestPath.push(a);
			a = predecessors[a.gridX][a.gridY];
		}
		fastestPath.push(a);

		if (Config.debugOn) 
			System.out.println("Found shortest path of length: "+fastestPath.size());
		
		if (des==null) {
			if (validatePath(fastestPath))
				return true;
			else {
				fastestPath.clear();
				return false;
			}
		} else {
			return true;
		}
		
	}
	
public boolean findShortestPath(){
		
		//if (validatePath(shortestPath)) return true;
		
		//initialize
		shortestPath.clear();
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
				v = u.getNeighbors(i);
				if (v != null) {
					// System.out.println("distance["+u.gridX+"]["+u.gridY+"]+u.gridDistanceTo("+v.gridX+" "+v.gridY+")"+distance[u.gridX][u.gridY]);
					if (distance[v.gridX][v.gridY] > (distance[u.gridX][u.gridY] + u.gridDistanceTo(v))) {
						if (v.robotMovable(map)) {
							queue.remove(v);
							distance[v.gridX][v.gridY] = distance[u.gridX][u.gridY]
									+ u.gridDistanceTo(v);
							predecessors[v.gridX][v.gridY] = u;
							queue.add(v);
						}
					} else if (distance[v.gridX][v.gridY] == (distance[u.gridX][u.gridY] + u.gridDistanceTo(v))){
						
					}
				}
			}
		}
		
		Point a = destination;
		while (a!=null && a != start) {
			if (Config.debugOn)
				System.out.println("predecessors to stack "
						+ predecessors.length);
			shortestPath.push(a);
			a = predecessors[a.gridX][a.gridY];
		}
		shortestPath.push(a);

		if (Config.debugOn) 
			System.out.println("Found shortest path of length: "+shortestPath.size());
		if (validatePath(shortestPath))
			return true;
		else {
			shortestPath.clear();
			return false;
		}
	}
	
	public boolean findRandomPath() {
		//if (!aRandomPath.empty()) return true;
		aRandomPath.clear();
		boolean [][] visited = new boolean[ArenaMapPointMAXN][ArenaMapPointMAXM];
		for (int i=0;i<ArenaMapPointMAXN;i++)
			for (int j=0;j<ArenaMapPointMAXM;j++)
				visited[i][j] = false;
		
		Point here = start;
		while (!here.sameGridPoint(destination)){//ArenaMap.END_POINT)) {
			System.out.println("random path here at ("+here.gridX+","+here.gridY+")");
			visited[here.gridX][here.gridY] = true;
			int next = -1;
			for (int i = 0; i < 4; i++) {
				System.out.println("i: "+i+" next: "+next);
				if (here.getNeighbors(i) != null) {
					if (!visited[here.getNeighbors(i).gridX][here.getNeighbors(i).gridY]) {
						//System.out.println("not visited");
						if (here.robotMovable(map)) {
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
		
		if (validatePath(aRandomPath))
			return true;
		else {
			aRandomPath.clear();
			return false;
		}
	}
	
	public boolean validatePath(Stack <Point> p){
		return (p!=null && p.size()>=destination.gridDistanceTo(start));
	}
	
	public void reset(){
		map = null;
		aRandomPath.clear();
		shortestPath.clear();
		fastestPath.clear();
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

	public Stack<Point> getFastestPath() {
		return fastestPath;
	}

	public void setFastestPath(Stack<Point> fastestPath) {
		this.fastestPath = fastestPath;
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
