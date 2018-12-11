package MySQL;

import Contract.ICourse;
import Contract.IDbContext;
import DateConversion.DateUtils;
import Entities.AccessLevel;
import Entities.Course;
import Entities.Registration;
import Entities.User;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MySQLRepositoryCourse implements ICourse {

    private IDbContext context;

    public MySQLRepositoryCourse(IDbContext context) {
        this.context = context;

    }

    @Override
    public List<Course> getAllCourses() {

        //get db context
        Session session = context.getContext();

        //begin transaction
        session.beginTransaction();


        //put the results in a list
        List<Course> courses = session.createQuery("from Course").getResultList();

        //commit transaction
        session.getTransaction().commit();

        session.close();

        context.closeFactory();

        return courses;

    }

    @Override
    public List<Course> getCourseByStudent(short studentId) {

        //get db context
        Session session = context.getContext();

        //begin transaction
        session.beginTransaction();

        //get the access level
        User student = session.get(User.class, studentId);

        //initialize array to put the courses
        List<Course> courses = new ArrayList<>();

        //get registration for the student
        List<Registration> registrations = student.getRegistrations();

        for(Registration items:registrations) {
            //get each course the student is registered
            Course course = items.getCourse();
            //add each course to a courses list
            courses.add(course);
        }

        //commit transaction
        session.getTransaction().commit();

        session.close();

        context.closeFactory();

        return courses;

    }

    @Override
    public void deleteCourse(Course course) throws Exception {

        //get db context
        Session session = context.getContext();

        //begin transaction
        session.beginTransaction();

        Course courseToDelete = findCourse(course.getId());

        if(courseToDelete != null) {

            session.delete(courseToDelete);
        } else {
            throw new Exception("Course not found");
        }

        //commit transaction
        session.getTransaction().commit();

        session.close();

        context.closeFactory();

    }

    @Override
    public Course findCourse(short id) {

        //get db context
        Session session = context.getContext();

        //begin transaction
        session.beginTransaction();

        Course courseToFind = session.get(Course.class, id);

        //commit transaction
        session.getTransaction().commit();

        session.close();

        context.closeFactory();

        return courseToFind;

    }

    @Override
    public void insertCourse(Course course, User teacher, String strStartDate, String strEndDate) throws Exception {

        //get db context
        Session session = context.getContext();

        //begin transaction
        session.beginTransaction();

        User courseTeacher;

        //access type teacher
        short accessId = 2;
        AccessLevel teacherAccess = session.get(AccessLevel.class, accessId);

        //check if the user is a teacher
        if(teacher.getAccessLevel().getId() == teacherAccess.getId()) {

            //find the teacher
            courseTeacher = session.get(User.class, teacher.getId());

        } else {

            throw new Exception("Please choose a teacher to assign to the course");
        }

        //set the teacher for the course
        course.setTeacher(courseTeacher);

        //convert the dates from string to date
        Date startDate = DateUtils.parseDate(strStartDate);
        Date endDate = DateUtils.parseDate(strEndDate);

        //set the dates on the course object
        course.setStartDate(startDate);
        course.setFinishDate(endDate);

        //save the course
        session.save(course);

        //commit transaction
        session.getTransaction().commit();

        //close session
        session.close();

        context.closeFactory();
    }

    @Override
    public void updateCourse(Course course) throws Exception {

        //get db context
        Session session = context.getContext();

        //begin transaction
        session.beginTransaction();

        Course courseToEdit = findCourse(course.getId());

        //check if the course exist

        if(courseToEdit != null) {

            session.save(course);
        } else {
            throw new Exception("Course not found");
        }

        //commit transaction
        session.getTransaction().commit();

        session.close();

        context.closeFactory();

    }




}
