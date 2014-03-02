package mdp.algo;

import java.util.Stack;

import mdp.Config;
import mdp.competition.Communicator;
import mdp.simulation.SimCommunicator;



public class Robot {

	ArenaMap mapKnowledgeBase;
	VirtualPerceptron sensors;
	Explorer explorer;
	PathCalculator pathCalculator;
	
	
	boolean isExploring, isMoving, isOnTheWayReturning; 
	private Point currentLocation;
	public Direction direction;
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
	}
	
	public void reset(){
		isExploring = true;
		isMoving = false;
		isOnTheWayReturning = false;
		currentLocation = ArenaMap.START_POINT;
		mapKnowledgeBase.reset();
		explorer.reset();
		pathCalculator.reset();
		route.clear();
	}
	
	public void startMoving(){
		setCurrentLocation(ArenaMap.START_POINT);
		isMoving = true;
		isOnTheWayReturning = false;
	}
	
	@SuppressWarnings("unchecked")
	public Stack<Point> generateShortestPath(){
		pathCalculator.setMap(getMapKnowledgeBase().getArrayMap());
		if(pathCalculator.findShortestPath()){
			route = (Stack<Point>) pathCalculator.getShortestPath().clone();
			return pathCalculator.getShortestPath();
		}
		else 
			return null;
	}
	
	@SuppressWarnings("unchecked")
	public Stack<Point> generateRandomPath(){
		pathCalculator.setMap(this.getMapKnowledgeBase().getArrayMap());
		if(pathCalculator.findRandomPath()){
			route = (Stack<Point>) pathCalculator.getRandomPath().clone();
			return pathCalculator.getRandomPath();
		}
		else 
			return null;
	}

    public void explore(String s){
		switch (s) {
		case Explorer.FLOODFILL:
			if (Config.debugOn)
				System.out.println("Exploring Floodfill");
			explorer.exploreFloodFill(mapKnowledgeBase, currentLocation);
			updateLocation(currentLocation);
			if (currentLocation.gridX == 16 && currentLocation.gridY == 21) {
				if (Config.debugOn) {
					System.out.println("Explore Done");
				}
				isExploring = false;
			}
			break;
		case Explorer.ASTAR: 
			if (Config.debugOn)
				System.out.println("Exploring A*");
			explorer.exploreAStar(this);
			updateLocation(currentLocation);
			if (currentLocation.gridX == 16 && currentLocation.gridY == 21) {
				if (Config.debugOn) {
					System.out.println("Explore Done");
				}
				isExploring = false;
			}
			break;
		}

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
		if (sensors.communicator instanceof SimCommunicator){
			SimCommunicator s ;
			s = (SimCommunicator) sensors.communicator;
			s.getMapPanel().updateRobot(p,direction);
		} else if (sensors.communicator instanceof Communicator){
			//TODO
		}
	}

	public void moveForwardByOneStep(){
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
	
	public void turnLeft(){
		direction.rotate(Direction.LEFT);
	}
	
	public void turnRight(){
		direction.rotate(Direction.RIGHT);
	}
	
	public void turnBack(){
		direction.rotate(Direction.BACK);
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

	public VirtualPerceptron getSensors() {
		return sensors;
	}

	public void setSensors(VirtualPerceptron sensors) {
		this.sensors = sensors;
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

}
