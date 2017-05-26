package gui.login;

import communication.Communication;
import gui.Controller;
import gui.Manager;
import gui.TransitionControl;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginGUI implements Initializable, Controller<MenuState> {
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

    /**
     * Creates the stage for the login/register page.
     * @return The stage.
     * @throws IOException Throws IOException if the fxml file is not found.
     */
    public Stage start() throws IOException {
        Stage primaryStage = new Stage();

        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        primaryStage.setTitle("LittleChat");
        Scene scene = new Scene(root, 600, 400);
        scene.getStylesheets().add(
                getClass().getResource("../assets/style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);

        return primaryStage;
    }

    /**
     * Initializes the stage. Called after FXMLLoader. Initializes handlers.
     * @param location location
     * @param resources resource
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeHandlers();
    }

    /**
     * Initialize handlers.
     */
    private void initializeHandlers() {

        menuLoginButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
                e -> setNewState(MenuState.LOGIN));
        cancelLogin.addEventHandler(MouseEvent.MOUSE_CLICKED,
                e -> setNewState(MenuState.MENU));
        menuRegisterButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
                e -> setNewState(MenuState.REGISTER));
        loginButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
                e -> loginHandler());
        password.setOnKeyReleased(keyEvent -> {
            if(keyEvent.getCode() == KeyCode.ENTER) loginHandler();
        });
    }

    /**
     * Login handler.
     */
    private void loginHandler() {
        String username = this.username.getText();
        String password = this.password.getText();

        try {
            boolean loggedIn = false;

            if(state == MenuState.REGISTER)
                loggedIn = Communication.getInstance().sendRegisterRequest(username, password);
            if(state == MenuState.LOGIN)
                loggedIn = Communication.getInstance().sendLoginRequest(username, password);

            if(loggedIn)
                Manager.changeToMainPage(username);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    /**
     * Set panel.
     * @param pane Panel.
     * @param show Show.
     */
    @Override
    public void setPane(Pane pane, boolean show) {
        TransitionControl.showTransition(pane, show, getPaneTransition(pane, show));
    }

    /**
     * Disable current state.
     */
    @Override
    public void disableCurrState() {
        if(state == MenuState.LOGIN || state == MenuState.REGISTER)
            setPane(loginPane, false);
        else if(state == MenuState.MENU)
            setPane(menuPane, false);
    }

    /**
     * Set a new state.
     *
     * @param newState State.
     */
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

    /**
     * Get pane transition.
     * @param pane Panel.
     * @param show Show
     * @return TranslateTransition.
     */
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
