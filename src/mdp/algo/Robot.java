package mdp.algo;



public class Robot {

	ArenaMap mapKnowledgeBase;
	VirtualPerceptron sensors;
	
	
	boolean isExploring, isMoving, isOnTheWayReturning; 
	private Point currentLocation;
	
	
	public Robot() {
		isExploring = false;
		isMoving = false;
		isOnTheWayReturning = false;
		mapKnowledgeBase = new ArenaMap();
	}
	
	public void start(){
		setCurrentLocation(ArenaMap.START_POINT);
		isExploring = true;
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
