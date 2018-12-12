package sample;

import Contract.ICourse;
import Contract.IRegistration;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

public class MyCoursesController {


    //read spring config java class
    AnnotationConfigApplicationContext context =
            new AnnotationConfigApplicationContext(SQLConfig.class);

    //get the bean from the bean factory and inject the necessary dependency
    ICourse sqlCourse = context.getBean("courseRepository", ICourse.class);

    IRegistration sqlReg = context.getBean("registrationRepository", IRegistration.class);


    @FXML
    private TableView<Course> courseTable;
    @FXML
    private TableColumn<Course, String> courseNameColumn;
    @FXML
    private TableColumn<Course, String> teacherNameColumn;
    @FXML
    private TableColumn<Course, String> startDateColumn;
    @FXML
    private TableColumn<Course, String> finishDateColumn;
    @FXML
    private TableColumn<Course, String> gradeColumn;
    @FXML
    private TableColumn<Course, String> approvedColumn;

    private ObservableList<Course> listOfCourses;

    private User userLoggedIn;




    @FXML
    public void initialize(User user) {

    userLoggedIn = user;

        listOfCourses = FXCollections.observableArrayList(populateList());

        courseNameColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        teacherNameColumn.setCellValueFactory(new PropertyValueFactory<>("teacher"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        finishDateColumn.setCellValueFactory(new PropertyValueFactory<>("finishDate"));
        gradeColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));
        approvedColumn.setCellValueFactory(new PropertyValueFactory<>("isApproved"));

        courseTable.setItems(listOfCourses);

    }

    public List<Course> populateList() {

        return sqlCourse.getCourseByStudent(userLoggedIn.getId());
    }


    @FXML
    public void deleteCourse(ActionEvent e) {

        try {

            int index = courseTable.getSelectionModel().getSelectedIndex();

            if(index != -1) {
                Course courseSelected = courseTable.getItems().get(index);

                //pop up a window asking the user to confirm the deletion
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                        "Are you sure you want to remove the registration for " + courseSelected + " ?",
                        ButtonType.YES, ButtonType.CANCEL);
                alert.showAndWait();

                if (alert.getResult() == ButtonType.YES) {

                    Registration reg = sqlReg.findRegByCourseByUser(courseSelected, userLoggedIn);
                    sqlReg.deleteRegistration(reg);

                }

                initialize(userLoggedIn);

            } else {

                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error message");
                errorAlert.setContentText("Please choose at list one course from the list");
                errorAlert.showAndWait();
            }

        } catch (Exception ex) {

            System.out.println(ex);
        }

    }

    @FXML
    public void backToMain(ActionEvent event) {

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
