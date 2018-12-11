package Entities;

import DateConversion.DateUtils;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="courses")
public class Course {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private short id;

    @Column(name="course_name")
    private String courseName;

    @Column(name="numbers_hours")
    private short numberOfHours;

    @ManyToOne(cascade= {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name="teacher")
    private User teacher;

    @Column(name="start_date")
    private Date startDate;

    @Column(name="finish_date")
    private Date finishDate;

    @Column(name="total_seat")
    private short totalSeats;

    @Column(name="availabel_seat")
    private short seatsAvailable;

    @Column(name="course_description")
    private String courseDescription;

    public Course() {

    }

    public Course(String courseName, short numberOfHours, Date startDate,
                  Date finishDate, short totalSeats, String courseDescription) {
        this.courseName = courseName;
        this.numberOfHours = numberOfHours;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.totalSeats = totalSeats;
        this.courseDescription = courseDescription;
    }

    public short getId() {
        return id;
    }

    public String getCourseName() {
        return courseName;
    }

    public short getNumberOfHours() {
        return numberOfHours;
    }

    public User getTeacher() {
        return teacher;
    }

    public String getStartDate() {

        String strStartDate = DateUtils.formatDate(startDate);

        return strStartDate;
    }

    public String getFinishDate() {

        String strFinishDate = DateUtils.formatDate(finishDate);

        return strFinishDate;
    }

    public short getTotalSeats() {
        return totalSeats;
    }

    public short getSeatsAvailable() {
        return seatsAvailable;
    }

    public String getCourseDescription() {
        return courseDescription;
    }


    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setNumberOfHours(short numberOfHours) {
        this.numberOfHours = numberOfHours;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public void setTotalSeats(short totalSeats) {
        this.totalSeats = totalSeats;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public void setSeatsAvailable(short seatsAvailable) {
        this.seatsAvailable = seatsAvailable;
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseName='" + courseName + '\'' +
                ", numberOfHours=" + numberOfHours +
                ", teacher=" + teacher.getFirstName() + teacher.getLastName() +
                '}';
    }
}
