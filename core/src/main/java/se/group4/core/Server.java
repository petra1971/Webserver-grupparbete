package se.group4.core;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import se.group4.core.models.Todo;
import se.group4.core.models.Todos;
import se.group4.fileutils.FileReader;

public class Server {

    public static void main(String[] args) {

        ExecutorService execServ = Executors.newCachedThreadPool();

        try {
            ServerSocket serverSocket = new ServerSocket(8080);
            System.out.println(Thread.currentThread());
            while (true) {
                Socket socket = serverSocket.accept();

                execServ.execute(()-> handleConnection(socket));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    private static void handleConnection(Socket socket) {
//        System.out.println(Thread.currentThread());
//
//        try {
//            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//
//            while (true) {
//                String headerLine = input.readLine();
//                System.out.println(headerLine);
//                if (headerLine.isEmpty())
//                    break;
//            }
//
//            var output = new PrintWriter(socket.getOutputStream());
//            String page = """
//                    <html>
//                    <head>
//                        <title>Hello World!</title>
//                    </head>
//                    <body>
//                    <h1 style="color:red";>Hello there</h1>
//                    <div style="font-size:50px";>First page</div>
//                    </body>
//                    </html>""";
//
//            output.println("HTTP/1.1 200 OK");
//            output.println("Content-Length:" + page.getBytes().length);
//            output.println("Content-Type:text/html");  //application/json
//            output.println("");
//            output.print(page);
//
//            output.flush();
//            socket.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    private static void handleConnection(Socket socket) {
        System.out.println(Thread.currentThread());
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String url = readHeaders(input);

            var output = new PrintWriter(socket.getOutputStream());

            File file = new File("web" + File.separator + url);
            byte[] page = FileReader.readFromFile(file);

            String contentType = Files.probeContentType(file.toPath());

            output.println("HTTP/1.1 200 OK");
            output.println("Content-Length:" + page.length);
            output.println("Content-Type:"+contentType);  //application/json
            output.println("");
            //output.print(page);
            output.flush();

            var dataOut = new BufferedOutputStream(socket.getOutputStream());
            dataOut.write(page);
            dataOut.flush();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String readHeaders(BufferedReader input) throws IOException {
        String requestedUrl = "";
        while (true) {
            String headerLine = input.readLine();
            if( headerLine.startsWith("GET"))
            {
                requestedUrl = headerLine.split(" ")[1];
            }
            System.out.println(headerLine);
            if (headerLine.isEmpty())
                break;
        }
        return requestedUrl;
    }

//    private static void createJsonResponse() {
//
//        var todos = new Todos();
//        todos.todos = new ArrayList<>();
//        todos.todos.add(new Todo("1", "Todo 1", false));
//        todos.todos.add(new Todo("2", "Todo 2", false));
//
//        JsonConverter converter = new JsonConverter();
//
//        var json = converter.convertToJson(todos);
//        System.out.println(json);
//    }
}

