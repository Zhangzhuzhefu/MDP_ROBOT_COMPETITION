package mdp.competition;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import mdp.Config;
import mdp.algo.Robot;
import mdp.algo.VirtualCommunicator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class Communicator extends VirtualCommunicator {

    private Robot robot;

    String host = Config.host;
    int port = Config.port;
    String testmessage = "wifi handshake";

    boolean termination = false;
    public static String message;
    public String cmd;
    public int[] uS = new int[3];
    public int lS;
    public int rS;
    public volatile int[] nLs = new int[1];
    public volatile int[] nUs = new int[3];
    public volatile int[] nRs = new int[1];
    final Integer turn = new Integer(0);
    public static volatile int movedDistance;
    final static Integer runWait = new Integer(0);


    public String d;
    public String s;
    public List<String> pv = new ArrayList<>();


    public Communicator() throws IOException{

    	if (Config.debugOn)
    		System.out.println("Communicator: Constructor start.");
        // handshake
        try{
        	if (Config.debugOn)
        		System.out.println("Communicator: handshaking...");
            Socket socket = new Socket(host,port);
            DataOutputStream outp = new DataOutputStream(socket.getOutputStream());
            outp.writeBytes(testmessage);
            outp.flush();
            socket.close();

            if (Config.debugOn){
                System.out.println("Communicator: handshaked");
            }


        } catch (UnknownHostException e){
            System.err.println(e);
            System.exit(1);
        } catch (IOException e){
            System.err.println("Communicator: Couldn't get I/O connection and handshake with " + host);
            System.exit(1);
        }

        //end handshake

        // start server to receive message from Raspberry Pi
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    ServerSocket serverSocket = new ServerSocket(4014);
                    System.out.println("Communicator: Server Up! at "+serverSocket.getInetAddress().getHostName());
                    while (true){
                        try{

                            Socket clientSocket = serverSocket.accept();
                            long startTime = System.currentTimeMillis();
                            reintialize(); // to prevent command re-read

                            InputStream clientInputStream = clientSocket.getInputStream();
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientInputStream));
                            message = bufferedReader.readLine();

                            if (Config.debugOn){
                                System.out.println("Communicator: Message Received: "+message);
                            }
                            //sendMessage("I received your message: "+ message);
                            //sendMessage("f"); // test for checklist

                            parser(message); // parse message to pass values

                            if (cmd != null)  {

                                // follow command

                                switch (cmd){


                                    case "f":    // choose floodfill algorithm
                                        Competition.updateRandomPath();
                                        Competition.secondRun();
                                        break;

                                    case "s":    // choose shortestpath algorithm
                                        Competition.updateFastestPath();
                                        //Competition.robotManager.robotRun();
                                        Competition.secondRun();
                                        break;

                                    case "a":   // move forward by one step
                                        Competition.robot.moveForwardByOneStep(false);
                                        break;

                                    case "b":   // turn left
                                        Competition.robot.turnLeft(false);
                                        break;

                                    case "c":   // turn right
                                        Competition.robot.turnRight(false);
                                        break;

                                    case "d":   // turn back
                                        Competition.robot.turnBack(false);
                                        break;

                                    case "e":   // explore
                                        Competition.explore_FollowWall();
                                        break;

                                    case "t":   // auto run

                                        break;

                                    case "u":   // manual request map
                                        sendMapToAndroid(Competition.robotManager.getRobot().getRobotState());
                                        break;

                                    case "on":   // enable auto-update
                                        System.out.println("Communicator: he auto update");
                                        setAuto();
                                        break;

                                    case "off":   // disable auto-update
                                        System.out.println("Communicator: he disable auto update");
                                        setManual();
                                        break;


                                    default:
                                        break;
                                }
                            }// end if (cmd != null)

                            Competition.robot.updateRobotLoc();


                            long endTime = System.currentTimeMillis();
                            System.out.println("Communicator: Message read in " + (endTime - startTime) + " ms.");

                        } catch (IOException e){
                            System.err.println(e.toString());
                        }

                    } // end while loop

                } catch (IOException e){
                      System.err.println("no its me "+ e.toString());
                }
            }
        }).start();
        // end thread



    }


    public void reintialize(){
        d = null;
        s = null;
        pv = null;
        cmd = null;
    }


    public void terminate(){
        termination = true;
    }


    // Open connection to send Message to Raspberry Pi
    public static void sendMessage(String message) throws IOException{

        final String sentMessage;
        sentMessage = message;
        final String host = Config.host;
        final int port = Config.port;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // create socket
                    Socket socket = new Socket(host, port);
                    System.out.println("Communicator: Connected to "+host);


                    OutputStream socketOutputStream = socket.getOutputStream();
                    long startTime = System.currentTimeMillis();

                    socketOutputStream.write(sentMessage.getBytes());
                    socketOutputStream.close();
                    socket.close();

                    long endTime = System.currentTimeMillis();
                    if (Config.debugOn){
                        System.out.println("Communicator: message: "+ sentMessage +"\tduration: "  + (endTime - startTime) + " ms.");
                    }

                } catch (Exception e) {
                    System.err.println("Communicator: something wrong with me! : "+e.toString());
                }
            }
        }).start();

    }



    // get and write map to JSON
    @SuppressWarnings("unchecked")
    public static String writeMap(String state){
        JSONObject obj = new JSONObject();
        obj.put("cmd","u");
        obj.put("map", Competition.robotManager.getRobot().getMapKnowledgeBase().getStringMap());
        JSONObject obj1 = new JSONObject();
        JSONArray list = new JSONArray();
        list.add(Competition.robotManager.getRobot().getCurrentLocation().gridX-3);
        list.add(Competition.robotManager.getRobot().getCurrentLocation().gridY-3);
        obj1.put("p", list);
        obj1.put("s",state);
        obj1.put("d",Competition.robotManager.getRobot().getDirection().getDirection());
        obj.put("status",obj1);
        return obj.toString();
    }

    // send map for android
    public static void sendMapToAndroid(String state){
        try {
            String map = writeMap(state);
            sendMessage(map); 
        } catch (IOException e){
            System.err.print(e);
        }
    }



    // send command to arduino
    // command robot:
    // eg.
    // a: move forward by one step
    // b: turn left
    // c: turn right
    // d: turn back
    // e: start explore
    // int: move int cm
    // f: full alignment
    // h: half alignment
    public static void moveFor() {
        writeCommandToArduino("a");
    }

    public static void turnLeft() {
        writeCommandToArduino("b");
    }

    public static void turnRight(){
        writeCommandToArduino("c");
    }

    public static void turnBack(){
        writeCommandToArduino("d");
    }

    // tell arduino to start exploring
    public static void startExplore(){
        writeCommandToArduino("e");
    }

    // tell arduino to change from exploration to race
    public static void startRace(){
        writeCommandToArduino("r");
    }
    
    // request for sensor value
    public static void sensorValue(){
        writeCommandToArduino("s");
    }

    // move a certain distance
    public static void moveInt(int distance){
        int newdistance = 0;
        int newdistance2 = 0;
        if (distance>=10){
            newdistance = distance/2;
            newdistance2 = distance/2;
            if (newdistance * 2 != distance) newdistance += 1;
        } else {
            newdistance = distance;
        }
        writeCommandToArduino(Integer.toString(newdistance));
        if (newdistance2 != 0) writeCommandToArduino(Integer.toString(newdistance2));
    }

    // ask to do full alignment
    public static void fullAlign(){
        writeCommandToArduino("f");
    }

    // ask to do half alignment
    public static void halfAlign(){
        writeCommandToArduino("h");
    }

    // write any command to arduino
    public static void writeCommandToArduino(String cmd){
        JSONObject obj = new JSONObject();
        obj.put("cmd","ard");
        obj.put("ard", cmd);
        try {
            sendMessage(obj.toString());
        } catch (IOException e ){
            System.err.print(e);
        }


    }



    // parse JSON
    @SuppressWarnings("unchecked")
    public void parser(String message){
        //JSON Parser
        JSONParser jp = new JSONParser();
        try {
            Object obj = jp.parse(message);
            JSONObject jsonObject = (JSONObject) obj;

            cmd = jsonObject.get("cmd").toString();

            // from Arduino
            if (cmd.equals("ssr")){

                if (!jsonObject.get("usl").toString().equals("")){

                    String usl = jsonObject.get("usl").toString();
                    String usr = jsonObject.get("usr").toString();
                    String usc = jsonObject.get("usc").toString();
                    String irl = jsonObject.get("irl").toString();
                    String irr = jsonObject.get("irr").toString();
                    
                    if (Config.twoBytwo) {
                    	nUs[0] = Integer.parseInt(usl);
                        nUs[1] = Integer.parseInt(usc);
                        nUs[2] = Integer.parseInt(usr);
                        
                        nUs[0] = checkInfraredRange(nUs[0]);
						nUs[0] = roundingToTen(nUs[0]) / 10;
						nUs[1] = checkInfraredRange(nUs[1]);
						nUs[1] = roundingToTen(nUs[1]) / 10;
						nUs[2] = checkInfraredRange(nUs[2]);
						nUs[2] = roundingToTen(nUs[2]) / 10;

						nLs[0] = Integer.parseInt(irl) ;
						nLs[0] = checkInfraredRange(nLs[0]);
						nLs[0] = roundingToTen(nLs[0]) / 10;

						nRs[0] = Integer.parseInt(irr);
						nRs[0] = checkUltrasonicRange(nRs[0]);
						nRs[0] = roundingToTen(nRs[0]) / 10;
                    } else {
                    	nUs[0] = Integer.parseInt(usl);
                        nUs[1] = Integer.parseInt(usc);
                        nUs[2] = Integer.parseInt(usr);
                        
                        nUs[0] = checkInfraredRange(nUs[0]);
                        nUs[0] -= 5;
						nUs[0] = roundingToTen(nUs[0]) / 10;
						
						nUs[1] = checkInfraredRange(nUs[1]);
						nUs[1] -= 5;
						nUs[1] = roundingToTen(nUs[1]) / 10;
						
						nUs[2] = checkInfraredRange(nUs[2]);
						nUs[2] -= 5;
						nUs[2] = roundingToTen(nUs[2]) / 10;

						nLs[0] = Integer.parseInt(irl);
						nLs[0] = checkInfraredRange(nLs[0]) - 5;
						nLs[0] = roundingToTen(nLs[0]) / 10;

						nRs[0] = Integer.parseInt(irr);
						nRs[0] = checkUltrasonicRange(nRs[0])- 5;
						nRs[0] = roundingToTen(nRs[0]) / 10;
                    }

                    if ( nUs[0]<0 || nUs[1]<0 || nUs[2]<0 || nLs[0]<0 || nRs[0]<0
                    		
                    		) {
                    	//Communicator.sensorValue();
                    }



                    synchronized (turn){
                        turn.notify();
                    }


                    // pass sensors values
                    uS[0] = Integer.parseInt(usl);
                    uS[1] = Integer.parseInt(usc);
                    uS[2] = Integer.parseInt(usr);
                    lS = Integer.parseInt(irl);
                    rS = Integer.parseInt(irr);


                    if (Config.debugOn){
                        System.out.print(" usl: "+usl);
                        System.out.print(" usc: "+usc);
                        System.out.print(" usr: "+usr);
                        System.out.print(" irl: "+irl);
                        System.out.print(" irr: "+irr);

                    }
                } else {
                	if (Config.debugOn)
                		System.out.println("Communicator: warning sensor velue is empty");
                }
                System.out.println("the isRace is: " + Competition.robotManager.getRobot().isRace());
                if (Competition.robotManager.getRobot().isRace()){
                    if (!jsonObject.get("dis").toString().equals("")){
                        movedDistance = Integer.getInteger(jsonObject.get("dis").toString());}
                    synchronized (runWait){
                        runWait.notify();
                    }

                }
            }


        } catch (ParseException e){
            System.err.println(e);

        }

    }

    public int roundingToTen(int a){
    	int offSet = a%10;
    	if (offSet<=5) {
    		a -= offSet; 
    	} else {
    		a += 10-offSet;
    	}
        return a;
    }
    public int checkInfraredRange(int a){
    	int shortIR = Config.InfraRedDetectDist*10;
		if (a > shortIR || a < 0)
			return shortIR+1;
		else
			return a;

	}
    public int checkUltrasonicRange(int a){
    	int longIR = Config.LongInfraredDetectDist*10;
		if (a> longIR || a<0)
			return longIR+1;
		else return a;
}
    public void setAuto(){
        robot.setAutoUpdate(true);
    }

    public void setManual(){
        robot.setAutoUpdate(false);
    }

    public static void getSensorValue(){
    	writeCommandToArduino("s");
    }

    @Override
    public int[] ultraSonic() {
        synchronized (turn){
        try{
            System.out.println("Communicator: I am waiting");
            long tBefore=System.currentTimeMillis();
            turn.wait(Config.TIMEOUT);
            if ((System.currentTimeMillis() - tBefore) > Config.TIMEOUT) 
              { 
            	 System.out.println("Communicator: timeout liao");
                 getSensorValue();
                 turn.wait();
              }
            System.out.println("Communicator: finish waiting");
            } catch (InterruptedException e) {
                System.err.print(e);
            }
        }

        return nUs;//detectInt;
        }





    @Override
    public int leftSensor() {
        // TODO Auto-generated method stub
        int  detectInt;
        detectInt = lS;
        /*
        synchronized (nLs){
            try {
                nLs.wait();
            } catch (InterruptedException e){

            }
        }*/
        return nLs[0];//detectInt;
    }

    @Override
    public int rightSensor() {
        // TODO Auto-generated method stub
        int  detectInt;
        detectInt = rS;
        /*synchronized (nRs){
            try {
                nRs.wait();
            } catch (InterruptedException e){

            }
        }*/
        return nRs[0];//detectInt
    }

    public static int getMovedDistance(){
        synchronized (runWait){
            try{
                System.out.println("Communicator: I am waiting for movedDistance");
                runWait.wait();
                System.out.println("Communicator: finish waiting");
            } catch (InterruptedException e) {
                System.err.print(e);
            }
        }
        return movedDistance;
    }



}
