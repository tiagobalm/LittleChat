package message;

import gui.mainPage.MainPage;

import java.io.IOException;

import static message.MessageConstants.*;

public abstract class ReactMessage {
    protected Message message;
    protected MainPage mainPage;

    ReactMessage(MainPage mainPage, Message message) {
        this.mainPage = mainPage;
        this.message = message;
    }

    public void react() throws IOException {
        throw new AbstractMethodError("Wrong class");
    }

    public static ReactMessage getReactMessage(MainPage mainPage, Message message) {
        String[] parameters = message.getHeader().split(" ");
        if( parameters.length < 1 )
            return null;

        String messageHeaderType = parameters[0];
        switch (messageHeaderType) {
            case messageType:
                return new MessageType(mainPage, message);
            case getRooms:
                return new GetRoomsType(mainPage, message);
            case getMessages:
                return new GetMessagesType(mainPage, message);
            default: break;
        }

        return null;
    }
}
