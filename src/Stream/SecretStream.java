package Stream;

import Algorithm.CRC;
import Algorithm.XOR;
import Algorithm.RSA;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigInteger;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author Juan Lucas Vieira
 */
public class SecretStream {

    Scanner in;
    PrintStream out;
    RSA rsa;
    BigInteger[] public_key;
    String simmetric_key = "";
    boolean status;
    static boolean OPEN = true;
    static boolean CLOSED = true;
    char RS = 0x1e;
    String separator = RS + "";

    public SecretStream(Socket s) throws IOException {
        in = new Scanner(s.getInputStream());
        out = new PrintStream(s.getOutputStream());
        rsa = new RSA(1024);
        publicKeyExchange();
        status = OPEN;
    }

    public void sendSecretMessage(String message) {
        /*TODO:
        Send PK to server
        Calculate hash of file
        encrypt hash with client's private key
        Concatenate file + crypted hash
        encrypt file + crypted hash with simetric key
        encrypt simetric key using server public key
        Send [(File + [(CRC)-])SK] + [(Simetric Key)+] Client
        Server [(File + [(CRC)+])SK] + [(Simetric Key)-]
         */
        if (simmetric_key.isEmpty()) {
            simmetric_key = XOR.generateKey();
            System.out.println("SEND - Simmetric KEY: " + simmetric_key);
        }
        long msgCRC = CRC.calculateCRC(message);
        System.out.println("SEND - CRC CALCULADO: " + msgCRC);
        String encodedCRC = rsa.cypher(msgCRC + "");                                            //CRC encode using RSA client's private key
        String encodedMSG_eCRC = XOR.cypher(message + separator + "CRC" + separator + encodedCRC, simmetric_key);     //XOR encode (MSG + Encoded CRC) using simmetric key
        String encodedSK = rsa.cypher(simmetric_key, public_key);                               //Encode simmetric key using server's public key
        System.out.print("SEND - ENCODED SK: " + encodedSK);
        String send = encodedMSG_eCRC + separator + encodedSK;
        System.out.print("SEND - Sending message... ");
        System.out.print("SEND - " + send);
        out.println(send + "\nEND");
        System.out.println("Done.");
    }

    public String receiveSecretMessage() throws IOException {
        /*
        Receive [(File + [(CRC)+])SK] + [(Simetric Key)-]
        Decrypt simetric key using server private key
        Decrypt file + crypted hash with simetric key
        Calculate hash of file
        Decrypt hash with client's public key
        Compare calculated hash and received hash
        Create file, getting file name and content from message
         */

        String[] received = retrieveMessage().split(separator);
        String encodedSK = received[1].trim();
        System.out.println("RECEIVE - ENCODED SK: " + encodedSK);
        String encodedMSG_CRC = received[0];

        if (simmetric_key.isEmpty()) {
            simmetric_key = rsa.decypher(encodedSK);
        }

        System.out.println("RECEIVE - DECODED SK: " + simmetric_key);
        String[] msg_eCRC = XOR.decypher(encodedMSG_CRC, simmetric_key).split(separator + "CRC" + separator);

        String msg = msg_eCRC[0];
        long msgCRC = CRC.calculateCRC(msg);
        System.out.println("RECEIVE - CRC CALCULADO: " + msgCRC);

        long rcvdMsgCRC = Long.parseLong(rsa.decypher(msg_eCRC[1].trim(), public_key));
        System.out.println("RECEIVE - CRC RECEBIDO: " + rcvdMsgCRC);
        if (msgCRC == rcvdMsgCRC) {
            return msg;
        }
        return separator +"CRC Mismatch" + separator;

    }

    private String retrieveMessage() {
        String received = in.nextLine();
        String buffer;
        boolean EOS = false;
        while (!EOS) {
            buffer = in.nextLine();
            if (received.equals("END") || buffer.equals("END")) {
                EOS = true;
            } else {
                received += buffer;
            }
        }
        System.out.println("Message Received: " + received);
        return received;
    }

    private void publicKeyExchange() {
        System.out.print("Sending PK...");
        sendClientPublicKey(rsa.getPublicKey());
        System.out.println("Done.");
        System.out.print("Receiving PK...");
        public_key = getServerPublicKey();
        System.out.println("Done.");
    }

    private void sendClientPublicKey(BigInteger[] publicKey) {
        String pk = publicKey[0] + separator + publicKey[1];
        System.out.println("PK: " + pk);
        out.println(pk);
    }

    private BigInteger[] getServerPublicKey() {
        String[] pkRequest = in.nextLine().split(separator);
        System.out.println("PK: " + pkRequest[0] + separator + pkRequest[1]);
        BigInteger[] serverPK = {new BigInteger(pkRequest[0]), new BigInteger(pkRequest[1])};
        return serverPK;
    }

    public void close() {
        in.close();
        out.close();
        status = CLOSED;
    }
}
