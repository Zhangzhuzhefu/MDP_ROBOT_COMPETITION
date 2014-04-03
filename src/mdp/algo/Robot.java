package mdp.algo;

import java.io.IOException;
import java.util.Stack;

import mdp.Config;
import mdp.competition.Communicator;
import mdp.competition.Competition;
import mdp.simulation.SimCommunicator;
import mdp.simulation.SimPerceptron;
import mdp.simulation.Simulator;



public class Robot {

	ArenaMap mapKnowledgeBase;
	SimPerceptron sensors;
	Explorer explorer;
	PathCalculator pathCalculator;	
	
	boolean isExploring, isMoving, isOnTheWayReturning, isTurning, race, autoUpdate;
	private Point currentLocation;
	private Direction direction;
	private Stack<Point> route;
	private Stack<Point> newRoute;

	
	
	public Robot() throws IOException{
        autoUpdate = true;
		race = false;
		isExploring = true;
		isMoving = false;
        isTurning = false;
		isOnTheWayReturning = false;
		currentLocation = ArenaMap.START_POINT;
		mapKnowledgeBase = new ArenaMap();
		explorer = new Explorer();
		pathCalculator = new PathCalculator();
		route = new Stack<Point>();
		direction = new Direction(Config.RobotStartingDirection);
		sensors = new SimPerceptron(this);
	}
	
	public void reset(){
        autoUpdate = false;
		race = false;
		isExploring = true;
		isMoving = false;
        isTurning = false;
		isOnTheWayReturning = false;
		currentLocation = ArenaMap.START_POINT;
		mapKnowledgeBase.reset();
		explorer.reset();
		pathCalculator.reset();
		if (route!=null)
			route.clear();
	}
	
	public void startMoving(){
        if (!race){
		    setCurrentLocation(ArenaMap.START_POINT);}
		isMoving = true;
		isOnTheWayReturning = false;
	}
	
	public Stack<Point> generateFastestPath(){
        if (!Config.Simulator){
            Communicator.startRace();}
        setRace(true);
        //if (!Config.Simulator)
        pathCalculator.setMap(getMapKnowledgeBase().getArrayMap());
		if(pathCalculator.findFastestPath(null)){
			route = (Stack<Point>) pathCalculator.getFastestPath();
            newRoute = distanceDetermination(route);
            //newRoute = (Stack)route.clone();
            return route;
		}
		else 
			return null;
	}
	
	public Stack<Point> generateShortestPath(){
        if (!Config.Simulator){
            Communicator.startRace();}
        setRace(true);
      //if (!Config.Simulator) 
		pathCalculator.setMap(getMapKnowledgeBase().getArrayMap());
		if(pathCalculator.findShortestPath()){
			route = (Stack<Point>) pathCalculator.getShortestPath();
            newRoute = distanceDetermination(route);
            //newRoute = (Stack)route.clone();
			return route;
		}
		else {Competition.explore_FollowWall();
			return null;}
	}
	
	public Stack<Point> generateRandomPath(){
        if (!Config.Simulator){
            Communicator.startRace();}
        setRace(true);
        //if (!Config.Simulator) 
		pathCalculator.setMap(this.getMapKnowledgeBase().getArrayMap());
		if(pathCalculator.findRandomPath()){
			route = (Stack<Point>) pathCalculator.getRandomPath();
            newRoute = distanceDetermination(route);
            //newRoute = (Stack)route.clone();
			return route;
		}
		else 
			return null;
	}

    public Stack <Point> explore(String s){
    	if (!Config.Simulator){
            Communicator.startExplore();
			Communicator.sendMapToAndroid("0");
        }
		switch (s) {
		case Explorer.FLOODFILL:
			if (Config.debugOn)
				System.out.println("Robot: Exploring Floodfill");
			route = explorer.exploreFloodFill(this);
			if (route!=null) {
				//testing = true;
				return route;
			} else {
				System.out.println("Robot: Floodfill null route");
				return null;
			}
		case Explorer.FLLWALL: 
			if (Config.debugOn)
				System.out.println("Robot: Exploring Follow Wall");
			route = explorer.exploreFollowWall(this);
			if (route!=null) {
				//testing = true;
				return route;
			} else {
				System.out.println("fallow-wall null route");
				return null;
			}
		}
		return null;

    }
	
