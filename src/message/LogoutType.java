package message;

import gui.Manager;
import javafx.application.Platform;

public class LogoutType extends ReactMessage {
    /**
     * Get logout type.
     * @param message Message text.
     */
    LogoutType(Message message) {
        super(message);
    }

    /**
     * React.
     */
    @Override
    public void react() {
        String[] parameters = message.getHeader().split(" ");
        if( parameters.length != 1 )
            return ;

        if( Manager.wantToClose ) {
            Manager.mainpage.stopWorkers();
            Platform.runLater(() -> Manager.getStage().close());
            return;
        }

        Platform.runLater(() -> {
                try {
                    Manager.mainpage.stopWorkers();
                    Manager.changeToLogin();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
    }
}
