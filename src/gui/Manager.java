package gui;

import communication.Communication;
import gui.login.LoginGUI;
import gui.mainPage.ChatSettings;
import gui.mainPage.ConversationPopUp;
import gui.mainPage.MainPage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Manager extends Application {

    private static Stage Stage, startConversation, chatSettings;
    public static boolean wantToClose = false;
    private static MainPage mainpage;
    private static LoginGUI login;

    /**
     * Main function.
     * @param args Arguments to launch the program.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Start.
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

    /**
     * Change to main page.
     * @param username User username.
     * @throws Exception
     */
    public static void changeToMainPage(String username) throws Exception {
        mainpage = new MainPage();
        mainpage.setUsername(username);

        Stage.close();
        login = null;
        Stage = mainpage.start();
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
        login = new LoginGUI();

        Stage.close();
        mainpage = null;
        Stage = login.start();
        Stage.show();
    }

    /**
     * Open conversation PopUp.
     * @throws Exception
     */
    public static void showConversationPopUp() throws Exception {
        ConversationPopUp popup = new ConversationPopUp();

        startConversation = popup.start();
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
    public static void showChatSettings(String roomName) throws Exception {
        ChatSettings popup = new ChatSettings();

        chatSettings = popup.start(roomName);
        chatSettings.show();
    }

    /**
     * Stop main page threads.
     */
    public static void stopMainPageThreads() { if(mainpage != null) mainpage.stopWorkers();  }

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

}
