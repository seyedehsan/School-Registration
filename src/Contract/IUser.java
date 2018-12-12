package Contract;

import Entities.AccessLevel;
import Entities.User;

import java.util.List;

public interface IUser {

    List<User> getUserByAccess(short accessLevelId);

    void deleteUser(User user) throws Exception;

    User findUser(short id) throws Exception;

    void insertUser(User user, short accessLevelId) throws Exception;

    void updateUser(User user) throws Exception;

    List<AccessLevel> getAccessLevels();
}
