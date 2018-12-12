package sample;

import Contract.ICourse;
import Contract.IRegistration;
import Contract.IUser;
import Entities.Course;
import Spring.SQLConfig;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;

public class CourseListController {

    @FXML
    private TableView<Course> tableCourses;

    @FXML
    private TableColumn<Course, String> courseNameColumn;

    @FXML
    private TableColumn<Course, String> teacherNameColumn;

    @FXML
    private TableColumn<Course, String> startDateColumn;

    @FXML
    private TableColumn<Course, String> endDateColumn;

    @FXML
    private TableColumn<Course, String> numberOfHoursColumn;

    @FXML
    private TableColumn<Course, String> totalSeatsColumn;

    @FXML
    private TableColumn<Course, String> seatsAvailableColumn;

    @FXML
    private Button btnEdit;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnBack;

    //do the injections to get access to the database
    //read spring config java class
    AnnotationConfigApplicationContext context =
            new AnnotationConfigApplicationContext(SQLConfig.class);

    //get the bean from the bean factory and inject the necessary dependency
    ICourse sqlCourse = context.getBean("courseRepository", ICourse.class);
    IUser sqlUser = context.getBean("userRepository", IUser.class);
    IRegistration sqlReg = context.getBean("registrationRepository", IRegistration.class);



    @FXML
    public void initialize() {

        ObservableList<Course> listofCourses = FXCollections.observableArrayList(sqlCourse.getAllCourses());

        courseNameColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        teacherNameColumn.setCellValueFactory(new PropertyValueFactory<>("teacher"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("finishDate"));
        numberOfHoursColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfHours"));
        totalSeatsColumn.setCellValueFactory(new PropertyValueFactory<>("totalSeats"));
        seatsAvailableColumn.setCellValueFactory(new PropertyValueFactory<>("seatsAvailable"));

        tableCourses.setItems(listofCourses);

    }

    @FXML
    public void editCourse(ActionEvent event) {

        int index = tableCourses.getSelectionModel().getSelectedIndex();

        if(index != -1) {

            try {

                Course courseSelected = tableCourses.getItems().get(index);

                //get the loader
                FXMLLoader l = new FXMLLoader(getClass().getResource("CourseRegistration.fxml"));


                Parent moreDetails = l.load();

                //get access to the controller through the FXMLLoader
                CourseRegistrationController cd = (CourseRegistrationController)l.getController();

                //then on the cd you will access your functions
                //then you can pass the object
                cd.initializeFields(courseSelected);
                cd.initialize(courseSelected);

                Scene moreDetailsScene = new Scene(moreDetails);
                Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                stage.hide();
                stage.setScene(moreDetailsScene);
                stage.show();

            } catch (IOException ex) {

                System.out.println(ex);
            }

        } else {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setContentText("Please choose at list one course from the list");
            errorAlert.showAndWait();
        }

    }

    //action to delete course
    @FXML
    public void deleteCourse(ActionEvent event) {

        try {

            //get the index of the chosen option
            int index = tableCourses.getSelectionModel().getSelectedIndex();

            //check if the user chose any option
            if(index != -1) {
                //if yes, get the object selected by the user
                Course courseSelected = tableCourses.getItems().get(index);

                //pop up a window asking the user to confirm the deletion
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                        "Are you sure you want to delete the course " + courseSelected + " ?",
                        ButtonType.YES, ButtonType.CANCEL);
                alert.showAndWait();

                if (alert.getResult() == ButtonType.YES) {

                    sqlCourse.deleteCourse(courseSelected);
                    initialize();

                } else {

                    initialize();
                }


            } else {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Error message");
                errorAlert.setContentText("Please choose at list one course from the list");
                errorAlert.showAndWait();
            }

        } catch ( Exception ex) {

            System.out.println(ex);
        }


    }

    //return to the main page
    @FXML
    public void returnMain (ActionEvent event) {

        try {
            //get the loader
            FXMLLoader l = new FXMLLoader(getClass().getResource("mainpage.fxml"));

            Parent moreDetails = l.load();

            Scene moreDetailsScene = new Scene(moreDetails);
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.hide();
            stage.setScene(moreDetailsScene);
            stage.show();

        } catch (Exception ex) {

            System.out.println(ex);
        }


    }

}
