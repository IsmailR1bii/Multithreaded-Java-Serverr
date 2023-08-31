package com.company;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleServer {
    public static void main(String[] args) throws IOException {
        ServerSocket server_socket = new ServerSocket(1234);
        System.out.println("=================== waiting for connection =================");
        Socket socket = server_socket.accept();
        InputStream is = socket.getInputStream();
        OutputStream os = socket.getOutputStream();
        System.out.println("=================== waiting for data ===================");
        int nb = is.read();
        int res = nb * 98;
        os.write(res);
        socket.close();
        // we need the streams handlers
    }
}
