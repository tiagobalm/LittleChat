package gui.mainPage;

import communication.Communication;
import gui.Manager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AnswerFriend implements Initializable {

    @FXML
    private Button accept;

    @FXML
    private Button decline;

    private static String username;
    private static MainPage mainPage;

    public Stage start(String user, MainPage page) throws Exception {
        Stage primaryStage = new Stage();
        username = user;
        mainPage = page;

        Parent root = FXMLLoader.load(getClass().getResource("answerFriend.fxml"));
        primaryStage.setTitle("");

        Scene scene = new Scene(root, 333, 85);
        scene.getStylesheets().add(
                getClass().getResource("../assets/style.css").toExternalForm());

        primaryStage.setResizable(false);
        primaryStage.setScene(scene);

        return primaryStage;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        accept.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            Communication.getInstance().sendAnswerFriend(username, "True");
            mainPage.removeRequestedFriend(username, "True");
            try {
                Manager.closeAnswerFriendPop();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        decline.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            Communication.getInstance().sendAnswerFriend(username, "False");
            mainPage.removeRequestedFriend(username, "False");
            try {
                Manager.closeAnswerFriendPop();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

    }
}
