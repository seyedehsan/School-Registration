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
import javafx.scene.control.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

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
    private Label seatsAvailable;

    @FXML
    private TextArea courseDescription;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnSaveCourse;

    //do the injections to get access to the database
    //read spring config java class
    AnnotationConfigApplicationContext context =
            new AnnotationConfigApplicationContext(SQLConfig.class);

    //get the bean from the bean factory and inject the necessary dependency
    ICourse sqlCourse = context.getBean("courseRepository", ICourse.class);

    IUser sqlUser = context.getBean("userRepository", IUser.class);

    @FXML
    private void initialize() {

        //create list of Users of Access Level 2 = teacher
        ObservableList<User> listOfTeachers = FXCollections.observableArrayList(sqlUser.getUserByAccess((short)2));

        //load the list of teacher in the combo box
        teacherCombo.setItems(listOfTeachers);

        //create a new course object
        Course newCourse = new Course();

        //retrieve the information from the form controllers when the save button is clicked
        btnSaveCourse.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                //check the mandatory fields are complete
                //check if the values are correct
                //call validation method to check
                if (checkForm() && checkNumber()) {

                    try {

                        //get the form elements and set the object
                        newCourse.setCourseName(courseName.getText());
                        newCourse.setNumberOfHours(Short.parseShort(numberOfHours.getText()));
                        newCourse.setTotalSeats(Short.parseShort(numberOfSeats.getText()));
                        newCourse.setSeatsAvailable(Short.parseShort(seatsAvailable.getText()));
                        newCourse.setCourseDescription(courseDescription.getText());

                        //convert Date in the form java.time.local to java.util.date
                        Date startDate = java.sql.Date.valueOf(startDatePicker.getValue());
                        Date endDate = java.sql.Date.valueOf(finishDatePicker.getValue());

                        //set the date values in the course object
                        newCourse.setStartDate(startDate);
                        newCourse.setFinishDate(endDate);

                        //get the teacher object from the comboBox, cast to User type
                        User teacher = (User)teacherCombo.getSelectionModel().getSelectedItem();

                        //insert the new course in the database
                        //ADD A CHECKING IN THE INSERT METHOD TO DETECT IF THE COURSE ALREADY EXISTS
                        //OVERWRITE EQUALS METHOD OR COMPARABLE
                        sqlCourse.insertCourse(newCourse, teacher);

                        //** pop-up window saying the course was saved **
                        //clean-up the form


                    } catch (Exception ex) {

                        System.out.println(ex + ": Database Error");
                    }

                } else {

                    System.out.println("Missing or Invalid fields.");
                }
            }
        });
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



}
