package message;

import gui.mainPage.MainPage;
import javafx.application.Platform;

import static message.MessageConstants.*;

public class MessageType extends ReactMessage {

    MessageType(MainPage mainPage, Message message) {
        super(mainPage, message);
    }

    @Override
    public void react() {
        String[] parameters = message.getHeader().split(" ");
        for(String str : parameters)
            System.out.println(str);
        if( parameters.length != messageSize || message.getMessage() == null)
            return ;
        System.out.println("Add message");
        Platform.runLater(() -> mainPage.addNewMessage(parameters[1], Integer.parseInt(parameters[2]), message.getMessage()));
    }
}
