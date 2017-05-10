package workers;

import communication.Communication;

import java.util.concurrent.BlockingQueue;

/**
 * Created by tiagobalm on 09-05-2017.
 */
public class ReadThread implements Runnable {
    private BlockingQueue<String> messages;
    private boolean running;

    public ReadThread(BlockingQueue<String> messages) {
        this.messages = messages;
        running = true;
    }

    @Override
    public void run() {
        System.out.println("Running read thread.");

        while (running) {
            String message = Communication.getInstance().read();

            if(message != "")
                messages.add(message);
        }
    }

    public void stopThread() { this.running = false; }
}
