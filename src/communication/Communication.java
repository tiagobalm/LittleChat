package communication;

import gui.mainPage.MainPage;
import message.Message;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;

import static message.MessageConstants.*;

public class Communication {
    private static final String keystorePath = Communication.class.getResource("../keys/client.private").getPath();
    private static final String keystorePass = "littlechat";
    private static final String truststorePath = Communication.class.getResource("../keys/truststore").getPath();
    private static final String truststorePass = "littlechat";

    private static final String MAINIP = "192.168.1.16";
    private static final String BACKUPIP = "192.168.1.17";
    private static final int MAINPORT = 15000;
    private static final int BACKUPORT = 14999;
    private static ObjectOutputStream os;
    private static ObjectInputStream is;
    private static SSLSocket socket;
    private static SSLSocketFactory factory;

    private static Communication instance = null;

    private static boolean reconnecting = false;

    private static String username = "", password = "";

    /**
     * Establishes the communication.
     *
     */
    private Communication() {
        setSystemSetting();

        try {
            factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            socket = (SSLSocket) factory.createSocket();

            String[] ciphers = new String[1];
            ciphers[0] ="TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256";
            socket.setEnabledCipherSuites(ciphers);

            System.out.println("Trying to connect to main server.");

            socket.connect(new InetSocketAddress(MAINIP, MAINPORT));

            System.out.println("Loading output streams");
            os = new ObjectOutputStream(socket.getOutputStream());
            os.flush();
            is = new ObjectInputStream(socket.getInputStream());

            System.out.println("Streams loaded");

        } catch (IOException e) {
            System.out.println("Reconnecting");
            reconnect();
        }
    }

    /**
     * Gets the communication instance.
     *
     * @return Instance communication.
     */
    public static Communication getInstance() {
        if(instance == null)
            instance = new Communication();

        return instance;
    }

    private void reconnect() {
        int counter = 0;

        if (!reconnecting) {
            reconnecting = true;

            while (reconnecting) {
                try {
                    socket = (SSLSocket) factory.createSocket();
                    socket.setReuseAddress(true);

                    if (counter % 2 == 0)
                        socket.connect(new InetSocketAddress(MAINIP, MAINPORT));
                    if (counter % 2 != 0)
                        socket.connect(new InetSocketAddress(BACKUPIP, BACKUPORT));

                    os = new ObjectOutputStream(socket.getOutputStream());
                    os.flush();
                    is = new ObjectInputStream(socket.getInputStream());

                    if (!username.equals(""))
                        sendLoginRequest(username, password);

                    reconnecting = false;

                } catch (IOException e) {
                    counter++;
                }
            }
        } else {
            while (reconnecting) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Set system settings.
     */
    private void setSystemSetting() {
        System.setProperty("javax.net.ssl.keyStore", keystorePath);
        System.setProperty("javax.net.ssl.keyStorePassword", keystorePass);
        System.setProperty("javax.net.ssl.trustStore", truststorePath);
        System.setProperty("javax.net.ssl.trustStorePassword", truststorePass);
    }

    /**
     * Read a message.
     *
     * @return Message read.
     */
    public Message read() {
        Message message = null;
        try {
            socket.setSoTimeout(500);
            message = (Message)is.readObject();
        }
        catch (SocketTimeoutException ignore) {}
        catch ( ClassNotFoundException e) {
            System.out.println("Message class is not the same.");
        }
        catch (IOException e ) {
            System.out.println("Reconnecting");
            reconnect();
        }
        return message;
    }

    /**
     * Send a register request.
     *
     * @param username User username.
     * @param password User password.
     * @return result of waitForLoginResponse function.
     */
    public boolean sendRegisterRequest(String username, String password) {

        String header = registerType + " " + username + " " + password;
        Message message = new Message(header, "");

        Communication.username = username;
        Communication.password = password;

        sendMessage(message);

        return waitForLoginResponse();
    }

    /**
     * Send login request.
     * @param username User username.
     * @param password User password.
     * @return result of waitForLoginResponse function.
     */
    public boolean sendLoginRequest(String username, String password) {

        String header = loginType + " " + username + " " + password;
        Message message = new Message(header, "");

        Communication.username = username;
        Communication.password = password;

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

            if(!loggedIn) {
                Communication.username = "";
                Communication.password = "";
            }
        }
        catch (SocketTimeoutException timeout) {
            System.out.println("Timeout exception");
        }
        catch(ClassNotFoundException e) {
            System.out.println("Message class is not the same.");
        }
        catch (IOException e) {
            reconnect();
            return waitForLoginResponse();
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
        Message message = new Message(messageType + " " + room + " " + MainPage.getUsername() + " " + System.currentTimeMillis(), body);
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
        Message message = new Message(friendRequestType + " " + username + " " + MainPage.getUsername(), "");
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
        Message message = new Message(answerFriendType + " " + username + " " + MainPage.getUsername(), answer);
        sendMessage(message);
    }

    /**
     * Send change chat room name message.
     * @param roomID Chat room id.
     * @param newName New chat room name.
     */
    public void sendChangeRoomName(int roomID, String newName) {
        Message message = new Message(changeRoomNameType + " " + roomID, newName);
        sendMessage(message);
    }

    /**
     * Get chat room.
     * @param roomID Chat room id.
     */
    public void getRoom(String roomID) {
        Message message = new Message(getRoomType + " " + roomID, "");
        sendMessage(message);
    }

    /**
     * Add user to chat room.
     * @param username User username.
     * @param roomID Chat room id.
     */
    public void addToRoom(String username, int roomID) {
        Message message = new Message(addToRoomType + " " + roomID, username);
        sendMessage(message);
    }


    /**
     * Delete user from chat room.
     * @param username User username.
     * @param roomID Chat room id.
     */
    public void deleteFromRoom(String username, int roomID) {
        Message message = new Message(deleteFromRoomType + " " + roomID, username);
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
            reconnect();
            sendMessage(message);
        }
    }
}
