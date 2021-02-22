package se.group4.core;
//Testa att ta bort functionalinterface sen!
@FunctionalInterface
public interface URLHandler {

    Response readFromFile(Request request);

}
