package message;

import gui.Manager;
import javafx.application.Platform;

import static message.MessageConstants.addRoomSize;

public class AddRoomType extends ReactMessage {

    AddRoomType(Message message) {
        super(message);
    }

    @Override
    public void react() {
        String[] headerParameters = message.getHeader().split(" ");

        if( message.getMessage() == null || headerParameters.length != addRoomSize)
            return ;

        Platform.runLater(() -> Manager.mainpage.addNewRoom(headerParameters[1], message.getMessage()));
    }
}
