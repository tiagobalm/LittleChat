package message;

import gui.mainPage.MainPage;
import javafx.application.Platform;

import static message.MessageConstants.getRoomsSize;

public class GetRoomsType extends ReactMessage {

    /**
     * Get rooms type react message.
     * @param mainPage Main page.
     * @param message Message text.
     */
    GetRoomsType(MainPage mainPage, Message message) {
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

        Platform.runLater(() -> {
            for(String room : message.getOptionalMessage())
                mainPage.addRoom(room);
        });
    }
}
