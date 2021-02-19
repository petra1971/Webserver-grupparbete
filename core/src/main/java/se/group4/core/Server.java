package se.group4.core;

//import se.group4.core.HttpRequest (FRÅN IO: INNEHÅLLER ENBART URL)
//import se.group4.core.HttpResponse (FRÅN IO: INNEHÅLLER BYTE[] BODY OCH STRING HEADER SAMT LITE VIKTIGA METODER )
import se.group4.core.persistence.*;
//import se.group4.spi.PluginType;          //
//import se.group4.core.spi.UrlHandler;


//Imports från vanliga java bibliotek

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.Provider;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

public class Server {
//    private static HttpResponse httpResponse;
//    private static HttpRequest httpRequest;
//    private static Service serviceBooks;
//    private static BookDAO bookDAO;
//    private static Service serviceStats;
//    private static StatisticsDAO statisticsDAO;

private static Map<String,UrlHandler> route;

    public static void main(String[] args) {
        bookDAO = new BookDAOWithJPAImpl();
        serviceBooks = new Service(bookDAO);
        statisticsDAO = new StatisticsDAOWithJPAImpl();
        serviceStats = new Service(statisticsDAO);
        httpResponse = new HttpResponse();

        ExecutorService executorService = Executor.newCachedThreadPool();

        try {
            ServerSocket serverSocket = new ServerSocket(8080);

            createMap();

            while(true) {
                Socket socket = serverSocket.accept();
                executorService.execute(() -> handleConnection(socket));
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private static void createMap() {
        route = new HashMap<>();
        var loader = PluginLoader.findUrlHandlers();

        for(var handler : loader){
            route.put(handler.getClass().getAnnotation(PluginType.class).route(),handler);
        }
    }

    private static void handleConnection(Socket socket){
        try{
            BufferedInputStream input = new BufferedInputStream(socket.getInputStream());

            String headerline = readLine(input);

            String[] header = headerline.split(" ");

            boolean isHead = true;
            String url =header[1];

            switch(header[0]) {

                case "GET":
                    isHead=false;
                    readHeaderLines(input,url);
                    httpResponse = findRoute(url);
                    break;

                case "HEAD":
                    isHead = true;
                    readHeaderLines(input,url);
                    httpResponse = findRoute(url);
                    break;

                case "POST":
                    postRequest(input,url);
                    httpResponse.redirect("/postConfirmation.html");
                    break;
            }

            sendHTTP(socket,isHead,httpResponse);
            socket.close();


        }catch (IOException e){
            e.printStackTrace();
        }
    }

private static void sendHTTP(Socket socket, boolean isHead, HttpResponse httpResponse) throws IOException {
        PrintWriter output = new PrintWriter(socket.getOutputStream());
        output.print(httpResponse.getHeader());
        output.flush();

        if(!isHead){
            var dataOutput = new BufferedOutputStream(socket.getOutputStream());
            dataOutput.write(httpResponse.getBody());
            dataOutput.flush();
        }
}

private static void postRequest(BufferedInputStream input, String url)throws IOException {

        int contentLength = readHeaderLines(input,url);

        String bodyLine = new String(input.readNBytes(contentLength));

        String [] body = bodyLine.split("&");

        String id =body[1].
}
}
