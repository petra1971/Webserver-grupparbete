package se.group4.core;

public interface UserDAO{

    void create (String id, String firstname, String lastname);
    User findUserById(String id);
}