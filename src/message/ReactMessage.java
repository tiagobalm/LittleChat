package message;

import gui.mainPage.MainPage;

import static message.MessageConstants.*;

public abstract class ReactMessage {
    protected Message message;
    protected MainPage mainPage;

    /**
     * React message.
     * @param mainPage Main page.
     * @param message Message.
     */
    ReactMessage(MainPage mainPage, Message message) {
        this.mainPage = mainPage;
        this.message = message;
    }

    /**
     * React.
     * @throws Exception
     */
    public void react() throws Exception {
        throw new AbstractMethodError("Wrong class");
    }

    /**
     * React message.
     * @param mainPage Main page.
     * @param message Message.
     * @return React message.
     */
    public static ReactMessage getReactMessage(MainPage mainPage, Message message) {
        String[] parameters = message.getHeader().split(" ");
        if( parameters.length < 1 )
            return null;

        String messageHeaderType = parameters[0];
        System.out.println("Reacting to message: " + messageHeaderType);
        switch (messageHeaderType) {
            case logoutType:
                return new LogoutType(mainPage, message);
            case messageType:
                return new MessageType(mainPage, message);
            case getRoomsType:
                return new GetRoomsType(mainPage, message);
            case getMessagesType:
                return new GetMessagesType(mainPage, message);
            case getFriendsType:
                return new GetFriendsType(mainPage, message);
            case getFriendRequestsType:
                return new GetFriendRequestType(mainPage, message);
            case addRoomType:
                return new AddRoomType(mainPage, message);
            case answerFriendType:
                return new AnswerFriendType(mainPage, message);
            case friendRequestType:
                return new FriendRequestType(mainPage, message);
            default: break;
        }

        return null;
    }
}
