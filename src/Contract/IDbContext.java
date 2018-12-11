package Contract;

import Entities.Course;
import Entities.Registration;
import Entities.User;
import org.hibernate.Session;

import java.util.List;

public interface IDbContext {

    Session getContext();

    void closeFactory();
}
