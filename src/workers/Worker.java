package workers;

import gui.mainPage.MainPage;
import message.Message;
import message.ReactMessage;

import java.util.concurrent.BlockingQueue;

public class Worker implements Runnable {
    private BlockingQueue<Message> messages;
    private MainPage mainPage;
    private boolean running;

    public Worker(MainPage mainpage) {
        this.mainPage = mainpage;
        this.messages = mainpage.getMessages();
        running = true;
    }

    @Override
    public void run() {
        while(running) {
            try {
                ReactMessage reactMessage = ReactMessage.getReactMessage(mainPage, messages.take());
                if( reactMessage == null ) return ;
                reactMessage.react();
            } catch (Exception e) {
                running = false;
            }
        }
    }
}
