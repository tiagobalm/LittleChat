package message;

import gui.mainPage.MainPage;

import static message.MessageConstants.getMessagesSize;

public class GetMessagesType extends ReactMessage {

    /**
     * Get messages type react message.
     * @param mainPage Main page.
     * @param message Message text.
     */
    GetMessagesType(MainPage mainPage, Message message) {
        super(mainPage, message);
    }


    /**
     * React.
     */
    @Override
    public void react() {
        String[] headerParameters = message.getHeader().split(" ");
        if( message.getOptionalMessage() == null || headerParameters.length != getMessagesSize)
            return ;
        mainPage.getChatMessages().put(Integer.parseInt(headerParameters[1]), message.getOptionalMessage());
    }
}
