package gui.login;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginGUI extends Application implements Initializable {
    @FXML
    private Button loginButton;
    @FXML
    private Button registerButton;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        primaryStage.setTitle("LittleChat");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loginButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
                e -> System.out.println("Login dude"));
        registerButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
                e -> System.out.println("Register dude"));
    }
}
