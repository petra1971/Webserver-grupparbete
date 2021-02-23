package se.group4.core;

import se.group4.fileutils.FileReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;


public class FileHandler implements URLHandler {

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    private byte[] body;

    public Response handleURL(Request request) {
        Response response = new Response();
        HttpResponse httpResponse = new HttpResponse();

        try {
            File file = new File("web" + File.separator + request.getUrl());
            System.out.println("Filehandler filepath: " + request.getUrl());

            byte[] content = FileReader.readFromFile(file);                                   //Read file contents to bit-array
            StringBuilder stringBuilder = new StringBuilder();
            String usefulString;

//            if (request.getRequestType().contains("GET")) {                                 //Kommenterar vi bort, så funkar allt, förutom users?"ID=xxxxxxx-xxxx
//                stringBuilder.append(httpResponse.getBody());
//                usefulString = stringBuilder.toString();
//                content = usefulString.getBytes(StandardCharsets.UTF_8);
//                System.out.println("Usefulstring= " + usefulString);
//            }
//            if (!httpResponse.getBody().equals(null)) {
//                stringBuilder.append(httpResponse.getBody());
//
//            }


            if (content.length != 0)  {                                                                        //Kolla om filen finns, om inte returnera felkod 404
                String contentType = Files.probeContentType(file.toPath());                                 //Find out content type
                response.setContent(content);
                System.out.println("Response content (in string form):" + response.getContent().toString());
                response.setContentType(Files.probeContentType(file.toPath()));
                System.out.println("Response content type: " + contentType);
                response.setContentLength(content.length);
                System.out.println("Response Content Length: " + response.getContentLength());
                response.setStatusMessage("HTTP/1.1 200");
            }else if(request.getUrl().contains("/users")){
                System.out.println("Get body: "+request.getBody());
                response.setContent(request.getBody().getBytes());
                response.setContentType("application/json");
                response.setContentLength(request.getContentLength());
                response.setStatusMessage("HTTP/1.1 200");
            }else{

                System.out.println("Print error:");
                FileReader fileReader = new FileReader();
                setBody(fileReader.readFromFile(new File("web" + File.separator + "/404.html")));
                String contentType = "text/html";
                response.setContent(body);
                response.setContentType(contentType);
                System.out.println("Response content type: " + contentType);
                response.setContentLength(body.length);
                System.out.println("Response Content Length: " + response.getContentLength());
                response.setStatusMessage("HTTP/1.1 404");
//                System.out.println("Raden under print error:  " +new String(pageNotFound().getBytes(StandardCharsets.UTF_8)));
//                response.setContent(pageNotFound().getBytes(StandardCharsets.UTF_8));
                //Förutsatt att detta fungerar så ska vi printa 404 error här
//                response.setStatusMessage("HTTP/1.1 400");
//                response.setContentLength(0);
//                String error = "Bad Request";
//                response.setContent(error.getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}

    //Metoden funkar 23/2 kl 10:13... Skrivs ut om sidan ej finns
//    public void pageNotFound() {
//        FileReader fileReader = new FileReader();
//        setBody(fileReader.readFromFile(new File( "web" + File.separator + "/404.html")));
////        StringBuilder sb = new StringBuilder();
////
////        sb.append("HTTP/1.1 404 Not Found\r\n");
////        sb.append("Content-Length:" +this.body.length +"\r\n");
////        sb.append("Content-Type: text/html\r\n");
////
////        return sb.toString();
//    }
//}






//    private String printHeaderResponse(String contentType){
//        StringBuilder sb = new StringBuilder();
//        sb.append("HTTP/1.1 200 OK\r\n");
//        sb.append("Content-Length:" + this.body.length + "\r\n");
//        sb.append("Content-Type:" + contentType + "\r\n");
//        sb.append("\r\n");
//
//        return sb.toString();
//    }
//




//package se.group4.core;
//
//import se.group4.fileutils.FileReader;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//
//
//public class FileHandler implements URLHandler {
//
//    public byte[] getBody() {
//        return body;
//    }
//
//    public void setBody(byte[] body) {
//        this.body = body;
//    }
//
//    private byte[] body;
//
//    public Response handleURL(Request request) {
//        Response response = new Response();
//        HttpResponse httpResponse = new HttpResponse();
//
//        try {
//            File file = new File("web" + File.separator + request.getUrl());
//            System.out.println("Filehandler filepath: " + request.getUrl());
//
//            byte[] content = FileReader.readFromFile(file);                                 //Read file contents to bit-array
//            StringBuilder stringBuilder = new StringBuilder();
//            String usefulString;
//
//            if ((request.getRequestType().contains("GET")) && (!request.getBody().equals("0"))) {
//                response.setContentLength(request.getBody().length());
//                System.out.println("IF-sats med getBody " +request.getBody());
//
//            }
////            if (request.getUrl().contains("/users")) {
////                System.out.println("IF-sats med getURL 2 " +request.getUrl());                                     //Kommenterar vi bort, så funkar allt, förutom http://localhost:8080/users?ID=500603-4268
////                stringBuilder.append(httpResponse.getBody());
////                usefulString = stringBuilder.toString();
////                content = usefulString.getBytes(StandardCharsets.UTF_8);
////                System.out.println("Usefulstring= " + usefulString);
////            }
////            if (!httpResponse.getBody().equals(null)) {
////                stringBuilder.append(httpResponse.getBody());
////
////            }
//
//
//            if (content.length != 0){
//            //Kolla om filen finns, om inte returnera felkod 404
//                String contentType = Files.probeContentType(file.toPath());//Find out content type
//                response.setContent(content);
//                System.out.println("Response content (in string form):" +response.getContent().toString());
//                response.setContentType(Files.probeContentType(file.toPath()));
//                System.out.println("Response content type: " +contentType);
//                response.setContentLength(content.length);
//                System.out.println("Response Content Length: " +response.getContentLength());
//                response.setStatusMessage("HTTP/1.1 200");
//            } else {
//                System.out.println("Print error:" );
//                System.out.println("Fixa 404 filen här på rad 66 filehandler");
//                // response.setContent(pageNotFound().getBytes(StandardCharsets.UTF_8));
//                //Förutsatt att detta fungerar så ska vi printa 404 error här
////                response.setStatusMessage("HTTP/1.1 400");
////                response.setContentLength(0);
////                String error = "Bad Request";
////                response.setContent(error.getBytes());
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return response;
//    }
//
//    private String printHeaderResponse(String contentType){
//        StringBuilder sb = new StringBuilder();
//        sb.append("HTTP/1.1 200 OK\r\n");
//        sb.append("Content-Length:" + this.body.length + "\r\n");
//        sb.append("Content-Type:" + contentType + "\r\n");
//        sb.append("\r\n");
//
//        return sb.toString();
//    }//Motsvarande printHeaderLines finns i Server.postHttpResponse
//
//
//    //Metoden funkar 23/2 kl 10:13... Skrivs ut om sidan ej finns
//    public void pageNotFound() {
//        FileReader fileReader = new FileReader();
//        System.out.println("Second read from file...");
//        setBody(fileReader.readFromFile(new File( "web" + File.separator + "404.html")));
////        System.out.println("pageNotFound metod efter setBody");
//////this.body.length +
////        StringBuilder sb = new StringBuilder();
////
////        sb.append("HTTP/1.1 404 Not Found\r\n");
////        sb.append("Content-Length: " +"\r\n");
////        sb.append("Content-Type: text/html\r\n");
////        sb.append("\r\n");
////
////        int bodyLength = sb.length();
////        System.out.println("pagenotfound: only bodylength: " + bodyLength);
////        bodyLength = bodyLength + String.valueOf(bodyLength).length();
////        System.out.println("pagenotfound with added value:" + bodyLength);
////
////        return sb.toString();
//    }
//
////    public Response handleURL(Request request) {
////        Response fileResponse = null;
////        try {
////            File file = new File("web" + File.separator + request.getUrl());    //Create file url
////            byte[] content = se.group4.fileutils.FileReader.readFromFile(file);        //Read file contents to bit-array
////            String contentType = Files.probeContentType(file.toPath());                 //Find out content type
////            int contentLength = content.length;
////            fileResponse = new Response(content, contentType, contentLength);
////
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
////        return fileResponse;
////    }
//
//}