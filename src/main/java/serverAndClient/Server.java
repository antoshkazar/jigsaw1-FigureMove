package serverAndClient;

import figures.Figure;
import figures.FigureGenerator;
import userInterface.Params;

import java.io.IOException;
import java.io.*;
import java.net.*;
import java.util.Random;
import java.util.Vector;

public class Server extends Thread {
    Vector<ClientInServer> clients = new Vector<>();
    private final ServerSocket serverSocket;
    private final int usersMax;
    public int totalClients = 0;
    Vector<Figure> figures = new Vector<>();

    public Server(int usersMax, int timeMax) throws IOException {
        serverSocket = new ServerSocket(Params.PORT);
        this.usersMax = usersMax;
    }

    @Override
    public void run() {
        try {
            System.out.println("Сreated server. Socket:  " + serverSocket);
            while (!Thread.interrupted()) {
                try {
                    Socket socket = serverSocket.accept();
                    System.out.println("New request from client accepted : " + socket);
                    var client = new ClientInServer(socket, ++totalClients, this);
                    if (clients.size() + 1 > usersMax) {
                        client.cancelConnection();
                        continue;
                    }
                    client.start();
                    clients.add(client);
                    System.out.println("Client added on server, created handler for client :" + totalClients);
                } catch (IOException ex) {
                    System.out.println("Creating handler for client " + totalClients + "went with an ERROR");
                }
            }
            // Close the server once done.
            if (!serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException ex) {
            System.out.println("Socket Error!");
        }
    }
}
