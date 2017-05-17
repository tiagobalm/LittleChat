package workers;

import communication.Communication;
import message.Message;

import java.util.concurrent.BlockingQueue;

public class ReadThread extends Thread {
    private BlockingQueue<Message> messages;
    private boolean running;

    /**
     * Read thread.
     * @param messages Messages passed.
     */
    public ReadThread(BlockingQueue<Message> messages) {
        this.messages = messages;
        running = true;
    }

    /**
     * Run.
     */
    @Override
    public void run() {
        while (running) {
            Message message = Communication.getInstance().read();
            if(message != null)
                messages.add(message);
        }
    }

    /**
     * Stop thread from running.
     */
    public void stopThread() { this.running = false; }
}
