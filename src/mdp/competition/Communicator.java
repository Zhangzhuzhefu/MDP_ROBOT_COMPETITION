package mdp.competition;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


import mdp.Config;
import mdp.algo.Robot;
import mdp.simulation.Simulator;
import org.json.simple.*;


import mdp.algo.VirtualCommunicator;
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
                                        Competition.updateShortestPath();
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
                                        Competition.explore_floodFill();
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
        writeCommandToArduino(Integer.toString(distance*2));
    }

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
						nUs[1] = checkUltrasonicRange(nUs[1]);
						nUs[1] = roundingToTen(nUs[1]) / 10;
						nUs[2] = checkInfraredRange(nUs[2]);
						nUs[2] = roundingToTen(nUs[2]) / 10;

						nLs[0] = Integer.parseInt(irl) ;
						nLs[0] = checkInfraredRange(nLs[0]);
						nLs[0] = roundingToTen(nLs[0]) / 10;

						nRs[0] = Integer.parseInt(irr);
						nRs[0] = checkInfraredRange(nRs[0]);
						nRs[0] = roundingToTen(nRs[0]) / 10;
                    } else {
                    	nUs[0] = Integer.parseInt(usl);
                        nUs[1] = Integer.parseInt(usc);
                        nUs[2] = Integer.parseInt(usr);
                        
                        nUs[0] = checkInfraredRange(nUs[0]);
                        nUs[0] -= 5;
						nUs[0] = roundingToTen(nUs[0]) / 10;
						nUs[1] = checkUltrasonicRange(nUs[1]);
						nUs[1] -= 5;
						nUs[1] = roundingToTen(nUs[1]) / 10;
						nUs[2] = checkInfraredRange(nUs[2]);
						nUs[2] -= 5;
						nUs[2] = roundingToTen(nUs[2]) / 10;

						nLs[0] = checkInfraredRange(nLs[0]);
						nLs[0] = Integer.parseInt(irl) - 5;
						nLs[0] = roundingToTen(nLs[0]) / 10;

						nRs[0] = checkInfraredRange(nRs[0]);
						nRs[0] = Integer.parseInt(irr) - 5;
						nRs[0] = roundingToTen(nRs[0]) / 10;
                    }

                    if ( nUs[0]<0 || nUs[1]<0 || nUs[2]<0 || nLs[0]<0 || nRs[0]<0
                    		
                    		) {
                    	//Communicator.sensorValue();
                    }

                    if (!jsonObject.get("dist").toString().equals(" ") && Config.race){
                        movedDistance = Integer.getInteger(jsonObject.get("dist").toString());
                        synchronized (runWait){
                            runWait.notify();
                        }

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
                        System.out.print("usl: "+usl);
                        System.out.print(" usc: "+usc);
                        System.out.print(" usr: "+usr);
                        System.out.print(" irl: "+irl);
                        System.out.print(" irr: "+irr);

                    }
                } else {
                	if (Config.debugOn)
                		System.out.println("Communicator: warning sensor velue is empty");
                }
            }


        } catch (ParseException e){
            System.err.println(e);

        }

    }

    public int roundingToTen(int a){
    	int offSet = a%10;
    	if (offSet<5) {
    		a -= offSet; 
    	} else {
    		a += 10-offSet;
    	}
        return a;
    }
    public int checkInfraredRange(int a){
		if (a > 30 || a < 0)
			return 30;
		else
			return a;

	}
    public int checkUltrasonicRange(int a){
		if (a>150 || a<0)
			return 150;
		else return a;
}
    public void setAuto(){
        Config.autoUpdate = true;
    }

    public void setManual(){
        Config.autoUpdate = false;
    }

    public static void getSensorValue(){
    	writeCommandToArduino("s");
    }

    @Override
    public int[] ultraSonic() {
        synchronized (turn){
        try{
            System.out.println("Communicator: I am waiting");
            turn.wait();
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
                System.out.println("Communicator: I am waiting");
                runWait.wait();
                System.out.println("Communicator: finish waiting");
            } catch (InterruptedException e) {
                System.err.print(e);
            }
        }
        return movedDistance;
    }



}
