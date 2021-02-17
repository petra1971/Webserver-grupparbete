package se.group4.core;

import javax.persistence.*;
import java.util.List;


public class JPAMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("JPA");

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        User p = em.find(User.class, "530407-7989");
        p.setFirstname("Pelle");
        System.out.println(p);
        em.getTransaction().commit();


        em.getTransaction().begin();
        User ppp = new User("505012313","Pelle","Hagstedt");
        em.persist(ppp);
        System.out.println("pelleid = " + ppp.getId());
        List<User> list = em.createQuery("FROM User",User.class).getResultList();
        System.out.println(list);
        em.getTransaction().commit();

////		em = emf.createEntityManager();
//		em.getTransaction().begin();
//		list = em.createQuery("from User u where u.firstName = :namn", User.class)
//				.setParameter("namn", "Pelle").getResultList();
//		for (User uu : list) {
//			em.remove(uu);
//		}
//		em.getTransaction().commit();

        em.close();
        emf.close();
    }
}
