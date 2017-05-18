package gui.mainPage;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.*;

public class ChatSettings implements Initializable {

    @FXML
    private TextField roomName;

    @FXML
    private ListView<String> membersList;

    @FXML
    private ComboBox<String> memberAdd;

    @FXML
    private Button buttonAdd;

    @FXML
    private ComboBox<String> memberRemove;

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
        roomID = room;
        mainPage = main;

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

        membersList.setItems(FXCollections.observableArrayList(mainPage.getRoomMembers(roomID)));

        List<String> toAdd = new ArrayList<>(mainPage.getFriendsList());
        //NOT WORKING, TRY TO FIGURE OUT WHY
        toAdd.removeAll(mainPage.getRoomMembers(roomID));
        memberAdd.setVisibleRowCount(3);
        memberAdd.setItems(FXCollections.observableArrayList(mainPage.getFriendsList()));
        memberAdd.getSelectionModel().selectFirst();

        List<String> chatMembers = new ArrayList<>(mainPage.getRoomMembers(roomID));
        chatMembers.remove(mainPage.getUsername());
        memberRemove.setVisibleRowCount(3);
        memberRemove.setItems(FXCollections.observableArrayList(chatMembers));
        memberRemove.getSelectionModel().selectFirst();


    }
}
