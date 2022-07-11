package serverAndClient;

import dto.GameResult;
import figures.Figure;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * Класс для общения с сервером
 */
public class ClientReciever implements AutoCloseable {
    final Client client;
    final ObjectInputStream objectInputStream;
    final ObjectOutputStream objectOutputStream;

    public ClientReciever(Client client) throws IOException {
        this.client = client;
        this.objectOutputStream = new ObjectOutputStream(client.socket.getOutputStream());
        this.objectInputStream = new ObjectInputStream(client.socket.getInputStream());
    }

    public void sendLogin(String login) {
        try {
            objectOutputStream.writeObject(login);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Figure getFigure(int placed) throws IOException, ClassNotFoundException {
        objectOutputStream.writeObject("getFigure");
        objectOutputStream.writeObject(placed);
        client.form.currentFigure = (Figure) objectInputStream.readObject();
        // System.out.println(client.form.currentFigure);
        client.figuresReceived++;
        System.out.println(client.form.currentFigure);
        return client.form.currentFigure;
    }

    public void sendEndGame() {
        try {
            objectOutputStream.writeObject("endgame");
            objectOutputStream.writeObject(client.figuresReceived);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<GameResult> getResults() throws IOException, ClassNotFoundException {
        objectOutputStream.writeObject("top10");
        return (List<GameResult>) (objectInputStream.readObject());
    }

    @Override
    public void close() throws Exception {
        this.client.socket.close();
    }
}
