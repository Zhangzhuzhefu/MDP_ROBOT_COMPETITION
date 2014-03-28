package mdp.algo;

import java.util.Stack;

import mdp.Config;
import mdp.competition.Communicator;
import mdp.competition.Competition;
import mdp.simulation.Simulator;

public class Explorer {
	public static final String FLOODFILL = "FloodFill";
	public static final String FLLWALL = "FallowWall";
	public static final int North = 1, South = 3, East = 0, West = 2;
	long timeStart;
	long timeEnd;
	
	Point destination = ArenaMap.END_POINT;
	Point start = ArenaMap.START_POINT;

	boolean[][] visited = new boolean[18][23];
	boolean[][] unsafe = new boolean[18][23];

	private Stack<Point> floodFillPath;
	private Stack<Point> followWallPath;
	private Stack<Point> pathEstimate;

	public Explorer() {
		if (!Config.twoBytwo) {
			destination = ArenaMap.END_POINT3by3;
		}
		followWallPath = new Stack<Point>();
		floodFillPath = new Stack<Point>();
		pathEstimate = new Stack<Point>();
		// initialize visited map array

		timeStart = System.currentTimeMillis();
		timeEnd = timeStart + (Config.EXP_TIMER_MIN * 60 + Config.EXP_TIMER_SEC) * 1000; 
		
		for (int i = 0; i <= 17; i++) {
			for (int j = 0; j <= 22; j++) {
				visited[i][j] = false;
			}
		}

		for (int i = 0; i <= 17; i++) {
			for (int j = 0; j <= 22; j++) {
				unsafe[i][j] = false;
			}
		}

	}

	public void reset() {
		floodFillPath.clear();
		followWallPath.clear();
		pathEstimate.clear();
		if (Config.trackingOn)
			System.out.println("Explorer reset!");
	}

	public Stack<Point> exploreFloodFill(Robot robot) {
		FloodFillExplorer rExp = new FloodFillExplorer(robot);
		rExp.start();
		return floodFillPath;
	}

	public class FloodFillExplorer extends Thread {
		Robot robot;

		public FloodFillExplorer(Robot r) {
			this.robot = r;
		}