	public void move()throws IOException{
        if (!race){

			if (route != null && !route.empty()) {
				if (Config.debugOn)
					System.out.println("Robot route exists");
				if (route.peek().sameGridPoint(ArenaMap.END_POINT)) {
					isOnTheWayReturning = true;
					if (Config.debugOn)
						System.out.println("isOnTheWayReturning = true");
				}
				Point nextLoc = route.peek();
				;
				int xDiff, yDiff;
				xDiff = nextLoc.gridX - currentLocation.gridX;
				yDiff = nextLoc.gridY - currentLocation.gridY;
				if (xDiff > 0) {
					this.turnEast(true);
				} else if (xDiff < 0) {
					this.turnWest(true);
				} else if (yDiff > 0) {
					this.turnNorth(true);
				} else if (yDiff < 0) {
					this.turnSouth(true);
				}
				currentLocation = route.pop();
				updateRobotLoc();

			} else {
				if (Config.debugOn)
					System.out.println("Robot route is empty..");
				isMoving = false;
			}
        }

        else { // if competition or testing

            if (newRoute != null && !newRoute.empty() ) {
            	if (route.peek().sameGridPoint(ArenaMap.END_POINT)) {
					isOnTheWayReturning = true;
					if (Config.debugOn)
						System.out.println("isOnTheWayReturning = true");
				}
                Point nextLoc = newRoute.peek();

                int xDiff, yDiff;
                if (Config.debugOn){
                    System.out.println("current location gridX: "+ currentLocation.gridX + "\tgridY: " + currentLocation.gridY);
                    System.out.println("next location gridX: "+ nextLoc.gridX + "\tgridY: " + nextLoc.gridY);
                }

                xDiff = nextLoc.gridX - currentLocation.gridX;
                yDiff = nextLoc.gridY - currentLocation.gridY;

                if (xDiff > 0 ) {
                    this.turnEast(true);
                    if (Config.debugOn){
                        System.out.println("I tell arduino to move "+Math.abs(xDiff) + " grids");}

                    if (!Config.Simulator) Communicator.moveInt(Math.abs(xDiff));
                } else if (xDiff < 0 ){
                    this.turnWest(true);
                    if (Config.debugOn){
                        System.out.println("I tell arduino to move "+Math.abs(xDiff) + " grids");}

                    if (!Config.Simulator) Communicator.moveInt(Math.abs(xDiff));
                } else if (yDiff > 0 ){
                    this.turnNorth(true);
                    if (Config.debugOn){
                        System.out.println("I tell arduino to move "+Math.abs(yDiff) + " grids");}
                    if (!Config.Simulator) Communicator.moveInt(Math.abs(yDiff));
                } else if (yDiff < 0 ){
                    this.turnSouth(true);
                    if (Config.debugOn){
                        System.out.println("I tell arduino to move "+Math.abs(yDiff) + " grids");}
                    if (!Config.Simulator) Communicator.moveInt(Math.abs(yDiff));
                }


                currentLocation = newRoute.pop();
                updateRobotLoc();
                if (!Config.Simulator) Communicator.getMovedDistance();

                if (Config.debugOn){
                    System.out.println("current location gridX: "+ currentLocation.gridX + "\tgridY: " + currentLocation.gridY);
                }
            } else {
				if (Config.debugOn)
					System.out.println("Robot route is empty..");
				isMoving = false;
			}
        }

    }

    public Stack<Point> distanceDetermination(Stack<Point> oriRoute){
        Stack<Point> tempRoute = (Stack)oriRoute.clone();
        Stack<Point> newRoute = new Stack<>();
        Stack<Point> revRoute = new Stack<>();
        Point curLoc = PointManager.getPoint(currentLocation.gridX,currentLocation.gridY);

        // if false, moving toward grid x
        // if true, moving toward grid y
        
        boolean toD=false;
        if(tempRoute.peek().gridX == curLoc.gridX){
        	toD = true;
        } else {
        	toD = false;
        }
        Point endPoint;
        Point startPoint;
        if (Config.twoBytwo){
        	startPoint = ArenaMap.START_POINT;
            endPoint = ArenaMap.END_POINT;
        } else {
        	startPoint = ArenaMap.START_POINT;
            endPoint = ArenaMap.END_POINT3by3;
        }
        while (tempRoute != null && !tempRoute.empty()){

            Point peeka = tempRoute.peek();
            if (peeka != endPoint && peeka != startPoint ){
                if (toD){

                    if (peeka.gridY == curLoc.gridY && peeka.gridX != curLoc.gridX){
                        toD = false;
                        newRoute.push(curLoc);
                        if (Config.debugOn){
                            System.out.println("distanceDetermination\tcurLoc gridX: "+curLoc.gridX + "curLoc gridY: "+curLoc.gridY);}
                    }

                } else {
                    if (peeka.gridX == curLoc.gridX && peeka.gridY != curLoc.gridY){
                        toD = true;
                        newRoute.push(curLoc);
                        if (Config.debugOn){
                            System.out.println("distanceDetermination\tcurLoc gridX: "+curLoc.gridX + "curLoc gridY: "+curLoc.gridY);}
                    }
                }
                curLoc =tempRoute.pop();

            } else { // if reach end point, pop the end point from route and push to the newRoute
                curLoc = tempRoute.pop();
                if (Config.debugOn){
                    System.out.println("distanceDetermination\tcurLoc gridX: " + curLoc.gridX + "curLoc gridY: " + curLoc.gridY);
                }
                newRoute.push(curLoc);
            }
        }

        Point tempPoint;
        // reverse the stack
        while(newRoute != null && !newRoute.empty()){
            tempPoint = newRoute.pop();
            if (tempPoint != currentLocation) {
                revRoute.push(tempPoint);}
        }

        return revRoute;
    }


