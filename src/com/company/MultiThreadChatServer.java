package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MultiThreadChatServer extends Thread{
    int clientsCount ;
    List<Conversation> conversations = new ArrayList<>();

    public static void main(String[] args){
        new MultiThreadChatServer().start();
    }

    public void run(){
        System.out.println("======== server started in the port 1234 ============");
        try {
            ServerSocket server_socket = new ServerSocket(1234);
            while(true){
                Socket socket = server_socket.accept();
                ++clientsCount;
                Conversation conversation = new Conversation(socket, clientsCount);
                conversations.add(conversation);
                conversation.start();
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
                String request;
                while((request = bfr.readLine()) != null){
                    System.out.println("new Request => " + request);
                    List<Integer> clientsTo = new ArrayList<>();
                    if (request.contains("=>")){
                        String[] items = request.split("=>");
                        String clients = items[0];
                        String message = items[1];
                        if (clients.contains(",")){
                            String[] clientsIds = clients.split(",");
                            for(String id : clientsIds){
                                clientsTo.add(Integer.parseInt(id));
                            }
                        }
                        else {
                            clientsTo.add(Integer.parseInt(clients));
                        }
                        request = message;
                    }
                    else {
                        clientsTo = conversations.stream().map(c->c.clientID).collect(Collectors.toList());
                    }
                    broadcastMessage(request, this, clientsTo);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void broadcastMessage(String message, Conversation from, List<Integer> clientsTo){
        try {
            for (Conversation conversation : conversations){
                if(conversation == from && !clientsTo.contains(conversation.clientID)  ){
                    continue;
                }
                Socket socket = conversation.socket;
                OutputStream os = socket.getOutputStream();
                PrintWriter pw = new PrintWriter(os, true);
                pw.println(message);
            }
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