		@SuppressWarnings("deprecation")
		@Override
		public void run() {
			if (Config.debugOn)
				System.out.println("Explorer: flood fill");
			int ArenaMapPointMAXN = ArenaMap.MAXN + 1;
			int ArenaMapPointMAXM = ArenaMap.MAXM + 1;
			boolean[][] visited = new boolean[ArenaMapPointMAXN][ArenaMapPointMAXM];
			for (int i = 0; i < ArenaMapPointMAXN; i++)
				for (int j = 0; j < ArenaMapPointMAXM; j++)
					visited[i][j] = false;

			Point here = start;
			
			while (robot.getMapKnowledgeBase().lessThanEnoughExploration() 
					&& System.currentTimeMillis() < timeEnd) {
				if (Config.trackingOn) System.out.println("explore: fld fll path here at ("
						+ here.gridX + "," + here.gridY + ")");
				robot.getSensors().perceptEnvironment();
                //System.out.println("knowledge base: " + robot.getMapKnowledgeBase().getArrayMap().toString());
                if(!Config.Simulator){
                    Competition.simulatorMapPanel.updateMap(robot.getMapKnowledgeBase().getArrayMap());
                }else{
				    Simulator.simulatorMapPanel.updateMap(robot
						.getMapKnowledgeBase().getArrayMap());
                }
				visited[here.gridX][here.gridY] = true;
				int next = -1;
				for (int i = 0; i < 4; i++) {
					if (Config.trackingOn) System.out.println("i: " + i + " next: " + next);
					if (here.getNeighbors(i) != null) {
						if (!visited[here.getNeighbors(i).gridX][here
								.getNeighbors(i).gridY]) {
							Point hereNext = here.getNeighbors(i);
							if (hereNext.robotMovable(robot.getMapKnowledgeBase().getArrayMap())) {
								switch (i) {
								case 0:
									robot.turnWest(true);
									break;
								case 1:
									robot.turnEast(true);
									break;
								case 2:
									robot.turnSouth(true);
									break;
								case 3:
									robot.turnNorth(true);
									break;


								}
								robot.updateRobotLoc();
                                robot.getSensors().perceptEnvironment();
                                if (Config.Simulator){
                                	Simulator.simulatorMapPanel.updateMap(robot.getMapKnowledgeBase().getArrayMap());}
                                else {
                                    Competition.simulatorMapPanel.updateMap(robot.getMapKnowledgeBase().getArrayMap());
                                }
								robot.moveForwardByOneStep(true);
								robot.updateRobotLoc();
								floodFillPath.push(here);
								floodFillPath.push(hereNext);
                                if (!Config.Simulator){
                                    Competition.simulatorMapPanel.updatePath(floodFillPath);}
                                else{
								    Simulator.simulatorMapPanel.updatePath(floodFillPath);}
								next = i;
								break;
							}

							//robot.getSensors().perceptEnvironment();
                            if (Config.Simulator){
                            	Simulator.simulatorMapPanel.updateMap(robot.getMapKnowledgeBase().getArrayMap());}
                            else {
                                Competition.simulatorMapPanel.updateMap(robot.getMapKnowledgeBase().getArrayMap());
                            }

						}
					}
				}
				if (next != -1) {
					here = here.getNeighbors(next);
				} else {
					if (floodFillPath.isEmpty()) {
						System.out.println("Expolorer: " + this.getClass()
								+ ": path not found");
						return;
					} else {
						here = floodFillPath.pop();
                        if (!Config.Simulator){
                            Competition.simulatorMapPanel.updatePath(floodFillPath);
                        } else {
                        	Simulator.simulatorMapPanel.updatePath(floodFillPath);}
						int xDiff, yDiff;
						xDiff = here.gridX - robot.getCurrentLocation().gridX;
						yDiff = here.gridY - robot.getCurrentLocation().gridY;
						if (xDiff > 0) {
							robot.turnEast(true);
						} else if (xDiff < 0) {
							robot.turnWest(true);
						} else if (yDiff > 0) {
							robot.turnNorth(true);
						} else if (yDiff < 0) {
							robot.turnSouth(true);
						}
						robot.getSensors().perceptEnvironment();
						if (Config.Simulator) {
							Simulator.simulatorMapPanel.updateMap(robot.getMapKnowledgeBase().getArrayMap());
						} else {
							Competition.simulatorMapPanel.updateMap(robot.getMapKnowledgeBase().getArrayMap());
						}
						robot.jumpToPoint(here.gridX, here.gridY, true);
						robot.updateRobotLoc();
						

					}
				}

			}
			floodFillPath.push(here);
            if (!Config.Simulator){
                Competition.simulatorMapPanel.updatePath(floodFillPath);
            } else {
            	Simulator.simulatorMapPanel.updatePath(floodFillPath);}

			// To reverse the randomPath stack
			Stack<Point> temp = new Stack<Point>();
			while (!floodFillPath.isEmpty()) {
				temp.push(floodFillPath.pop());
			}
			floodFillPath = temp;
			this.stop();
			//robot.setTesting(true);
		}
	}

	public boolean validatePath(Stack<Point> p) {
		return (p != null && p.size() >= destination.gridDistanceTo(start));
	}

	public Stack<Point> getfloodFillPath() {
		return floodFillPath;
	}

	public Stack<Point> exploreFollowWall(Robot robot) {
		FollowWall followWall = new FollowWall(robot);
		followWall.start();

		return followWallPath;
	}

	public class FollowWall extends Thread {
		Robot robot;

		public FollowWall(Robot r) {
			this.robot = r;
		}

