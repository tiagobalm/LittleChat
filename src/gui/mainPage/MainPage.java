package gui.mainPage;

import communication.Communication;
import gui.Controller;
import gui.Manager;
import gui.TransitionControl;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import message.Message;
import org.controlsfx.control.Notifications;
import workers.ReadThread;
import workers.Worker;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.*;

public class MainPage implements Initializable, Controller<MainPageState> {
    private static final String roomsID = "Room";
    private static final String friendsID = "Friend";
    private static final String friendRequestWaitingID = "FriendRequestWaiting";
    private static final String friendRequestAskingID = "FriendRequestAsking";
    private static String username;
    private static ChatSettings chatSettings;
    private static int room = -1;
    private static int numberOfWorkerThreads = 10;
    private static ExecutorService executor = Executors.newFixedThreadPool(numberOfWorkerThreads);
    private static ReadThread readThread;
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

    //@FXML
    //private VBox profileButtons;
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
    @FXML
    private VBox messagesPanel;
    @FXML
    private ScrollPane messagesScrollPane;
    @FXML
    private TextArea messageInput;
    @FXML
    private Button settingsButton;
    private BlockingQueue<Message> messages;
    private ConcurrentHashMap<Integer, List<String>> chatMessages, chatMembers;
    private CopyOnWriteArrayList<String> friends;

    /**
     * Get user username.
     *
     * @return User username.
     */
    public static String getUsername() {
        return username;
    }

    /**
     * Set User username.
     *
     * @param username User username to set.
     */
    public void setUsername(String username) {
        MainPage.username = username;
    }

    /**
     * Start.
     * @return Stage.
     * @throws Exception
     */
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

    /**
     * Initialize.
     * @param location URL location.
     * @param resources ResourceBundle resources.
     */
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

        //Ask for the user's rooms
        Communication.getInstance().getRooms();

        //Ask for the user's friends
        Communication.getInstance().getFriends();

