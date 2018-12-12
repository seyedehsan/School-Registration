package sample;

import Contract.ICourse;
import Contract.IUser;
import Entities.Course;
import Entities.User;
import Spring.SQLConfig;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;

public class MainPage {
    @FXML
    protected ImageView imgCourse;
    @FXML
    protected ImageView imgEdit;
    @FXML
    protected ImageView imgRegister;
    @FXML
    protected ImageView imgCreate;
    @FXML
    protected ImageView imgCreateCourse;
    @FXML
    protected ImageView imgManage;
    @FXML
    protected Button btnManage;
    @FXML
    protected Button btnCreateCourse;
    @FXML
    protected Button btnCreate;
    @FXML
    protected Button btnRegister;
    @FXML
    protected Button btnEdit;
    @FXML
    protected Button btnCourses;

    private User userLoggedIn;


    //do the injections to get access to the database
    //read spring config java class
    AnnotationConfigApplicationContext context =
            new AnnotationConfigApplicationContext(SQLConfig.class);

    IUser sqlUser = context.getBean("userRepository", IUser.class);


    @FXML
    public void initialize(String userEmail){

        userLoggedIn = sqlUser.findUserByEmail(userEmail);


        btnCourses.setGraphic(imgCourse);
        btnEdit.setGraphic(imgEdit);
        btnRegister.setGraphic(imgRegister);
        btnCreate.setGraphic(imgCreate);
        btnCreateCourse.setGraphic(imgCreateCourse);
        btnManage.setGraphic(imgManage);

        //btn to course management (edit and delete course)
        btnManage.setOnAction(e->{

            try {

                //get the loader
                FXMLLoader l = new FXMLLoader(getClass().getResource("CourseList.fxml"));

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

        //button to create a new course
        btnCreateCourse.setOnAction(e-> {

            try {

                //get the loader
                FXMLLoader l = new FXMLLoader(getClass().getResource("CourseRegistration.fxml"));

                Parent moreDetails = l.load();

                //access courseRegistration controller
                CourseRegistrationController cr = (CourseRegistrationController)l.getController();

                //send a new Course to the initialize method on the CourseRegistration
                //since it is a new course

                Course newCourse = new Course();
                cr.initialize(newCourse);

                Scene moreDetailsScene = new Scene(moreDetails);
                Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
                stage.hide();
                stage.setScene(moreDetailsScene);
                stage.show();

            } catch (NullPointerException ex) {

                System.out.println(ex);

            } catch (IOException ex) {

                System.out.println(ex);
            }
        });

        //Register new student
        btnRegister.setOnAction(e->{

            try {

                //get the loader
                FXMLLoader l = new FXMLLoader(getClass().getResource("StudentRegistration.fxml"));

                Parent moreDetails = l.load();

                //access courseRegistration controller
                StudentRegController src = (StudentRegController)l.getController();

                //send a new Course to the initialize method on the CourseRegistration
                //since it is a new course


                src.initialize(userLoggedIn);

                Scene moreDetailsScene = new Scene(moreDetails);
                Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
                stage.hide();
                stage.setScene(moreDetailsScene);
                stage.show();

            } catch (NullPointerException ex) {

                System.out.println(ex);

            } catch (IOException ex) {

                System.out.println(ex);

            } catch (Exception ex) {

                System.out.println(ex);
            }


        });

        //list all the courses from the logged user
        btnCourses.setOnAction(e->{

            try {

                //get the loader
                FXMLLoader l = new FXMLLoader(getClass().getResource("MyCourses.fxml"));

                Parent moreDetails = l.load();

                //access courseRegistration controller
                MyCoursesController mc = (MyCoursesController)l.getController();

                //send a new Course to the initialize method on the CourseRegistration
                //since it is a new course


                mc.initialize(userLoggedIn);

                Scene moreDetailsScene = new Scene(moreDetails);
                Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
                stage.hide();
                stage.setScene(moreDetailsScene);
                stage.show();

            } catch (NullPointerException ex) {

                System.out.println(ex);

            } catch (IOException ex) {

                System.out.println(ex);

            } catch (Exception ex) {

                System.out.println(ex);
            }

        });

        //create a new User
        btnCreate.setOnAction(e->{

            try {

                //get the loader
                FXMLLoader l = new FXMLLoader(getClass().getResource("Register.fxml"));

                Parent moreDetails = l.load();

                Scene moreDetailsScene = new Scene(moreDetails);
                Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
                stage.hide();
                stage.setScene(moreDetailsScene);
                stage.show();

            } catch (NullPointerException ex) {

                System.out.println(ex);

            } catch (IOException ex) {

                System.out.println(ex);

            } catch (Exception ex) {

                System.out.println(ex);
            }

        });
    }




}
