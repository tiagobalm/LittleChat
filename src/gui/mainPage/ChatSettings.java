package gui.mainPage;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ChatSettings implements Initializable {

    @FXML
    private TextField roomName;

    @FXML
    private ListView<String> membersList;

    @FXML
    private ChoiceBox<String> memberAdd;

    @FXML
    private Button buttonAdd;

    @FXML
    private ChoiceBox<String> memberRemove;

    @FXML
    private Button buttonRemove;

    @FXML
    private Button leave;

    @FXML
    private Button changeName;

    private static String roomNameTemp;

    private static MainPage mainPage;

    private static int roomID;

    public Stage start(int room, String roomName, MainPage main) throws Exception {
        Stage primaryStage = new Stage();

        roomNameTemp = roomName;
        mainPage = main;
        roomID = room;

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
