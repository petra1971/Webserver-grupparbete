package se.group4.core;

import java.util.List;

public interface UserDAO {

    void create (se.group4.core.User u);

    List<se.group4.core.User> getByName(String name);

    boolean updateName(String id, String newName);

    boolean remove(int id);
}
