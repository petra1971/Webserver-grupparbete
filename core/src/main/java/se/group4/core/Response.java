package se.group4.core;

public class Response {
    private byte[] content;
    private String contentType;
    int contentLength;
    String statusMessage; //"HTTP/1.1 200 OK"//error

    public Response() {
    }

    public Response(byte[] content, String contentType, int contentLength) {
        this.contentType = contentType;
        this.contentLength = contentLength;
        this.content = content;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

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

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
}