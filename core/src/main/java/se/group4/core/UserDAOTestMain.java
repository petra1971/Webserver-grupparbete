package se.group4.core;

public class UserDAOTestMain {
    public static void main(String[] args) {
        UserDAO udao = new UserDAOWithJPAImpl();
        System.out.println("Program start");

//         udao.updateName("500603-4268","Jolene");
        System.out.println(udao.getByName("Pelle"));    }
}
