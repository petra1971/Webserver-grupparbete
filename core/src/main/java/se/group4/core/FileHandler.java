package se.group4.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileHandler implements URLHandler {

        public Response readFromFile(Request request) {
            Response fileResponse = null;
            try {
                File file = new File("web" + File.separator + request.getUrl());  //Create file url
                byte[] content = se.group4.fileutils.FileHandler.readFromFile(file); //Read file contents to bit-array
                String contentType = Files.probeContentType(file.toPath()); //Find out content type
                int contentLength = content.length;
                fileResponse = new Response(content, contentType, contentLength);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return fileResponse;
        }
    }
