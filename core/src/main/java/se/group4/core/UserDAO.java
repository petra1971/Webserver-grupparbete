package se.group4.core;

import java.util.List;

public interface UserDAO {

    void create (User u);
    List <User> getAllUsers();
}