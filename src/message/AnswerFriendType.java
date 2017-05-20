package message;

import gui.mainPage.MainPage;
import javafx.application.Platform;

import static message.MessageConstants.answerFriendSize;

public class AnswerFriendType extends ReactMessage {

    /**
     * Answer friend type react message.
     *
     * @param mainPage Main page.
     * @param message  Message.
     */
    AnswerFriendType(MainPage mainPage, Message message) {
        super(mainPage, message);
    }

    /**
     * React.
     */
    @Override
    public void react() {
        String[] headerParameters = message.getHeader().split(" ");

        if( message.getMessage() == null || headerParameters.length != answerFriendSize)
            return ;

        Platform.runLater(() -> mainPage.reactToFriendRequestAnswer(headerParameters[1], message.getMessage()));
    }
}
