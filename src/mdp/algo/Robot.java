package mdp.algo;

import java.io.IOException;
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
    //Communicator communicator; // remove slash when testing with rPi
	
	
	boolean isExploring, isMoving, isOnTheWayReturning; 
	private Point currentLocation;
	private Direction direction;
	private Stack<Point> route;
	
	
	public Robot() throws IOException{
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
        //communicator = new Communicator(); // remove when testing with rPi
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
	
	public Stack<Point> generateFastestPath(){
		pathCalculator.setMap(getMapKnowledgeBase().getArrayMap());
		if(pathCalculator.findFastestPath()){
			route = (Stack<Point>) pathCalculator.getFastestPath();
			return pathCalculator.getFastestPath();
		}
		else 
			return null;
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
	
	public void move()throws IOException{
        if (route!=null && !route.empty() ){
            if (Config.debugOn) System.out.println("Robot route exists");
            if (route.peek().sameGridPoint(ArenaMap.END_POINT)){
                isOnTheWayReturning = true;
                if (Config.debugOn) System.out.println("isOnTheWayReturning = true");
            }
            Point nextLoc = route.peek();;
            int xDiff, yDiff;
			xDiff = nextLoc.gridX - currentLocation.gridX;
			yDiff = nextLoc.gridY - currentLocation.gridY;
			if (xDiff>0) {
				this.turnEast(true);
			} else if (xDiff<0) {
				this.turnWest(true);
			} else if (yDiff>0) {
				this.turnNorth(true);
			} else if (yDiff<0) {
				this.turnSouth(true);
			}
            currentLocation = route.pop();
            updateRobotLoc();
            
        } else {
            if (Config.debugOn) System.out.println("Robot route is empty..");
            isMoving = false;
        }
	}
	
	public void updateRobotLoc(){
		if (sensors.getCommunicator() instanceof SimCommunicator){
//			SimCommunicator s ;
//			s = (SimCommunicator) sensors.communicator;
			Simulator.simulatorMapPanel.updateRobot(currentLocation,direction);
		} else if (sensors.getCommunicator() instanceof Communicator){
			//TODO
		}
	}

	public void moveForwardByOneStep(boolean delay){
        try {
            Communicator.sendMessage("m");
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

        }catch (IOException e){

        }

	}
	
	public void jumpToPoint(int x, int y, boolean delay){
		currentLocation = PointManager.getPoint(x,y);
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
	public void turnLeft(boolean delay)throws IOException{
        Communicator.sendMessage("l");
		direction.rotate(Direction.LEFT);
		updateRobotLoc();
		delay(delay);
	}
	
	public void turnRight(boolean delay)throws IOException{
        Communicator.sendMessage("r");
		direction.rotate(Direction.RIGHT);
		updateRobotLoc();
		delay(delay);
	}
	
	public void turnBack(boolean delay)throws IOException{
        Communicator.sendMessage("b");
		direction.rotate(Direction.BACK);
		updateRobotLoc();
		delay(delay);
	}
	
	public void turnNorth(boolean delay)throws IOException{
		switch (direction.getDirection()){
		case Direction.UP:
			break;
		case Direction.DOWN:
			this.turnBack(delay);
			break;
		case Direction.LEFT:
			this.turnRight(delay);
			break;
		case Direction.RIGHT:
			this.turnLeft(delay);
			break;
		}
	}
	
	public void turnSouth(boolean delay)throws IOException{
		switch (direction.getDirection()){
		case Direction.UP:
			this.turnBack(delay);
			break;
		case Direction.DOWN:
			break;
		case Direction.LEFT:
			this.turnLeft(delay);
			break;
		case Direction.RIGHT:
			this.turnRight(delay);
			break;
		}
	}
	
	public void turnWest(boolean delay)throws IOException{
		switch (direction.getDirection()){
		case Direction.UP:
			this.turnLeft(delay);
			break;
		case Direction.DOWN:
			this.turnRight(delay);
			break;
		case Direction.LEFT:
			break;
		case Direction.RIGHT:
			this.turnBack(delay);
			break;
		}
	}
	
	public void turnEast(boolean delay)throws IOException{
		switch (direction.getDirection()){
		case Direction.UP:
			this.turnRight(delay);
			break;
		case Direction.DOWN:
			this.turnLeft(delay);
			break;
		case Direction.LEFT:
			this.turnBack(delay);
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

    public void tellRPI(){

    }

	public void setRoute(Stack<Point> route) {
		this.route = route;
	}

}