		@Override
		public void run() {
			if (Config.debugOn)
				System.out.println("Explorer: Follow Wall");
			
			int ArenaMapPointMAXN = ArenaMap.MAXN + 1;
			int ArenaMapPointMAXM = ArenaMap.MAXM + 1;
			int[][] map = robot.getMapKnowledgeBase().getArrayMap();
			boolean[][] visited = new boolean[ArenaMapPointMAXN][ArenaMapPointMAXM];
			int[][] costIncc = new int[ArenaMapPointMAXN][ArenaMapPointMAXM];
			int[][] costEstm = new int[ArenaMapPointMAXN][ArenaMapPointMAXM];
			for (int i = 0; i < ArenaMapPointMAXN; i++)
				for (int j = 0; j < ArenaMapPointMAXM; j++){
					visited[i][j] = false;
					costIncc[i][j] = Integer.MAX_VALUE;
					costEstm[i][j] = PointManager.getPoint(i, j).gridDistanceTo(destination);
				}

			Point here = start;
			followWallPath.push(start);
			Point hereNext,hereLeft,hereRight;
			//TODO
			boolean clockwise;
			if (robot.getDirection().getDirection()==Direction.UP) 
				clockwise = true;
			else clockwise = false;

            robot.getSensors().perceptEnvironment();
            if (!Config.Simulator){
                Competition.simulatorMapPanel.updateMap(robot.getMapKnowledgeBase().getArrayMap());
            } else {
                Simulator.simulatorMapPanel.updateMap(robot.getMapKnowledgeBase().getArrayMap());
            }

            robot.turnNorth(true);
			boolean endVisit = false;
			while (robot.getMapKnowledgeBase().lessThanEnoughExploration() 
					&& System.currentTimeMillis() < timeEnd
                    && ((!endVisit&&robot.getCurrentLocation()!=start)
                    		||(endVisit&&robot.getCurrentLocation()!=start)
                    		||(!endVisit&&robot.getCurrentLocation()==start))) {
				
				if (robot.getCurrentLocation()== ArenaMap.END_POINT3by3) endVisit = true;
				//if (Config.trackingOn) 
					System.out.println("explore: Fll-Wall path here at (" + here.gridX + "," + here.gridY + ")");
				
				visited[here.gridX][here.gridY] = true;
				robot.getSensors().perceptEnvironment();
                if (!Config.Simulator){
                    Competition.simulatorMapPanel.updateMap(robot.getMapKnowledgeBase().getArrayMap());
                } else {
                	Simulator.simulatorMapPanel.updateMap(robot.getMapKnowledgeBase().getArrayMap());
                }
                switch (robot.getDirection().getDirection()) {
				case Direction.UP:
					hereNext = PointManager.getPoint(here.gridX, here.gridY+1);
					hereLeft = PointManager.getPoint(here.gridX-1, here.gridY);
					hereRight = PointManager.getPoint(here.gridX + 1, here.gridY);
					
					break;
				case Direction.DOWN:
					hereNext = PointManager.getPoint(here.gridX, here.gridY-1);
					hereLeft = PointManager.getPoint(here.gridX+1, here.gridY);
					hereRight = PointManager.getPoint(here.gridX-1, here.gridY);
					
					break;
				case Direction.LEFT:
					hereNext = PointManager.getPoint(here.gridX-1, here.gridY);
					hereLeft = PointManager.getPoint(here.gridX, here.gridY-1);
					hereRight = PointManager.getPoint(here.gridX, here.gridY+1);
					
					break;
				case Direction.RIGHT:
					hereNext = PointManager.getPoint(here.gridX+1, here.gridY);
					hereLeft = PointManager.getPoint(here.gridX, here.gridY+1);
					hereRight = PointManager.getPoint(here.gridX, here.gridY-1);
					
					break;
					
				default: 
					hereNext = null;
					hereLeft = null;
					hereRight = null;
				}

				if (hereNext != null) {
					if (hereLeft.robotMovable(map)){// && hereLeftLeft.robotMovable(map)) {
						robot.turnLeft(true);
						followWallPath.push(here);
						followWallPath.push(hereLeft);
						if (!Config.Simulator) {
							Competition.simulatorMapPanel.updatePath(followWallPath);
						} else {
							Simulator.simulatorMapPanel.updatePath(followWallPath);
						}
						here = hereLeft;
						
						robot.getSensors().perceptEnvironment();
						if (Config.Simulator) {
							Simulator.simulatorMapPanel.updateMap(robot
									.getMapKnowledgeBase().getArrayMap());
						} else {
							Competition.simulatorMapPanel.updateMap(robot
									.getMapKnowledgeBase().getArrayMap());
						}
						
						robot.moveForwardByOneStep(true);
						robot.updateRobotLoc();
						
					} else if (hereNext.robotMovable(map)){// && hereNextNext.robotMovable(map)) {
						followWallPath.push(here);
						followWallPath.push(hereNext);
						if (!Config.Simulator) {
							Competition.simulatorMapPanel.updatePath(followWallPath);
						} else {
							Simulator.simulatorMapPanel.updatePath(followWallPath);
						}
						here = hereNext;
						
						//robot.getSensors().perceptEnvironment();
						if (Config.Simulator) {
							Simulator.simulatorMapPanel.updateMap(robot
									.getMapKnowledgeBase().getArrayMap());
						} else {
							Competition.simulatorMapPanel.updateMap(robot
									.getMapKnowledgeBase().getArrayMap());
						}
						
						robot.moveForwardByOneStep(true);
						robot.updateRobotLoc();
						
					} else if (hereRight.robotMovable(map)){// && hereRightRight.robotMovable(map)){
						robot.turnRight(true);
						followWallPath.push(here);
						followWallPath.push(hereRight);
						if (!Config.Simulator) {
							Competition.simulatorMapPanel.updatePath(followWallPath);
						} else {
							Simulator.simulatorMapPanel.updatePath(followWallPath);
						}
						here = hereRight;
						
						robot.getSensors().perceptEnvironment();
						if (Config.Simulator) {
							Simulator.simulatorMapPanel.updateMap(robot
									.getMapKnowledgeBase().getArrayMap());
						} else {
							Competition.simulatorMapPanel.updateMap(robot
									.getMapKnowledgeBase().getArrayMap());
						}
						
						robot.moveForwardByOneStep(true);
						robot.updateRobotLoc();
					}else {
						if (followWallPath.isEmpty()) {
							System.out.println("Expolorer: " + this.getClass()
									+ ": path not found");
							return;
						} else {
							here = followWallPath.pop();
							if (Config.Simulator) {
								Simulator.simulatorMapPanel
										.updatePath(followWallPath);
							} else {
								Competition.simulatorMapPanel
										.updatePath(followWallPath);
							}
							int xDiff, yDiff;
							xDiff = here.gridX
									- robot.getCurrentLocation().gridX;
							yDiff = here.gridY
									- robot.getCurrentLocation().gridY;
							if (xDiff > 0) {
								robot.turnEast(true);
							} else if (xDiff < 0) {
								robot.turnWest(true);
							} else if (yDiff > 0) {
								robot.turnNorth(true);
							} else if (yDiff < 0) {
								robot.turnSouth(true);
							}
							robot.getSensors().perceptEnvironment();
							if (Config.Simulator) {
								Simulator.simulatorMapPanel.updateMap(robot
										.getMapKnowledgeBase().getArrayMap());
							} else {
								Competition.simulatorMapPanel.updateMap(robot
										.getMapKnowledgeBase().getArrayMap());
							}
							robot.jumpToPoint(here.gridX, here.gridY, true);
						}
					}
					if (Config.Simulator){
                    	Simulator.simulatorMapPanel.updateMap(robot.getMapKnowledgeBase().getArrayMap());}
                    else {
                        Competition.simulatorMapPanel.updateMap(robot.getMapKnowledgeBase().getArrayMap());
                    }

				}
				
			}

			//return to start point
            System.out.println("fastest back path");
			PathCalculator pathCalculator = new PathCalculator();
			Stack <Point> returnPsth = null;
			pathCalculator.setMap(robot.getMapKnowledgeBase().getArrayMap());
			if(pathCalculator.findFastestPath(robot.getCurrentLocation())){
				returnPsth = (Stack<Point>) pathCalculator.getFastestPath();
				if (Config.Simulator) {
					Simulator.simulatorMapPanel.updatePath(returnPsth);
				} else {
					Competition.simulatorMapPanel.updatePath(returnPsth);
				}
			}
			else {
				if (Config.debugOn)
					System.out.println("Robot doesn't know how to get back");
			}
			//To reverse the returnPath stack

			Stack <Point> temp = new Stack<Point>();
			if (returnPsth!=null) {
				while (!returnPsth.isEmpty()) {
					temp.push(returnPsth.pop());
				}
				returnPsth = robot.distanceDetermination(temp);
                //returnPsth = temp;
				robot.setNewRoute(returnPsth);

                robot.setRace(true);
                if (!Config.Simulator){
				    Communicator.startRace();
                    Competition.secondRun();}
                else Simulator.robotManager.robotRun();
			}

		}
		
	}
	
	

