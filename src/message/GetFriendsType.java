package message;

import gui.mainPage.MainPage;

import static message.MessageConstants.getRoomsSize;

public class GetFriendsType extends ReactMessage {

    GetFriendsType(MainPage mainPage, Message message) {
        super(mainPage, message);
    }

    @Override
    public void react() {
        String[] headerParameters = message.getHeader().split(" ");

        if( message.getOptionalMessage() == null || headerParameters.length != getRoomsSize)
            return ;

        System.out.println("Adding rooms");
        for( String str : message.getOptionalMessage() )
            System.out.println(str);
    }
}
