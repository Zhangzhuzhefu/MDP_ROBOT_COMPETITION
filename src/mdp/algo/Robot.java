package mdp.algo;

import java.util.Stack;

import mdp.Config;
import mdp.competition.Communicator;
import mdp.simulation.SimCommunicator;
import mdp.simulation.SimPerceptron;
import mdp.simulation.Simulator;



public class Robot {

	ArenaMap mapKnowledgeBase;
	SimPerceptron sensors;
	Explorer explorer;
	PathCalculator pathCalculator;
	
	
	boolean isExploring, isMoving, isOnTheWayReturning; 
	private Point currentLocation;
	private Direction direction;
	private Stack<Point> route;
	
	
	public Robot() {
		isExploring = true;
		isMoving = false;
		isOnTheWayReturning = false;
		currentLocation = ArenaMap.START_POINT;
		mapKnowledgeBase = new ArenaMap();
		explorer = new Explorer();
		pathCalculator = new PathCalculator();
		route = new Stack<Point>();
		direction = new Direction(0);
		sensors = new SimPerceptron(this);
	}
	
	public void reset(){
		isExploring = true;
		isMoving = false;
		isOnTheWayReturning = false;
		currentLocation = ArenaMap.START_POINT;
		mapKnowledgeBase.reset();
		explorer.reset();
		pathCalculator.reset();
		if (route!=null)
			route.clear();
	}
	
	public void startMoving(){
		setCurrentLocation(ArenaMap.START_POINT);
		isMoving = true;
		isOnTheWayReturning = false;
	}
	
	public Stack<Point> generateShortestPath(){
		pathCalculator.setMap(getMapKnowledgeBase().getArrayMap());
		if(pathCalculator.findShortestPath()){
			route = (Stack<Point>) pathCalculator.getShortestPath();
			return pathCalculator.getShortestPath();
		}
		else 
			return null;
	}
	
	public Stack<Point> generateRandomPath(){
		pathCalculator.setMap(this.getMapKnowledgeBase().getArrayMap());
		if(pathCalculator.findRandomPath()){
			route = (Stack<Point>) pathCalculator.getRandomPath();
			return pathCalculator.getRandomPath();
		}
		else 
			return null;
	}

    public Stack <Point> explore(String s){
		switch (s) {
		case Explorer.FLOODFILL:
			if (Config.debugOn)
				System.out.println("Exploring Floodfill");
			route = explorer.exploreFloodFill(this);
			if (route!=null) {
				return route;
			} else {
				System.out.println("Floodfill null route");
				return null;
			}
		case Explorer.ASTAR: 
			if (Config.debugOn)
				System.out.println("Robot: Exploring A*");
			route = explorer.exploreAStar(this);
			if (route!=null) {
				return route;
			} else {
				System.out.println("Astar null route");
				return null;
			}
		}
		return null;

    }
	
	public void move(){
        if (route!=null && !route.empty() ){
            if (Config.debugOn) System.out.println("Robot route exists");
            if (route.peek().sameGridPoint(ArenaMap.END_POINT)){
                isOnTheWayReturning = true;
                if (Config.debugOn) System.out.println("isOnTheWayReturning = true");
            }
            currentLocation = route.pop();
            updateLocation(currentLocation);
        } else {
            if (Config.debugOn) System.out.println("Robot route is empty..");
            isMoving = false;
        }
	}
	
	public void updateLocation(Point p){
		if (sensors.getCommunicator() instanceof SimCommunicator){
//			SimCommunicator s ;
//			s = (SimCommunicator) sensors.communicator;
			Simulator.simulatorMapPanel.updateRobot(p,direction);
		} else if (sensors.getCommunicator() instanceof Communicator){
			//TODO
		}
	}

	public void moveForwardByOneStep(){
		try {
			Thread.sleep(Config.robotWaitingTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
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
	}
	
	public void jumpToPoint(int x, int y){
		currentLocation = PointManager.getPoint(x,y); 
	}
	
	public void turnLeft(){
		direction.rotate(Direction.LEFT);
	}
	
	public void turnRight(){
		direction.rotate(Direction.RIGHT);
	}
	
	public void turnBack(){
		direction.rotate(Direction.BACK);
	}
	
	public void turnNorth(){
		switch (direction.getDirection()){
		case Direction.UP:
			break;
		case Direction.DOWN:
			try {
				Thread.sleep(Config.robotWaitingTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.turnBack();
			break;
		case Direction.LEFT:
			this.turnRight();
			try {
				Thread.sleep(Config.robotWaitingTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			break;
		case Direction.RIGHT:
			try {
				Thread.sleep(Config.robotWaitingTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.turnLeft();
			break;
		}
	}
	
	public void turnSouth(){
		switch (direction.getDirection()){
		case Direction.UP:
			try {
				Thread.sleep(Config.robotWaitingTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.turnBack();
			break;
		case Direction.DOWN:
			break;
		case Direction.LEFT:
			try {
				Thread.sleep(Config.robotWaitingTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.turnLeft();
			break;
		case Direction.RIGHT:
			try {
				Thread.sleep(Config.robotWaitingTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.turnRight();
			break;
		}
	}
	
	public void turnWest(){
		switch (direction.getDirection()){
		case Direction.UP:
			try {
				Thread.sleep(Config.robotWaitingTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.turnLeft();
			break;
		case Direction.DOWN:
			try {
				Thread.sleep(Config.robotWaitingTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.turnRight();
			break;
		case Direction.LEFT:
			break;
		case Direction.RIGHT:
			try {
				Thread.sleep(Config.robotWaitingTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.turnBack();
			break;
		}
	}
	
	public void turnEast(){
		switch (direction.getDirection()){
		case Direction.UP:
			try {
				Thread.sleep(Config.robotWaitingTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.turnRight();
			break;
		case Direction.DOWN:
			try {
				Thread.sleep(Config.robotWaitingTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.turnLeft();
			break;
		case Direction.LEFT:
			try {
				Thread.sleep(Config.robotWaitingTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.turnBack();
			break;
		case Direction.RIGHT:
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

	public void setRoute(Stack<Point> route) {
		this.route = route;
	}

}
