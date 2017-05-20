package message;

import gui.mainPage.MainPage;
import javafx.application.Platform;

import static message.MessageConstants.deleteFromRoomSize;

public class DeleteFromRoomType extends ReactMessage {
    /**
     * React message.
     *
     * @param mainPage Main page.
     * @param message  Message.
     */
    DeleteFromRoomType(MainPage mainPage, Message message) {
        super(mainPage, message);
    }

    @Override
    public void react() {
        String[] headerParameters = message.getHeader().split(" ");

        if( message.getMessage() == null || headerParameters.length != deleteFromRoomSize)
            return ;

        Platform.runLater(() -> mainPage.deleteFromRoom(headerParameters[1], message.getMessage()));
    }
}
