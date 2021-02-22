package se.group4.core;

import se.group4.fileutils.FileReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;


public class FileHandler implements URLHandler {

    public Response handleURL(Request request) {
        Response response = new Response();
        try {
            File file = new File("web" + File.separator + request.getUrl());
            System.out.println("Filehandler filepath: " + request.getUrl());

            byte[] content = FileReader.readFromFile(file);                                 //Read file contents to bit-array

            if (content.length != 0) {                                                      //Kolla om filen finns, om inte returnera felkod 404
                String contentType = Files.probeContentType(file.toPath());                 //Find out content type
                response.setContent(content);
                System.out.println("Response content (in string form):" +response.getContent().toString());
                response.setContentType(Files.probeContentType(file.toPath()));
                System.out.println("Response content type: " +request.getContentType());
                response.setContentLength(content.length);
                System.out.println("Response Content Length: " +request.getContentLength());
                response.setStatusMessage("HTTP/1.1 200");
            } else {
                //Förutsatt att detta fungerar så ska vi printa 404 error här
                response.setStatusMessage("HTTP/1.1 400");
                response.setContentLength(0);
                String error = "Bad Request";
                response.setContent(error.getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public Response readFromFile(Request request) {
        Response fileResponse = null;
        try {
            File file = new File("web" + File.separator + request.getUrl());    //Create file url
            byte[] content = se.group4.fileutils.FileReader.readFromFile(file);        //Read file contents to bit-array
            String contentType = Files.probeContentType(file.toPath());                 //Find out content type
            int contentLength = content.length;
            fileResponse = new Response(content, contentType, contentLength);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileResponse;
    }


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