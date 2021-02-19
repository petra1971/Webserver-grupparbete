package se.group4.core;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class UserDAOWithJPAImpl implements UserDAO {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("JPA");

    @Override
    public void create(User u) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction();
        em.persist(u);
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


    @Override
    public List<User> getByFirstName(String name) {
        List<User> list;
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        list = em.createQuery("from User u where u.firstName = :firstName", User.class)
                .setParameter("firstName", name).getResultList();
        em.getTransaction().commit();
        return list;
    }
}








//    @Override
//    public List<User> getByName(String name) {
//        List<se.group4.core.User> list;
//        EntityManager em = emf.createEntityManager();
//        em.getTransaction().begin();
//        list = em.createQuery("from User u where u.firstName =:firstname", se.group4.core.User.class)
//                .setParameter("firstname",name).getResultList();
//        em.getTransaction().commit();
//        return list;
//    }

//    @Override
//    public boolean updateName(String id, String newName) {
//        boolean success = false;
//        EntityManager em = emf.createEntityManager();
//        em.getTransaction().begin();
//        se.group4.core.User u = em.find(se.group4.core.User.class, id);
//        if (u != null ) {
//            u.setFirstName(newName);
//            success = true;
//        }
//        em.getTransaction().commit();
//        return success;
//    }
//
//    @Override
//    public boolean remove(int id) {
//        boolean success = false;
//        EntityManager em = emf.createEntityManager();
//        em.getTransaction().begin();
//        se.group4.core.User u = em.find(se.group4.core.User.class,id);
//        if(u != null) {
//            em.remove(u);
//            success = true;
//        }
//        em.getTransaction().commit();
//        return success;
//    }
