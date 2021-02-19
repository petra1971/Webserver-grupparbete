package se.group4.core;

import java.util.List;

public interface UserDAO {

    void create (User u);
    List <User> getAllUsers();
    List<User> getByFirstName(String firstName);
}