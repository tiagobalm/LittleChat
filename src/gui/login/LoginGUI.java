package gui.login;

import javafx.animation.*;
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
import javafx.util.Duration;

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
        primaryStage.setResizable(false);
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

    @FXML
    private void setPane(Pane pane, boolean show) {
        if( show ) {
            pane.setDisable(!show);
            pane.setVisible(show);
            TranslateTransition tt = getPaneTransition(pane, show);
            tt.play();
        } else {
            TranslateTransition tt = getPaneTransition(pane, show);
            tt.setOnFinished(e -> {
                tt.getNode().setVisible(false);
                tt.getNode().setDisable(true);
                System.out.println(tt.getNode().isDisable());
                System.out.println(tt.getNode().isVisible());
            });
            tt.play();
        }
    }

    private TranslateTransition getPaneTransition(Pane pane, boolean show){
        TranslateTransition tt;
        int orgX = (state == MenuState.MENU) ? -600 : 600;
        int dstX = 0;

        tt = new TranslateTransition(Duration.millis(500), pane);
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
