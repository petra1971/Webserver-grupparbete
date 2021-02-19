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
//        System.out.println(Thread.currentThread());
        try {
            BufferedInputStream input = new BufferedInputStream((socket.getInputStream()));

            Map<String,  URLHandler > routes = new HashMap<>();

            Request request = readRequest(input);

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



    public static String readLineHeaders(BufferedInputStream inputStream) throws IOException {
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


    private static Request readRequest(BufferedInputStream input) throws IOException {
        Request request = new Request();

        while (true) {
            String headerLine = readLineHeaders(input);

            if (headerLine.startsWith("GET") || headerLine.startsWith("POST") || headerLine.startsWith("PUT") || headerLine.startsWith("DELETE")) {
                parseFirstHeaderLine(request, headerLine);
            }

            if (headerLine.startsWith("Content-Length"))
                request.setContentLength(Integer.parseInt(headerLine.split(" ")[1]));

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




//    private static void handleURLParameters(String url){
//        //Separates key and value and returns them as an URLParameter object (IS TESTED AND WORKING CORRECTLY)
//        List<URLParameter> listOfParameters = getParametersFromUrl2(url);
//        System.out.println("Key:" + listOfParameters.get(0).getKey() +"\tValue:"+ listOfParameters.get(0).getValueUrl());
//        getUserInformationFromKey(listOfParameters.get(0).getValueUrl());
//        //Create method that fetches object from database and returns it as user object
//    }
//    private static void getUserInformationFromKey(String idNumber) {
//        EntityManagerFactory emf = Persistence.createEntityManagerFactory("JPA");
//
//        // Letar efter "500603-4268" i databasen och skriver ut personen
//        EntityManager em = emf.createEntityManager();
//        em.getTransaction().begin();
//        User u = em.find(User.class, idNumber);
//        System.out.println("Skriver ut personens information från databasen: " +u.toString());
//        em.getTransaction().commit();
//
//        getUserInformationAsJson(u);
//    }
//
//    private static void getUserInformationAsJson(User u){
//        String userInfoAsJson = u.toString();
//        System.out.println("UserInfoAsJson: " + userInfoAsJson);
//        //JsonConverter jsonConverter = new JsonConverter();
//        //System.out.println("Jsonconvert gives: " + jsonConverter.convertToJson(u));
//    }




////Text som låg i readheaders



//                if(headerLine.contains("Content-Type")){
////                request.setContentType(headerLine.split("[:]")[1]);

//            if(request.getRequestType().equals("POST")){
////                while(!headerLine.contains("</HTML>")){
//                    completeRequest.concat(headerLine);
//                    System.out.println(""+completeRequest);
//                    headerLine = readLineHeaders(input);
////                }
//                System.out.println("HEADERLINE: " + headerLine);
//            }


//            System.out.println("Printar completeRequest:  " +completeRequest);


//            System.out.println("Whole header: " + headerLine +"\n"+
//            "splitheadline[0] = " + splitHeadline[0] +"\n"+
//                    "splitheadline[1] = "+ splitHeadline[1]+
//                    "\nsplitheadline[2] = "+splitHeadline[2]);


//            request.setRequestType(headerLine.split(" ")[0]);
//            request.setUrl(headerLine.split(" ")[1]);
//            request.setHttpVersion(headerLine.split(" ")[2]);
//        }

//        while (headerLine != null) {
//            headerLine = input.readLine();

//            if(headerLine.contains("Content-Type")){
//                request.setContentType(headerLine.split("[:]")[1]);
//            }
//request.setHttpVersion(split[2]);
//}
//        return request;
//    }