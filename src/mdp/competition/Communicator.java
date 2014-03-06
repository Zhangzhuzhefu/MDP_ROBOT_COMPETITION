package mdp.competition;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;


import mdp.algo.VirtualCommunicator;


public class Communicator extends VirtualCommunicator {

    String host = "192.168.4.4";
    int BUFFER_SIZE = 1024;
    int port = 4014;
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
            //System.out.println("handshaked");

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
                            byte[] buffer = new byte[BUFFER_SIZE];
                            int read;
                            int totalRead = 0;
                            InputStream clientInputStream = clientSocket.getInputStream();
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientInputStream));
                            message = bufferedReader.readLine();
                            System.out.println("Message Received: "+message);
                            sendMessage("I received your message: "+ message);
                            parser(message);


                            long endTime = System.currentTimeMillis();
                            System.out.println(totalRead + " bytes read in " + (endTime - startTime) + " ms.");

                        } catch (IOException e){
                            System.err.println(e.toString());
                        }
                    }

                } catch (IOException e){
                      System.err.println(e.toString());
                }
            }
        }).start();




    }





    public void terminate(){
        termination = true;
    }


    // Send Message to Raspberry Pi
    public void sendMessage(String message) throws IOException{
        final String sentMessage;
        sentMessage = message;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

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



    public void parser(String message){
    //JSON Parser

    }

    @Override
    public int[] ultraSonic() {
        int [] detect = new int[3];
        detect = uS;

        return detect;
    }

    @Override
    public int leftSensor() {
        // TODO Auto-generated method stub
        int  detect;
        detect = lS;
        return detect;
    }

    @Override
    public int rightSensor() {
        // TODO Auto-generated method stub
        int  detect;
        detect = rS;
        return detect;
    }



}
