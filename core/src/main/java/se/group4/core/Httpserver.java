//package se.group4.core;
//
//import com.sun.net.httpserver.*;
//import java.io.*;
//import java.net.InetSocketAddress;
//
//public class Httpserver{
//
//    public static void main(String[] args) throws IOException {
//        HttpServer server = HttpServer.create(new InetSocketAddress(8000),0);
//        HttpContext context = server.createContext("/login");
//        context.setHandler(Httpserver::handleRequest);
//        server.start();
//    }
//
//    private static void handleRequest(HttpExchange exchange) throws IOException {
//        String response = "Hi there!";
//        exchange.sendResponseHeaders(200, response.getBytes().length);
//        OutputStream os = exchange.getResponseBody();
//        os.write(response.getBytes());
//        os.close();
//    }
//
//}
