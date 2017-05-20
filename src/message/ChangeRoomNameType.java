package message;

import gui.mainPage.MainPage;
import javafx.application.Platform;

import static message.MessageConstants.changeRoomNameSize;


public class ChangeRoomNameType extends ReactMessage {
    /**
     * Change room name type react message.
     *
     * @param mainPage Main page.
     * @param message  Message.
     */
    ChangeRoomNameType(MainPage mainPage, Message message) {
        super(mainPage, message);
    }

    /**
     * React.
     */
    @Override
    public void react() {
        String[] headerParameters = message.getHeader().split(" ");

        if( message.getMessage() == null || headerParameters.length != changeRoomNameSize)
            return ;

        Platform.runLater(() -> mainPage.changeRoomName(headerParameters[1], message.getMessage()));
    }
}
