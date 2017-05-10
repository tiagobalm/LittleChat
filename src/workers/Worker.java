package workers;

import gui.mainPage.MainPage;
import message.WorkMessage;

import java.util.concurrent.BlockingQueue;

/**
 * Created by tiagobalm on 09-05-2017.
 */
public class Worker implements Runnable {
    private BlockingQueue<String> messages;
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
                String message = messages.take();
                WorkMessage worker = new WorkMessage(mainPage, message);
                worker.decode();

            } catch (InterruptedException e) {
                running = false;
            }
        }
    }
}
