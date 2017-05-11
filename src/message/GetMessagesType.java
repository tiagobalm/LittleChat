package message;

import gui.mainPage.MainPage;

import static message.MessageConstants.getMessagesSize;

public class GetMessagesType extends ReactMessage {

    GetMessagesType(MainPage mainPage, Message message) {
        super(mainPage, message);
    }

    @Override
    public void react() {
        String[] headerParameters = message.getHeader().split(" ");
        if( message.getOptionalMessage() == null || headerParameters.length != getMessagesSize)
            return ;

        String[] messageParameters = message.getHeader().split(" ");
        mainPage.getChatMessages().put(Integer.parseInt(messageParameters[1]), message.getOptionalMessage());
    }
}
