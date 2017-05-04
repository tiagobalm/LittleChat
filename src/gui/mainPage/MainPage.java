package gui.mainPage;

import gui.Controller;
import gui.TransitionControl;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class MainPage extends Application implements Initializable, Controller<MainPageState> {
    private MainPageState state = MainPageState.EMPTY;

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


    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("mainpage.fxml"));
        primaryStage.setTitle("Little Chat");

        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());

        Scene scene = new Scene(root, bounds.getWidth(), bounds.getHeight());
        scene.getStylesheets().add(
                getClass().getResource("../assets/style.css").toExternalForm());

        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        roomsButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
                e -> setNewState(MainPageState.ROOMS));

        friendsButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
                e -> setNewState(MainPageState.FRIENDS));

        friendRequestButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
                e -> setNewState(MainPageState.FRIENDREQUEST));

        profileButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
                e -> setNewState(MainPageState.PROFILE));

        logoutButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
                e -> System.out.println("logout"));

        menuVBox.setSpacing(0);
        menuVBox.setPadding(new Insets(0, 0, 0, 0));

        menuVBox.setMaxHeight(Double.MAX_VALUE);
        roomsButton.setMaxWidth(Double.MAX_VALUE);
        friendsButton.setMaxWidth(Double.MAX_VALUE);
        friendRequestButton.setMaxWidth(Double.MAX_VALUE);
        profileButton.setMaxWidth(Double.MAX_VALUE);
        logoutButton.setMaxWidth(Double.MAX_VALUE);
    }


    @Override
    public void disableCurrState() {
        switch (state) {
            case ROOMS:
                setPane(roomsPanel, false);
                break;
            case FRIENDS:
                setPane(friendsPanel, false);
                break;
            case FRIENDREQUEST:
                setPane(friendRequestPanel, false);
                break;
            case PROFILE:
                setPane(profilePanel, false);
                break;
            default:
                break;
        }
    }

    @Override
    public void setNewState(MainPageState newState) {
        disableCurrState();
        state = state == newState ? MainPageState.EMPTY : newState;

        switch(state) {
            case ROOMS:
                setPane(roomsPanel, true);
                break;
            case FRIENDS:
                setPane(friendsPanel, true);
                break;
            case FRIENDREQUEST:
                setPane(friendRequestPanel, true);
                break;
            case PROFILE:
                setPane(profilePanel, true);
                break;
            default:
                break;
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
}
