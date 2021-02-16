package se.group4.core;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;


public class TestHandler implements HttpHandler{
        FileHandler filehandler = new FileHandler();
    public void handle(HttpExchange exchange) throws IOException {
        //File file = new File(NameConstants.FILES + File.separator + handle(exchange));
        String requestMethod = exchange.getRequestMethod();

        if(requestMethod == "HEAD"){
            headHandler(exchange);
        }

        else if(requestMethod == "GET"){
            headHandler(exchange);
            bodyHandler(exchange);
        }

        else if(requestMethod == "POST"){
            System.out.println("This is a POST");
            headHandler(exchange);
            bodyHandler(exchange);

        }
        else{
            System.out.println("Error 404");
            //exchange.sendResponseHeaders(404, file.length());

        }
    }

    //headhandler returns a list of all user information from the database in json-format
    private void headHandler(HttpExchange exchange) {
        //Creates jsonfile with all User information
        UserDAO uDAO = new UserDAOWithJPAImpl();
        var list = uDAO.getAllUsers();
        JsonConverter converter = new JsonConverter();
        String json = converter.convertToJson(list);
        exchange.getResponseHeaders().set("Content-type", "application/json");
        exchange.getResponseHeaders().set("Content-Length", String.valueOf(json.length()));

            try {
                exchange.sendResponseHeaders(43, json.length());
            } catch (IOException e) {
                e.printStackTrace();
            }
    }


    public void bodyHandler(HttpExchange exchange){
        //Creates jsonfile with all User information
        UserDAO uDAO = new UserDAOWithJPAImpl();
        var list = uDAO.getAllUsers();
        JsonConverter converter = new JsonConverter();
        String json = converter.convertToJson(list);
        
        OutputStream streamOut = exchange.getResponseBody();
        try {
            streamOut.write(json.getBytes());
            streamOut.flush();
            streamOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }




    }

    public void postHandler(HttpExchange exchange) throws IOException {
        //Creates jsonfile with all User information
        UserDAO uDAO = new UserDAOWithJPAImpl();
        var list = uDAO.getAllUsers();
        JsonConverter converter = new JsonConverter();
        String json = converter.convertToJson(list);

        String body = bodyToString(exchange);

    }

    public String bodyToString(HttpExchange exchange) throws IOException {
        BufferedReader streamInput = new BufferedReader(new InputStreamReader(exchange.getRequestBody(),StandardCharsets.UTF_8));
        StringBuilder in =new StringBuilder();
        String input;

        while ((input = streamInput.readLine())!=null){
           // String string = "?ID=19950904-7777&firstName=John&lastName=Roth";
            
            if(input.contains("[?]")){
                
                int indexQuestionMark = input.indexOf("[?]");
                ArrayList<Integer> indexListAnd = new ArrayList<>();
                ArrayList<Integer> indexListEquals = new ArrayList<>();
                
                for(int i=0 ;input.indexOf("[&]",i)!= -1; i++){
                    
                    indexListAnd.add(input.indexOf("[&]",i));
                    i = input.indexOf("[&]",i);
                    
                }
                for(int i=0 ;input.indexOf("[=]",i)!= -1; i++){
                    
                    indexListEquals.add(input.indexOf("[=]",i));
                    i = input.indexOf("[=]",i);
                    
                }
                
                System.out.println("And List: "+indexListAnd + "   Equal List: " + indexListEquals);
                for(int j = 0; j <= 1; j++ ) {
                    if (j == 0) {
                        //Gets value inbetween "?" and "="
                        System.out.println(input.substring(indexQuestionMark + 1, indexListEquals.get(j)));
                        //Get value inbetween first "=" and  first "&"
                        System.out.println(input.substring(indexListEquals.get(j) + 1, indexListAnd.get(j)));
                        //Get value inbetween first "&" and second "="
                        System.out.println(input.substring(indexListAnd.get(j) + 1, indexListEquals.get(j + 1)));
                    } else {
                        //Gets input between second "=" and second "&"
                        System.out.println(input.substring(indexListEquals.get(j) + 1, indexListAnd.get(j)));
                        //Gets input between second "&" and third "="
                        System.out.println(input.substring(indexListAnd.get(j) + 1, indexListEquals.get(j + 1)));
                        //Gets input between third "=" and end of input
                        System.out.println(input.substring(indexListEquals.get(2) + 1));
                    }
                }

            }
        }
        return "klar";
    }
}