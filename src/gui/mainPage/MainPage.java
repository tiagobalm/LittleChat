package gui.mainPage;

import communication.Communication;
import gui.Controller;
import gui.Manager;
import gui.TransitionControl;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class MainPage implements Initializable, Controller<MainPageState> {
    private MainPageState state = MainPageState.ROOMS;

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

    @FXML
    private VBox menuVBox;

    @FXML
    private VBox conversationButtons;

    @FXML
    private VBox friendsButtons;

    @FXML
    private VBox friendRequestButtons;

    @FXML
    private VBox profileButtons;

    @FXML
    private VBox MessagesPanel;

    @FXML
    private TextArea messageInput;

    @FXML
    private Button messageSend;

    private Communication conn;

    public Stage start() throws Exception {
        Stage primaryStage = new Stage();

        Parent root = FXMLLoader.load(getClass().getResource("mainpage.fxml"));
        primaryStage.setTitle("Little Chat");

        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(
                getClass().getResource("../assets/style.css").toExternalForm());

        primaryStage.setResizable(false);
        primaryStage.setScene(scene);

        return primaryStage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        conn = Communication.getInstance();

        roomsButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
                e -> setNewState(MainPageState.ROOMS));

        friendsButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
                e -> setNewState(MainPageState.FRIENDS));

        friendRequestButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
                e -> setNewState(MainPageState.FRIENDREQUEST));

        profileButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
                e -> setNewState(MainPageState.PROFILE));

        logoutButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
                e -> changeToLogin());

        messageSend.addEventHandler(MouseEvent.MOUSE_CLICKED,
                e -> changeToLogin());

        setPaneMaxWidth();

        Button button = new Button("VascoUP");
        Button button2 = new Button("SaraUP");

        button.setMaxWidth(Double.MAX_VALUE);
        button2.setMaxWidth(Double.MAX_VALUE);

        conversationButtons.getChildren().addAll(button, button2);

        HBox hbox =  new HBox();
        HBox hbox1 = new HBox();

        hbox.setAlignment(Pos.BOTTOM_RIGHT);
        Label label1 = new Label("Hello Vasco :)");
        label1.setPadding(new Insets(10));
        label1.getStyleClass().add("hboxMe");
        Label label2 = new Label("Hello Tiago :)");
        label2.getStyleClass().add("hboxThey");
        label2.setPadding(new Insets(10));
        hbox.getChildren().add(label1);
        hbox1.getChildren().add(label2);

        MessagesPanel.setPadding(new Insets(10));
        MessagesPanel.getChildren().addAll(hbox, hbox1);
    }

    @Override
    public void disableCurrState() {
        switch (state) {
            case ROOMS:
                roomsButton.getStyleClass().remove("buttonSelected");
                setPane(roomsPanel, false);
                setPane(MessagesPanel, false);
                break;
            case FRIENDS:
                friendsButton.getStyleClass().remove("buttonSelected");
                setPane(friendsPanel, false);
                break;
            case FRIENDREQUEST:
                friendRequestButton.getStyleClass().remove("buttonSelected");
                setPane(friendRequestPanel, false);
                break;
            case PROFILE:
                profileButton.getStyleClass().remove("buttonSelected");
                setPane(profilePanel, false);
                break;
            default:
                break;
        }
    }

    @Override
    public void setNewState(MainPageState newState) {

        if(newState != state) {
            disableCurrState();
            state = newState;

            switch (state) {
                case ROOMS:
                    roomsButton.getStyleClass().add("buttonSelected");
                    setPane(roomsPanel, true);
                    setPane(MessagesPanel, true);
                    break;
                case FRIENDS:
                    friendsButton.getStyleClass().add("buttonSelected");
                    setPane(friendsPanel, true);
                    break;
                case FRIENDREQUEST:
                    friendRequestButton.getStyleClass().add("buttonSelected");
                    setPane(friendRequestPanel, true);
                    break;
                case PROFILE:
                    profileButton.getStyleClass().add("buttonSelected");
                    setPane(profilePanel, true);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void setPane(Pane pane, boolean show) {
        TransitionControl.showTransition(pane, show, getPaneTransition(pane, show));
    }

    @Override
    public TranslateTransition getPaneTransition(Pane pane, boolean show){
        TranslateTransition tt;
        int orgX = -200;
        int dstX = 0;

        tt = new TranslateTransition(Duration.millis(250), pane);
        tt.setCycleCount(1);
        tt.setAutoReverse(true);

        if( !show ) {
            int tmp = orgX;
            orgX = dstX;
            dstX = tmp;
        }

        tt.setFromX(orgX);
        tt.setToX(dstX);

        return tt;
    }

    public void changeToLogin() {

        try {
            Manager.changeToLogin();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setPaneMaxWidth() {

        menuVBox.setMaxHeight(Double.MAX_VALUE);
        roomsButton.setMaxWidth(Double.MAX_VALUE);
        friendsButton.setMaxWidth(Double.MAX_VALUE);
        friendRequestButton.setMaxWidth(Double.MAX_VALUE);
        profileButton.setMaxWidth(Double.MAX_VALUE);
        logoutButton.setMaxWidth(Double.MAX_VALUE);
    }

    private void sendMessage() {

        System.out.println("");
    }
}
