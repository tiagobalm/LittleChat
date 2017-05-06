package gui;

import communication.Communication;
import gui.login.LoginGUI;
import gui.mainPage.MainPage;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Created by tiagobalm on 05-05-2017.
 */
public class Manager extends Application {

    private static Stage Stage;
    private static Communication conn;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Stage = primaryStage;
        changeToLogin();
    }

    public static void changeToMainPage() throws Exception {
        MainPage mainPage = new MainPage();

        Stage.close();
        Stage = mainPage.start();
        Stage.show();
    }

    public static void changeToLogin() throws Exception {
        LoginGUI login = new LoginGUI();

        Stage.close();
        Stage = login.start();
        Stage.show();
    }
}