    public void updateRobotLoc(){
		if (sensors.getCommunicator() instanceof SimCommunicator){
//			SimCommunicator s ;
//			s = (SimCommunicator) sensors.communicator;
			Simulator.simulatorMapPanel.updateRobot(currentLocation,direction);
		} else if (sensors.getCommunicator() instanceof Communicator){


            Competition.simulatorMapPanel.updateRobot(currentLocation,direction);
		}
	}

	public void moveForwardByOneStep(boolean delay){

           if (!Config.Simulator){
                if (autoUpdate){
                    Communicator.sendMapToAndroid(getRobotState());}
                Communicator.moveFor();
            }
            switch (direction.getDirection()){
                case Direction.DOWN:
                    if (currentLocation.gridY==ArenaMap.START_POINT.gridY) return;
                    currentLocation=PointManager.getPoint(currentLocation.gridX, currentLocation.gridY-1);
                    break;
                case Direction.UP:
                    if (currentLocation.gridY==ArenaMap.END_POINT.gridY) return;
                    currentLocation=PointManager.getPoint(currentLocation.gridX, currentLocation.gridY+1);
                    break;
                case Direction.LEFT:
                    if (currentLocation.gridX==ArenaMap.START_POINT.gridX) return;
                    currentLocation=PointManager.getPoint(currentLocation.gridX-1, currentLocation.gridY);
                    break;
                case Direction.RIGHT:
                    if (currentLocation.gridX==ArenaMap.END_POINT.gridX) return;
                    currentLocation=PointManager.getPoint(currentLocation.gridX+1, currentLocation.gridY);
                    break;
            }
            delay(delay);

	}
	
	public void jumpToPoint(int x, int y, boolean delay){
		if (Config.Simulator){
			currentLocation = PointManager.getPoint(x, y);
		} else {
			Communicator.moveFor();
		}
		delay(delay);
	}
	
