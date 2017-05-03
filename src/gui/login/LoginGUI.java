package gui.login;

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

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginGUI extends Application implements Initializable {
    private enum MenuState {MENU, LOGIN, REGISTER}
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
        primaryStage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        menuLoginButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
                e -> goToLogin());
        cancelLogin.addEventHandler(MouseEvent.MOUSE_CLICKED,
                e -> goToMenu());
        menuRegisterButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
                e -> goToRegister());
        loginButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
                e -> System.out.println("Current state:" + state));
    }

    private void setPane(Pane pane, boolean arg) {
        pane.setDisable(!arg);
        pane.setVisible(arg);
    }

    private void disableCurrState() {
        if(state == MenuState.LOGIN || state == MenuState.REGISTER)
            setPane(loginPane, false);
        else if(state == MenuState.MENU)
            setPane(menuPane, false);
    }

    private void goToMenu() {
        disableCurrState();
        state = MenuState.MENU;
        setPane(menuPane, true);
    }

    private void goToLogin() {
        disableCurrState();
        state = MenuState.LOGIN;
        setPane(loginPane, true);
        loginButton.setText("Login");
    }

    private void goToRegister() {
        disableCurrState();
        state = MenuState.REGISTER;
        setPane(loginPane, true);
        loginButton.setText("Register");
    }
}
