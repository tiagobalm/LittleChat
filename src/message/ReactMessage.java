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

    public void react() throws Exception {
        throw new AbstractMethodError("Wrong class");
    }

    public static ReactMessage getReactMessage(MainPage mainPage, Message message) {
        System.out.println("Work message " + message.getHeader());
        String[] parameters = message.getHeader().split(" ");
        if( parameters.length < 1 )
            return null;

        String messageHeaderType = parameters[0];
        switch (messageHeaderType) {
            case logoutType:
                System.out.println("Receive logout");
                return new LogoutType(mainPage, message);
            case messageType:
                System.out.println("Receive MESSAGE");
                return new MessageType(mainPage, message);
            case getRooms:
                System.out.println("Receive GETROOMS");
                return new GetRoomsType(mainPage, message);
            case getMessages:
                System.out.println("Receive MESSAGES");
                return new GetMessagesType(mainPage, message);
            case getFriendsType:
                System.out.println("Receive GETFRIENDS");
                return new GetFriendsType(mainPage, message);
            default: break;
        }

        return null;
    }
}
