package message;

import communication.Communication;

import java.io.IOException;

public abstract class ReactMessage {
    protected String[] message;
    ReactMessage(String[] message) {
        this.message = message;
    }

    public void react(Communication client) throws IOException {
        throw new AbstractMethodError("Wrong class");
    }

    static ReactMessage getReactMessage(String message) {
        String[] parameters = message.split(" ");
        if( parameters.length < 1 )
            return null;

        String messageType = parameters[0];
        switch (messageType) {
           default: break;
        }

        return null;
    }
}
