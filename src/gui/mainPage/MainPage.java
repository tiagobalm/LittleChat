package gui.mainPage;

import gui.login.LoginGUI;
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
        primaryStage.setMaximized(true);
        Scene scene = new Scene(root, 600, 400);
        scene.getStylesheets().add(
                getClass().getResource("../assets/style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        /*roomsButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
                e -> goToLogin());*/
    }

    private void disableCurrState() {
        /*if(state == LoginGUI.MenuState.LOGIN || state == LoginGUI.MenuState.REGISTER)
            setPane(loginPane, false);
        else if(state == LoginGUI.MenuState.MENU)
            setPane(menuPane, false);*/
    }

    private void setPane(Pane pane, boolean arg) {
        pane.setDisable(!arg);
        pane.setVisible(arg);
    }
}
