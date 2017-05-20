package message;

import gui.mainPage.MainPage;
import javafx.application.Platform;

import static message.MessageConstants.friendRequestSize;

public class FriendRequestType extends ReactMessage {

    /**
     * Get friend request type react message.
     * @param mainPage Main page.
     * @param message Message.
     */
    FriendRequestType(MainPage mainPage, Message message) {
        super(mainPage, message);
    }

    /**
     * React.
     */
    @Override
    public void react() {
        String[] headerParameters = message.getHeader().split(" ");

        if( headerParameters.length != friendRequestSize)
            return ;

        Platform.runLater(() -> mainPage.addFriendRequest(headerParameters[1], mainPage.getUsername()));
    }
}
