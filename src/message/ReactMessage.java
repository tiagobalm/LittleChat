package message;

import gui.mainPage.MainPage;

import java.io.IOException;

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

    static ReactMessage getReactMessage(MainPage mainPage, Message message) {
        String[] parameters = message.getHeader().split(" ");
        if( parameters.length < 1 )
            return null;

        String messageType = parameters[0];
        switch (messageType) {
            case MessageConstants.messageType:
                return new MessageType(mainPage, message);
            default: break;
        }

        return null;
    }
}
