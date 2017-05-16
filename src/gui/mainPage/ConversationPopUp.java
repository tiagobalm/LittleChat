package gui.mainPage;

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

public class ConversationPopUp implements Initializable {

    //@FXML
    //private Button conversationYes;

    @FXML
    private Button conversationNo;

    public Stage start() throws Exception {
        Stage primaryStage = new Stage();

        Parent root = FXMLLoader.load(getClass().getResource("conversationPopUp.fxml"));
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

        conversationNo.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            try {
                Manager.closeConversationPopUp();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

    }
}
