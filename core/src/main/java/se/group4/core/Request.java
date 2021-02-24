package se.group4.core;

import java.util.List;

public class Request {
    private String requestType;
    private String url;
    private String httpVersion;
    private String body;
    private int contentLength;

    public Request() {
    }

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

    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    public String getBody() { return body; }

    public void setBody(String body) { this.body = body; }
}