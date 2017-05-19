package communication;

import message.Message;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketTimeoutException;

import static message.MessageConstants.*;

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

    /**
     * Establishes the communication.
     *
     */
    private Communication() {
        setSystemSetting();

        SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();

        try {
            socket = (SSLSocket) factory.createSocket(IP, PORT);

            String[] ciphers = new String[1];
            ciphers[0] ="TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256";
            socket.setEnabledCipherSuites(ciphers);

            os = new ObjectOutputStream(socket.getOutputStream());
            os.flush();
            is = new ObjectInputStream(socket.getInputStream());

            System.out.println("Loading output streams");

            System.out.println("Streams loaded");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setSystemSetting() {
        System.setProperty("javax.net.ssl.keyStore", keystorePath);
        System.setProperty("javax.net.ssl.keyStorePassword", keystorePass);
        System.setProperty("javax.net.ssl.trustStore", truststorePath);
        System.setProperty("javax.net.ssl.trustStorePassword", truststorePass);
    }

    /**
     * Gets the communication instance.
     *
     * @return instance communication.
     */
    public static Communication getInstance() {
        if(instance == null)
            instance = new Communication();

        return instance;
    }

    /**
     * Read a message.
     *
     * @return message.
     */
    public Message read() {
        Message message = null;
        try {
            socket.setSoTimeout(500);
            message = (Message)is.readObject();
        }
        catch (SocketTimeoutException ignore) {}
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return message;
    }

    /**
     * Send a register request.
     *
     * @param username User username.
     * @param password User password.
     * @param IPAddress Ip address.
     * @param port Port Nnumber.
     * @return result of waitForLoginResponse function.
     */
    public boolean sendRegisterRequest(String username, String password, String IPAddress, int port) {

        String header = registerType + " " + username + " " + password + " " + IPAddress + " " + port;
        Message message = new Message(header, "");

        sendMessage(message);

        return waitForLoginResponse();
    }

    /**
     * Send login request.
     * @param username User username.
     * @param password User password.
     * @param IPAddress Ip address.
     * @param port Port Number.
     * @return result of waitForLoginResponse function.
     */
    public boolean sendLoginRequest(String username, String password, String IPAddress, int port) {

        String header = loginType + " " + username + " " + password + " " + IPAddress + " " + port;
        Message message = new Message(header, "");

        sendMessage(message);

        return waitForLoginResponse();
    }

    /**
     * Wait for login response.
     *
     * @return True if loggedIn, otherwise return false.
     */
    private boolean waitForLoginResponse() {
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

    /**
     * Send logout request.
     */
    public void sendLogoutRequest() {
        Message logout = new Message(logoutType, "");
        sendMessage(logout);
    }

    /**
     * Send Message Request.
     * @param room Chat room.
     * @param body Message body.
     */
    public void sendMessageRequest(int room, String body) {
        Message message = new Message(messageType + " " + room, body);
        sendMessage(message);
    }

    /**
     * Get chat rooms.
     */
    public void getRooms() {
        Message message = new Message(getRoomsType, "");
        sendMessage(message);
    }

    /**
     * Get friends.
     */
    public void getFriends() {
        Message message = new Message(getFriendsType, "");
        sendMessage(message);
    }

    /**
     * Get room messages.
     * @param room Chat room.
     */
    public void getRoomMessages(Integer room) {
        Message message = new Message(getMessagesType + " " + room, "");
        sendMessage(message);
    }

    /**
     * Get friend requests.
     */
    public void getFriendRequests() {
        Message message = new Message(getFriendRequestsType + " ", "");
        sendMessage(message);
    }

    /**
     * Send friend request.
     * @param username User to ask friendship.
     */
    public void sendFriendRequest(String username) {
        Message message = new Message(friendRequestType + " " + username, "");
        sendMessage(message);
    }

    /**
     * Add room chat.
     * @param roomName Room chat name.
     */
    public void addRoom(String roomName) {
        Message message = new Message(addRoomType , roomName + "\0" + roomName);
        sendMessage(message);
    }

    /**
     * Send answer friend.
     * @param username User username.
     * @param answer Message answer.
     */
    public void sendAnswerFriend(String username, String answer) {
        Message message = new Message(answerFriendType + " " + username, answer);
        sendMessage(message);
    }

    public void sendChangeRoomName(int roomID, String newName) {
        Message message = new Message(changeRoomNameType + " " + roomID, newName);
        sendMessage(message);
    }

    /**
     * Send message.
     * @param message Message to send.
     */
    private void sendMessage(Message message) {
        try {
            synchronized (this) {
                os.writeObject(message);
                os.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
