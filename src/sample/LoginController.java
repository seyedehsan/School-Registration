package sample;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import Contract.ICourse;
import Contract.IUser;
import Spring.SQLConfig;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.swing.JOptionPane;


public class LoginController {

    //This is the 1st page of the program
        @FXML
        private ToggleGroup loginUserToggleGroup;

        @FXML
        private Label lbselectedUserType;

        @FXML
        private Label lbLoginProgress;

        @FXML
        private RadioButton rbtStudent;

        @FXML
        private RadioButton rbtEmployee;

        @FXML
        private AnchorPane AnchorPaneBackGround;

        @FXML
        private AnchorPane ancorPane2;

        @FXML
        private TextField textfieldUserName;

        @FXML
        private TextField textfieldPassword;

        @FXML
        private Button btnLogin;


    //do the injections to get access to the database
    //read spring config java class
    AnnotationConfigApplicationContext context =
            new AnnotationConfigApplicationContext(SQLConfig.class);

    //get the bean from the bean factory and inject the necessary dependency
    ICourse sqlCourse = context.getBean("courseRepository", ICourse.class);

    IUser sqlUser = context.getBean("userRepository", IUser.class);


        //- this variable holds the email address of the authenticated user (logged in user)
        protected String currUserEmail;

        private void initComponents(){
            String mainPaneBackground = "-fx-background-image: url('/image/14283946_0fa478ed23_b.jpg');\n" +
                    " -fx-background-repeat: stretch;   \n" +
                    " -fx-background-size: 950 550;\n" +
                    " -fx-background-position: center center;\n" +
                    " -fx-effect: dropshadow(three-pass-box, black, 30, 0.5, 0, 0); ";

            this.AnchorPaneBackGround.setStyle(mainPaneBackground);

            //loginUserToggleGroup = new ToggleGroup();
            //this.rbtStudent.setToggleGroup(loginUserToggleGroup);
            //this.rbtEmployee.setToggleGroup(loginUserToggleGroup);

            //- Initial value to display for user type
            this.lbselectedUserType.setText("STUDENT");

            //- Initial user type radiobutton selection
            this.rbtStudent.setSelected(true);

            //- Initial display text for login success
            this.lbLoginProgress.setText("");

        }

        @FXML
        void rbtUserTypeSelected(ActionEvent event) {
            if(this.rbtStudent.isSelected())
            {
                this.lbselectedUserType.setText("STUDENT");
            }

            if(this.rbtEmployee.isSelected())
            {
                this.lbselectedUserType.setText("EMPLOYEE");
            }
        }

        @FXML
        void loginBtnClicked(ActionEvent event) {

            try{
                String userName= this.textfieldUserName.getText();
                String password = this.textfieldPassword.getText();

                //Validate a username was entered
                if(userName.isEmpty())
                {
                    System.out.println("Username missing.");
                    throw new IllegalArgumentException("Username missing. "
                            + "Make sure to type in a valid username.");
                }

                //Validate a password was entered
                if(password.isEmpty())
                {
                    System.out.println("Password missing.");
                    throw new IllegalArgumentException("Password missing.");
                }

                //- Checks if the username exists in the DB
                if(sqlUser.isEmailUnique(userName) )
                {
                    //Check the password
                    System.out.println("User does not exist. if block");
                    //- throw this specific exception just to display the right error message
                    throw new SQLException("User Does not exist.");
                }

                //- Checks whether the entered password is correct for an existing user
                if(sqlUser.isPasswordCorrect(userName, password) )
                {
                    //- Keeps info on the current session user
                    this.currUserEmail = userName;
                    System.out.println("User entered password correct.");
                    this.lbLoginProgress.setText("Authentication successful. Loading your page...");
                    loadMainPage(event);

                }

                /*-------------------- Next Page kicks in here ---------------------*/
                //- Once login is successful, clean the username and paassword fields
                this.textfieldUserName.setText("");
                this.textfieldPassword.setText("");

            }
            catch(SQLException ex){
                System.out.println("User does not exist.");
                this.lbLoginProgress.setText("Username or Password incorrect. Try again.");
            }
            catch (IllegalArgumentException ex){
                this.lbLoginProgress.setText(ex.getMessage());
            }
        }

        public void loadMainPage(ActionEvent event) {

            try {
                //get the loader
                FXMLLoader l = new FXMLLoader(getClass().getResource("mainpage.fxml"));

                Parent moreDetails = l.load();

                MainPage mp = (MainPage)l.getController();

                mp.initialize(currUserEmail);


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





