package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

import java.util.Random;

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
    protected Button btnCancel;
    @FXML
    protected RadioButton genMale;
    @FXML
    protected RadioButton genFemale;
    @FXML
    private void initialize(){
        btnPassword.setOnAction(e -> txtPassword.setText(passGenerator(8)));
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
}
