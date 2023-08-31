package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiThreadBlockingServer extends Thread{
    int clientsCount ;
    public static void main(String[] args){
        new MultiThreadBlockingServer().start();
    }

    public void run(){
        System.out.println("======== server started in the port 1234 ============");
        try {
            ServerSocket server_socket = new ServerSocket(1234);
            while(true){
                Socket socket = server_socket.accept();
                ++clientsCount;
                new Conversation(socket, clientsCount).start();
            }
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    class Conversation extends Thread{
        private int clientID;
        public Socket socket;

        public Conversation(Socket socket, int clientID){
            this.socket = socket;
            this.clientID = clientID;
        }

        public void run(){
            try {
                InputStream is = socket.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader bfr = new BufferedReader(isr);
                OutputStream os = socket.getOutputStream();
                PrintWriter pw = new PrintWriter(os, true);
                String ipClient = socket.getRemoteSocketAddress().toString();
                System.out.println("New client connection => " + clientID + " IP= " + ipClient);
                pw.println("welcom your id  is => " + ipClient);
                while(true){
                    String request = bfr.readLine();
                    System.out.println("new Request => " + request);
                    int response = request.length();
                    pw.println(response);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
