package message;

import gui.Manager;
import javafx.application.Platform;

import static message.MessageConstants.getFriendRequestsSize;

public class GetFriendRequestType extends ReactMessage {

    /**
     * Get friend request type.
     * @param message text Message.
     */
    GetFriendRequestType(Message message) {
        super(message);
    }

    /**
     * React.
     */
    @Override
    public void react() {
        String[] headerParameters = message.getHeader().split(" ");

        if( message.getOptionalMessage() == null || headerParameters.length != getFriendRequestsSize)
            return ;

        Platform.runLater(() -> Manager.mainpage.addFriendRequests(message.getOptionalMessage()));
    }
}
