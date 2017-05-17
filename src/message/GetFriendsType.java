package message;

import gui.Manager;
import javafx.application.Platform;

import static message.MessageConstants.getRoomsSize;

public class GetFriendsType extends ReactMessage {

    /**
     * Get friends type.
     * @param message Message text.
     */
    GetFriendsType(Message message) {
        super(message);
    }

    /**
     * React.
     */
    @Override
    public void react() {
        String[] headerParameters = message.getHeader().split(" ");

        if( message.getOptionalMessage() == null || headerParameters.length != getRoomsSize)
            return ;

        Platform.runLater(() -> Manager.mainpage.addFriends(message.getOptionalMessage()));
    }
}
