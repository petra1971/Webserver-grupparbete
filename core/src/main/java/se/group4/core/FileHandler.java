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

            byte[] content = FileReader.readFromFile(file);                                 //Read file contents to bit-array
            StringBuilder stringBuilder = new StringBuilder();
            String usefulString;
            //stringBuilder.append(usefulString);
            if (request.getRequestType().contains("GET")) {                                 //Kommenterar vi bort, så funkar allt, förutom users?"ID=xxxxxxx-xxxx
                stringBuilder.append(httpResponse.getBody());
                usefulString = stringBuilder.toString();
                content = usefulString.getBytes(StandardCharsets.UTF_8);
                System.out.println("Usefulstring= " + usefulString);
            }

            if ((content.length != 0) && (!content.equals("null"))){
            //Kolla om filen finns, om inte returnera felkod 404
                String contentType = Files.probeContentType(file.toPath());                 //Find out content type
                response.setContent(content);
                System.out.println("Response content (in string form):" +response.getContent().toString());
                response.setContentType(Files.probeContentType(file.toPath()));
                System.out.println("Response content type: " +contentType);
                response.setContentLength(content.length);
                System.out.println("Response Content Length: " +response.getContentLength());
                response.setStatusMessage("HTTP/1.1 200");
            } else {
                System.out.println("Print error:" +pageNotFound());
                response.setContent(pageNotFound().getBytes(StandardCharsets.UTF_8));
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

    private String printHeaderResponse(String contentType){
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.1 200 OK\r\n");
        sb.append("Content-Length:" + this.body.length + "\r\n");
        sb.append("Content-Type:" + contentType + "\r\n");
        sb.append("\r\n");

        return sb.toString();
    }//Motsvarande printHeaderLines finns i Server.postHttpResponse


    //Metoden funkar 23/2 kl 10:13... Skrivs ut om sidan ej finns
    public String pageNotFound() {
        FileHandler fileHandler = new FileHandler();
        setBody(fileHandler.readFromFile(new File(".." + File.separator + "web" + File.separator + "404.html")));
        StringBuilder sb = new StringBuilder();

        sb.append("HTTP/1.1 404 Not Found\r\n");
        sb.append("Content-Length:" +this.body.length +"\r\n");
        sb.append("Content-Type: text/html\r\n");
        sb.append("\r\n");

        return sb.toString();
    }

//    public Response handleURL(Request request) {
//        Response fileResponse = null;
//        try {
//            File file = new File("web" + File.separator + request.getUrl());    //Create file url
//            byte[] content = se.group4.fileutils.FileReader.readFromFile(file);        //Read file contents to bit-array
//            String contentType = Files.probeContentType(file.toPath());                 //Find out content type
//            int contentLength = content.length;
//            fileResponse = new Response(content, contentType, contentLength);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return fileResponse;
//    }


    public static byte[] readFromFile(File file) {
        byte[] content = new byte[0];
        System.out.println("Does file exists: " + file.exists());
        if (file.exists() && file.canRead()) {
            try (FileInputStream fileInputStream = new FileInputStream(file)) {
                content = new byte[(int) file.length()];
                int count = fileInputStream.read(content);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return content;
    }
}