package communication;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;


public class EchoClient {
    private static final String keystorePath = EchoClient.class.getResource("../keystore").getPath();
    private static final String keystorePass = "gruposdis";
    private static final String truststorePath = EchoClient.class.getResource("../truststore").getPath();
    private static final String truststorePass = "gruposdis";

    private static final String IP = "127.0.0.1";
    private static final int PORT = 15000;
    private static DataOutputStream os;
    private static DataInputStream is;
    private static final byte messageEnd = 0;

    public static void main(String[] args) {
        System.setProperty("javax.net.ssl.keyStore", keystorePath);
        System.setProperty("javax.net.ssl.keyStorePassword", keystorePass);
        System.setProperty("javax.net.ssl.trustStore", truststorePath);
        System.setProperty("javax.net.ssl.trustStorePassword", truststorePass);

        SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();

        try {
            SSLSocket sslsocket = (SSLSocket) factory.createSocket(IP, PORT);
            sslsocket.setNeedClientAuth(true);
            
            is = new DataInputStream(sslsocket.getInputStream());
            System.out.println("Loading output streams");
            os = new DataOutputStream(sslsocket.getOutputStream());
            System.out.println("Streams loaded");
            os.write("Hi\0".getBytes());

            byte character;
            List<Byte> message = new ArrayList<>();
            while ((character = is.readByte()) != messageEnd) {
                message.add(character);
            }

            byte[] messageBytes = byteListToByteArray(message);
            String response = new String(messageBytes);
            System.out.println("Server response: " + response);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static byte[] byteListToByteArray(List<Byte> bytes) {
        byte[] result = new byte[bytes.size()];
        for (int i = 0; i < bytes.size(); i++) {
            result[i] = bytes.get(i).byteValue();
        }

        return result;
    }
}
