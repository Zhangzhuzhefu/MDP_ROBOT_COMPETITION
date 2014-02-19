package mdp.algo;

import mdp.Config;


public class RobotManager{
	Robot robot;
	RobotRunner robotRunner;
	public RobotManager() {
		this.robot = new Robot();
	}
	
	public Robot getRobot(){
		return robot;
	}
	
	public void robotRun(){
		robotRunner = new RobotRunner();
		robotRunner.start();
	}
	
	@SuppressWarnings("deprecation")
	class RobotRunner extends Thread{
		@Override
		public void run() {
			robot.startMoving();
			System.out.println("RobotManager: Robot started!");
			while (robot.isMoving) {
				robot.move();
				try {
					Thread.sleep(Config.robotWaitingTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			this.stop();
		}
	}
}
