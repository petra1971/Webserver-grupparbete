package se.group4.core;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
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

public class Server2 {


    public static void main(String[] args) {

        ExecutorService execServ = Executors.newCachedThreadPool();

        try {
            ServerSocket serverSocket = new ServerSocket(8080);
            System.out.println(Thread.currentThread());

            createMap();

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

            String headerLine = readLineHeaders(input);

            String[] header = headerLine.split(" ");

            //boolean isHead = true;

            String url = header[1];

            switch(header[0]){
                case "GET":
//                    isHead = false;
                    readHeaderLines(input, url);
                    httpResponse = findRoute(url);
                    break;

                case "POST":
                    postRequest(input, url);
                    httpResponse.redirect("/postConfirmation.html");
                    break;

                case "HEAD":
//                    isHead = true;
                    readHeaderLines(input, url);
                    httpResponse = findRoute(url);
                    break;

            }

            if (request != null) {
                if (request.getRequestType().equals("GET")) {
                    URLHandler handler = routes.get(request.getUrl());

                    if (handler == null) {
                        handler = new FileHandler();
                    }
                    Response response = handler.handleURL(request);
                    if(request.getUrl().equals("/users")){
                        String jsonBody = handleURLParamUltimate(request.getUrl());
                        System.out.println("jsonbody: " + jsonBody);
                        //URL parametrar, ställa frågor mot databas

                    }
                    if(response != null) {

                        postHttpResponse(socket, response);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void createMap() {

        route = new HashMap<>();
        var loader = PluginLoader.findUrlHandlers();

        for (var handler : loader) {
            route.put(handler.getClass().getAnnotation(PluginType.class).route(), handler);
        }
    }


    //OK METOD
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

    // OK METOD
    private static int readRequest(BufferedInputStream input, String url) throws IOException {
        int contentLength = 0;
        String headerLine = readLineHeaders(input);

        while (true) {

            if (headerLine.startsWith("Content-Length"))
                contentLength = Integer.parseInt(headerLine.split(" ")[1]);

//            if (headerLine.startsWith("User-Agent")) {
//                writeUserToDB(headerLine, url);
//            }

            if (headerLine.isEmpty()) {
                break;
            }
        }
        return contentLength;
    }

//    private static void writeUserToDB(String headerLine, String url) {
//
//        serviceStats.getStatistics().create(headerLine, url);
//    }



    private static void readBody(BufferedInputStream input, Request request) throws IOException {
        byte[] body = new byte[request.getContentLength()];
        int i = input.read(body);
        System.out.println("Actual: " + i + ", Expected: " + request.getContentLength());
        String bodyText = new String(body);
        System.out.println(bodyText);
    }

    private static void parseFirstHeaderLine(Request request, String headerLine) {
        String[] splitHeadline = headerLine.split(" ");

        request.setRequestType(splitHeadline[0]);
        System.out.println("Request type: " + request.getRequestType());
        if(splitHeadline[1].contains("?")){
            String key = splitHeadline[1].split("\\?")[1];
            request.setUrl(key);
            handleURLParamUltimate(key);
            System.out.println("Printas ?-tecknet ut??");
        }
        else{
            request.setUrl(splitHeadline[1]);
            System.out.println("Url: " + request.getUrl());
        }

        request.setHttpVersion(splitHeadline[2]);
        System.out.println("Http Version: " + request.getHttpVersion());


    }


    public static List<URLParameter> getParametersFromUrl2(String urlParameterString) {                     //Method returning List with UrlParameter instances
        System.out.println("Printar från getParametersFromUrl2" + urlParameterString);
        List<URLParameter> urlParameters = new ArrayList();
        String[] parameterPairs = urlParameterString.split("[&]");     //Får ut par av key och värde
        for(String parameterPair : parameterPairs) {
            String keyUrl = parameterPair.split("=")[0];                 //splitta på [=] och lägg in i en Map
            String valueUrl = parameterPair.split("=")[1];
            urlParameters.add(new URLParameter(keyUrl, valueUrl));
            System.out.println("In getParametersFromUrl2 - RequestClass Key: " + keyUrl + " Value: " + valueUrl);
        }
        return urlParameters;
    }

    //Exempel inmatning av ID mot databasen nedan
    //http://localhost:8080/users?ID=500603-4268
    //firstname: Johanna, lastname: Lennartsson

    private static String handleURLParamUltimate(String url) {
        List<URLParameter> listOfParameters = getParametersFromUrl2(url);
        System.out.println("Key:" + listOfParameters.get(0).getKey() +"\tValue:"+ listOfParameters.get(0).getValueUrl());

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("JPA");

        // Letar efter "500603-4268" i databasen och skriver ut personen
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        User u = em.find(User.class, listOfParameters.get(0).getValueUrl());
        System.out.println("Skriver ut personens information från databasen: " +u.toString());
        em.getTransaction().commit();

        String userInfoAsJson = u.toString();
        return userInfoAsJson;
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
}
