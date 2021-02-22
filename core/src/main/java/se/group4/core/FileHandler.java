package se.group4.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;

public class FileHandler implements URLHandler {

        public Response readFromFile(Request request) {
            Response fileResponse = null;
            try {
                File file = new File("web" + File.separator + request.getUrl());    //Create file url
                byte[] content = se.group4.fileutils.FileHandler.readFromFile(file);        //Read file contents to bit-array
                String contentType = Files.probeContentType(file.toPath());                 //Find out content type
                int contentLength = content.length;
                fileResponse = new Response(content, contentType, contentLength);
                System.out.println("Detta är content:  " +content);

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

//    public Response readFromFile(String fname, String lname) {
//        Response fileResponse = null;
//        try {
//            File file = new File("web/showperson");                 //Create file url
//            byte[] content = "html " + "h1"+ fname + "h1" + lname;        //Read file contents to bit-array
//            String contentType = "text/html";                               //Find out content type
//            int contentLength = content.length;
//            fileResponse = new Response(content, contentType, contentLength);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return fileResponse;
//    }
}

