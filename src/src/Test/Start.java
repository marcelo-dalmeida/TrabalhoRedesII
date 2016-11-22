package Test;

import Parts.Client;
import Parts.Server;

/**
 * @author Juan Lucas Vieira
 */
public class Start {

    static int port = 5000;

    public static void main(String[] args) {
        switch (args[0]) {
            case "client":
                Client cliente = new Client(port);
                cliente.start();
                break;
            case "server":
                Server server = new Server(port);
                server.start();
                break;
        }

    }
}
