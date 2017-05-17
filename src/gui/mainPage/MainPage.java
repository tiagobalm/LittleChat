package gui.mainPage;

import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.text.*;
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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import workers.Worker;

import java.net.URL;
import java.util.*;
import java.util.concurrent.*;

public class MainPage implements Initializable, Controller<MainPageState> {
    private MainPageState state = MainPageState.ROOMS;

    private static String roomsID = "Room";
    private static String friendsID = "Friend";
    private static String friendRequestWaitingID = "FriendRequestWaiting";
    private static String friendRequestAskingID = "FriendRequestAsking";

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
    private TextField friendRequestInput;

    //@FXML
    //private VBox profileButtons;

    @FXML
    private VBox messagesPanel;

    @FXML
    private ScrollPane messagesScrollPane;

    @FXML
    private TextArea messageInput;

    @FXML
    private Button settingsButton;

    private static String username;

    private BlockingQueue<Message> messages;

    private int room = -1;

    private static int numberOfWorkerThreads = 10;
    private static ExecutorService executor = Executors.newFixedThreadPool(numberOfWorkerThreads);

    private static ReadThread readThread;

    private ConcurrentHashMap<Integer, List<String>> chatMessages, chatMembers;
    private CopyOnWriteArrayList<String> friends;

    public Stage start() throws Exception {
        Stage primaryStage = new Stage();
        executor = Executors.newFixedThreadPool(numberOfWorkerThreads);

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
        chatMembers = new ConcurrentHashMap<>();
        friends = new CopyOnWriteArrayList<>();
        messageInput.setWrapText(true);
        messagesScrollPane.vvalueProperty().bind(messagesPanel.heightProperty());

        initializeHandlers();
        setPaneMaxWidth();
        startWorkerThreads();
        getRooms();
        getFriends();
        getFriendRequests();
    }

    public void setUsername(String username) { MainPage.username = username; }

    public String getUsername() { return username; }

    private void getRooms() { Communication.getInstance().getRooms(); }

    private void getFriends() {
        Communication.getInstance().getFriends();
    }

    private void getFriendRequests() { Communication.getInstance().getFriendRequests(); }

    private void roomButtonHandler(MouseEvent event) {
        Integer buttonID = Integer.parseInt(((Button)event.getSource()).getId().replaceAll(roomsID, ""));

        changeActiveRoom(room, buttonID);
        room = buttonID;
        Platform.runLater(() -> messageInput.requestFocus());

        if(chatMessages.containsKey(buttonID))
            addRoomMessagesToPanel(buttonID);
        else
            getRoomMessages(buttonID);

        toggleSettingButton(true);
    }

