package sample;

import Entities.Course;
import Entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class CourseDetails {


    @FXML
    private Label courseName;
    @FXML
    private Label numberHours;
    @FXML
    private Label teacher;
    @FXML
    private Label startDate;
    @FXML
    private Label finishDate;
    @FXML
    private Label nSeats;
    @FXML
    private Label courseDescript;
    @FXML
    private Label seatsAvailable;

    private User userLoggedIn;


    protected void initialize(Course course, User user) {

            userLoggedIn = user;

            courseName.setText(course.getCourseName());
            numberHours.setText(String.valueOf(course.getNumberOfHours()));
            teacher.setText(course.getTeacher().toString());
            startDate.setText(course.getStartDate());
            finishDate.setText(course.getFinishDate());
            nSeats.setText(String.valueOf(course.getTotalSeats()));
            courseDescript.setText(course.getCourseDescription());
            seatsAvailable.setText(String.valueOf(course.getSeatsAvailable()));

    }

    @FXML
    protected void previousPage(ActionEvent e) throws IOException {

        FXMLLoader l = new FXMLLoader(getClass().getResource("StudentRegistration.fxml"));

        Parent sreg = l.load();

        StudentRegController reg = (StudentRegController)l.getController();

        reg.initialize(userLoggedIn);

        Scene sregScene = new Scene(sreg);

        Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();

        stage.hide();

        stage.setScene(sregScene);
        stage.show();

    }
}
