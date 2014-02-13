package mdp.algo;

import mdp.Config;


public class RobotManager extends Thread{
	Robot robot;
	public RobotManager() {
		this.robot = new Robot();
	}
	
	@Override
	public void run() {
		robot.start();
		System.out.println("RobotManager: Robot started!");
		while (robot.isMoving) {
			robot.move();
			try {
				Thread.sleep(Config.robotWaitingTime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public Robot getRobot(){
		return robot;
	}
}
