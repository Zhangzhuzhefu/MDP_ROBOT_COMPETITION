package mdp.algo;

import mdp.Config;


public class Robot {

	ArenaMap mapKnowledgeBase;
	VirtualPerceptron sensors;
	
	boolean isMoving, isOnTheWayReturning; 
	private Point currentLocation;
	
	
	public Robot() {
		isMoving = false;
		isOnTheWayReturning = false;
		mapKnowledgeBase = new ArenaMap();
	}
	
	public void start(){
		setCurrentLocation(Config.START_POINT);
		isMoving = true;
		isOnTheWayReturning = false;
	}
	
	public void move(){
		
	}

	public void moveForwardByOneStep(){
		
	}
	
	public void turnLeft(){
		
	}
	
	public void turnRight(){
	
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

}
