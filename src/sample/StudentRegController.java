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
import javafx.scene.control.Button;
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

    @FXML
    private TableView<Course> selCourseList;
    @FXML
    private TableColumn<Course, String> selCourseNameColumn;
    @FXML
    private TableColumn<Course, String> selTeacherNameColumn;
    @FXML
    private TableColumn<Course, String> selStartDateColumn;
    @FXML
    private TableColumn<Course, String> selFinishDateColumn;


    private ObservableList<Course> listofCourses;

    private ObservableList<Course> listSelCourses;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnRemove;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnRegister;


    private User userLoggedIn;


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

        //grab the information from the logged user
        userLoggedIn = user;

        //call the removeReg method to return a list containing only the course to which the user is not registered
        List<Course> nonRegisteredCourses = removeReg(userLoggedIn);

        //initialize the collection with the correspondent lists
        listofCourses = FXCollections.observableArrayList(nonRegisteredCourses);
        listSelCourses = FXCollections.observableArrayList(new ArrayList<>());

        //set the factory for the columns
        courseNameColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        teacherNameColumn.setCellValueFactory(new PropertyValueFactory<>("teacher"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        finishDateColumn.setCellValueFactory(new PropertyValueFactory<>("finishDate"));

        selCourseNameColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        selTeacherNameColumn.setCellValueFactory(new PropertyValueFactory<>("teacher"));
        selStartDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        selFinishDateColumn.setCellValueFactory(new PropertyValueFactory<>("finishDate"));

        //assign the items of each table
        listCourses.setItems(listofCourses);

        selCourseList.setItems(listSelCourses);

        //when click add, remove from one list and add in another
        btnAdd.setOnAction(e->{

            int index = listCourses.getSelectionModel().getSelectedIndex();

            if(index>-1) {

                Course courseSelected = listCourses.getItems().get(index);

                listofCourses.remove(courseSelected);
                listSelCourses.add(courseSelected);

                listCourses.setItems(listofCourses);
                selCourseList.setItems(listSelCourses);
            }
        });

        //click remove, remove from one list to add in the other one
        btnRemove.setOnAction(e-> {

            int index = selCourseList.getSelectionModel().getSelectedIndex();

            if(index>-1) {

                Course courseSelected = selCourseList.getItems().get(index);

                listofCourses.add(courseSelected);
                listSelCourses.remove(courseSelected);

                listCourses.setItems(listofCourses);
                selCourseList.setItems(listSelCourses);
            }
        });

        btnCancel.setOnAction(e->{

            try {
                //get the loader
                FXMLLoader l = new FXMLLoader(getClass().getResource("mainpage.fxml"));

                Parent moreDetails = l.load();

                Scene moreDetailsScene = new Scene(moreDetails);
                Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
                stage.hide();
                stage.setScene(moreDetailsScene);
                stage.show();

            } catch (Exception ex) {

                System.out.println(ex);
            }
        });

        //save the registration
        btnRegister.setOnAction(e->{

            try {
                //do a foreach in the list of courses selected
                for (Course items : listSelCourses) {

                    //foreach Course decrease by 1 the seatsAvailable in the course
                    short seats = items.getSeatsAvailable();
                    if(seats>0) {
                        items.setSeatsAvailable(--seats);
                    }
                    //update the course in the database with the new seatsAvailable
                    sqlCourse.updateCourse(items);
                    //save the registration
                    sqlReg.insertRegistration(userLoggedIn, items);

                }
            } catch (Exception ex) {

                System.out.println(ex);
            }

            //** pop-up window saying the course was saved **
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Confirmation");
            alert.setContentText("Your registration has been saved !");
            alert.showAndWait();

            initialize(userLoggedIn);

        });
    }

    //remove for the total pool of courses, the courses to which the user is already registered
    private List<Course> removeReg(User user) {

        List<Course> firstCheck = sqlCourse.getAllCourses();

        List<Course> secondCheck = new ArrayList<>();

        //get all the registrations from the Registration table
        List<Course> allCourses = sqlCourse.getAllCourses();

        List<Course> stdCourse = sqlCourse.getCourseByStudent(user.getId());

        //iterate the courses to which that student is registered
        for(Course i : stdCourse) {
            //if the list of all courses contains that course it will remove from first check list
            if(allCourses.contains(i)) {
                firstCheck.remove(i);
            }
        }
        //copy first check list to a secondCheck list
        for(Course i : firstCheck) {

            secondCheck.add(i);
        }

        //iterate the firstCheck list
        for(Course i : firstCheck) {

            //if any of the courses inside the firstCheck list has 0 spots available, remove that course from the list
            if(i.getSeatsAvailable() == 0) {
                secondCheck.remove(i);
            }
        }
        //return the secondCheck list which is in fact the list of all courses minus courses registered for that user
        //and courses with 0 seats available
        return secondCheck;
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
            cd.initialize(courseSelected, userLoggedIn);

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
