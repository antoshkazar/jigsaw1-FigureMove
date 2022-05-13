package serverAndClient;

import figures.Figure;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ClientReciever extends Thread {
    final Client client;
    final ObjectInputStream objectInputStream;
    final ObjectOutputStream objectOutputStream;

    public ClientReciever(Client client) throws IOException {
        this.client = client;
        this.objectInputStream = new ObjectInputStream(client.socket.getInputStream());
        this.objectOutputStream = new ObjectOutputStream(client.socket.getOutputStream());
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                // System.out.println(client.figuresRecieved + 1);
                objectOutputStream.write(client.figuresRecieved + 1);
                var receivedObject = (Figure) objectInputStream.readObject();
                client.form.currentFigure = receivedObject;
                client.figuresRecieved++;
                System.out.println(client.form.currentFigure);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
