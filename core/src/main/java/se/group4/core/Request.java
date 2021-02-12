package se.group4.core;

public class Request {
    private String requestType;
    private String url;
    private String httpVersion;
    private String contectType;
    private int contentLength;

    //First check for URL
    //If not url check for file
    //If no file send 404 error

    public Request() {
    }

//    public Request(String requestType, String url, String httpVersion, String contectType, int contentLength) {
//        this.requestType = requestType;
//        this.url = url;
//        this.httpVersion = httpVersion;
//        this.contectType = contectType;
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

    public void setHttpVersion(String httpVersion) { this.httpVersion = httpVersion;
    }

    public String getContectType() {
        return contectType;
    }

    public void setContectType(String contectType) {
        this.contectType = contectType;
    }

    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }
}
