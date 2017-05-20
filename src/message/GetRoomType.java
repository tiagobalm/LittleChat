package message;

import gui.mainPage.MainPage;
import javafx.application.Platform;

import static message.MessageConstants.getRoomSize;

public class GetRoomType extends ReactMessage {
    /**
     * Get room type react message.
     *
     * @param mainPage Main page.
     * @param message  Message.
     */
    GetRoomType(MainPage mainPage, Message message) {
        super(mainPage, message);
    }

    /**
     * React.
     */
    @Override
    public void react() {
        String[] headerParameters = message.getHeader().split(" ");

        if( message.getMessage() == null || headerParameters.length != getRoomSize)
            return ;

        Platform.runLater(() -> mainPage.addRoom(message.getMessage()));
    }
}
