package se.group4.core;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//Exempel inmatning av ID mot databasen nedan
//http://localhost:8080/users?ID=500603-4268
//firstname: Johanna, lastname: Lennartsson


public class Server{

    private static UserHandler userHandler;
    private static UserDAO userDAO;

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
//        System.out.println(Thread.currentThread());
        try {
            BufferedInputStream input = new BufferedInputStream((socket.getInputStream()));

            Map<String,  URLHandler > routes = new HashMap<>();

            Request request = readRequest(input);

            boolean isHead = false;
            if (request != null) {


                if (request.getRequestType().equals("GET")) {
                    URLHandler handler = routes.get(request.getUrl());

                    if (handler == null) {
                        handler = new FileHandler();
                    }
                    Response response = handler.handleURL(request);
                    postHttpResponse(socket, response, isHead);

                }

                if(request.getRequestType().equals("POST")){
                    URLHandler handler = routes.get(request.getUrl());

                    if (handler == null) {
                        handler = new FileHandler();
                    }

                    postRequest(request.getBody());
                    Response response = handler.handleURL(request);

                    postHttpResponse(socket, response, isHead);
                    //Skicka en respons, men är det bara en vanlig response???
                }

                if(request.getRequestType().equals("HEAD")){
                    isHead = true;

                    URLHandler handler = routes.get(request.getUrl());

                    if (handler == null) {
                        handler = new FileHandler();
                    }
                    Response response = handler.handleURL(request);

                    //Input isHead needed
                    postHttpResponse(socket, response, isHead);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static String readLineHeaders(BufferedInputStream inputStream) throws IOException {
        final int MAX_READ = 4096;
        byte[] buffer = new byte[MAX_READ];
        int bytesRead = 0;
        while (bytesRead < MAX_READ) {
            buffer[bytesRead++] = (byte) inputStream.read();
            if (buffer[bytesRead - 1] == '\r') {
                buffer[bytesRead++] = (byte) inputStream.read();
                if( buffer[bytesRead - 1] == '\n') {
                   break;
                }
            }
        }
        return new String(buffer,0,bytesRead-2, StandardCharsets.UTF_8);
    }


    public static Request readRequest(BufferedInputStream input) throws IOException {
        Request request = new Request();

        while (true) {
            String headerLine = readLineHeaders(input);

            if (headerLine.startsWith("GET") || headerLine.startsWith("POST") || headerLine.startsWith("PUT") || headerLine.startsWith("DELETE")) {
                parseFirstHeaderLine(request, headerLine);
            }
            //Körs inte
            if (headerLine.startsWith("Content-Length")) {
                request.setContentLength(Integer.parseInt(headerLine.split(" ")[1]));
//                System.out.println("Have we set contentlength?  " + request.getContentLength());
            }

            if (headerLine.isEmpty()) {
                break;
            }
        }

        if (request.getRequestType().equals("POST")) {
            readBody(input, request);
        }
        return request;
    }


    private static void readBody(BufferedInputStream input, Request request) throws IOException {
        byte[] body = new byte[request.getContentLength()];
        int i = input.read(body);
        System.out.println("readbody: Actual: " + i + ", Expected: " + request.getContentLength());
        String bodyText = new String(body);
        System.out.println("readBody bodyText:   " + bodyText);
        request.setBody(bodyText);
    }

    private static void postRequest(String bodyIn) throws IOException {


//        int contentLength = readHeaderLines(input, url);

//        String bodyLine = new String(input.readNBytes(contentLength));

        String[] body = bodyIn.split("&");

        String id = body[0].substring(body[0].indexOf("=") + 1);
        String firstname = body[1].substring(body[1].indexOf("=") + 1);
        String lastname = body[2].substring(body[2].indexOf("=") + 1);

        userHandler.createAndAddUser(id,firstname,lastname);

    }


    private static void parseFirstHeaderLine (Request request, String headerLine) {
        String[] splitHeadline = headerLine.split(" ");

        request.setRequestType(splitHeadline[0]);
        System.out.println("parseFirstHeaderLine: Request type: " + request.getRequestType());
        request.setUrl(splitHeadline[1]);
        System.out.println("parseFirstHeaderLine: Url: " + request.getUrl());
        request.setHttpVersion(splitHeadline[2]);
        System.out.println("parseFirstHeaderLine: Http Version: " + request.getHttpVersion());

        if (splitHeadline[1].contains("?")) {

            request.setUrl(splitHeadline[1].split("\\?")[0]);
            System.out.println("parseFirstHeaderLine() if(?): Request setURL = " + splitHeadline[1].split("\\?")[0]);

            if (headerLine.startsWith("GET")) {
                String idNr = splitHeadline[1].split("\\?")[1].split("=")[1];
                UserDAO userDAO = new UserDAOWithJPAImpl();

                System.out.println("idnr = " +idNr);
                User userFromDatabase = userDAO.findUserById(idNr);
                String userAsJson = userFromDatabase.toString();
                request.setBody(userAsJson);
                System.out.println("User from database: " + userFromDatabase);

            }
        }
    }


    private static void postHttpResponse(Socket socket, Response response, boolean isHead) {  //kan alla svar, både filer och json, skickas som byte[]?
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
            if(!isHead) {
                var dataOut = new BufferedOutputStream(socket.getOutputStream());
                dataOut.write(response.getContent());
                dataOut.flush();
                socket.close();
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}

// Content length verkar fungera... Content type säger null