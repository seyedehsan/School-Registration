package sample;


import Contract.IUser;
import Entities.AccessLevel;
import Entities.Course;
import Entities.Registration;
import Entities.User;
import Spring.SQLConfig;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Test {



    public static void main(String[] args) {

        //read spring config java class
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(SQLConfig.class);

        IUser theUser = context.getBean("userRepository", IUser.class);


        try {

            User student1 = new User();
            student1.setFirstName("Ehsan");
            student1.setLastName("Seyedian");
            student1.setEmail("ehsan@gmail.com");
            student1.setPwd("123");

            theUser.insertUser(student1, (short)3);


        } catch (Exception ex) {

            System.out.println(ex);
        }
    }





}
