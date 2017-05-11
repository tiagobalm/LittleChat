package message;

import gui.mainPage.MainPage;

import static message.MessageConstants.*;

public class MessageType extends ReactMessage {

    MessageType(MainPage mainPage, Message message) {
        super(mainPage, message);
    }

    @Override
    public void react() {
        if( message.getMessage().length() != messageSize || message.getMessage() == null)
            return ;

        String[] parameters = message.getHeader().split(" ");
        mainPage.addNewMessage(parameters[1], Integer.parseInt(parameters[2]), message.getMessage());
    }
}
