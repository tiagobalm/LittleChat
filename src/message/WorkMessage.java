package message;

import gui.mainPage.MainPage;

import java.io.IOException;

public class WorkMessage implements MessageProcessor {
    private ReactMessage reactMessage;
    private Message message;
    private MainPage mainPage;

    public WorkMessage(MainPage mainPage, Message message) {
        this.message = message;
        reactMessage = ReactMessage.getReactMessage(mainPage, message);
        this.mainPage = mainPage;
    }

    @Override
    public void decode() {

        if( reactMessage == null ) return ;

        try {
            reactMessage.react();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
