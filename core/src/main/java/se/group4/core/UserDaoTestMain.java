package se.group4.core;

public class UserDaoTestMain {
    public static void main(String[] args) {
        UserDAO udao = new UserDAOWithJPAImpl();

        System.out.println(udao.getByFirstName("Johanna"));

    }
}