	public void delay(boolean delay){
		if (delay) {
			try {
				Thread.sleep(Config.robotWaitingTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	public void turnLeft(boolean delay){

            if (!Config.Simulator){
                Communicator.turnLeft();
                if(autoUpdate){
                    Communicator.sendMapToAndroid(getRobotState());
                }
            }

		direction.rotate(Direction.LEFT);
        isTurning = true;
		updateRobotLoc();
		delay(delay);
	}
	
	public void turnRight(boolean delay){
        //try {

            if(!Config.Simulator){
                Communicator.turnRight();
                if(autoUpdate){
                    Communicator.sendMapToAndroid(getRobotState());
                }
            }

		//} catch (IOException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
		//}
		direction.rotate(Direction.RIGHT);
        isTurning = true;
		updateRobotLoc();
		delay(delay);
	}
	
	public void turnBack(boolean delay){
        //try {
			//Communicator.sendMessage("b"); // for checklist
            if(!Config.Simulator){
                Communicator.turnBack();
                if(autoUpdate){
                    Communicator.sendMapToAndroid(getRobotState());
                }
            }
		//} catch (IOException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
		//}
		direction.rotate(Direction.BACK);
        isTurning = true;
		updateRobotLoc();
		delay(delay);
	}
	
	public void turnNorth(boolean delay){
		switch (direction.getDirection()){
		case Direction.UP:
            // Do nothing
            break;
		case Direction.DOWN:
			this.turnBack(delay);
            if (Config.debugOn) System.out.println("i turn back");
            if (race && !Config.Simulator){// wait for robot to finish turning
                Communicator.getMovedDistance();
                if (Config.debugOn) System.out.println("i turn back\nI wait for movedDistance");}
			break;
		case Direction.LEFT:
			this.turnRight(delay);
            if (Config.debugOn) System.out.println("i turn right");
            if (race && !Config.Simulator){// wait for robot to finish turning
                Communicator.getMovedDistance();
                if (Config.debugOn) System.out.println("i turn right\nI wait for movedDistance");}
			break;
		case Direction.RIGHT:
			this.turnLeft(delay);
            if (Config.debugOn) System.out.println("i turn left");
            if (race && !Config.Simulator){
                Communicator.getMovedDistance();
                if (Config.debugOn) System.out.println("i turn left\nI wait for movedDistance");}
			break;
		}
	}
	
	public void turnSouth(boolean delay) {
		switch (direction.getDirection()){
		case Direction.UP:
			this.turnBack(delay);
            if (Config.debugOn) System.out.println("i turn back");
            if (race && !Config.Simulator){
                Communicator.getMovedDistance();
                 if (Config.debugOn) System.out.println("i turn back\nI wait for movedDistance");}
			break;
		case Direction.DOWN:
            // same direction, no turning, do nothing
			break;
		case Direction.LEFT:
			this.turnLeft(delay);
            if (Config.debugOn) System.out.println("i turn left");
            if (race && !Config.Simulator){
                Communicator.getMovedDistance();
                if (Config.debugOn) System.out.println("i turn left\nI wait for movedDistance");}
			break;
		case Direction.RIGHT:
			this.turnRight(delay);
            if (Config.debugOn) System.out.println("i turn right");
            if (race && !Config.Simulator){
                Communicator.getMovedDistance();
                if (Config.debugOn) System.out.println("i turn right\nI wait for movedDistance");}
			break;
		}
	}
	
	public void turnWest(boolean delay) {
		switch (direction.getDirection()){
		case Direction.UP:
			this.turnLeft(delay);
            if (Config.debugOn) System.out.println("i turn left");
            if (race && !Config.Simulator){
                Communicator.getMovedDistance();
                if (Config.debugOn) System.out.println("i turn left\nI wait for movedDistance");}
			break;
		case Direction.DOWN:
			this.turnRight(delay);
            if (Config.debugOn) System.out.println("i turn right");
            if (race && !Config.Simulator){
                Communicator.getMovedDistance();
                if (Config.debugOn) System.out.println("i turn right\nI wait for movedDistance");}
			break;
		case Direction.LEFT:
            // do nothing
			break;
		case Direction.RIGHT:
			this.turnBack(delay);
            if (Config.debugOn) System.out.println("i turn back");
            if (race && !Config.Simulator){
                Communicator.getMovedDistance();
                if (Config.debugOn) System.out.println("i turn back\nI wait for movedDistance");}
			break;
		}
	}
	
	public void turnEast(boolean delay){
		switch (direction.getDirection()){
		case Direction.UP:
			this.turnRight(delay);
            if (Config.debugOn) System.out.println("i turn right");
            if (race && !Config.Simulator){
                Communicator.getMovedDistance();
                if (Config.debugOn) System.out.println("i turn right\nI wait for movedDistance");}
			break;
		case Direction.DOWN:
			this.turnLeft(delay);
            if (Config.debugOn) System.out.println("i turn left");
            if (race && !Config.Simulator){
                Communicator.getMovedDistance();
                if (Config.debugOn) System.out.println("i turn left\nI wait for movedDistance");}
			break;
		case Direction.LEFT:
			this.turnBack(delay);
            if (Config.debugOn) System.out.println("i turn back");
            if (race && !Config.Simulator){
                Communicator.getMovedDistance();
                if (Config.debugOn) System.out.println("i turn back\nI wait for movedDistance");}
			break;
		case Direction.RIGHT:
           // do nothing
            if (Config.debugOn) System.out.println("do nothing");
			break;
		}
	}
	
	public void updatePerceptronToKnowledgebase(){
		mapKnowledgeBase.setArrayMap(sensors.getEnvironment());
	}

	public Point getCurrentLocation() {
		return currentLocation;
	}

	public void setCurrentLocation(Point currentLocation) {
		this.currentLocation = currentLocation;
	}
	
	public ArenaMap getMapKnowledgeBase() {
		return mapKnowledgeBase;
	}

	public void setMapKnowledgeBase(ArenaMap mapKnowledgeBase) {
		this.mapKnowledgeBase = mapKnowledgeBase;
	}

	public SimPerceptron getSensors() {
		return sensors;
	}

	public Stack<Point> getRoute() {
		return route;
	}

    public void setExploringToBeTrue() { isExploring = true;}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	
	public void setDirectionDegree(double d) {
		this.direction.setDegree(d);
	}

    public void tellRPI(){

    }

	public void setRoute(Stack<Point> route) {
		this.route = route;
	}


    // get robot state, to pass to android
    // 2: is turning
    // 0: idle
    // 1: exploring
    // -1: ?
    public String getRobotState(){
        if (isTurning){
            return "2";
        } else if(currentLocation == ArenaMap.END_POINT || currentLocation == ArenaMap.START_POINT){
            return "0";
        } else if (isExploring){
            return "1";
        }
        return "-1";
    }

	public boolean isRace() {
		return race;
	}

	public void setRace(boolean race) {
		this.race = race;
	}

	public Stack<Point> getNewRoute() {
		return newRoute;
	}

	public void setNewRoute(Stack<Point> newRoute) {
		this.newRoute = newRoute;
	}

    public void setAutoUpdate(boolean autoUpdate){
        this.autoUpdate=autoUpdate;
    }

}
