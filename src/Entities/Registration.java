package Entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="registration")
public class Registration {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private short id;

    @ManyToOne(cascade= {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name="student")
    private User student;

    @ManyToOne(cascade= {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name="course")
    private Course course;

    @Column(name="approved")
    private boolean isApproved;

    @Column(name="final_grade")
    private double finalGrade;

    @Column(name="date_registered")
    private Date dateRegistered;

    public Registration() {

    }

    public Registration(boolean isApproved, double finalGrade) {
        this.isApproved = isApproved;
        this.finalGrade = finalGrade;
    }

    public short getId() {
        return id;
    }

    public User getStudent() {
        return student;
    }

    public Course getCourse() {
        return course;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public double getFinalGrade() {
        return finalGrade;
    }

    public Date getDateRegistered() {
        return dateRegistered;
    }


    public void setStudent(User student) {
        this.student = student;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    public void setFinalGrade(double finalGrade) {
        this.finalGrade = finalGrade;
    }

    public void setDateRegistered(Date dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    @Override
    public String toString() {
        return "Registration{" +
                "course=" + course.getCourseName() +
                '}';
    }
}
