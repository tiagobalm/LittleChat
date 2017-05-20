package message;

import gui.mainPage.MainPage;
import javafx.application.Platform;

import static message.MessageConstants.addToRoomSize;

public class AddToRoomType extends ReactMessage {

    /**
     * Add to room type react message.
     *
     * @param mainPage Main page.
     * @param message  Message.
     */
    AddToRoomType(MainPage mainPage, Message message) {
        super(mainPage, message);
    }

    /**
     * React.
     *
     */
    @Override
    public void react() {
        String[] headerParameters = message.getHeader().split(" ");

        if( message.getMessage() == null || headerParameters.length != addToRoomSize)
            return ;

        Platform.runLater(() -> mainPage.addToRoom(headerParameters[1], message.getMessage()));
    }
}
