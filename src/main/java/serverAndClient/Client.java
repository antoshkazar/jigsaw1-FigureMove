package serverAndClient;

import userInterface.Form;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Основной класс клиента
 */
public class Client {
    public Socket socket;
    public ObjectOutputStream objectOutputStream;
    // Форма клиента
    public Form form;
    //Количество размещенных клиентом фигур
    public int figuresRecieved = 0;

    public Client() {
    }

    /**
     * Подключение клиента к серверу
     * @param host хост сервера
     * @param port порт сервера
     */
    public void connect(String host, String port) {
        try {
            var parsedPort = Integer.parseInt(port);
            InetAddress ip = InetAddress.getByName(host);
            socket = new Socket(ip, parsedPort);
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            // Класс получателя клиента
            ClientReciever clientReciever = new ClientReciever(this);
            clientReciever.start();
        } catch (IOException | NumberFormatException ignored) {
        }
    }
}
