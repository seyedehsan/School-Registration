package sample;

import Entities.Course;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class MainPage {
    @FXML
    protected Button btnAdd;
    @FXML
    protected ImageView img;

    @FXML
    private Button btnCourseList;

    @FXML
    private Button btnCreateCourse;


    @FXML
    private void initialize(){

        btnAdd.setGraphic(img);

        btnCourseList.setOnAction(e->{

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
    }




}
