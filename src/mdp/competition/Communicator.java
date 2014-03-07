package mdp.competition;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;


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
    public int[] uS;
    public int lS;
    public int rS;

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
                    System.out.println("Server Up!");
                    while (true){
                        try{
                            Socket clientSocket = serverSocket.accept();
                            long startTime = System.currentTimeMillis();



                            InputStream clientInputStream = clientSocket.getInputStream();
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientInputStream));
                            message = bufferedReader.readLine();
                            //System.out.println("Message Received: "+message);
                            //sendMessage("I received your message: "+ message);
                            sendMessage("f"); // test for checklist
                            sendMessage("f"); // test for checklist

                            //String cmd = parser(message);

                            switch (message){
                                case "f":    // choose floodfill algorithm

                                    Simulator.updateRandomPath();
                                    Simulator.secondRun();
                                    break;

                                case "s":    // choose shortestpath algorithm
                                    Simulator.updateShortestPath();
                                    Simulator.secondRun();
                                    break;

                                case "a":   // move forward by one step
                                    Simulator.robot.moveForwardByOneStep(false);
                                    break;

                                case "b":   // turn left
                                    Simulator.robot.turnLeft(false);
                                    break;

                                case "c":   // turn right
                                    Simulator.robot.turnRight(false);
                                    break;

                                case "d":   // turn back
                                    Simulator.robot.turnBack(false);
                                    break;

                                case "e":   // explore
                                    Simulator.explore_floodFill();
                                    break;

                                case "t":   // auto run

                                    break;

                                default:
                                    break;
                            }
                            Simulator.robot.updateRobotLoc();


                            long endTime = System.currentTimeMillis();
                            System.out.println("Message read in " + (endTime - startTime) + " ms.");

                        } catch (IOException e){
                            System.err.println(e.toString());
                        }

                    } // end while loop

                } catch (IOException e){
                      System.err.println(e.toString());
                }
            }
        }).start();
        // end thread



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
                    System.out.println(" messaged sent");


                    socketOutputStream.close();

                    socket.close();
                    long endTime = System.currentTimeMillis();
                    System.out.println(" bytes written in " + (endTime - startTime) + " ms.");
                } catch (Exception e) {
                    System.err.println(e.toString());
                }
            }
        }).start();

    }


    // compile message into json
    @SuppressWarnings("unchecked")
    public String writeJson(String cmd,String map, String p1,String p2,String m,String r,String d,String x){
        JSONObject obj = new JSONObject();
        JSONObject obj1 = new JSONObject();
        JSONArray list = new JSONArray();
        list.add(p1);
        list.add(p2);
        obj.put("cmd",cmd);
        obj1.put("p",list);
        obj1.put("m",m);
        obj1.put("x",x);
        obj1.put("r",r);
        obj1.put("d",d);
        obj.put("status",obj1);
        obj.put("map",map);

        return obj.toString();

    }


    // compile message with int cmd
    @SuppressWarnings("unchecked")
    public String writeJson(int cmd,String map, String p1,String p2,String m,String r,String d,String x){
        JSONObject obj = new JSONObject();
        JSONObject obj1 = new JSONObject();
        JSONArray list = new JSONArray();
        list.add(p1);
        list.add(p2);
        obj.put("cmd",cmd);
        obj1.put("p",list);
        obj1.put("m",m);
        obj1.put("x",x);
        obj1.put("r",r);
        obj1.put("d",d);
        obj.put("status",obj1);
        obj.put("map",map);

        return obj.toString();

    }


    // parse JSON
    //@SuppressWarnings("unchecked")
    public String parser(String message){
        //JSON Parser
        JSONParser jp = new JSONParser();
        try {
            Object obj = jp.parse(message);
            JSONObject jsonObject = (JSONObject) obj;

            // from Android
            String cmd = jsonObject.get("cmd").toString();

            // from Arduino
            String usl = jsonObject.get("usl").toString();
            String usr = jsonObject.get("usr").toString();
            String usc = jsonObject.get("usc").toString();
            String irl = jsonObject.get("irl").toString();
            String irr = jsonObject.get("irr").toString();

            // pass values
            uS[0] = Integer.getInteger(usl);
            uS[1] = Integer.getInteger(usc);
            uS[2] = Integer.getInteger(usr);
            lS = Integer.getInteger(irl);
            rS = Integer.getInteger(irr);

            if (Config.debugOn){
                System.out.println("usl: "+usl);
                System.out.println("usc: "+usc);
                System.out.println("usr: "+usr);
                System.out.println("irl: "+irl);
                System.out.println("irr: "+irr);
            }

            return cmd;

        } catch (ParseException e){
            System.err.println(e);
            return null;
        }




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
