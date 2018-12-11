package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

public class MainPage {
    @FXML
    protected Button btnAdd;
    @FXML
    protected ImageView img;
    @FXML
    private void initialize(){
        btnAdd.setGraphic(img);
    }
}
