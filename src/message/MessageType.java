package message;

import gui.Manager;
import javafx.application.Platform;

import static message.MessageConstants.*;

public class MessageType extends ReactMessage {

    /**
     * Get message type.
     * @param message Message.
     */
    MessageType(Message message) {
        super(message);
    }

    /**
     * React.
     */
    @Override
    public void react() {
        String[] parameters = message.getHeader().split(" ");
        for(String str : parameters)
            System.out.println(str);
        if( parameters.length != messageSize || message.getMessage() == null)
            return ;
        System.out.println("Add message");
        Platform.runLater(() -> Manager.mainpage.addNewMessage(parameters[1], Integer.parseInt(parameters[2]), message.getMessage()));
    }
}
