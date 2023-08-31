package com.company;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class SimpleClient {
    public static void main(String[] main) throws IOException {
        Socket client_socket = new Socket("localhost", 1234);
        InputStream is = client_socket.getInputStream();
        OutputStream os = client_socket.getOutputStream();
        Scanner scanner = new Scanner(System.in);
        System.out.println("===================== pray for number ====================");
        int nb = scanner.nextInt();
        os.write(nb);
        int response = is.read();
        System.out.println("result = " + response);
    }
}
