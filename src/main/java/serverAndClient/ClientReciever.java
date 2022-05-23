package serverAndClient;

import figures.Figure;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Класс, получающий от сервера фигуру для клиента
 */
public class ClientReciever extends Thread {
    final Client client;
    final ObjectInputStream objectInputStream;
    final ObjectOutputStream objectOutputStream;

    public ClientReciever(Client client) throws IOException {
        this.client = client;
        this.objectInputStream = new ObjectInputStream(client.socket.getInputStream());
        this.objectOutputStream = new ObjectOutputStream(client.socket.getOutputStream());
    }

    /**
     * Запускаем поток.
     */
    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                // Запрашиваем фигуры, пока поле не заполнено
                do {
                    // System.out.println(client.figuresRecieved + 1);
                    objectOutputStream.write(client.figuresRecieved + 1);
                    client.form.currentFigure = (Figure) objectInputStream.readObject();
                    client.figuresRecieved++;
                    System.out.println(client.form.currentFigure);
                } while (client.form.gameFinished());
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
