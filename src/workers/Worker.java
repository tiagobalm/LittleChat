package workers;

import gui.mainPage.MainPage;
import message.Message;
import message.ReactMessage;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

/**
 * Created by tiagobalm on 09-05-2017.
 */
public class Worker implements Runnable {
    private BlockingQueue<Message> messages;
    private MainPage mainPage;
    private boolean running;
    private ReactMessage reactMessage;

    public Worker(MainPage mainpage) {
        this.mainPage = mainpage;
        this.messages = mainpage.getMessages();
        running = true;
    }

    @Override
    public void run() {

        while(running) {

            try {
                reactMessage = ReactMessage.getReactMessage(mainPage, messages.take());

                if( reactMessage == null ) return ;
                reactMessage.react();

            } catch (Exception e) {
                running = false;
            }
        }
    }
}