        //Ask for the user's friendRequests
        Communication.getInstance().getFriendRequests();
    }

    /**
     * Set chat settings.
     * @param chat Settings of new chat.
     */
    public void setChatSettings(ChatSettings chat) { MainPage.chatSettings = chat; }

    /**
     * Get room members.
     * @param room Chat room id.
     * @return List of usernames of the members from the chat room.
     */
    public List<String> getRoomMembers(int room) { return chatMembers.get(room); }

    /**
     * Get messages.
     * @return Messages.
     */
    public BlockingQueue<Message> getMessages() { return messages; }

    /**
     * Get chat messages.
     * @return Messages.
     */
    public ConcurrentHashMap<Integer, List<String>> getChatMessages() {
        return chatMessages;
    }

    /**
     * Get friends list.
     * @return friends.
     */
    public CopyOnWriteArrayList<String> getFriendsList() { return friends; }

    /**
     * Room button handler.
     * @param event Mouse event to handler.
     */
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

    /**
     * Change active chat room.
     * @param room Chat room id.
     * @param buttonID Button id.
     */
    private void changeActiveRoom(int room, Integer buttonID) {
        if(room != -1) {
            Button previousRoom = (Button) Manager.getScene().lookup("#" + roomsID + room);
            previousRoom.getStyleClass().remove("roomsButtons-selected");
        }

        Button nextRoom = (Button)Manager.getScene().lookup("#" + roomsID + buttonID);
        nextRoom.getStyleClass().add("roomsButtons-selected");
    }

    /**
     * Initialize handlers.
     */
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
                e -> Communication.getInstance().sendLogoutRequest());

        messageInput.setOnKeyReleased(keyEvent -> {
            if(keyEvent.getCode() == KeyCode.ENTER) {
                String message = messageInput.getText();

                if( message.length() == 0 )
                    return;

                if (chatMessages.containsKey(MainPage.room))
                    chatMessages.get(MainPage.room).add(username + "\0" + message);

                for (String s : chatMessages.get(MainPage.room))
                    System.out.println(s);
                addMessageToPanel(MainPage.username, System.currentTimeMillis(), message);
                Communication.getInstance().sendMessageRequest(room, message);
                messageInput.setText("");
            }
        });

        friendRequestInput.setOnKeyReleased(keyEvent -> {
            if(keyEvent.getCode() == KeyCode.ENTER) {
                String username = friendRequestInput.getText();
                if( username.length() == 0 )
                    return ;

                friendRequestInput.setText("");

                if( friends.contains(username) || username.equals(MainPage.username) ) return ;

                Communication.getInstance().sendFriendRequest(username);

                addWaitingFriendRequest(username);
            }
        });

        settingsButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
                event -> {
                    try {
                        Button roomButton = (Button) Manager.getScene().lookup("#" + roomsID + room);
                        Manager.showChatSettings(room, roomButton.getText(), this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    /**
     * Start worker threads.
     */
    private void startWorkerThreads() {
        System.out.println("Starting worker threads");
        readThread = new ReadThread(messages);
        readThread.start();

        System.out.println("After Read Thread");
        for( int i = 0; i < numberOfWorkerThreads; i++ ) {
            executor.submit(new Worker(this));
        }
    }

    /**
     * Stop workers threads.
     */
    public void stopWorkers() {
        executor.shutdownNow();
        readThread.stopThread();
    }

    /**
     * Disable current state.
     */
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

    /**
     * Hide the chat box when user leaves the chat room.
     * @param
     *
     */
    private void toggleInput(boolean show) {
        if(show) Platform.runLater(() -> messageInput.requestFocus());
        messageInput.setVisible(show);
        messageInput.setDisable(!show);
    }

    /**
     * Hide settings button.
     * @param show
     */
    private void toggleSettingButton(boolean show) {
        settingsButton.setVisible(show);
        settingsButton.setDisable(!show);
    }

    /**
     * Set new state.
     * @param newState New state.
     */
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

    /**
     * Set panel.
     * @param pane panel.
     * @param show show.
     */
    @Override
    public void setPane(Pane pane, boolean show) {
        TransitionControl.showTransition(pane, show, getPaneTransition(pane, show));
    }

    /**
     * Get panel transition.
     * @param pane Panel.
     * @param show Show.
     * @return translate transition.
     */
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

    /**
     * Set panel max width.
     */
    private void setPaneMaxWidth() {

        menuVBox.setMaxHeight(Double.MAX_VALUE);
        roomsButton.setMaxWidth(Double.MAX_VALUE);
        friendsButton.setMaxWidth(Double.MAX_VALUE);
        friendRequestButton.setMaxWidth(Double.MAX_VALUE);
        profileButton.setMaxWidth(Double.MAX_VALUE);
        logoutButton.setMaxWidth(Double.MAX_VALUE);
    }

    /**
     * Get chat room messages.
     * @param room Chat room id.
     */
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

    /**
     * Get chat room.
     * @param roomID Chat room id.
     */
    private void getRoom(String roomID) {
        Communication.getInstance().getRoom(roomID);
    }

    /**
     * Add chat room.
     * @param room Room to be added.
     */
    public void addRoom(String room) {
        String[] roomParameters = room.split("\0");

        Button button = new Button(roomParameters[1]);
        button.setId(roomsID+roomParameters[0]);
        button.addEventHandler(MouseEvent.MOUSE_CLICKED, this::roomButtonHandler);
        button.setMaxWidth(Double.MAX_VALUE);
        button.getStyleClass().add("roomsButtons");

        List<String> members = new ArrayList<>(Arrays.asList(roomParameters).subList(2, roomParameters.length));
        chatMembers.put(Integer.parseInt(roomParameters[0]), members);

        conversationButtons.getChildren().add(button);
    }

    /**
     * Add new message.
     * @param from Username that sends the message.
     * @param to User that receives the message.
     * @param message Message.
     */
    public void addNewMessage(String from, int to, long date, String message) {

        if(chatMessages.containsKey(to))
            chatMessages.get(to).add(message);
        if(room == to)
            addMessageToPanel(from, date, message);
        else {
            Notifications.create()
                    .title("LittleChat Notification")
                    .text("New message in " + ((Button) Manager.getScene().lookup("#" + roomsID + to)).getText())
                    .graphic(new ImageView(new Image(getClass().getResource("../assets/images/LittleChatLogoNoTextResized2.png").toString())))
                    .hideAfter(Duration.seconds(2))
                    .position(Pos.BOTTOM_RIGHT)
                    .hideCloseButton().show();
        }
    }

    /**
     * Add chat room messages to panel.
     * @param room Chat room id.
     */
    private void addRoomMessagesToPanel(Integer room) {
        messagesPanel.getChildren().clear();

        for(String message : chatMessages.get(room)) {
            String[] messageParameters = message.split("\0");
            if (messageParameters.length != 3)
                continue;
            addMessageToPanel(messageParameters[0].trim(), Long.parseLong(messageParameters[1].trim()), messageParameters[2].trim());
        }
    }

    /**
     * Add message to panel.
     * @param username User username.
     * @param message Message text.
     */
    private void addMessageToPanel(String username, long date, String message) {

        HBox hbox = new HBox();
        Label messageLabel = new Label(username + ": " + message);
        messageLabel.setMaxWidth(300);
        messageLabel.setMaxHeight(Integer.MAX_VALUE);
        messageLabel.setWrapText(true);
        VBox.setVgrow(messageLabel, Priority.ALWAYS);

        //Date dateFormat = new Date(date);
        //messageLabel.setTooltip(new Tooltip("Message sent: " + dateFormat.toString()));

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

    /**
     * Add friend.
     * @param friend Friend username.
     */
    public void addFriend(String friend) {

        if(!friends.contains(friend)) {
            friends.add(friend);

            Button button = new Button(friend);
            button.setId(friendsID+friend);
            button.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                try {
                    Manager.showConversationPopUp(friend);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            button.setMaxWidth(Double.MAX_VALUE);
            button.getStyleClass().add("roomsButtons");

            friendsButtons.getChildren().add(button);
        }
    }

    /**
     * Add friend requests.
     * @param friendRequests List of friends requests.
     */
    public void addFriendRequests(List<String> friendRequests) {
        for(String friend: friendRequests) {
            String[] users = friend.split("\0");

            if(users[0].equals(username))
                addWaitingFriendRequest(users[1]);
            else
                addRequestedFriendRequest(users[0]);
        }
    }

    /**
     * Add new chat room.
     * @param roomID Chat room id.
     * @param message Message text.
     */
    public void addNewRoom(String roomID, String message) {
        System.out.println("Add new room message: " + message);
        String[] messageParameters = message.split("\0");

        if(messageParameters[0].equals("True"))
            addRoom(roomID + "\0" + messageParameters[1] + "\0" + username + "\0" + messageParameters[2]);
    }

    /**
     * React to friend request answer.
     * @param friend Friend username.
     * @param message Message text.
     */
    public void reactToFriendRequestAnswer(String friend, String message) {

        Button friendRequest = (Button) Manager.getScene().lookup("#" + friendRequestWaitingID + friend);

        if(friendRequest != null)
            friendRequestButtons.getChildren().remove(friendRequest);

        if(message.equals("True")) {
            Notifications.create()
                    .title("LittleChat Notification")
                    .text("You and " + friend + " are now friends!")
                    .graphic(new ImageView(new Image(getClass().getResource("../assets/images/LittleChatLogoNoTextResized.png").toString())))
                    .hideAfter(Duration.seconds(3))
                    .position(Pos.BOTTOM_RIGHT)
                    .hideCloseButton().show();
            addFriend(friend);
        }
    }

    /**
     * Add requested friend request.
     * @param request Request.
     */
    private void addRequestedFriendRequest(String request) {
        MainPage main = this;
        Text userName = new Text(request);
        Text label = new Text("\nClick to answer request.");
        label.setFont(Font.font("", FontPosture.ITALIC, -1));
        TextFlow flow = new TextFlow(userName, label);
        flow.setTextAlignment(TextAlignment.CENTER);

        Button button = new Button("", flow);
        button.setId(friendRequestAskingID+request);
        button.setPrefHeight(flow.getHeight());
        button.setMaxWidth(Double.MAX_VALUE);
        button.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            try {
                Manager.showAnswerFriendPop(main, request);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
        button.getStyleClass().add("roomsButtons");

        friendRequestButtons.getChildren().add(button);
    }

    /**
     * Add waiting friend request.
     * @param asked Username asked.
     */
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

    /**
     * Add friend request.
     * @param request Request.
     * @param asked Username asked.
     */
    public void addFriendRequest(String request, String asked) {
        if(request.equals(username))
            addWaitingFriendRequest(asked);
        else {
            Notifications.create()
                    .title("LittleChat Notification")
                    .text("You received a new friend request from " + request)
                    .graphic(new ImageView(new Image(getClass().getResource("../assets/images/LittleChatLogoNoTextResized.png").toString())))
                    .hideAfter(Duration.seconds(3))
                    .position(Pos.BOTTOM_RIGHT)
                    .hideCloseButton().show();
            addRequestedFriendRequest(request);
        }
    }

    /**
     * Remove requested friend.
     * @param username User username.
     * @param answer Answer to request(accept or not accept friendship).
     */
    void removeRequestedFriend(String username, String answer) {
        Button friendRequest = (Button) Manager.getScene().lookup("#" + friendRequestAskingID + username);

        if(friendRequest != null)
            friendRequestButtons.getChildren().remove(friendRequest);

        if(answer.equals("True"))
            addFriend(username);
    }

    /**
     * Change chat room name.
     * @param roomID chat room id.
     * @param message Message text.
     */
    public void changeRoomName(String roomID, String message) {
        String[] messageParameters = message.split("\0");

        if(messageParameters[0].equals("True")) {
            Button room = (Button) Manager.getScene().lookup("#" + roomsID + roomID);

            if(room != null)
                room.setText(messageParameters[1]);

            if(chatSettings != null)
                chatSettings.changeRoomName(roomID, messageParameters[1]);
        }
    }

    /**
     * Add chat room.
     * @param roomID chat room id.
     * @param message Message text.
     */
    public void addToRoom(String roomID, String message) {
        String[] messageParameters = message.split("\0");

        if(messageParameters[0].equals("True")) {
            if(chatMembers.containsKey(Integer.parseInt(roomID)))
                chatMembers.get(Integer.parseInt(roomID)).add(messageParameters[1]);
            else
                getRoom(roomID);

            if(chatSettings != null)
                chatSettings.addToRoom(roomID, messageParameters[1]);
        }

    }

    /**
     * Delete message from chat room.
     * @param roomID chat room id.
     * @param message Message to be deleted.
     */
    public void deleteFromRoom(String roomID, String message) {
        String[] messageParameters = message.split("\0");

        if(messageParameters[0].equals("True")) {
            if(messageParameters[1].equals(username)) {
                try {
                    if(chatSettings != null)
                        Manager.closeChatSettings();
                    Button room = (Button) Manager.getScene().lookup("#" + roomsID + roomID);

                    if(room != null)
                        conversationButtons.getChildren().remove(room);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                chatMembers.get(Integer.parseInt(roomID)).remove(messageParameters[1]);

                if(chatSettings != null) {
                    chatSettings.deleteFromRoom(roomID, messageParameters[1]);
                }
            }
        }

    }
}
