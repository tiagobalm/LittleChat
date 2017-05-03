package gui.login;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by vasco on 03-05-2017.
 */
public class RegisterButton implements Initializable {

    @FXML //  fx:id="register"
    private Button registerButton; // Value injected by FXMLLoader

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        assert registerButton != null :
                "fx:id=\"myButton\" was not injected: check your FXML file 'login.fxml'.";
    }
}
