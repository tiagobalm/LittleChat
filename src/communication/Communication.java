package communication;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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
    private static DataOutputStream os;
    private static DataInputStream is;
    private static final byte messageEnd = 0;

    private static Communication instance = null;

    public Communication() {
        System.setProperty("javax.net.ssl.keyStore", keystorePath);
        System.setProperty("javax.net.ssl.keyStorePassword", keystorePass);
        System.setProperty("javax.net.ssl.trustStore", truststorePath);
        System.setProperty("javax.net.ssl.trustStorePassword", truststorePass);

        SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();

        try {
            SSLSocket sslsocket = (SSLSocket) factory.createSocket(IP, PORT);

            String[] ciphers = new String[1];
            ciphers[0] ="TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256";
            sslsocket.setEnabledCipherSuites(ciphers);

            is = new DataInputStream(sslsocket.getInputStream());
            System.out.println("Loading output streams");
            os = new DataOutputStream(sslsocket.getOutputStream());
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

    public boolean sendRegisterRequest(String username, String password) {

        try {
            String request = "REGISTER " + username + " " + password + "\0";
            os.write(request.getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    public boolean sendLoginRequest(String username, String password) {
        boolean loggedIn = false;

        try {
            String request = "LOGIN " + username + " " + password + "\0";
            os.write(request.getBytes());

            List<Byte> answerList = new ArrayList<>();
            byte character;

            while ((character = is.readByte()) != messageEnd)
                answerList.add(character);

            byte[] answer = byteListToByteArray(answerList);
            String response = new String(answer);

            loggedIn = ("True".equals(response.trim()));

        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch(Exception e) {}

        return loggedIn;
    }

    public byte[] byteListToByteArray(List<Byte> bytes) {
        byte[] result = new byte[bytes.size()];
        for (int i = 0; i < bytes.size(); i++) {
            result[i] = bytes.get(i).byteValue();
        }

        return result;
    }
}
