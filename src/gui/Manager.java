package gui;

import communication.Communication;
import gui.login.LoginGUI;
import gui.mainPage.ChatSettings;
import gui.mainPage.ConversationPopUp;
import gui.mainPage.MainPage;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Manager extends Application {

    public static Stage Stage, startConversation, chatSettings;
    public static boolean wantToClose = false;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Stage = primaryStage;
        startConversation = null;
        chatSettings = null;
        changeToLogin();
    }

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

    public static void changeToLogin() throws Exception {
        LoginGUI login = new LoginGUI();

        Stage.close();
        Stage = login.start();
        Stage.show();
    }

    public static void showConversationPopUp() throws Exception {
        ConversationPopUp popup = new ConversationPopUp();

        startConversation = popup.start();
        startConversation.show();
    }

    public static void closeConversationPopUp() throws Exception {
        if(startConversation != null) {
            startConversation.close();
            startConversation = null;
        }
    }

    public static void showChatSettings(String roomName) throws Exception {
        ChatSettings popup = new ChatSettings();

        chatSettings = popup.start(roomName);
        chatSettings.show();
    }

}
