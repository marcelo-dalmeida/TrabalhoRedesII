package Parts;

import Stream.SecretStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * @author Juan Lucas Vieira
 */
public class Client {
    String filename;
    String ip;
    int port;
    Socket clientS;
    String file;
    Scanner keyboard;
    SecretStream io;
    char RS = 0x1e;
    String separator = RS + "";

    public Client(int port) {
        this.port = port;
        keyboard = new Scanner(System.in);
    }

    public void start() {
        try {
            getFromUser();
            startCommunication();
        } catch (SocketException e) {
            if ("Address already in use: JVM_Bind".equals(e.getMessage())) {
                System.err.println("Port number not available.");
            } else {
                System.err.println("Couldn't connect to Server - " + e.getMessage());
            }
        } catch (UnknownHostException e) {
            System.out.println("Invalid IP");
        } catch (NoSuchFileException e) {
            System.err.println("ERROR: File " + filename + " not found.");
        } catch (IOException e) {
            System.out.println("I/O Error: " + e.getMessage());
        } finally {
            if (clientS != null) {
                try {
                    clientS.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private void startCommunication() throws IOException {
        clientS = new Socket(InetAddress.getByName(ip), port);
        io = new SecretStream(clientS);
        String message = filename + separator + file;
        io.sendSecretMessage(message);
        System.out.println("Reply: " + io.receiveSecretMessage());
    }

    private void getFromUser() throws IOException {
        System.out.print("Type the file to transfer:\n");
        filename = keyboard.nextLine();
        file = new String(fileToByteArray(filename));
        System.out.println("Type the server's IP:");
        ip = keyboard.nextLine();
    }

    private byte[] fileToByteArray(String path) throws IOException {
        return Files.readAllBytes(Paths.get(path));
    }
}
