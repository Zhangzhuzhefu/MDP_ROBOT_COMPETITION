package mdp.algo;

import mdp.simulation.SimPerceptron;
import mdp.gui.MapPanel;
import java.util.Stack;

public class Explorer {
    VirtualPerceptron vp;
    MapPanel mp;
    Point destination = ArenaMap.END_POINT;
    Point start = ArenaMap.START_POINT;
    private Stack<Point> path;

	public Explorer() {
        int[] naLoc;
        path = new Stack<Point>();
		
	}
	
	public void reset(){

		
	}

	public void explore(ArenaMap newMap, Point curLoc){
        int[][] map2 = newMap.getArrayMap();
        int turn = 0;

        if ((curLoc.gridX ) < 17){

            SimPerceptron sp = new SimPerceptron(mp);

            int unknownF1, unknownF2,unknownL1,unknownL2, unknownR1, unknownR2;

            Point front1 = new Point(curLoc.gridX-1,curLoc.gridY+1);
            Point front2 = new Point(curLoc.gridX,curLoc.gridY+1);

            Point left1 = new Point(curLoc.gridX-2,curLoc.gridY);
            Point left2 = new Point(curLoc.gridX-2,curLoc.gridY-1);

            Point right1 = new Point(curLoc.gridX+1,curLoc.gridY);
            Point right2 = new Point(curLoc.gridX+1,curLoc.gridY-1);


            unknownF1 = sp.percept(front1);
            unknownF2 = sp.percept(front2);
            unknownR1 = sp.percept(right1);
            unknownR2 = sp.percept(right2);
            unknownL1 = sp.percept(left1);
            unknownL2 = sp.percept(left2);

            //set knowledge base
            if (unknownF1 == 1){
                map2[front1.gridX][front1.gridY] = 1;

            }
            if (unknownF2 == 1){
                map2[front2.gridX][front2.gridY] = 1;

            }
            if (unknownR1 == 1){
                map2[right1.gridX][right1.gridY] = 1;

            }
            if (unknownR2 == 1){
                map2[right2.gridX][right2.gridY] = 1;

            }
            if (unknownL1 == 1){
                map2[left1.gridX][left1.gridY] = 1;

            }
            if (unknownL2 == 1){
                map2[left2.gridX][left2.gridY] = 1;

            }


            System.out.println("Robot X: " + curLoc.gridX + "\tY: " + curLoc.gridY + "cur: " + sp.percept(curLoc));
            System.out.println("Front: "+unknownF1 + "\t" + unknownF2 + "\tRight: " + unknownR1 + "\t" + unknownR2 + "\tLeft: " + unknownL1 + "\t" + unknownL2);


            // dumb heuristic to make decision
            if (unknownF1 == 0 && unknownF2 == 0 && turn == 0){
                System.out.println("Go Front");
                curLoc.gridY = curLoc.gridY + 1;
            }
            else if (unknownR1 == 0 && unknownR2 == 0 && (turn == 0 || turn == 1)){
                System.out.println("Go Right");
                curLoc.gridX = curLoc.gridX + 1;
                turn = 0;
            }
            else if(unknownL1 == 0 && unknownL2 == 0) {
                System.out.println("Go left");
                curLoc.gridX = curLoc.gridX - 1;
                turn = 0;
            }

            else{
                System.out.println("Reverse");
                curLoc.gridY = curLoc.gridY -1;
                if(turn == 1){
                    turn = 2;
                }
                else {
                    turn = 1;
                }

            }





        }
        newMap.setArrayMap(map2);

	}

}
