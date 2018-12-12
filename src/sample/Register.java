package sample;

import Contract.IUser;
import Entities.AccessLevel;
import Entities.User;
import Spring.SQLConfig;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.swing.*;
import java.io.IOException;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register {
    @FXML
    protected ComboBox cmbType;
    @FXML
    protected TextField txtFirstName;
    @FXML
    protected TextField txtLastName;
    @FXML
    protected TextField txtEmail;
    @FXML
    protected TextField txtAddress;
    @FXML
    protected TextField txtCity;
    @FXML
    protected TextField txtState;
    @FXML
    protected TextField txtPostalCode;
    @FXML
    protected TextField txtPassword;
    @FXML
    protected Button btnPassword;
    @FXML
    protected Button btnRegister;
    @FXML
    protected Button btnBack;
    @FXML
    protected RadioButton genMale;
    @FXML
    protected RadioButton genFemale;
    @FXML
    private void initialize(){

        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(SQLConfig.class);

        IUser sqlUser = context.getBean("userRepository", IUser.class);


        User newUser = new User();
        btnPassword.setOnAction(e -> txtPassword.setText(passGenerator(8)));
        ObservableList<AccessLevel> listOfAccess = FXCollections.observableArrayList(sqlUser.getAccessLevels());

        cmbType.setItems(listOfAccess);
        cmbType.getSelectionModel().selectFirst();

        btnRegister.setOnAction(e ->{
            //initialize
            newUser.setAccessLevel((AccessLevel) cmbType.getSelectionModel().getSelectedItem());
            newUser.setFirstName(txtFirstName.getText());
            newUser.setLastName(txtLastName.getText());
            if(genMale.isCache()) newUser.setSex('M');
            if(genFemale.isCache()) newUser.setSex('F');
            if(isValidEmailId(txtEmail.getText()))
                newUser.setEmail(txtEmail.getText());
            else {
                //JOptionPane.showMessageDialog(null,"Please Enter a Valid Email Address.");
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setContentText("Please enter a valid email address");
                alert.showAndWait();

            }

            newUser.setAddress(txtAddress.getText());
            newUser.setCity(txtCity.getText());
            newUser.setState(txtState.getText());
            newUser.setPwd(txtPassword.getText());
            //check validation and initialize
            try{
                if(newUser.getAccessLevel() != null ||
                        newUser.getFirstName() != null ||
                        newUser.getLastName() != null ||
                        "FM".indexOf(newUser.getSex()) < 0 ||
                        newUser.getEmail() != null ||
                        newUser.getAddress() != null ||
                        newUser.getCity() != null ||
                        newUser.getState() != null ||
                        newUser.getPwd() != null){
                    if(sqlUser.isEmailUnique(newUser.getEmail())){
                        //Save User to Database
                        sqlUser.insertUser(newUser, newUser.getAccessLevel().getId());

                        //JOptionPane.showMessageDialog(null,"Record has been saved successfully.");
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Confirmation");
                        alert.setContentText("New user has been saved!");
                        alert.showAndWait();

                        txtPassword.setText("");
                        txtAddress.setText("");
                        txtCity.setText("");
                        txtEmail.setText("");
                        txtFirstName.setText("");
                        txtFirstName.setText("");
                        txtState.setText("");
                    }else{
                        //JOptionPane.showMessageDialog(null,"This Email is already Used.");
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Warning");
                        alert.setContentText("This Email is already Used.");
                        alert.showAndWait();
                    }
                }else{
                    //JOptionPane.showMessageDialog(null,"Please fill out all fields.");
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setContentText("TPlease fill out all fields.");
                    alert.showAndWait();
                }

            }catch (Exception ex){
                System.out.println(ex);
                JOptionPane.showMessageDialog(null,ex.toString());
            }
        });
        btnBack.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {

                    FXMLLoader l = new FXMLLoader(getClass().getResource("mainpage.fxml"));

                    Parent sreg = l.load();

                    Scene sregScene = new Scene(sreg);

                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                    stage.hide();

                    stage.setScene(sregScene);
                    stage.show();

                }catch (IOException ex){
                    JOptionPane.showMessageDialog(null,ex);
                }
            }
        });

    }

    //Password Generator
    private String passGenerator(int len){
        int x = 0;
        String strMain = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sb = new StringBuilder();
        Random rnd = new Random();
        while(x < len){
            sb.append(strMain.charAt(rnd.nextInt(strMain.length())));
            x++;
        }
        System.out.println(sb.toString());
        return sb.toString();
    }

    //to check the validity of email address
    public static boolean isValidEmailId(String email) {
        String emailPattern = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern p = Pattern.compile(emailPattern);
        Matcher m = p.matcher(email);
        return m.matches();
    }
}
