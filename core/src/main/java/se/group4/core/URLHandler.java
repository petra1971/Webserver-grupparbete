package se.group4.core;

@FunctionalInterface
public interface URLHandler {

    Response handleURL(Request request);
}
