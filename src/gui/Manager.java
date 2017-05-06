package gui;

import gui.login.LoginGUI;
import gui.mainPage.MainPage;
import javafx.application.Application;
import javafx.stage.Stage;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by tiagobalm on 05-05-2017.
 */
public class Manager extends Application {

    private static Stage Stage;
    private static SSLSocket socket;
    private static DataOutputStream readStream;
    private static DataInputStream writeStream;

    public static void main(String[] args) {
        launch(args);

        try {
            //Creates socket with IP and Port passed as argument
            SSLSocketFactory socketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            socket = (SSLSocket) socketFactory.createSocket(args[0], Integer.parseInt(args[1]));

            //Stores read and write stream in variables
            readStream = new DataOutputStream(socket.getOutputStream());
            writeStream = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
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
