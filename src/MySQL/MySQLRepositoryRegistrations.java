package MySQL;

import Contract.IDbContext;
import Entities.AccessLevel;
import Entities.Course;
import Entities.Registration;
import Entities.User;
import org.hibernate.Session;

import java.sql.Timestamp;
import java.util.List;

public class MySQLRepositoryRegistrations implements Contract.IRegistration {

    private IDbContext context;

    public MySQLRepositoryRegistrations(IDbContext context) {

        this.context = context;
    }

    @Override
    public List<Registration> getAllRegistrations() {

        //get db context
        Session session = context.getContext();

        //begin transaction
        session.beginTransaction();


        //put the results in a list
        List<Registration> registrations = session.createQuery("from Registration").getResultList();

        //commit transaction
        session.getTransaction().commit();

        session.close();

        context.closeFactory();

        return registrations;
    }

    @Override
    public Registration findRegistration(short id) {

        //get db context
        Session session = context.getContext();

        //begin transaction
        session.beginTransaction();

        Registration registrationToFind = session.get(Registration.class, id);

        //commit transaction
        session.getTransaction().commit();

        session.close();

        context.closeFactory();

        return registrationToFind;
    }

    @Override
    public void deleteRegistration(Registration reg) throws Exception {

        //get db context
        Session session = context.getContext();

        //begin transaction
        session.beginTransaction();

        Registration regToDelete = findRegistration(reg.getId());

        if(regToDelete != null) {

            session.delete(regToDelete);
        } else {
            throw new Exception("Course not found");
        }

        //commit transaction
        session.getTransaction().commit();

        session.close();

        context.closeFactory();
    }

    @Override
    public void insertRegistration(Registration reg, User student, Course course) throws Exception {

        Session session = context.getContext();

        //begin transaction
        session.beginTransaction();

        //set the desired access level for student
        short studentAccessId = 1;
        AccessLevel studentAccess = session.get(AccessLevel.class,studentAccessId);

        User userToReg;
        Course courseToReg;

        //find the student and the course
        //check if the correct student status
        if(student.getAccessLevel().getId() == studentAccess.getId()) {

            userToReg = session.get(User.class, student.getId());
            courseToReg = session.get(Course.class, course.getId());

        } else {

            throw new Exception("Please assign a student for a registration");
        }

        //set the timestamp for the date and time of registration
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        reg.setDateRegistered(timestamp);

        //set student and course on the registration
        reg.setStudent(userToReg);
        reg.setCourse(courseToReg);

        session.save(reg);

        //commit transaction
        session.getTransaction().commit();

        //close session
        session.close();

        context.closeFactory();

    }

    @Override
    public void updateRegistration(Registration reg) throws Exception {

        //get db context
        Session session = context.getContext();

        //begin transaction
        session.beginTransaction();

        Registration regToEdit = findRegistration(reg.getId());

        //check if the course exist

        if(regToEdit != null) {

            session.save(regToEdit);
        } else {
            throw new Exception("Course not found");
        }

        //commit transaction
        session.getTransaction().commit();

        session.close();

        context.closeFactory();
    }
}
