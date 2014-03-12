package mdp.competition;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
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

    public String m;
    public String r;
    public String d;
    public String x;
    public List<String> pv = new ArrayList<>();


    public Communicator() throws IOException{


        // handshake
        try{
            //System.out.println("handshaking...");
            Socket socket = new Socket(host,port);
            DataOutputStream outp = new DataOutputStream(socket.getOutputStream());

            outp.writeBytes(testmessage);
            outp.flush();
            socket.close();

            if (Config.debugOn){
                System.out.println("handshaked");
            }


        } catch (UnknownHostException e){
            System.err.println(e);
            System.exit(1);
        } catch (IOException e){
            System.err.println("Couldn't get I/O connection and handshake with " + host);
            System.exit(1);
        }

        //end handshake





        // start server to receive message from Raspberry Pi
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    ServerSocket serverSocket = new ServerSocket(4014);
                    System.out.println("Server Up! at "+serverSocket.getInetAddress().getHostName());
                    while (true){
                        try{

                            Socket clientSocket = serverSocket.accept();
                            long startTime = System.currentTimeMillis();
                            reintialize(); // to prevent command re-read

                            InputStream clientInputStream = clientSocket.getInputStream();
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientInputStream));
                            message = bufferedReader.readLine();

                            if (Config.debugOn){
                                System.out.println("Message Received: "+message);
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
                                        sendMapToAndroid();
                                        break;

                                    case "y":   // enable auto-update
                                        setAuto();
                                        break;

                                    case "n":   // disable auto-update
                                        setManual();
                                        break;


                                    default:
                                        break;
                                }
                            }// end if (cmd != null)

                            Competition.robot.updateRobotLoc();


                            long endTime = System.currentTimeMillis();
                            System.out.println("Message read in " + (endTime - startTime) + " ms.");

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

        m = null;
        r = null;
        d = null;
        x = null;
        pv = null;

        cmd = null;

    }


    public void terminate(){
        termination = true;
    }


    // Send Message to Raspberry Pi
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
                    System.out.println("Connected to "+host);


                    OutputStream socketOutputStream = socket.getOutputStream();
                    long startTime = System.currentTimeMillis();

                    socketOutputStream.write(sentMessage.getBytes());
                    socketOutputStream.close();
                    socket.close();
                    long endTime = System.currentTimeMillis();
                    if (Config.debugOn){
                        System.out.println("bytes written in " + (endTime - startTime) + " ms.");
                    }

                } catch (Exception e) {
                    System.err.println("something wrong with me! : "+e.toString());
                }
            }
        }).start();

    }

    // get and write map to JSON
    public String writeMap(){
        JSONObject obj = new JSONObject();
        obj.put("map",Simulator.robotManager.getRobot().getMapKnowledgeBase().getArrayMap().toString());
        return obj.toString();
    }

    // send map for android
    public void sendMapToAndroid(){
        try {
            String map = writeMap();
            sendMessage(map);
        } catch (IOException e){
            System.err.print(e);
        }
    }



    // send command to arduino
    // command robot:
    // eg. {“ard”:”a”}
    // a: move forward by one step
    // b: turn left
    // c: turn right
    // d: turn back
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

    public static void moveInt(int distance){
        writeCommandToArduino(Integer.toString(distance));
    }

    public static void writeCommandToArduino(String cmd){
        JSONObject obj = new JSONObject();
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




            // from Arduino
            if (jsonObject.get("usl")!=null){
                String usl = jsonObject.get("usl").toString();
                String usr = jsonObject.get("usr").toString();
                String usc = jsonObject.get("usc").toString();
                String irl = jsonObject.get("irl").toString();
                String irr = jsonObject.get("irr").toString();

                // pass sensors values
                uS[0] = Integer.parseInt(usl);
                uS[1] = Integer.parseInt(usc);
                uS[2] = Integer.parseInt(usr);
                lS = Integer.parseInt(irl);
                rS = Integer.parseInt(irr);


                if (Config.debugOn){
                    System.out.println("usl: "+usl);
                    System.out.println("usc: "+usc);
                    System.out.println("usr: "+usr);
                    System.out.println("irl: "+irl);
                    System.out.println("irr: "+irr);
                }
            }

            // values from Android
            if (jsonObject.get("cmd")!=null){
                cmd = jsonObject.get("cmd").toString();
            }

            if (jsonObject.get("status")!=null){
                Object stati = jsonObject.get("status");
                JSONObject status = (JSONObject) stati;

                d = status.get("d").toString();
                m = status.get("m").toString();
                r = status.get("r").toString();
                x = status.get("x").toString();

                JSONArray pl = (JSONArray) status.get("p");
                Iterator<String> pIT = pl.iterator();
                while(pIT.hasNext()){
                    pv.add(pIT.next());
                }
            }

        } catch (ParseException e){
            System.err.println(e);

        }


    }

    public void setAuto(){
        Config.autoUpdate = true;
    }

    public void setManual(){
        Config.autoUpdate = false;
    }

    @Override
    public int[] ultraSonic() {
        int [] detectInt;
        detectInt = uS;

        return detectInt;
    }

    @Override
    public int leftSensor() {
        // TODO Auto-generated method stub
        int  detectInt;
        detectInt = lS;
        return detectInt;
    }

    @Override
    public int rightSensor() {
        // TODO Auto-generated method stub
        int  detectInt;
        detectInt = rS;
        return detectInt;
    }



}
