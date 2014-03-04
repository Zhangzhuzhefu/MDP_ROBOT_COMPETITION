package mdp.competition;

import com.oracle.javafx.jmx.json.JSONDocument;
import mdp.algo.VirtualCommunicator;
import java.io.*;
import java.net.*;

public class Communicator extends VirtualCommunicator {

    String host = "192.168.4.4";

    int port = 4014;
    String testmessage = "wifi handshake";

    boolean termination = false;

    public Communicator() throws IOException{
        // handshake

        try{
            System.out.println("handshaking...");
            Socket socket = new Socket(host,port);
            System.out.println("handshaked");

            DataOutputStream outp = new DataOutputStream(socket.getOutputStream());

            outp.writeBytes(testmessage);
            outp.flush();
            socket.close();

        } catch (UnknownHostException e){
            System.err.println(e);
            System.exit(1);
        } catch (IOException e){
            System.err.println("Couldn't get I/O connection and handshake with " + host);
            System.exit(1);
        } //end handshake


        // server connection
        int count = 0;

        try(ServerSocket serverSocket = new ServerSocket(port)) {

            System.out.println("Server Up!");
            while (true){
                try {
                    Socket clientSocket = serverSocket.accept();

                    count++;
                    System.out.println("#" + count + " from " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());

                    HostThread myHostThread = new HostThread(clientSocket, count);
                    myHostThread.start();

                } catch (IOException ex) {
                    System.out.println(ex.toString());
                }

            }


        } catch (IOException e){
            System.err.println("Couldn't set up server");
            System.exit(1);
        }
        // server connection


        // client connection
        try(Socket cSocket = new Socket(host,port)){
            System.out.println("Connected to raspberry pi");

            DataOutputStream outp = new DataOutputStream(cSocket.getOutputStream());
            outp.writeBytes(testmessage);
            outp.flush();

        } catch (IOException ex) {
            System.out.println(ex.toString());
        }
    }






    public void terminate(){
        termination = true;
    }

    public void sendMessage(String message) throws IOException{
        try(Socket cSocket = new Socket(host, port)){
            DataOutputStream out = new DataOutputStream(cSocket.getOutputStream());
            out.writeBytes(message);
            out.flush();
            cSocket.close();

        }
    }

    private static class ClientThread extends Thread{

        private Socket clientThreadSocket;
        int cnt;

        ClientThread(Socket socket, int c) {
            clientThreadSocket = socket;
            cnt = c;
        }

        @Override
        public void run() {
            OutputStream outputStream;

            try{
                outputStream = clientThreadSocket.getOutputStream();

                try (PrintStream printStream = new PrintStream(outputStream)){
                    printStream.print("hello"+cnt);
                }
                finally {
                    try{
                        clientThreadSocket.close();
                    } catch (IOException ex){
                        System.out.println(ex.toString());
                    }
                }

            }catch (IOException ex) {
                System.out.println(ex.toString());
            }
        }
    }

    private static class HostThread extends Thread{

        private Socket hostThreadSocket;
        int cnt;

        HostThread(Socket socket, int c) {
            hostThreadSocket = socket;
            cnt = c;
        }

        @Override
        public void run() {
            OutputStream outputStream;
            InputStream inputStream;
            try {
                inputStream = hostThreadSocket.getInputStream();



                try (BufferedReader in = new BufferedReader(new InputStreamReader(inputStream)) ){
                    System.out.println("Message received from Raspberry: "+ in.readLine());
                } catch (IOException ex){
                    System.out.println(ex.toString());
                }
                /*
                outputStream = hostThreadSocket.getOutputStream();
                try (PrintStream printStream = new PrintStream(outputStream)){
                    printStream.print("Hello, you are #" +cnt);
                }*/

                finally {
                    try{
                        hostThreadSocket.close();
                    } catch (IOException ex) {
                        System.out.println(ex.toString());
                    }
                }
            }catch (IOException ex){
                System.out.println(ex.toString());

            }
        }
    }

    @Override
    public int[] ultraSonic() {
        int [] detect = new int[3];

        return detect;
    }

    @Override
    public int leftSensor() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int rightSensor() {
        // TODO Auto-generated method stub
        return 0;
    }



}
