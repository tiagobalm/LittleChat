package message;

import gui.mainPage.MainPage;
import javafx.application.Platform;

import static message.MessageConstants.messageSize;

public class MessageType extends ReactMessage {

    /**
     * Get message type react message.
     * @param mainPage Main page.
     * @param message Message.
     */
    MessageType(MainPage mainPage, Message message) {
        super(mainPage, message);
    }

    /**
     * React.
     */
    @Override
    public void react() {
        String[] parameters = message.getHeader().split(" ");
        if( parameters.length != messageSize || message.getMessage() == null)
            return ;
        Platform.runLater(() -> mainPage.addNewMessage(parameters[1], Integer.parseInt(parameters[2]), Long.parseLong(parameters[3]), message.getMessage()));
    }
}
