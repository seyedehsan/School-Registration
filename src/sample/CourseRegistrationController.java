package sample;

import Contract.ICourse;
import Contract.IUser;
import Entities.Course;
import Entities.User;
import Spring.SQLConfig;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class CourseRegistrationController {

    //declare the form controllers to obtain the information

    @FXML
    private TextField courseName;

    @FXML
    private TextField numberOfHours;

    @FXML
    private ComboBox teacherCombo;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker finishDatePicker;

    @FXML
    private TextField numberOfSeats;

    @FXML
    private TextArea courseDescription;

    @FXML
    private Button btnClear;

    @FXML
    private Button btnSaveCourse;

    @FXML
    private Button btnReturn;

    private boolean isNew = true;


    //do the injections to get access to the database
    //read spring config java class
    AnnotationConfigApplicationContext context =
            new AnnotationConfigApplicationContext(SQLConfig.class);

    //get the bean from the bean factory and inject the necessary dependency
    ICourse sqlCourse = context.getBean("courseRepository", ICourse.class);

    IUser sqlUser = context.getBean("userRepository", IUser.class);

    @FXML
    public void initialize(Course theCourse) {


        //create a new course object
        Course newCourse = theCourse;

        //create list of Users of Access Level 2 = teacher
        ObservableList<User> listOfTeachers = FXCollections.observableArrayList(sqlUser.getUserByAccess((short)2));

        //load the list of teacher in the combo box
        teacherCombo.setItems(listOfTeachers);

        if(newCourse.getId() != 0) {

            isNew = false;


        }

        //retrieve the information from the form controllers when the save button is clicked
        btnSaveCourse.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                //check the mandatory fields are complete
                //check if the values are correct
                //check if the dates are correct
                //call the validations method to check
                if (checkForm() && checkNumber() && checkDates()) {

                    try {

                        //get the form elements and set the object
                        newCourse.setCourseName(courseName.getText());
                        newCourse.setNumberOfHours(Short.parseShort(numberOfHours.getText()));
                        newCourse.setTotalSeats(Short.parseShort(numberOfSeats.getText()));
                        newCourse.setSeatsAvailable(Short.parseShort(numberOfSeats.getText()));
                        newCourse.setCourseDescription(courseDescription.getText());

                        //convert Date in the form java.time.local to java.util.date
                        Date startDate = java.sql.Date.valueOf(startDatePicker.getValue());
                        Date endDate = java.sql.Date.valueOf(finishDatePicker.getValue());

                        //set the date values in the course object
                        newCourse.setStartDate(startDate);
                        newCourse.setFinishDate(endDate);

                        //get the teacher object from the comboBox, cast to User type
                        User teacher = (User)teacherCombo.getSelectionModel().getSelectedItem();

                        if(isNew) {

                            //insert the new course in the database
                            sqlCourse.insertCourse(newCourse, teacher);

                        } else {

                            newCourse.setTeacher(teacher);
                            sqlCourse.updateCourse(newCourse);

                        }

                        //** pop-up window saying the course was saved **
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Confirmation");
                        alert.setContentText("Course has been saved !");
                        alert.showAndWait();

                        //clean-up the form
                        clearForm();

                    } catch (Exception ex) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText(ex.toString());
                        alert.showAndWait();
                        System.out.println(ex + ": Database Error");
                    }

                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Missing or Invalid fields.");
                    alert.showAndWait();
                }
            }
        });

        //clear form button event
        btnClear.setOnAction(e-> clearForm());

        //add action to the back button

    }

    public void initializeFields(Course course) {

        //get the data from course as date.util and convert to local.date
        Date start = course.getDateStart();
        LocalDate dateStart = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        Date end = course.getDateFinish();
        LocalDate dateEnd = end.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        courseName.setText(course.getCourseName());
        numberOfHours.setText(String.valueOf(course.getNumberOfHours()));
        teacherCombo.getSelectionModel().select(course.getTeacher());
        startDatePicker.setValue(dateStart);
        finishDatePicker.setValue(dateEnd);
        numberOfSeats.setText(String.valueOf(course.getTotalSeats()));
        courseDescription.setText(course.getCourseDescription());
    }

    //method to check if the form is complete
    public boolean checkForm() {

        boolean isComplete = true;

        if (courseName.getText() == null || numberOfHours.getText() == null ||
                teacherCombo.getSelectionModel().getSelectedItem() == null ||
                startDatePicker.getValue() == null || finishDatePicker.getValue() == null ||
                numberOfSeats.getText() == null) {

            isComplete = false;

        }

        return isComplete;
    }

    //method to check if the user input is of number type
    public boolean checkNumber() {

        boolean isNumber = true;

        try {

            Short.parseShort(numberOfHours.getText());
            Short.parseShort(numberOfSeats.getText());


        } catch (NumberFormatException ex) {
            isNumber = false;
            System.out.println(ex);
        }

        return isNumber;
    }

    //check if the start and finish dates of the course are correct
    public boolean checkDates() {

        boolean datesCorrect = true;

        if(startDatePicker.getValue().isBefore(java.time.LocalDate.now()) ||
           startDatePicker.getValue().isAfter(finishDatePicker.getValue())) {

            datesCorrect = false;
        }

        return datesCorrect;
    }

    //method to clear the form either by btn click or after saving a new course
    public void clearForm() {

        courseName.setText(null);
        numberOfHours.setText(null);
        teacherCombo.getSelectionModel().clearSelection();
        startDatePicker.setValue(null);
        finishDatePicker.setValue(null);
        numberOfSeats.setText(null);
        courseDescription.setText(null);
    }

    @FXML
    public void returnPrevious(ActionEvent event) {

        try {
            //get the loader
            FXMLLoader l = new FXMLLoader(getClass().getResource("CourseList.fxml"));


            Parent moreDetails = l.load();

            Scene moreDetailsScene = new Scene(moreDetails);
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.hide();
            stage.setScene(moreDetailsScene);
            stage.show();

        } catch (IOException ex) {

            System.out.println(ex);
        }

    }


}
