package message;

import gui.mainPage.MainPage;
import javafx.application.Platform;

import static message.MessageConstants.getFriendRequestsSize;

public class FriendRequestType extends ReactMessage {

    FriendRequestType(MainPage mainPage, Message message) {
        super(mainPage, message);
    }

    @Override
    public void react() {
        String[] headerParameters = message.getHeader().split(" ");

        if( message.getOptionalMessage() == null || headerParameters.length != getFriendRequestsSize)
            return ;

        Platform.runLater(() -> mainPage.addFriendRequests(message.getOptionalMessage()));
    }
}
