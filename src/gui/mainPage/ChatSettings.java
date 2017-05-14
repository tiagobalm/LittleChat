package gui.mainPage;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ChatSettings implements Initializable {

    @FXML
    private TextField roomName;

    private static String roomNameTemp;

    public Stage start(String roomName) throws Exception {
        Stage primaryStage = new Stage();

        roomNameTemp = roomName;

        Parent root = FXMLLoader.load(getClass().getResource("chatSettings.fxml"));
        primaryStage.setTitle("");

        Scene scene = new Scene(root, 500, 300);
        scene.getStylesheets().add(
                getClass().getResource("../assets/style.css").toExternalForm());

        primaryStage.setResizable(false);
        primaryStage.setScene(scene);

        return primaryStage;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        roomName.setText(roomNameTemp);
    }
}
