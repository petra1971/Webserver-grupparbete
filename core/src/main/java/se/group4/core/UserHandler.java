package se.group4.core;

import java.util.List;

public class UserHandler {

    public static void createAndAddUser(String id, String firstname, String lastname) {
        UserDAO pdao = new UserDAOWithJPAImpl();
        User u = new User(id, firstname, lastname);
        pdao.create(u);
    }

    public static List<User> getAllUsers(){
        UserDAO pdao = new UserDAOWithJPAImpl();
        return pdao.getAllUsers();
    }
}















//package se.group4.core;
//
//import java.io.IOException;
//import java.util.List;
//import com.sun.net.httpserver.HttpHandler;
//import com.sun.net.httpserver.HttpExchange;
//
//
//public class UserHandler implements HttpHandler {
//
//    public Response handleURL(HttpExchange exchange) {
//        Response response = null;
//        if(exchange.getRequestMethod().equals("GET")) {
//
//            if(user!=null) {
//                JsonConverter jsonConverter = new JsonConverter();
//                var jsonResponse = jsonConverter.convertToJson(user);
//                response.setContentType("application/json"); //ska det vara nån annan typ?
//                byte[] jsonBytes = jsonResponse.getBytes();
//                response.setContentLength(jsonBytes.length);
//                response.setContent(jsonBytes);
//                response.setStatusMessage("HTTP/1.1 200 OK");
//            } else
//            response.setStatusMessage("HTTP/1.1 404");
//            response.setContentLength(0);
//            String error = "I am error";
//            byte[] errorBytes = error.getBytes();
//            response.setContent(errorBytes);
//        }
//        else if(exchange.getRequestMethod().equals("POST")){
//
//            UserDAO userDAO = new UserDAOWithJPAImpl();
//            List<User> user = userDAO.getByName("namn");
//
//        if(user!=null) {
//            JsonConverter jsonConverter = new JsonConverter();
//            var jsonResponse = jsonConverter.convertToJson(user);
//            response.setContentType("application/json"); //ska det vara nån annan typ?
//            byte[] jsonBytes = jsonResponse.getBytes();
//            response.setContentLength(jsonBytes.length);
//            response.setContent(jsonBytes);
//            response.setStatusMessage("HTTP/1.1 200 OK");
//        }
//        } else{
//            response.setStatusMessage("HTTP/1.1 404");
//            response.setContentLength(0);
//            String error = "I am error";
//            byte[] errorBytes = error.getBytes();
//            response.setContent(errorBytes);
//    }
//
//        return response;
//    }
//
//    @Override
//    public void handle(HttpExchange exchange) throws IOException {
//
//    }
//}
