package message;

import gui.mainPage.MainPage;
import javafx.application.Platform;

import static message.MessageConstants.getRoomsSize;

public class GetFriendsType extends ReactMessage {

    /**
     * Get friends type.
     * @param mainPage Main page.
     * @param message Message text.
     */
    GetFriendsType(MainPage mainPage, Message message) {
        super(mainPage, message);
    }

    /**
     * React.
     */
    @Override
    public void react() {
        String[] headerParameters = message.getHeader().split(" ");

        if( message.getOptionalMessage() == null || headerParameters.length != getRoomsSize)
            return ;

        Platform.runLater(() -> mainPage.addFriends(message.getOptionalMessage()));
    }
}
