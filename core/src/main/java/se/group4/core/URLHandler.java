package se.group4.core;

@FunctionalInterface
public interface URLHandler {

    Response readFromFile(Request request);

}
