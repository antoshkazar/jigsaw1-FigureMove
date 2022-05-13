package serverAndClient;

import userInterface.Form;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    public Socket socket;
    public ObjectOutputStream objectOutputStream;
    public Form form;
    private ClientReciever clientReciever;
    public int figuresRecieved = 0;

    public Client() {
    }

    public void connect(String host, String port) {
        try {
            var parsedPort = Integer.parseInt(port);
            InetAddress ip = InetAddress.getByName(host);
            socket = new Socket(ip, parsedPort);
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            clientReciever = new ClientReciever(this);
            clientReciever.start();
        } catch (IOException | NumberFormatException ex) {
        }
    }
}
