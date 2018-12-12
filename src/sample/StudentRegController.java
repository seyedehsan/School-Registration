package sample;

import Contract.ICourse;
import Contract.IRegistration;
import Contract.IUser;
import Entities.Course;
import Entities.Registration;
import Entities.User;
import Spring.SQLConfig;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StudentRegController {

    @FXML
    private TableView<Course> listCourses;
    @FXML
    private TableColumn<Course, String> courseNameColumn;
    @FXML
    private TableColumn<Course, String> teacherNameColumn;
    @FXML
    private TableColumn<Course, String> startDateColumn;
    @FXML
    private TableColumn<Course, String> finishDateColumn;

    private User userLoggedIn;

    public ArrayList<Course> courses;


    //do the injections to get access to the database
    //read spring config java class
    AnnotationConfigApplicationContext context =
            new AnnotationConfigApplicationContext(SQLConfig.class);

    //get the bean from the bean factory and inject the necessary dependency
    ICourse sqlCourse = context.getBean("courseRepository", ICourse.class);
    IUser sqlUser = context.getBean("userRepository", IUser.class);
    IRegistration sqlReg = context.getBean("registrationRepository", IRegistration.class);

    @FXML
    public void initialize(User user) {

        userLoggedIn = user;
        populateListCourses();



    }

    //remove for the total pool of courses, the courses to which the user is already registered
    private List<Course> removeReg(User user) {

        List<Course> cleanReg = new ArrayList<>();

        //get all the registrations from the Registration table
        List<Course> allCourses = sqlCourse.getAllCourses();

        List<Course> stdCourse = sqlCourse.getCourseByStudent(user.getId());

        allCourses.remove(stdCourse);

//        if(stdCourse.size() > 0) {
//
//            for(Course itemCourse : allCourses) {
//
//                for(Course itemReg : stdCourse) {
//
//                    if (itemCourse.getId() != itemReg.getId()) {
//
//                        cleanReg.add(itemCourse);
//                    }
//                }
//            }
//        } else {
//
//            cleanReg = allCourses;
//        }
        
        return allCourses;
    }


    public void populateListCourses() {

        //call the removeReg method to return a list containing only the course to which the user is not registered
        List<Course> nonRegisteredCourses = removeReg(userLoggedIn);


        ObservableList<Course> listofCourses = FXCollections.observableArrayList(nonRegisteredCourses);

        courseNameColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        teacherNameColumn.setCellValueFactory(new PropertyValueFactory<>("teacher"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        finishDateColumn.setCellValueFactory(new PropertyValueFactory<>("finishDate"));

        listCourses.setItems(listofCourses);

    }

    @FXML
    public void displayMoreDetails(ActionEvent e) throws IOException {

        int index = listCourses.getSelectionModel().getSelectedIndex();

        if(index != -1) {
            Course courseSelected = listCourses.getItems().get(index);

            //get the loader
            FXMLLoader l = new FXMLLoader(getClass().getResource("CourseDetails.fxml"));


            Parent moreDetails = l.load();

            //get access to the controller through the FXMLLoader
            CourseDetails cd = (CourseDetails)l.getController();

            //then on the cd you will access your functions
            //then you can pass the object
            cd.initialize(courseSelected);

            Scene moreDetailsScene = new Scene(moreDetails);
            Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
            stage.hide();
            stage.setScene(moreDetailsScene);
            stage.show();


        } else {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Error message");
            errorAlert.setContentText("Please choose at list one course from the list");
            errorAlert.showAndWait();
        }

    }

    //code to detect an event by a click
//        table.setOnMouseClicked((MouseEvent event) -> {
//            if (event.getButton().equals(MouseButton.PRIMARY)) {
//                int index = table.getSelectionModel().getSelectedIndex();
//                Person person = table.getItems().get(index);
//                System.out.println(person);
//            }
//        });



}
