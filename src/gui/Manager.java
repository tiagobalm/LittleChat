package gui;

import communication.Communication;
import gui.login.LoginGUI;
import gui.mainPage.AnswerFriend;
import gui.mainPage.ChatSettings;
import gui.mainPage.ConversationPopUp;
import gui.mainPage.MainPage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Manager extends Application {

    public static boolean wantToClose = false;
    private static Stage Stage, startConversation, chatSettings, answerFriend;

    /**
     * Main function.
     * @param args Arguments to launch the program.
     */
    public static void main(String[] args) { launch(args); }

    /**
     * Change to main page.
     * @param username User username.
     * @throws Exception
     */
    public static void changeToMainPage(String username) throws Exception {
        MainPage mainPage = new MainPage();
        mainPage.setUsername(username);

        Stage.close();
        Stage = mainPage.start();
        Stage.show();

        Stage.setOnCloseRequest((WindowEvent t) -> {
            if( !wantToClose ) {
                wantToClose = true;
                Communication.getInstance().sendLogoutRequest();
                t.consume();
            }
        });
    }

    /**
     * Change to login page.
     * @throws Exception
     */
    public static void changeToLogin() throws Exception {
        LoginGUI login = new LoginGUI();

        Stage.close();
        Stage = login.start();
        Stage.show();
    }

    /**
     * Open conversation PopUp.
     * @throws Exception
     */
    public static void showConversationPopUp(String roomName) throws Exception {
        ConversationPopUp popup = new ConversationPopUp();
        startConversation = popup.start(roomName);
        startConversation.show();
    }

    /**
     * Close conversation PopUp.
     * @throws Exception
     */
    public static void closeConversationPopUp() throws Exception {
        if(startConversation != null) {
            startConversation.close();
            startConversation = null;
        }
    }

    /**
     * Show chat settings.
     * @param roomName Chat room name.
     * @throws Exception
     */
    public static void showChatSettings(int room, String roomName, MainPage main) throws Exception {
        ChatSettings popup = new ChatSettings();

        chatSettings = popup.start(room, roomName, main);
        chatSettings.show();
    }

    /**
     * Close chat settings popup.
     *
     * @throws Exception
     */
    public static void closeChatSettings() throws Exception {
        if(chatSettings != null) {
            chatSettings.close();
            chatSettings = null;
        }
    }

    /**
     * Show answer friend popup.
     * @param mainpage Main page.
     * @param request Request.
     * @throws Exception
     */
    public static void showAnswerFriendPop(MainPage mainpage, String request) throws Exception {
        AnswerFriend popup = new AnswerFriend();

        answerFriend = popup.start(request, mainpage);
        answerFriend.show();
    }

    /**
     * Close answer friend popup.
     *
     * @throws Exception
     */
    public static void closeAnswerFriendPop() throws Exception {
        if(answerFriend != null) {
            answerFriend.close();
            answerFriend = null;
        }
    }

    /**
     * Get current stage.
     * @return Stage.
     */
    public static Stage getStage() { return Stage; }

    /**
     * Get current scene.
     * @return Scene.
     */
    public static Scene getScene() { return Stage.getScene(); }

    /**
     * Start.
     *
     * @param primaryStage Primary stage.
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Stage = primaryStage;
        startConversation = null;
        chatSettings = null;
        changeToLogin();
    }

}
