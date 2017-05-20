package gui.mainPage;

import communication.Communication;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

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

        primaryStage.setOnCloseRequest((WindowEvent t) -> mainPage.setChatSettings(null));

        return primaryStage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initializeRoomName();
        initializeMemberList();
        initializeAddMembers();
        initializeRemoveMembers();
        initializeLeaveButton();

        mainPage.setChatSettings(this);
    }

    private void initializeLeaveButton() {
        leave.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> Communication.getInstance().deleteFromRoom(mainPage.getUsername(), roomID));
    }

    private void initializeRemoveMembers() {
        List<String> chatMembers = new ArrayList<>(mainPage.getRoomMembers(roomID));
        chatMembers.remove(mainPage.getUsername());
        memberRemove.setVisibleRowCount(3);
        memberRemove.setItems(FXCollections.observableArrayList(chatMembers));
        memberRemove.getSelectionModel().selectFirst();

        buttonRemove.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> Communication.getInstance().deleteFromRoom(memberRemove.getValue(), roomID));
    }

    private void initializeAddMembers() {
        List<String> toAdd = new ArrayList<>(mainPage.getFriendsList());
        toAdd.removeAll(mainPage.getRoomMembers(roomID));

        memberAdd.setVisibleRowCount(3);
        memberAdd.setItems(FXCollections.observableArrayList(toAdd));
        memberAdd.getSelectionModel().selectFirst();

        buttonAdd.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> Communication.getInstance().addToRoom(memberAdd.getValue(), roomID));
    }

    private void initializeMemberList() {
        membersList.setItems(FXCollections.observableArrayList(mainPage.getRoomMembers(roomID)));
    }

    private void initializeRoomName() {
        roomName.setText(roomNameTemp);

        changeName.addEventHandler(MouseEvent.MOUSE_CLICKED, event ->
                Communication.getInstance().sendChangeRoomName(roomID, roomName.getText()));
    }

    public void changeRoomName(String roomID, String newName) {

        if(Integer.parseInt(roomID) == ChatSettings.roomID)
            roomName.setText(newName);
    }

    public void addToRoom(String roomID, String userToAdd) {

        if(ChatSettings.roomID == Integer.parseInt(roomID)) {
            memberAdd.getItems().remove(userToAdd);
            memberAdd.getSelectionModel().selectFirst();
            memberRemove.getItems().add(userToAdd);

            membersList.setItems(FXCollections.observableArrayList(mainPage.getRoomMembers(Integer.parseInt(roomID))));
        }
    }

    public void deleteFromRoom(String roomID, String username) {

        if(ChatSettings.roomID == Integer.parseInt(roomID)) {
            memberRemove.getItems().remove(username);
            memberRemove.getSelectionModel().selectFirst();

            if(mainPage.getFriendsList().contains(username))
                memberAdd.getItems().add(username);

            membersList.setItems(FXCollections.observableArrayList(mainPage.getRoomMembers(Integer.parseInt(roomID))));
        }
    }
}
