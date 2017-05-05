package gui.login;

import gui.Controller;
import gui.TransitionControl;
import javafx.animation.*;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginGUI extends Application implements Initializable, Controller<MenuState> {
    private MenuState state = MenuState.MENU;

    @FXML
    private Button menuLoginButton;
    @FXML
    private Button loginButton;
    @FXML
    private Button cancelLogin;
    @FXML
    private Button menuRegisterButton;
    @FXML
    private Pane menuPane;
    @FXML
    private Pane loginPane;
    @FXML
    private TextField username;
    @FXML
    private TextField password;

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        primaryStage.setTitle("LittleChat");
        Scene scene = new Scene(root, 600, 400);
        scene.getStylesheets().add(
                getClass().getResource("../assets/style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        menuLoginButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
                e -> setNewState(MenuState.LOGIN));
        cancelLogin.addEventHandler(MouseEvent.MOUSE_CLICKED,
                e -> setNewState(MenuState.MENU));
        menuRegisterButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
                e -> setNewState(MenuState.REGISTER));
        loginButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
                e -> {
                        System.out.println("Current state:" + state);

                        String username = this.username.getText();
                        String password = this.password.getText();

                        System.out.println("Username: " + username + "\n" + "Password: " + password);
                     });
    }


    @Override
    public void setPane(Pane pane, boolean show) {
        TransitionControl.showTransition(pane, show, getPaneTransition(pane, show));
    }

    @Override
    public void disableCurrState() {
        if(state == MenuState.LOGIN || state == MenuState.REGISTER)
            setPane(loginPane, false);
        else if(state == MenuState.MENU)
            setPane(menuPane, false);
    }

    @Override
    public void setNewState(MenuState newState) {
        disableCurrState();
        state = newState;
        switch (state) {
            case MENU:
                setPane(menuPane, true);
                break;
            case LOGIN:
                setPane(loginPane, true);
                loginButton.setText("Login");
                break;
            case REGISTER:
                setPane(loginPane, true);
                loginButton.setText("Register");
                break;
        }
    }

    @Override
    public TranslateTransition getPaneTransition(Pane pane, boolean show){
        TranslateTransition tt;
        int orgX = (state == MenuState.MENU) ? -600 : 600;
        int dstX = 0;

        tt = new TranslateTransition(Duration.millis(250), pane);
        tt.setCycleCount(1);
        tt.setAutoReverse(true);

        if( show ) {
            tt.setFromX(orgX);
            tt.setToX(dstX);
        } else {
            tt.setFromX(dstX);
            tt.setToX(orgX);
        }

        return tt;
    }
}
