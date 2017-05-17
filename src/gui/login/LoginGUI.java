package gui.login;

import communication.Communication;
import gui.Controller;
import gui.Manager;
import gui.TransitionControl;
import javafx.animation.*;
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
import java.net.*;
import java.util.Enumeration;
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

    private String IPAddress;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getIPAddress();
        initializeHandlers();
    }

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

    private void loginHandler() {
        String username = this.username.getText();
        String password = this.password.getText();

        try {
            boolean loggedIn = false;

            if(state == MenuState.REGISTER)
                loggedIn = Communication.getInstance().sendRegisterRequest(username, password, IPAddress, 4556);
            if(state == MenuState.LOGIN)
                loggedIn = Communication.getInstance().sendLoginRequest(username, password, IPAddress, 4556);

            if(loggedIn)
                Manager.changeToMainPage(username);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
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

    private void getIPAddress() {

        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                if (iface.isLoopback() || !iface.isUp())
                    continue;

                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while(addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();

                    if (address instanceof Inet6Address) continue;
                    IPAddress = address.getHostAddress();
                }
            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

}
