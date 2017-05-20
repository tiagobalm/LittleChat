package message;

import gui.mainPage.MainPage;
import javafx.application.Platform;

import static message.MessageConstants.getFriendRequestsSize;

public class GetFriendRequestType extends ReactMessage {

    /**
     * Get friend request type react message.
     * @param mainPage Main page.
     * @param message text Message.
     */
    GetFriendRequestType(MainPage mainPage, Message message) {
        super(mainPage, message);
    }

    /**
     * React.
     */
    @Override
    public void react() {
        String[] headerParameters = message.getHeader().split(" ");

        if( message.getOptionalMessage() == null || headerParameters.length != getFriendRequestsSize)
            return ;

        Platform.runLater(() -> mainPage.addFriendRequests(message.getOptionalMessage()));
    }
}
