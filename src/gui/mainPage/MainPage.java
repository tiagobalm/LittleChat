package gui.mainPage;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MainPage extends Application implements Initializable {
    private enum MainPageState {EMPTY, ROOMS, FRIENDS, FRIENDREQUEST, PROFILE}
    private MainPageState state = MainPageState.EMPTY;

    @FXML
    private Button roomsButton;

    @FXML
    private Button friendsButton;

    @FXML
    private Button friendRequestButton;

    @FXML
    private Button profileButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Pane roomsPanel;

    @FXML
    private Pane friendsPanel;

    @FXML
    private Pane friendRequestPanel;

    @FXML
    private Pane profilePanel;


    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("mainpage.fxml"));
        primaryStage.setTitle("Little Chat");
        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(
                getClass().getResource("../assets/style.css").toExternalForm());
        primaryStage.setMaximized(true);
        primaryStage.setResizable(false);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        roomsButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
                e -> setNewState(MainPageState.ROOMS));

        friendsButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
                e -> setNewState(MainPageState.FRIENDS));

        friendRequestButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
                e -> setNewState(MainPageState.FRIENDREQUEST));

        profileButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
                e -> setNewState(MainPageState.PROFILE));

        logoutButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
                e -> System.out.println("logout"));
    }

    private void disableCurrState() {
        switch (state) {
            case ROOMS:
                setPane(roomsPanel, false);
                break;
            case FRIENDS:
                setPane(friendsPanel, false);
                break;
            case FRIENDREQUEST:
                setPane(friendRequestPanel, false);
                break;
            case PROFILE:
                setPane(profilePanel, false);
                break;
            default:
                break;
        }
    }

    private void setNewState(MainPageState newState) {

        if(newState != state) {
            disableCurrState();

            switch (newState) {
                case ROOMS:
                    setPane(roomsPanel, true);
                    state = MainPageState.ROOMS;
                    break;
                case FRIENDS:
                    setPane(friendsPanel, true);
                    state = MainPageState.FRIENDS;
                    break;
                case FRIENDREQUEST:
                    setPane(friendRequestPanel, true);
                    state = MainPageState.FRIENDREQUEST;
                    break;
                case PROFILE:
                    setPane(profilePanel, true);
                    state = MainPageState.PROFILE;
                    break;
                default:
                    break;
            }
        }
    }

    private void setPane(Pane pane, boolean arg) {
        pane.setDisable(!arg);
        pane.setVisible(arg);
    }
}
