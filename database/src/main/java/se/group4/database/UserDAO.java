package se.group4.database;

import java.util.List;

public interface UserDAO {

    void create (String id, String firstname, String lastname);
    List <User> getAllUsers();

}