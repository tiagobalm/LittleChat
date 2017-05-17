package message;

import gui.Manager;
import javafx.application.Platform;

import static message.MessageConstants.friendRequestSize;

public class FriendRequestType extends ReactMessage {

    FriendRequestType(Message message) {
        super(message);
    }

    @Override
    public void react() {
        String[] headerParameters = message.getHeader().split(" ");

        if( headerParameters.length != friendRequestSize)
            return ;

        Platform.runLater(() -> Manager.mainpage.addFriendRequest(headerParameters[1], Manager.mainpage.getUsername()));
    }
}
