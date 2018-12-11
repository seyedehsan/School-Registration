package Contract;

import Entities.Course;
import Entities.Registration;
import Entities.User;

import java.util.List;

public interface IRegistration {

    List<Registration> getAllRegistrations();

    Registration findRegistration(short id);

    void deleteRegistration(Registration reg) throws Exception;

    void insertRegistration(Registration reg, User student, Course course) throws Exception;

    void updateRegistration(Registration reg) throws Exception;

    int NumberRegistrationsInACourse (Course course);
}
