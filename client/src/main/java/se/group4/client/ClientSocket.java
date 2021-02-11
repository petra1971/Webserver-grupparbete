package se.group4.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientSocket {


    public static void main(String[] args) {

        try {
            Socket socket = new Socket("localhost", 8080);

            var output = new PrintWriter(socket.getOutputStream());
            output.println("Hello from client");
            output.flush();
            var input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String lineFromResponse;
            while((lineFromResponse = input.readLine()) != null)
                System.out.println(lineFromResponse);

            output.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



