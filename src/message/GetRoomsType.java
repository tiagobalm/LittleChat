package message;

import gui.mainPage.MainPage;

import static message.MessageConstants.getRoomsSize;

public class GetRoomsType extends ReactMessage {

    GetRoomsType(MainPage mainPage, Message message) {
        super(mainPage, message);
    }

    @Override
    public void react() {
        if( message.getOptionalMessage() == null || message.getHeader().length() != getRoomsSize)
            return ;

        mainPage.addRooms(message.getOptionalMessage());
    }
}