    private void changeActiveRoom(int room, Integer buttonID) {
        if(room != -1) {
            Button previousRoom = (Button) Manager.getScene().lookup("#" + roomsID + room);
            previousRoom.getStyleClass().remove("roomsButtons-selected");
        }

        Button nextRoom = (Button)Manager.getScene().lookup("#" + roomsID + buttonID);
        nextRoom.getStyleClass().add("roomsButtons-selected");
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

        messageInput.setOnKeyReleased(keyEvent -> {
            if(keyEvent.getCode() == KeyCode.ENTER)
            {
                sendMessage(messageInput.getText());
                messageInput.setText("");
            }
        });

        friendRequestInput.setOnKeyReleased(keyEvent -> {
            if(keyEvent.getCode() == KeyCode.ENTER) {
                sendFriendRequest(friendRequestInput.getText());
                friendRequestInput.setText("");
            }
        });

        settingsButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
                event -> {
                    try {
                        Button roomButton = (Button) Manager.getScene().lookup("#" + roomsID + room);
                        Manager.showChatSettings(roomButton.getText());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
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
                setPane(messagesPanel, false);
                toggleInput(false);
                toggleSettingButton(false);
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

    private void toggleInput(boolean show) {
        if(show) Platform.runLater(() -> messageInput.requestFocus());
        messageInput.setVisible(show);
        messageInput.setDisable(!show);
    }

    private void toggleSettingButton(boolean show) {
        settingsButton.setVisible(show);
        settingsButton.setDisable(!show);
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
                    setPane(messagesPanel, true);
                    toggleInput(true);
                    if(room != -1)
                        toggleSettingButton(true);
                    break;
                case FRIENDS:
                    friendsButton.getStyleClass().add("buttonSelected");
                    setPane(friendsPanel, true);
                    break;
                case FRIENDREQUEST:
                    friendRequestButton.getStyleClass().add("buttonSelected");
                    setPane(friendRequestPanel, true);
                    Platform.runLater(() -> friendRequestInput.requestFocus());
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

    public ConcurrentHashMap<Integer, List<String>> getChatMessages() {
        return chatMessages;
    }

    private void changeToLogin() {
        Communication.getInstance().sendLogoutRequest();
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
        if( message.length() == 0 )
            return ;
        addMessageToPanel(MainPage.username, message);
        Communication.getInstance().sendMessageRequest(room, message);
    }

    private void sendFriendRequest(String text) {
        if( text.length() == 0 )
            return ;
        Communication.getInstance().sendFriendRequest(text);

        addWaitingFriendRequest(text);
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

            if(chatMessages.containsKey(room)) {
                addRoomMessagesToPanel(room);
                counter = 4;
            }
            counter++;
        }

        if(counter == 3) System.out.println("Unable to retrieve messages from room with ID " + room);
    }

    public void addRooms(List<String> rooms) {

        for(String room : rooms)
            addRoom(room);
    }

    private void addRoom(String room) {
        String[] roomParameters = room.split("\0");

        Button button = new Button(roomParameters[1]);
        button.setId(roomsID+roomParameters[0]);
        button.addEventHandler(MouseEvent.MOUSE_CLICKED, this::roomButtonHandler);
        button.setMaxWidth(Double.MAX_VALUE);
        button.getStyleClass().add("roomsButtons");

        /*List<String> members = new ArrayList<>();
        for(int i = 2; i < roomParameters.length; i++)
            members.add(roomParameters[i]);
        chatMembers.put(Integer.parseInt(roomParameters[0]), members);*/

        conversationButtons.getChildren().add(button);
    }

    public void addNewMessage(String from, int to, String message) {
        System.out.println("From: " + from + " To: " + to);
        if(chatMessages.containsKey(to))
            chatMessages.get(to).add(message);
        if(room == to)
            addMessageToPanel(from, message);
    }

    private void addRoomMessagesToPanel(Integer room) {
        messagesPanel.getChildren().clear();

        for(String message : chatMessages.get(room)) {
            String[] messageParameters = message.split("\0");
            for( String m : messageParameters )
                System.out.println(m);
            if( messageParameters.length != 2 )
                continue;
            addMessageToPanel(messageParameters[0].trim(), messageParameters[1].trim());
        }
    }

    private void addMessageToPanel(String username, String message) {

        HBox hbox = new HBox();
        Label messageLabel = new Label(username + ": " + message);
        messageLabel.setMaxWidth(300);
        messageLabel.setMaxHeight(Integer.MAX_VALUE);
        messageLabel.setWrapText(true);
        VBox.setVgrow(messageLabel, Priority.ALWAYS);

        if(!username.equals(MainPage.username)) {
            messageLabel.getStyleClass().add("hboxThey");
        } else {
            messageLabel.getStyleClass().add("hboxMe");
            hbox.setAlignment(Pos.BOTTOM_RIGHT);
        }
        messageLabel.setPadding(new Insets(10));
        hbox.getChildren().add(messageLabel);

        messagesPanel.getChildren().add(hbox);
    }

    public void addFriends(List<String> friendsList) {
        for(String friend: friendsList)
            addFriend(friend);
    }

    private void addFriend(String friend) {

        if(!friends.contains(friend)) friends.add(friend);

        Button button = new Button(friend);
        button.setId(friendsID+friend);
        button.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            try {
                Manager.showConversationPopUp(button.getId());

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        button.setMaxWidth(Double.MAX_VALUE);
        button.getStyleClass().add("roomsButtons");

        friendsButtons.getChildren().add(button);
    }

    public void addFriendRequests(List<String> friendRequests) {
        /*for(String friend: friendRequests) {

            Button button = new Button(friend);
            button.setId(friendRequestsID+friend);
            button.setMaxWidth(Double.MAX_VALUE);
            button.getStyleClass().add("roomsButtons");

            friendRequestButtons.getChildren().add(button);
        }*/
    }

    public void addNewRoom(String roomID, String message) {
        String[] messageParameters = message.split("\0");

        if(messageParameters[1].equals("True"))
            addRoom(roomID + "\0" + messageParameters[1] + "\0" + username + "\0" + messageParameters[2]);
    }

    public void reactToFriendRequestAnswer(String friend, String message) {

        if(message.equals("True")) {
            Button friendRequest = (Button) Manager.getScene().lookup("#" + friendRequestWaitingID + friend);

            if(friendRequest != null) {
                friendRequestButtons.getChildren().remove(friendRequest);
                addFriend(friend);
            } else
                System.out.println("Received friend request from a user that was not requested. User: " + friend);
        }
        else {
            System.out.println("Refused friend request from user " + friend);
        }
    }

    private void addRequestedFriendRequest(String request) {
        Text userName = new Text(request);
        Text label = new Text("\nClick to answer request.");
        label.setFont(Font.font("", FontPosture.ITALIC, -1));
        TextFlow flow = new TextFlow(userName, label);
        flow.setTextAlignment(TextAlignment.CENTER);

        Button button = new Button("", flow);
        button.setId(friendRequestWaitingID+request);
        button.setPrefHeight(flow.getHeight());
        button.setMaxWidth(Double.MAX_VALUE);
        button.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> System.out.println("Answering request."));
        button.getStyleClass().add("roomsButtons");

        friendRequestButtons.getChildren().add(button);
    }

    private void addWaitingFriendRequest(String asked) {
        Text userName = new Text(asked);
        Text label = new Text("\nAwaiting...");
        label.setFont(Font.font("", FontPosture.ITALIC, -1));
        TextFlow flow = new TextFlow(userName, label);
        flow.setTextAlignment(TextAlignment.CENTER);

        Button button = new Button("", flow);
        button.setId(friendRequestWaitingID+asked);
        button.setPrefHeight(flow.getHeight());
        button.setMaxWidth(Double.MAX_VALUE);
        button.getStyleClass().add("roomsButtons");

        friendRequestButtons.getChildren().add(button);
    }

    public void addFriendRequest(String request, String asked) {

        if(request.equals(username))
            addWaitingFriendRequest(asked);
        else
            addRequestedFriendRequest(request);
    }
}
