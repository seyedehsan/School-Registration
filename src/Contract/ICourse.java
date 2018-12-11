package Contract;

import Entities.Course;
import Entities.User;

import java.util.List;

public interface ICourse {

    List<Course> getAllCourses();

    List<Course> getCourseByStudent(short studentId);

    void deleteCourse(Course course) throws Exception;

    Course findCourse(short id);

    void insertCourse(Course course, User teacher) throws Exception;

    void updateCourse(Course course) throws Exception;
}
