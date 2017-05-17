package message;

import gui.mainPage.MainPage;
import javafx.application.Platform;

import static message.MessageConstants.addRoomSize;

public class AddRoomType extends ReactMessage {

    AddRoomType(MainPage mainPage, Message message) {
        super(mainPage, message);
    }

    @Override
    public void react() {
        String[] headerParameters = message.getHeader().split(" ");

        if( message.getMessage() == null || headerParameters.length != addRoomSize)
            return ;

        Platform.runLater(() -> mainPage.addNewRoom(headerParameters[1], message.getMessage()));
    }
}
