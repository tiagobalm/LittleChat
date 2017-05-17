package message;

import static message.MessageConstants.*;

public abstract class ReactMessage {
    protected Message message;

    /**
     * React message.
     * @param message Message.
     */
    ReactMessage(Message message) {
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
     * @param message Message.
     * @return React message.
     */
    public static ReactMessage getReactMessage(Message message) {
        String[] parameters = message.getHeader().split(" ");
        if( parameters.length < 1 )
            return null;

        String messageHeaderType = parameters[0];
        System.out.println("Reacting to message: " + messageHeaderType);
        switch (messageHeaderType) {
            case logoutType:
                return new LogoutType(message);
            case messageType:
                return new MessageType(message);
            case getRoomsType:
                return new GetRoomsType(message);
            case getMessagesType:
                return new GetMessagesType(message);
            case getFriendsType:
                return new GetFriendsType(message);
            case getFriendRequestsType:
                return new GetFriendRequestType(message);
            case addRoomType:
                return new AddRoomType(message);
            case answerFriendType:
                return new AnswerFriendType(message);
            case friendRequestType:
                return new FriendRequestType(message);
            default: break;
        }

        return null;
    }
}
