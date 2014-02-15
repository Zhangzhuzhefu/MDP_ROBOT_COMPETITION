package mdp.algo;

import java.util.Stack;



public class Robot {

	ArenaMap mapKnowledgeBase;
	VirtualPerceptron sensors;
	Explorer explorer;
	PathCalculator pathCalculator;
	
	
	boolean isExploring, isMoving, isOnTheWayReturning; 
	private Point currentLocation;
	
	
	public Robot() {
		isExploring = false;
		isMoving = false;
		isOnTheWayReturning = false;
		mapKnowledgeBase = new ArenaMap();
		explorer = new Explorer();
		pathCalculator = new PathCalculator();
	}
	
	public void start(){
		setCurrentLocation(ArenaMap.START_POINT);
		isExploring = true;
		isOnTheWayReturning = false;
	}
	
	public Stack<Point> generateShortestPath(){
		pathCalculator.setMap(getMapKnowledgeBase().getArrayMap());
		if(pathCalculator.findShortestPath())
			return pathCalculator.getShortestPath();
		else 
			return null;
	}
	
	public Stack<Point> generateRandomPath(){
		pathCalculator.setMap(this.getMapKnowledgeBase().getArrayMap());
		if(pathCalculator.findRandomPath())
			return pathCalculator.getRandomPath();
		else 
			return null;
	}
	
	public void move(){
		
	}

	public void moveForwardByOneStep(){
		
	}
	
	public void turnLeft(){
		
	}
	
	public void turnRight(){
	
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

}
