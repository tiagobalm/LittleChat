package gui;

import communication.Communication;
import gui.login.LoginGUI;
import gui.mainPage.MainPage;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Manager extends Application {

    public static Stage Stage;
    public static boolean wantToClose = false;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Stage = primaryStage;
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
}
