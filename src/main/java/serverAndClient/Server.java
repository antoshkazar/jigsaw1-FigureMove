package serverAndClient;

import figures.Figure;
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
    private int totalClients = 0;
    // Вектор номеров уже сгенерированных фигур
    Vector<Integer> generated = new Vector<>();
    Vector<Figure> figures = new Vector<>();
    // Рандомно получившаяся фигура
    Figure currentFigure;
    int total = 1;

    public Server(int usersMax, int timeMax) throws IOException {
        serverSocket = new ServerSocket(Params.PORT);
        this.usersMax = usersMax;
    }

    /**
     * Генерация фигуры
     */
    void generateFigure() {
        Random rnd = new Random();
        int rand = rnd.nextInt(19);
        if (generated.contains(rand) && total < 19) {
            do {
                rand = rnd.nextInt(19);
            } while (generated.contains(rand));
        }
        generated.add(rand);
        total++;
        currentFigure = getFigure(rand);
    }

    /**
     * Получение фигуры по номеру
     */
    private Figure getFigure(int rand) {
        return switch (rand) {
            case 0 -> Figure.J0;
            case 1 -> Figure.J1;
            case 2 -> Figure.J2;
            case 3 -> Figure.J3;
            case 4 -> Figure.L0;
            case 5 -> Figure.L1;
            case 6 -> Figure.L2;
            case 7 -> Figure.L3;
            case 8 -> Figure.Z0;
            case 9 -> Figure.Z1;
            case 10 -> Figure.Z2;
            case 11 -> Figure.Z3;
            case 12 -> Figure.smallL0;
            case 13 -> Figure.smallL1;
            case 14 -> Figure.smallL2;
            case 15 -> Figure.smallL3;
            case 16 -> Figure.I0;
            case 17 -> Figure.I1;
            case 18 -> Figure.singleton;
            case 19 -> Figure.doubleL1;
            case 20 -> Figure.doubleL2;
            case 21 -> Figure.doubleL3;
            case 22 -> Figure.doubleL0;
            case 23 -> Figure.T1;
            case 24 -> Figure.T2;
            case 25 -> Figure.T3;
            case 26 -> Figure.doubleT0;
            case 27 -> Figure.doubleT1;
            case 28 -> Figure.doubleT2;
            case 29 -> Figure.doubleT3;
            default -> Figure.T0;
        };
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
                    ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                    int num = 1;
                    if (figures.size() < num) {
                        generateFigure();
                        figures.add(currentFigure);
                    }
                    ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                    out.writeObject(figures.get(0));

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
