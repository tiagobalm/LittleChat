package message;

import gui.mainPage.MainPage;
import javafx.application.Platform;

import static message.MessageConstants.friendRequestSize;

public class FriendRequestType extends ReactMessage {

    FriendRequestType(MainPage mainPage, Message message) {
        super(mainPage, message);
    }

    @Override
    public void react() {
        String[] headerParameters = message.getHeader().split(" ");

        if( headerParameters.length != friendRequestSize)
            return ;

        Platform.runLater(() -> mainPage.addFriendRequest(headerParameters[1], mainPage.getUsername()));
    }
}
