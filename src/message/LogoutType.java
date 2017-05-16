package message;

import gui.Manager;
import gui.mainPage.MainPage;
import javafx.application.Platform;

public class LogoutType extends ReactMessage {
    LogoutType(MainPage mainPage, Message message) {
        super(mainPage, message);
    }

    @Override
    public void react() {
        String[] parameters = message.getHeader().split(" ");
        if( parameters.length != 1 )
            return ;

        if( Manager.wantToClose ) {
            mainPage.stopWorkers();
            Platform.runLater(() -> Manager.getStage().close());
            return;
        }

        Platform.runLater(() -> {
                try {
                    mainPage.stopWorkers();
                    Manager.changeToLogin();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
    }
}
