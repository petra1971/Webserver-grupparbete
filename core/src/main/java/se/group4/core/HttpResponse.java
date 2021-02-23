package se.group4.core;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class HttpResponse {
    private String header;
    private byte[] body;
    private byte[] content;
    private String contentType;


//    public HttpResponse(String header, byte[] body) {
//        this.header = header;
//        this.body = body;
//    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public void jsonResponse(String json) {
        setBody(json.getBytes(StandardCharsets.UTF_8));
//        setHeader(printHeaderResponse("application/json"));
    }

//    private String printHeaderResponse(String contentType){
//        StringBuilder sb = new StringBuilder();
//            sb.append("HTTP/1.1 200 OK\r\n");
//            sb.append("Content-Length:" + this.body.length + "\r\n");
//            sb.append("Content-Type:" + contentType + "\r\n");
//            sb.append("\r\n");
//
//            return sb.toString();
//    }
//    //Motsvarande printHeaderLines finns i Server.postHttpResponse

//    public String pageNotFound() {
//        FileHandler fileHandler = new FileHandler();
//        setBody(fileHandler.readFromFile(new File(".." + File.separator + "web" + File.separator + "404.html")));
//        StringBuilder sb = new StringBuilder();
//
//        sb.append("HTTP/1.1 404 Not Found\r\n");
//        sb.append("Content-Length:" +this.body.length +"\r\n");
//        sb.append("Content-Type: text/html\r\n");
//        sb.append("\r\n");
//
//        return sb.toString();
//    }

}
