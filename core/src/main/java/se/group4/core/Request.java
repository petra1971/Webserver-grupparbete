package se.group4.core;

import java.util.List;

public class Request {
    private String requestType;
    private String url;
    private String httpVersion;
    private String contentType;
    private String body;
    private int contentLength;
    private List<String> variablesToBeUpdated;


    //First check for URL
    //If not url check for file
    //If no file send 404 error

    public Request() {
    }

//    public Request(String requestType, String url, String httpVersion, String contentType, int contentLength) {
//        this.requestType = requestType;
//        this.url = url;
//        this.httpVersion = httpVersion;
//        this.contentType = contentType;
//        this.contentLength = contentLength;
//    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public void setHttpVersion(String httpVersion) { this.httpVersion = httpVersion;}

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    public String getBody() { return body; }

    public void setBody(String body) { this.body = body; }
}
