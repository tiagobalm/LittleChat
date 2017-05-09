package message;

import gui.mainPage.MainPage;

public class WorkMessage implements MessageProcessor {
    private ReactMessage reactMessage;
    private String message;
    private MainPage mainPage;

    public WorkMessage(MainPage mainPage, String message) {
        this.message = message;
        reactMessage = ReactMessage.getReactMessage(message);
        this.mainPage = mainPage;
    }

    @Override
    public void decode() {
    }
}
