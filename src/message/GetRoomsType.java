package message;

import gui.Manager;
import javafx.application.Platform;

import static message.MessageConstants.getRoomsSize;

public class GetRoomsType extends ReactMessage {

    /**
     * Get rooms type.
     * @param message Message text.
     */
    GetRoomsType(Message message) {
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

        Platform.runLater(() -> Manager.mainpage.addRooms(message.getOptionalMessage()));
    }
}
