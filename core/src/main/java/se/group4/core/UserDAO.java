package se.group4.core;

import java.util.List;

public interface UserDAO {

    void create (User u);
    List <User> getAllUsers();
}





//    List<se.group4.core.User> getByName(String name);
//    boolean updateName(String id, String newName);
//    boolean remove(int id);
