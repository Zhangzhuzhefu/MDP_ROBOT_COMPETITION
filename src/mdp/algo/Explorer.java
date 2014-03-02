package mdp.algo;

import java.util.Stack;

import mdp.Config;
import mdp.gui.MapPanel;
import mdp.simulation.SimPerceptron;
import mdp.simulation.Simulator;

public class Explorer {
    public static final String FLOODFILL = "FloodFill";
    public static final String ASTAR = "AStar";
    
    Point destination = ArenaMap.END_POINT;
    Point start = ArenaMap.START_POINT;
    
    private Stack<Point> path;
    private MapPanel mapPanel;


	public Explorer() {
        path = new Stack<Point>();
		
	}
	
	public void reset(){
        if(Config.trackingOn) System.out.println("Explorer reset!");
	}
	
	public void exploreAStar(Robot robot){
        if(Config.debugOn)
        	System.out.println("Explorer: A*");
        
        Point curLoc = robot.getCurrentLocation();
        ArenaMap mapKnowledgeBase = robot.mapKnowledgeBase;
        MapPanel mapPanel = Simulator.simulatorMapPanel;
        
        while ((curLoc.gridX ) <= 15 || (curLoc.gridY <= 20)){
        	
        	
        	
        }
	}

	public void exploreFloodFill(ArenaMap newMap, Point curLoc){
        if(Config.debugOn)
        	System.out.println("Explorer: flood fill");
        int[][] map2 = newMap.getArrayMap();
        int turn = 0;

        if ((curLoc.gridX ) <= 15 && (curLoc.gridY <= 20)){

            SimPerceptron sp = new SimPerceptron(null);
            sp.setEnvironment(ArenaMap.actualMap.clone());

            int unknownF1, unknownF2,unknownL1,unknownL2, unknownR1, unknownR2;

            Point front1 = PointManager.getPoint(curLoc.gridX-1,curLoc.gridY);
            Point front2 = PointManager.getPoint(curLoc.gridX-2,curLoc.gridY);

            Point left1 = PointManager.getPoint(curLoc.gridX-3,curLoc.gridY-1);
            Point left2 = PointManager.getPoint(curLoc.gridX-3,curLoc.gridY-2);

            Point right1 = PointManager.getPoint(curLoc.gridX,curLoc.gridY-1);
            Point right2 = PointManager.getPoint(curLoc.gridX,curLoc.gridY-2);


            unknownF1 = sp.percept(front1);
            unknownF2 = sp.percept(front2);
            unknownR1 = sp.percept(right1);
            unknownR2 = sp.percept(right2);
            unknownL1 = sp.percept(left1);
            unknownL2 = sp.percept(left2);


            //set knowledge base
            if (unknownF1 != 4){
                if (unknownF1 == 1){
                    map2[front1.gridX][front1.gridY] = 1;
                }else {
                    map2[front1.gridX][front1.gridY] = 0;
                }
            }

            if(unknownF2 != 4){
                if (unknownF2 == 1){
                    map2[front2.gridX][front2.gridY] = 1;
                }else map2[front2.gridX][front2.gridY] = 0;
            }

            if(unknownR1 != 4){
                if (unknownR1 == 1){
                    map2[right1.gridX][right1.gridY] = 1;
                }else  map2[right1.gridX][right1.gridY] = 0;
            }

            if(unknownR2 != 4){
                if (unknownR2 == 1){
                    map2[right2.gridX][right2.gridY] = 1;
                }else map2[right2.gridX][right2.gridY] = 0;
            }

            if(unknownL1 != 4){
                if (unknownL1 == 1){
                    map2[left1.gridX][left1.gridY] = 1;
                }else map2[left1.gridX][left1.gridY] = 0;
            }

            if(unknownL2 != 4){
                if (unknownL2 == 1){
                    map2[left2.gridX][left2.gridY] = 1;
                }else  map2[left2.gridX][left2.gridY] = 0;
            }












            System.out.println("Robot X: " + curLoc.gridX + "\tY: " + curLoc.gridY + "cur: " + sp.percept(curLoc));
            System.out.println("Front: "+unknownF1 + "\t" + unknownF2 + "\tRight: " + unknownR1 + "\t" + unknownR2 + "\tLeft: " + unknownL1 + "\t" + unknownL2);




            // dumb heuristic to make decision
            if( (unknownF1 == 1 || unknownF2 == 1) && (unknownR1 == 1 || unknownR2 == 1) && (unknownL1 == 1 || unknownL2 == 1)
                    ||  map2[curLoc.gridX][curLoc.gridY+1] == 4
                    ||  map2[curLoc.gridX+1][curLoc.gridY] == 4
                    ||  map2[curLoc.gridX-1][curLoc.gridY] == 4)
            {
                System.out.println("Reverse");
                Point ReversePoint = path.peek();
                map2[ReversePoint.gridX][ReversePoint.gridY] = 4;
                path.pop();
                Point newPoint = path.peek();
                curLoc = newPoint;
            }

            else{

                if (unknownF1 == 0 && unknownF2 == 0 ){
                    System.out.println("Go Front");
                    curLoc.gridY = curLoc.gridY + 1;

                }

                else if (unknownR1 == 0 && unknownR2 == 0){
                    System.out.println("Go Right");
                    curLoc.gridX = curLoc.gridX + 1;

                }

                else if(unknownL1 == 0 && unknownL2 == 0) {
                    System.out.println("Go left");
                    curLoc.gridX = curLoc.gridX - 1;

                }

                path.push(new Point(curLoc.gridX,curLoc.gridX));

            }





        }
        newMap.setArrayMap(map2);



	}

}
