package serverAndClient;

import derby.DerbyConnectionProvider;
import derby.DerbyService;
import figures.FigureGenerator;

import java.io.*;
import java.net.Socket;
import java.sql.*;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.Temporal;
import java.util.UUID;

/**
 * Класс, являющийся потоком клиента в потоке сервера.
 */
public class ClientInServer extends Thread {
    private final Socket clientSocket;
    private final Server server;
    private final int ID;
    private static ObjectInputStream in; // поток чтения из сокета
    private static ObjectOutputStream out;
    private String login;
    private Instant startTime;


    public ClientInServer(Socket clientSocket, int ID, Server server) throws IOException {
        if (server.clients.size() > 2) {
            server.clients.clear();
        }
        this.clientSocket = clientSocket;
        this.ID = ID;
        this.server = server;
        // и отправлять
        out = new ObjectOutputStream(clientSocket.getOutputStream());
        in = new ObjectInputStream(clientSocket.getInputStream());
    }

    @Override
    public void run() {
        try {
            login = (String) in.readObject();
            startTime = Instant.now();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        while (!isInterrupted()) {
            try {
                String command = (String) in.readObject();
                if ("getFigure".equals(command)) {
                    int num = (int) in.readObject();
                    // System.out.println(server.figures.size() + " " + num);
                    if (server.figures.size() < num + 1) {
                        server.figures.add(FigureGenerator.getInstance().generateFigure());
                    }
                    out.writeObject(server.figures.get(num));
                    //out.writeObject(FigureGenerator.getInstance().generateFigure());
                } else if ("top10".equals(command)) {
                    out.writeObject(DerbyService.getResultsTable());
                } else if ("endgame".equals(command)) {
                    int steps = (int) in.readObject();
                    System.out.println("ENDGAME!");
                    endGame(steps);
                    interrupt();
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        try {
            // closing resources
            in.close();
            out.close();
            this.clientSocket.close();
            System.out.println("Client " + ID + " closed");
            this.server.clients.remove(this);
            this.server.totalClients -= 1;
        } catch (IOException ignored) {
        }
    }

    private void endGame(int steps) {
        Instant endTime = Instant.now();
        DerbyService.writeGameResults(login, endTime, startTime, steps);
    }

    public void cancelConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
        System.out.println("User connection canceled. Maximum number of users reached.");
    }
}
