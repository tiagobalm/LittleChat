package message;

import gui.mainPage.MainPage;
import javafx.application.Platform;

import static message.MessageConstants.getRoomsSize;

public class GetRoomsType extends ReactMessage {

    GetRoomsType(MainPage mainPage, Message message) {
        super(mainPage, message);
    }

    @Override
    public void react() {
        String[] headerParameters = message.getHeader().split(" ");

        if( message.getOptionalMessage() == null || headerParameters.length != getRoomsSize)
            return ;

        System.out.println("Adding rooms");
        Platform.runLater(() -> mainPage.addRooms(message.getOptionalMessage()));
    }
}
