package sdf;

import java.net.Socket;

public class Main {
    public static void main (String[] args) throws Exception{

        String hostname = "";
        int port;

        switch (args.length) {
            case 1:
                port = Integer.parseInt(args[0]);
                break;
            case 2:
                hostname = args[0];
                port = Integer.parseInt(args[1]);
                break;
            default:
                hostname = "localhost";
                port = 3000;
                break;
        }

        // Connect to the server
        System.out.printf("Attempting to connect to %s on port %d\n", hostname, port);
        Socket socket = new Socket(hostname, port);
        System.out.println("Connected to server");

        Client sess = new Client(socket);
        sess.start();
    }

}
