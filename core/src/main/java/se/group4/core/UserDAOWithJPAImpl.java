package se.group4.core;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

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

    public User findUserById(String ID) {
        EntityManager em = emf.createEntityManager();
        var user = em.createQuery("from User u where u.id = :Id", User.class)
                .setParameter("Id", ID).getSingleResult();
        em.close();
        return user;
    }
}