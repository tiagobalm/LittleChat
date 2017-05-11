package communication;
import message.Message;

import java.io.*;
import java.net.SocketTimeoutException;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class Communication {
    private static final String keystorePath = Communication.class.getResource("../keys/client.private").getPath();
    private static final String keystorePass = "littlechat";
    private static final String truststorePath = Communication.class.getResource("../keys/truststore").getPath();
    private static final String truststorePass = "littlechat";

    private static final String IP = "127.0.0.1";
    private static final int PORT = 15000;
    private static ObjectOutputStream os;
    private static ObjectInputStream is;
    private static SSLSocket socket;

    private static Communication instance = null;

    private Communication() {
        System.setProperty("javax.net.ssl.keyStore", keystorePath);
        System.setProperty("javax.net.ssl.keyStorePassword", keystorePass);
        System.setProperty("javax.net.ssl.trustStore", truststorePath);
        System.setProperty("javax.net.ssl.trustStorePassword", truststorePass);

        SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();

        try {
            socket = (SSLSocket) factory.createSocket(IP, PORT);

            String[] ciphers = new String[1];
            ciphers[0] ="TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256";
            socket.setEnabledCipherSuites(ciphers);

            os = new ObjectOutputStream(socket.getOutputStream());
            os.flush();
            System.out.println("Hello!");
            is = new ObjectInputStream(socket.getInputStream());

            System.out.println("Loading output streams");

            System.out.println("Streams loaded");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Communication getInstance() {
        if(instance == null)
            instance = new Communication();

        return instance;
    }

    public Message read() {
        Message message = null;

        try {
            socket.setSoTimeout(500);
            message = (Message)is.readObject();

        } catch (SocketTimeoutException ignore) {}

        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return message;
    }

    public boolean sendRegisterRequest(String username, String password, String IPAddress, int port) {

        String header = "REGISTER " + username + " " + password + " " + IPAddress + " " + port;
        Message message = new Message(header, "");

        try {
            os.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return waitForLoginResponse();
    }

    public boolean sendLoginRequest(String username, String password, String IPAddress, int port) {

        String header = "LOGIN " + username + " " + password + " " + IPAddress + " " + port;
        Message message = new Message(header, "");

        try {
            System.out.println("Sending message " + message.getHeader());
            os.writeObject(message);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return waitForLoginResponse();
    }

    public boolean waitForLoginResponse() {
        boolean loggedIn = false;

        try {
            socket.setSoTimeout(1000);
            Message response = (Message)is.readObject();

            loggedIn = ("True".equals(response.getMessage()));
        }
        catch (SocketTimeoutException timeout) {
            System.out.println("Timeout exception");
        }
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return loggedIn;
    }

    public boolean sendLogoutRequest() {
        Message logout = new Message("LOGOUT ", "");
        boolean loggedOut = false;

        try {
            os.writeObject(logout);

            socket.setSoTimeout(1000);
            Message response = (Message)is.readObject();

            loggedOut = ("LOGOUT".equals(response.getHeader()));
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return loggedOut;
    }

    public void sendMessageRequest(int room, String body) {
        Message message = new Message("MESSAGE " + room, body);
        sendMessage(message);
    }

    public void getRooms() {
        Message message = new Message("GETROOMS", "");
        sendMessage(message);
    }

    public void getRoomMessages(Integer room) {
        Message message = new Message("GETMESSAGES " + room, "");
        sendMessage(message);
    }

    private void sendMessage(Message message) {
        try {
            os.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
