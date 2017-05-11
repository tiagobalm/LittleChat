package gui.mainPage;

import message.Message;
import workers.ReadThread;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import workers.Worker;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.*;

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

    private BlockingQueue<Message> messages;

    private int room = 1;

    private static final int numberOfWorkerThreads = 10;
    private static final ExecutorService executor = Executors.newFixedThreadPool(numberOfWorkerThreads);

    private static ReadThread readThread;

    private ConcurrentHashMap<Integer, ArrayList<String>> chatMessages;

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
        messages = new ArrayBlockingQueue<>(500);
        chatMessages = new ConcurrentHashMap<>();
        MessagesPanel.setPadding(new Insets(10));

        initializeHandlers();
        setPaneMaxWidth();
        startWorkerThreads();
        getRooms();
    }

    private void getRooms() {
        Communication.getInstance().getRooms();
    }

    public void addRooms(ArrayList<String> rooms) {

        for(String room : rooms) {
            String[] roomParameters = room.split("\0");

            System.out.println("Room parameters: " + roomParameters[0] + " " + roomParameters[1]);

            Button button = new Button(roomParameters[1]);

            button.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> roomButtonHandler(event));
            button.setId(roomParameters[0]);
            button.setMaxWidth(Double.MAX_VALUE);
            button.getStyleClass().add("roomsButtons");

            conversationButtons.getChildren().addAll(button);
        }
    }

    private void roomButtonHandler(MouseEvent event) {
        Integer buttonID = Integer.parseInt(((Button)event.getSource()).getId());
        room = buttonID;

        if(chatMessages.containsKey(buttonID))
            addRoomMessagesToPanel(buttonID);
        else
            getRoomMessages(buttonID);
    }

    private void initializeHandlers() {
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
                e -> sendMessage(messageInput.getText()));
    }

    private void startWorkerThreads() {
        System.out.println("Starting worker threads");

        readThread = new ReadThread(messages);
        readThread.start();
        System.out.println("After Read Thread");

        for( int i = 0; i < numberOfWorkerThreads; i++ ) {
            executor.submit(new Worker(this));
        }
    }

    public void stopWorkers() {
        executor.shutdownNow();
        readThread.stopThread();
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

    public ConcurrentHashMap<Integer, ArrayList<String>> getChatMessages() {
        return chatMessages;
    }

    public void changeToLogin() {
        boolean loggedOut =  Communication.getInstance().sendLogoutRequest();

        if(loggedOut) {
            try {
                Manager.changeToLogin();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
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

    private void sendMessage(String message) {
        HBox hbox = new HBox();
        Label label1 = new Label(message);

        hbox.setAlignment(Pos.BOTTOM_RIGHT);
        label1.setPadding(new Insets(10));
        label1.getStyleClass().add("hboxMe");

        hbox.getChildren().add(label1);
        MessagesPanel.getChildren().addAll(hbox, hbox);

        Communication.getInstance().sendMessageRequest(room, message);
    }

    public BlockingQueue<Message> getMessages() { return messages; }

    private void getRoomMessages(Integer room) {
        int counter = 0;

        while(counter < 3) {
            Communication.getInstance().getRoomMessages(room);

            try {
                Thread.sleep(1000 * (long)Math.pow(2, counter));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(chatMessages.containsKey(room))
                addRoomMessagesToPanel(room);
            counter++;
        }

        if(counter == 3) System.out.println("Unable to retrieve messages from room with ID " + room);
    }

    public void addNewMessage(String from, int to, String message) {
        System.out.println("From: " + from + " To: " + to);

        if(chatMessages.containsKey(to))
            chatMessages.get(to).add(message);

        if(room == to)
            addMessageToPanel(from, message);
    }

    private void addRoomMessagesToPanel(Integer room) {
        MessagesPanel.getChildren().removeAll();

        for(String message : chatMessages.get(room)) {
            String[] messageParameters = message.split("\0");
            addMessageToPanel(messageParameters[0], messageParameters[1]);
        }
    }

    private void addMessageToPanel(String username, String message) {

        HBox hbox = new HBox();
        Label messageLabel = new Label(username + ": " + message);

        messageLabel.getStyleClass().add("hboxThey");
        messageLabel.setPadding(new Insets(10));
        hbox.getChildren().add(messageLabel);

        MessagesPanel.getChildren().addAll(hbox);
    }
}
