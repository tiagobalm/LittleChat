package message;

import gui.mainPage.MainPage;

import static message.MessageConstants.*;

/**
 * Created by tiagobalm on 10-05-2017.
 */
public class MessageType extends ReactMessage {

    MessageType(MainPage mainPage, Message message) {
        super(mainPage, message);
    }

    @Override
    public void react() {
        if( message.getMessage().length() != messageSize || message.getMessage() == null)
            return ;

        String[] parameters = message.getHeader().split(" ");
        mainPage.addMessage(parameters[1], parameters[2], message.getMessage());
    }
}
