package se.group4.core;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class UserDAOWithJPAImpl implements UserDAO {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("JPA");

    @Override
    public void create(String id, String firstname, String lastname) {
        EntityManager em = emf.createEntityManager();
        User user = new User(id, firstname, lastname);
        System.out.println("User created");
        em.getTransaction().begin();
        em.persist(user);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public List<User> getAllUsers() {
        List<User> listOfUsers;
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        listOfUsers = em.createQuery("from User", User.class).getResultList();
        em.getTransaction().commit();
        return listOfUsers;
    }


    public User findUserById(String ID) {
        EntityManager em = emf.createEntityManager();
        var user = em.createQuery("from User u where u.id = :Id", User.class)
                .setParameter("Id", ID).getSingleResult();
        em.close();
        return user;
    }
}



//    @Override
//    public String findUserById(String ID) {
//        EntityManager em = emf.createEntityManager();
//        em.getTransaction().begin();
//        User userFromId = em.find(User.class,ID);
//        em.getTransaction().commit();
//        return userFromId;
//    }