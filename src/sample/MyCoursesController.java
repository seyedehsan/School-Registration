package sample;

import Contract.ICourse;
import Spring.SQLConfig;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

public class MyCoursesController {


    //read spring config java class
    AnnotationConfigApplicationContext context =
            new AnnotationConfigApplicationContext(SQLConfig.class);

    //get the bean from the bean factory and inject the necessary dependency
    ICourse sqlCourse = context.getBean("courseRepository", ICourse.class);


    @FXML
    private TableView<Entities.Course> courseTable;
    @FXML
    private TableColumn<Entities.Course, String> courseNameColumn;
    @FXML
    private TableColumn<Entities.Course, String> teacherNameColumn;
    @FXML
    private TableColumn<Entities.Course, String> startDateColumn;
    @FXML
    private TableColumn<Entities.Course, String> finishDateColumn;
    @FXML
    private TableColumn<Entities.Course, String> gradeColumn;
    @FXML
    private TableColumn<Entities.Course, String> approvedColumn;

    public List<Entities.Course> courses;


    @FXML
    private void initialize() {

       courses = sqlCourse.getCourseByStudent((short)1);
       populateList(courses);

    }

    public void populateList(List<Entities.Course> courses) {

        ObservableList<Entities.Course> listofCourses = FXCollections.observableArrayList(courses);

        courseNameColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        teacherNameColumn.setCellValueFactory(new PropertyValueFactory<>("teacher"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        finishDateColumn.setCellValueFactory(new PropertyValueFactory<>("finishDate"));
        gradeColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));
        approvedColumn.setCellValueFactory(new PropertyValueFactory<>("isApproved"));

        courseTable.setItems(listofCourses);

        //code to detect an event by a click
//        table.setOnMouseClicked((MouseEvent event) -> {
//            if (event.getButton().equals(MouseButton.PRIMARY)) {
//                int index = table.getSelectionModel().getSelectedIndex();
//                Person person = table.getItems().get(index);
//                System.out.println(person);
//            }
//        });


    }


//    @FXML
//    public void deleteCourse(ActionEvent e) {
//
//        int index = courseTable.getSelectionModel().getSelectedIndex();
//
//        if(index != -1) {
//            Course courseSelected = courseTable.getItems().get(index);
//            courses.remove(courseSelected);
//            populateList(courses);
//        } else {
//            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
//            errorAlert.setTitle("Error message");
//            errorAlert.setContentText("Please choose at list one course from the list");
//            errorAlert.showAndWait();
//        }
//
//
//    }


}
