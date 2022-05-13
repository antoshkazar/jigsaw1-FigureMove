package serverAndClient;

import userInterface.Params;

import java.io.*;
import java.net.*;
import java.util.Objects;

public class ClientHandler extends Thread {
    private Socket clientSocket;
    private Server server;
    private int ID;
    private static BufferedReader in; // поток чтения из сокета
    private static BufferedWriter out;


    public ClientHandler(Socket clientSocket, int ID, Server server) throws IOException {
        if (server.clients.size() > 2){
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
        System.out.println(" Client created ID " + " : " + ID);
        while (!isInterrupted()) {

        }
        try {
            // closing resources
            this.in.close();
            this.out.close();
            this.clientSocket.close();
            System.out.println("ClientHandler " + ID + " closed");
            this.server.clients.remove(this);
        } catch (IOException e) {
        }
    }

    public void cancelConnection() throws IOException {
        // ActionSender.sendCancelByMaxCount(objectOutputStream);
        in.close();
        out.close();
        clientSocket.close();
        System.out.println("User connection canceled. Maximum number of users reached.");
    }

    public void startConnection() {
        //System.out.println("here");
        try {
            try {
                // адрес - локальный хост, порт - 4004, такой же как у сервера
                clientSocket = new Socket("localhost", Params.PORT); // этой строкой мы запрашиваем
                //  у сервера доступ на соединение
                // нам нужен ридер читающий с консоли, иначе как
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                // читать соообщения с сервера
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                // писать туда же
                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                System.out.println("Вы что-то хотели сказать? Введите это здесь:");
                // если соединение произошло и потоки успешно созданы - мы можем
                //  работать дальше и предложить клиенту что то ввести
                // если нет - вылетит исключение
                String word = reader.readLine(); // ждём пока клиент что-нибудь
                // не напишет в консоль
                out.write(word + "\n"); // отправляем сообщение на сервер
                out.flush();
                String serverWord = in.readLine(); // ждём, что скажет сервер
                System.out.println(serverWord); // получив - выводим на экран
            } finally { // в любом случае необходимо закрыть сокет и потоки
                System.out.println("Клиент был закрыт...");
                clientSocket.close();
                in.close();
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
