package communication;
import message.Message;

import java.io.*;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

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

            is = new ObjectInputStream(socket.getInputStream());
            System.out.println("Loading output streams");
            os = new ObjectOutputStream(socket.getOutputStream());
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

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
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

            System.out.println(response.getServerAnswer());
            loggedIn = ("True".equals(response.getServerAnswer()));
        }
        catch (SocketTimeoutException timeout) {
            System.out.println("Timeout exception");
        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return loggedIn;
    }

    public void sendMessage(String username, int room, String body) {

        String header = "Message " + username + " " + room;
        Message message = new Message(header, body);

        try {
            os.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
