package Parts;

import Stream.SecretStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * @author Juan Lucas Vieira
 */
public class Server {

    Socket socket;
    String ip;
    String status;
    String filename;
    String file;
    SecretStream io;
    private int serverPort;
    char RS = 0x1e;
    String separator = RS + "";

    public Server(int serverPort) {
        this.serverPort = serverPort;
    }

    public void start() {
        System.out.println("Server running...");
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(serverPort);
            System.out.println("Waiting for connection...");
            socket = serverSocket.accept();
            ip = socket.getInetAddress().toString();
            System.out.println("Connected to " + ip + ".");
            startCommunication();
        } catch (SocketException e) {
            if ("Address already in use: JVM_Bind".equals(e.getMessage())) {
                System.err.println("ERROR: Port number not available.");
            } else {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private void startCommunication() throws IOException {
        io = new SecretStream(socket);
        String msg = io.receiveSecretMessage();
        if (msg.equalsIgnoreCase(separator+"CRC Mismatch"+separator)) {
            io.sendSecretMessage("File transfer failed: CRC mismatch!");
        } else {
            String[] fileProp = msg.split(separator);
            filename = fileProp[0];
            file = fileProp[1];
            byte[] filetext = file.getBytes();
            saveFile(filename, filetext);
            io.sendSecretMessage("File transfer succeded.");
        }
    }

    private void saveFile(String filename, byte[] answer) throws IOException {
        System.out.print("Saving data in file... ");
        File file = new File("Server/" + ip);
        file.mkdirs();
        FileOutputStream fos = new FileOutputStream("Server/" + ip + "/" + filename);
        fos.write(answer);
        fos.close();
        System.out.println("Done.");
    }
}
