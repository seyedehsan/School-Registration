package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader l = new FXMLLoader(getClass().getResource("Login.fxml"));

        Parent moreDetails = l.load();

        LoginController mp = (LoginController)l.getController();

        mp.initComponents();

        Scene moreDetailsScene = new Scene(moreDetails);
        Stage stage = primaryStage;
        stage.hide();
        stage.setScene(moreDetailsScene);
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
