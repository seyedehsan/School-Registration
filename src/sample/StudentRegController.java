package sample;

import Entities.Course;
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

import java.io.IOException;
import java.util.ArrayList;

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

    public ArrayList<Course> courses;

    @FXML
    private void initialize() {

//        courses = MyCoursesController.generateCourseList();
//        populateList(courses);

    }


    public void populateList(ArrayList<Course> courses) {

        ObservableList<Course> listofCourses = FXCollections.observableArrayList(courses);

        courseNameColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        teacherNameColumn.setCellValueFactory(new PropertyValueFactory<>("teacherName"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        finishDateColumn.setCellValueFactory(new PropertyValueFactory<>("finishDate"));


        listCourses.setItems(listofCourses);

        //code to detect an event by a click
//        table.setOnMouseClicked((MouseEvent event) -> {
//            if (event.getButton().equals(MouseButton.PRIMARY)) {
//                int index = table.getSelectionModel().getSelectedIndex();
//                Person person = table.getItems().get(index);
//                System.out.println(person);
//            }
//        });


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

}
