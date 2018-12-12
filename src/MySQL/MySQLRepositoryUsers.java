package MySQL;

import Contract.IDbContext;
import Entities.AccessLevel;
import Entities.User;
import org.hibernate.Session;

import javax.persistence.Query;
import java.util.List;

public class MySQLRepositoryUsers implements Contract.IUser {

    private IDbContext context;


    public MySQLRepositoryUsers(IDbContext context) {
        this.context = context;

    }

    @Override
    public List<User> getUserByAccess(short accessLevelId) {

        //get db context
        Session session = context.getContext();

        //begin transaction
        session.beginTransaction();

        //get the access level
        AccessLevel accessLevel = session.get(AccessLevel.class, accessLevelId);

        //set the string for the query
        String sql = "from User where accessLevel = :accessLevelId";

        //assemble the query
        Query query = session.createQuery(sql);

        //determine the parameter of the where clause
        query.setParameter("accessLevelId", accessLevel);

        //put the results in a list
        List<User> users = query.getResultList();

        //commit transaction
        session.getTransaction().commit();

        session.close();

        context.closeFactory();

        return users;

    }


    @Override
    public void deleteUser(User user) throws Exception {

        //get db context
        Session session = context.getContext();

        //begin transaction
        session.beginTransaction();

        //get object
        User tempUser = findUser(user.getId());

        //check if the user exist
        if (tempUser != null) {

            //delete object
            session.delete(tempUser);

        } else {

            throw new Exception("User not found");
        }

        //commit transaction
        session.getTransaction().commit();

        session.close();

        context.closeFactory();

    }


    @Override
    public User findUser(short id) throws Exception {

        //get db context
        Session session = context.getContext();

        //begin transaction
        session.beginTransaction();

        User userToFind = session.get(User.class, id);

        //commit transaction
        session.getTransaction().commit();

        session.close();

        context.closeFactory();


        return userToFind;
    }


    @Override
    public void insertUser(User user, short accessLevelId) throws Exception {

        //get db context
        Session session = context.getContext();

        //begin transaction
        session.beginTransaction();

        //get the access level object
        AccessLevel accessLevel = session.get(AccessLevel.class, accessLevelId);

        //set add the access level to the user
        user.setAccessLevel(accessLevel);

        //save the object
        session.save(user);

        //commit transaction
        session.getTransaction().commit();

        //close session
        session.close();

        context.closeFactory();

    }

    @Override
    public void updateUser(User user) throws Exception {

        //get db context
        Session session = context.getContext();

        //begin transaction
        session.beginTransaction();

        User userToEdit = findUser(user.getId());

        //check if the user exist
        if (userToEdit != null) {

            session.saveOrUpdate(userToEdit);

        } else {

            throw new Exception("User not found");
        }

        //commit transaction
        session.getTransaction().commit();

        session.close();

        context.closeFactory();

    }

    @Override
    public List<AccessLevel> getAccessLevels() {

        //get db context
        Session session = context.getContext();

        //begin transaction
        session.beginTransaction();

        List<AccessLevel> accessLevels = session.createQuery("from AccessLevel").getResultList();

        //commit transaction
        session.getTransaction().commit();

        session.close();

        context.closeFactory();

        return accessLevels;
    }

    @Override
    public boolean isEmailUnique(String str) {

        //get db context
        Session session = context.getContext();

        //begin transaction
        session.beginTransaction();


        //set the string for the query
        String sql = "from User where email = :theEmail";

        //assemble the query
        Query query = session.createQuery(sql);

        //determine the parameter of the where clause
        query.setParameter("theEmail", str);

        //put the results in a list
        List<User> users = query.getResultList();

        session.close();

        context.closeFactory();

        if(users.isEmpty()) {

            return true;

        } else {

            return false;
        }
    }
}
