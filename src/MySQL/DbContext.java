package MySQL;

import Contract.IDbContext;
import Entities.AccessLevel;
import Entities.Course;
import Entities.Registration;
import Entities.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

//database context
public class DbContext implements IDbContext {


    private Session context;
    private SessionFactory factory;

    public DbContext() {

    }

    @Override
    public Session getContext() {

        //create session factory
        factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Course.class)
                .addAnnotatedClass(AccessLevel.class)
                .addAnnotatedClass(Registration.class)
                .buildSessionFactory();

        //create session
        Session session = factory.getCurrentSession();

        return session;
    }

    @Override
    public void closeFactory() {

        factory.close();
    }
}
