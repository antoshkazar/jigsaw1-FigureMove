package serverAndClient;

import java.io.*;
import java.net.Socket;

/**
 * Класс, являющийся потоком клиента в потоке сервера.
 */
public class ClientInServer extends Thread {
    private final Socket clientSocket;
    private final Server server;
    private final int ID;
    private static BufferedReader in; // поток чтения из сокета
    private static BufferedWriter out;


    public ClientInServer(Socket clientSocket, int ID, Server server) throws IOException {
        if (server.clients.size() > 2) {
            server.clients.clear();
        }
        this.clientSocket = clientSocket;
        this.ID = ID;
        this.server = server;
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        // и отправлять
        out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
    }

    @Override
    public void run() {
        while (!isInterrupted()){
        }
        try {
            // closing resources
            in.close();
            out.close();
            this.clientSocket.close();
            System.out.println("ClientInServer " + ID + " closed");
            this.server.clients.remove(this);
        } catch (IOException ignored) {
        }
    }

    public void cancelConnection() throws IOException {
        // ActionSender.sendCancelByMaxCount(objectOutputStream);
        in.close();
        out.close();
        clientSocket.close();
        System.out.println("User connection canceled. Maximum number of users reached.");
    }
}
