package se.group4.core;

import se.group4.fileutils.FileReader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;


public class FileHandler implements URLHandler {

    public void setBody(byte[] body) {
        this.body = body;
    }

    private byte[] body;

    public Response handleURL(Request request) {
        Response response = new Response();

        try {
            File file = new File("web" + File.separator + request.getUrl());

            byte[] content = FileReader.readFromFile(file);

            if (content.length != 0)  {
                String contentType = Files.probeContentType(file.toPath());
                response.setContent(content);
                response.setContentType(Files.probeContentType(file.toPath()));
                System.out.println("Content Type: " + contentType);
                response.setContentLength(content.length);
                System.out.println("Content Length: " + response.getContentLength());
                response.setStatusMessage("HTTP/1.1 200");

            }else if(request.getUrl().contains("/users")){
                System.out.println("Get body: " +request.getBody());
                response.setContent(request.getBody().getBytes());
                response.setContentType("application/json");
                response.setContentLength(request.getContentLength());


            }else{
                FileReader fileReader = new FileReader();
                setBody(fileReader.readFromFile(new File("web" + File.separator + "/404.html")));
                String contentType = "text/html";
                response.setContent(body);
                response.setContentType(contentType);
                System.out.println("Content Type: " + contentType);
                response.setContentLength(body.length);
                System.out.println(" " + response.getContentLength());
                response.setStatusMessage("HTTP/1.1 404");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}