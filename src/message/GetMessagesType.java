package message;

import gui.Manager;

import static message.MessageConstants.getMessagesSize;

public class GetMessagesType extends ReactMessage {

    /**
     * Get messages type.
     * @param message Message text.
     */
    GetMessagesType(Message message) {
        super(message);
    }


    /**
     * React.
     */
    @Override
    public void react() {
        String[] headerParameters = message.getHeader().split(" ");
        if( message.getOptionalMessage() == null || headerParameters.length != getMessagesSize)
            return ;

        Manager.mainpage.getChatMessages().put(Integer.parseInt(headerParameters[1]), message.getOptionalMessage());
    }
}
