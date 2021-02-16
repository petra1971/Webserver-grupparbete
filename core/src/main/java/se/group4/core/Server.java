package se.group4.core;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//Enums  - Efter möte med Martin

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

    private static void handleConnection(Socket socket) {
        System.out.println(Thread.currentThread());
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Request request = readHeaders(input);

            if (request != null) {
                if (request.getRequestType().equals("GET")) {
                    URLHandler handler = routes.get(request.getUrl());

                    if (handler == null) {
                        handler = new FileHandler();
                    }
                    response = handler.handleURL(request);
                    postHttpResponse(socket, response);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public String readLine(BufferedInputStream inputStream) throws IOException {
        final int MAX_READ = 4096;
        byte[] buffer = new byte[MAX_READ];
        int bytesRead = 0;
        while (bytesRead < MAX_READ) {
            buffer[bytesRead++] = (byte) inputStream.read();
            if (buffer[bytesRead - 1] == '\r') {
                buffer[bytesRead++] = (byte) inputStream.read();
                if( buffer[bytesRead - 1] == '\n') {
                    //Här ska det försökas läsas från body
                    break;
                }
            }
        }
        return new String(buffer,0,bytesRead-2, StandardCharsets.UTF_8);
    }


    private static Request readHeaders(BufferedReader input) throws IOException {
        Request request = new Request();
        String headerLine = input.readLine();
        if(headerLine != null) {
            String[] splitHeadline = headerLine.split(" ");

            request.setRequestType(splitHeadline[0]);
            request.setUrl(splitHeadline[1]);
            request.setHttpVersion(splitHeadline[2]);

            String completeRequest = "";
            headerLine = input.readLine();

            if(request.getRequestType().equals("POST")){
                while(!headerLine.contains("</HTML>")){
                    completeRequest.concat(headerLine);
                    headerLine = input.readLine();
                }
            }

            System.out.println("Printar completeRequest:  " +completeRequest);




            //System.out.println("Whole header: " + headerLine +"\n"+
//            "splitheadline[0] = " + splitHeadline[0] +"\n"+
//                    "splitheadline[1] = "+ splitHeadline[1]+
//                    "\nsplitheadline[2] = "+splitHeadline[2]);


//            request.setRequestType(headerLine.split(" ")[0]);
//            request.setUrl(headerLine.split(" ")[1]);
//            request.setHttpVersion(headerLine.split(" ")[2]);
        }
//        while (headerLine != null) {
//            headerLine = input.readLine();

//            if(headerLine.contains("Content-Type")){
//                request.setContentType(headerLine.split("[:]")[1]);
//            }
            //request.setHttpVersion(split[2]);
        //}
        return request;
    }


    private static void postHttpResponse(Socket socket, Response response) {  //kan alla svar, både filer och json, skickas som byte[]?
        //Lägg detta i Response-klassen eventuellt
        try {
            var output = new PrintWriter(socket.getOutputStream());

            //Print header
            output.println("HTTP/1.1 200 OK");
            output.println("Content-Length:" + response.getContent().length);
            output.println("Content-Type:" + response.getContentType());
            output.println("");
            output.flush();

            //Print body/file-contents
            var dataOut = new BufferedOutputStream(socket.getOutputStream());
            dataOut.write(response.getContent());
            dataOut.flush();
            socket.close();

        } catch(IOException e) {
            e.printStackTrace();
        }
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








////Radera


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




// Låg i handleconnection

//            var output = new PrintWriter(socket.getOutputStream());
//
//            File file = new File("web" + File.separator + url);
//            byte[] page = FileReader.readFromFile(file);
//
//            String contentType = Files.probeContentType(file.toPath());
//
//            output.println("HTTP/1.1 200 OK");
//            output.println("Content-Length:" + page.length);
//            output.println("Content-Type:"+contentType);  //application/json
//            output.println("");
//            //output.print(page);
//            output.flush();
//
//            var dataOut = new BufferedOutputStream(socket.getOutputStream());
//            dataOut.write(page);
//            dataOut.flush();
//            socket.close();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }