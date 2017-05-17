package message;

import gui.Manager;
import javafx.application.Platform;

import static message.MessageConstants.answerFriendSize;

public class AnswerFriendType extends ReactMessage {

    AnswerFriendType(Message message) {
        super(message);
    }

    @Override
    public void react() {
        String[] headerParameters = message.getHeader().split(" ");

        if( message.getMessage() == null || headerParameters.length != answerFriendSize)
            return ;

        Platform.runLater(() -> Manager.mainpage.reactToFriendRequestAnswer(headerParameters[1], message.getMessage()));
    }
}