	/*
	 * public Stack<Point> exploreFloodFill(Robot robot) { // if(Config.debugOn)
	 * // System.out.println("Explorer: flood fill");
	 * 
	 * ArenaMap newMap = robot.getMapKnowledgeBase();
	 * 
	 * if (robot.getCurrentLocation().gridX <= 16 &&
	 * robot.getCurrentLocation().gridY <= 21 &&
	 * robot.getCurrentLocation().gridX > 0 && robot.getCurrentLocation().gridY
	 * > 0) { Point curLoc = robot.getCurrentLocation(); // int[][] map2 =
	 * newMap.getArrayMap(); visited[curLoc.gridX][curLoc.gridY] = true;
	 * 
	 * robot.getSensors().perceptEnvironment();
	 * 
	 * int[][] map = newMap.getArrayMap(); //
	 * System.out.println("percept front: " + //
	 * robot.getSensors().perceptFront()); // initialize
	 * 
	 * Point left1 = PointManager.getPoint(curLoc.gridX - 1, curLoc.gridY);
	 * Point left2 = PointManager.getPoint(curLoc.gridX - 2, curLoc.gridY);
	 * 
	 * Point right1 = PointManager.getPoint(curLoc.gridX - 1, curLoc.gridY - 3);
	 * Point right2 = PointManager.getPoint(curLoc.gridX - 2, curLoc.gridY - 3);
	 * 
	 * Point front1 = PointManager .getPoint(curLoc.gridX, curLoc.gridY - 1);
	 * Point front2 = PointManager .getPoint(curLoc.gridX, curLoc.gridY - 2);
	 * 
	 * Point front = PointManager.getPoint(curLoc.gridX, curLoc.gridY + 1);
	 * Point right = PointManager.getPoint(curLoc.gridX + 1, curLoc.gridY);
	 * Point left = PointManager.getPoint(curLoc.gridX - 1, curLoc.gridY);
	 * 
	 * SimPerceptron sp = robot.getSensors();
	 * robot.getSensors().setEnvironment(ArenaMap.actualMap.clone());
	 * 
	 * int unknownF1, unknownF2, unknownL1, unknownL2, unknownR1, unknownR2;
	 * 
	 * double d = robot.getDirection().getDegree(); int dir = (int) d / 90; dir
	 * = dir % 4; // direction of robot
	 * 
	 * switch (dir) { case North: System.out.println("direction: North"); left1
	 * = PointManager.getPoint(curLoc.gridX - 3, curLoc.gridY - 1); left2 =
	 * PointManager.getPoint(curLoc.gridX - 3, curLoc.gridY - 2);
	 * 
	 * right1 = PointManager.getPoint(curLoc.gridX, curLoc.gridY - 1); right2 =
	 * PointManager.getPoint(curLoc.gridX, curLoc.gridY - 2);
	 * 
	 * front1 = PointManager.getPoint(curLoc.gridX - 1, curLoc.gridY); front2 =
	 * PointManager.getPoint(curLoc.gridX - 2, curLoc.gridY);
	 * 
	 * // visited front = PointManager.getPoint(curLoc.gridX, curLoc.gridY + 1);
	 * right = PointManager.getPoint(curLoc.gridX + 1, curLoc.gridY); left =
	 * PointManager.getPoint(curLoc.gridX - 1, curLoc.gridY);
	 * 
	 * break;
	 * 
	 * case South: System.out.println("direction: South"); front1 =
	 * PointManager.getPoint(curLoc.gridX - 1, curLoc.gridY - 3); front2 =
	 * PointManager.getPoint(curLoc.gridX - 2, curLoc.gridY - 3);
	 * 
	 * left1 = PointManager.getPoint(curLoc.gridX, curLoc.gridY - 1); left2 =
	 * PointManager.getPoint(curLoc.gridX, curLoc.gridY - 2);
	 * 
	 * right1 = PointManager.getPoint(curLoc.gridX - 3, curLoc.gridY - 1);
	 * right2 = PointManager.getPoint(curLoc.gridX - 3, curLoc.gridY - 2);
	 * 
	 * // visited front = PointManager.getPoint(curLoc.gridX, curLoc.gridY - 1);
	 * right = PointManager.getPoint(curLoc.gridX - 1, curLoc.gridY); left =
	 * PointManager.getPoint(curLoc.gridX + 1, curLoc.gridY);
	 * 
	 * break;
	 * 
	 * case East: System.out.println("direction: East"); front1 =
	 * PointManager.getPoint(curLoc.gridX, curLoc.gridY - 1); front2 =
	 * PointManager.getPoint(curLoc.gridX, curLoc.gridY - 2);
	 * 
	 * left1 = PointManager.getPoint(curLoc.gridX - 1, curLoc.gridY); left2 =
	 * PointManager.getPoint(curLoc.gridX - 2, curLoc.gridY);
	 * 
	 * right1 = PointManager.getPoint(curLoc.gridX - 1, curLoc.gridY - 3);
	 * right2 = PointManager.getPoint(curLoc.gridX - 2, curLoc.gridY - 3);
	 * 
	 * // visited front = PointManager.getPoint(curLoc.gridX + 1, curLoc.gridY);
	 * right = PointManager.getPoint(curLoc.gridX, curLoc.gridY - 1); left =
	 * PointManager.getPoint(curLoc.gridX, curLoc.gridY + 1);
	 * 
	 * break;
	 * 
	 * case West: System.out.println("direction: West"); front1 =
	 * PointManager.getPoint(curLoc.gridX - 3, curLoc.gridY - 1); front2 =
	 * PointManager.getPoint(curLoc.gridX - 3, curLoc.gridY - 2);
	 * 
	 * left1 = PointManager.getPoint(curLoc.gridX - 1, curLoc.gridY - 3); left2
	 * = PointManager.getPoint(curLoc.gridX - 2, curLoc.gridY - 3);
	 * 
	 * right1 = PointManager.getPoint(curLoc.gridX - 1, curLoc.gridY); right2 =
	 * PointManager.getPoint(curLoc.gridX - 2, curLoc.gridY);
	 * 
	 * // visited front = PointManager.getPoint(curLoc.gridX - 1, curLoc.gridY);
	 * right = PointManager.getPoint(curLoc.gridX, curLoc.gridY + 1); left =
	 * PointManager.getPoint(curLoc.gridX, curLoc.gridY - 1);
	 * 
	 * break; }
	 * 
	 * int F1 = map[front1.gridX][front1.gridY]; int F2 =
	 * map[front2.gridX][front2.gridY];
	 * 
	 * int L1 = map[left1.gridX][left1.gridY]; int L2 =
	 * map[left2.gridX][left2.gridY];
	 * 
	 * int R1 = map[right1.gridX][right1.gridY]; int R2 =
	 * map[right2.gridX][right2.gridY];
	 * 
	 * System.out.println("Robot X: " + curLoc.gridX + "\tY: " + curLoc.gridY +
	 * "cur: " + sp.percept(curLoc)); System.out.println("Front: " + F1 + "\t" +
	 * F2 + "\tRight: " + R1 + "\t" + R2 + "\tLeft: " + L1 + "\t" + L2);
	 * 
	 * // floodfill heuristic boolean f, l, r; f = F1 == 0 && F2 == 0; // f =
	 * robot.getSensors().perceptFront() == 0; l = L1 == 0 && L2 == 0; // l =
	 * robot.getSensors().perceptFront() == 0; r = R1 == 0 && R2 == 0; // r =
	 * robot.getSensors().perceptFront() == 0;
	 * 
	 * boolean vf = visited[front.gridX][front.gridY]; boolean vr =
	 * visited[right.gridX][right.gridY]; boolean vl =
	 * visited[left.gridX][left.gridY];
	 * 
	 * System.out.println("front visited X: " + front.gridX + "\tY: " +
	 * front.gridY); System.out.println("right visited X: " + right.gridX +
	 * "\tY: " + right.gridY); System.out.println("left visited X: " +
	 * left.gridX + "\tY: " + left.gridY);
	 * 
	 * System.out.println("Front visited: " + vf + "\t" + "\tRight visited: " +
	 * vr + "\tLeft visited: " + vl);
	 * 
	 * Point result = null; // free of obstacles if (f && l && r) { if (vf && vr
	 * && vl) {
	 * 
	 * // robot.turnLeft(); // robot.turnLeft(); // result =
	 * compareV(front1,left1,right1);
	 * 
	 * // moveForward(curLoc,robot.getDirection()); } else if (vf && vr) {
	 * robot.turnLeft(); turnLeft(robot.getDirection()); } else if (vr && vl) {
	 * 
	 * } else if (vf && vl) { robot.turnRight();
	 * turnRight(robot.getDirection()); } else if (vf) { result = compare(left1,
	 * right1); } else if (vl) { // result = compare(front1,right1);
	 * 
	 * } else if (vr) { // result = compare(front1,left1); } else { // result =
	 * compare(front1,left1,right1); }
	 * 
	 * if (result == right1) { robot.turnRight();
	 * turnRight(robot.getDirection());
	 * 
	 * } if (result == left1) { robot.turnLeft();
	 * turnLeft(robot.getDirection());
	 * 
	 * } moveForward(curLoc, robot.getDirection()); } else if (f & l) { // front
	 * and left is empty if (vf && vl) { // robot.turnLeft(); //
	 * robot.turnLeft(); // result = compareV(front1,left1); } else if (vf) {
	 * robot.turnLeft();
	 * 
	 * } else if (vl) {
	 * 
	 * } else {
	 * 
	 * // result = compare(front1,left1); }
	 * 
	 * if (result == left1) { robot.turnLeft(); turnLeft(robot.getDirection());
	 * } moveForward(curLoc, robot.getDirection());
	 * 
	 * } else if (l & r) { // left and right are empty if (vr && vl) {
	 * robot.turnLeft(); robot.turnLeft(); // result = compareV(left1,right1); }
	 * else if (vr) { robot.turnLeft(); turnLeft(robot.getDirection()); } else
	 * if (vl) { robot.turnRight(); turnRight(robot.getDirection()); } else {
	 * result = compare(left1, right1); }
	 * 
	 * if (result == left1) { robot.turnLeft(); turnLeft(robot.getDirection());
	 * } if (result == right1) { robot.turnRight();
	 * turnRight(robot.getDirection()); } moveForward(curLoc,
	 * robot.getDirection());
	 * 
	 * result = compare(left1, right1); } else if (f & r) { // front and right
	 * are empty if (vf && vr) { // robot.turnLeft(); // robot.turnLeft(); //
	 * result = compareV(front1,left1); } else if (vf) { robot.turnRight();
	 * 
	 * } else if (vr) {
	 * 
	 * } else { // result = compare(front1,left1); }
	 * 
	 * if (result == left1) { robot.turnRight();
	 * turnRight(robot.getDirection()); } moveForward(curLoc,
	 * robot.getDirection());
	 * 
	 * } else if (f) { moveForward(curLoc, robot.getDirection()); } else if (r)
	 * { robot.turnRight(); turnRight(robot.getDirection()); moveForward(curLoc,
	 * robot.getDirection());
	 * 
	 * } else if (l) { robot.turnLeft(); turnLeft(robot.getDirection());
	 * moveForward(curLoc, robot.getDirection());
	 * 
	 * } else { // backtrack System.out.println("Backtrack!"); // Point unsafe =
	 * path.pop(); unsafe[curLoc.gridX][curLoc.gridY] = true; robot.turnLeft();
	 * robot.turnLeft();
	 * 
	 * // robot.setMapKnowledgeBase(newMap);
	 * Utils.printVirtualMap(robot.getMapKnowledgeBase().getArrayMap()); //
	 * robot.getDirection().rotate(180); moveForward(curLoc,
	 * robot.getDirection()); robot.setCurrentLocation(path.peek());
	 * 
	 * }
	 * 
	 * // if(path.peek() != curLoc){ path.push(curLoc); // }
	 * 
	 * } System.out.println("\n"); return path; }
	 * 
	 * 
	 * 
	 * 
	 * private Point compare(Point p1, Point p2) { if
	 * (Math.sqrt(Math.pow(p1.gridX, 2) + Math.pow(p1.gridY, 2)) >= Math
	 * .sqrt((p2.gridX) ^ 2 + (p2.gridY) ^ 2)) {
	 * 
	 * return p1; } else { return p2; } }
	 * 
	 * private Point compareV(Point p1, Point p2) { if
	 * (Math.sqrt(Math.pow(p1.gridX, 2) + Math.pow(p1.gridY, 2)) <= Math
	 * .sqrt((p2.gridX) ^ 2 + (p2.gridY) ^ 2)) {
	 * 
	 * return p1; } else { return p2; } }
	 * 
	 * private Point compare(Point p1, Point p2, Point p3) {
	 * 
	 * if (Math.sqrt(Math.pow(p1.gridX, 2) + Math.pow(p1.gridY, 2)) >= Math
	 * .sqrt((p2.gridX) ^ 2 + (p2.gridY) ^ 2)) { if
	 * (Math.sqrt(Math.pow(p1.gridX, 2) + Math.pow(p1.gridY, 2)) >= Math
	 * .sqrt(Math.pow(p3.gridX, 2) + Math.pow(p3.gridY, 2))) { return p1; } else
	 * { return p3; } } else { if (Math.sqrt(Math.pow(p2.gridX, 2) +
	 * Math.pow(p2.gridY, 2)) >= Math .sqrt(Math.pow(p3.gridX, 2) +
	 * Math.pow(p3.gridY, 2))) { return p2; } else { return p3; } } }
	 * 
	 * private Point compareV(Point p1, Point p2, Point p3) {
	 * 
	 * if (Math.sqrt(Math.pow(p1.gridX, 2) + Math.pow(p1.gridY, 2)) <= Math
	 * .sqrt((p2.gridX) ^ 2 + (p2.gridY) ^ 2)) { if
	 * (Math.sqrt(Math.pow(p1.gridX, 2) + Math.pow(p1.gridY, 2)) <= Math
	 * .sqrt(Math.pow(p3.gridX, 2) + Math.pow(p3.gridY, 2))) { return p1; } else
	 * { return p3; } } else { if (Math.sqrt(Math.pow(p2.gridX, 2) +
	 * Math.pow(p2.gridY, 2)) <= Math .sqrt(Math.pow(p3.gridX, 2) +
	 * Math.pow(p3.gridY, 2))) { return p2; } else { return p3; } } }
	 * 
	 * private void move(Point curLoc, Point p) { System.out.print("move!");
	 * curLoc = p; }
	 * 
	 * private void moveForward(Point curLoc, Direction direction) { double d =
	 * direction.getDegree(); int dir = (int) d / 90;
	 * System.out.println("direction: " + d + "\tdir: " + dir); dir = dir % 4;
	 * System.out.println("move Forward!"); switch (dir) { case North:
	 * System.out.println("direction: N!"); curLoc.gridY = curLoc.gridY + 1;
	 * break;
	 * 
	 * case West: System.out.println("direction: W!"); curLoc.gridX =
	 * curLoc.gridX - 1; break;
	 * 
	 * case South: System.out.println("direction: S!"); curLoc.gridY =
	 * curLoc.gridY - 1; break;
	 * 
	 * case East: System.out.println("direction: E!"); curLoc.gridX =
	 * curLoc.gridX + 1; break; } }
	 * 
	 * private void turnRight(Direction direction) {
	 * System.out.println("turn Right!"); direction.rotate(90); }
	 * 
	 * private void turnLeft(Direction direction) {
	 * System.out.println("turn Left!"); direction.rotate(-90); }
	 */
}
