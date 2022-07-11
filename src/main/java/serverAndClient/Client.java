package serverAndClient;

import userInterface.Form;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Основной класс клиента
 */
public class Client implements AutoCloseable {
    public Socket socket;
    // Форма клиента
    public Form form;
    //Количество размещенных клиентом фигур
    public int figuresReceived = 0;
    public ClientReciever clientReciever;
    private final String login;

    public Client(String login) {
        this.login = login;
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
            // Класс получателя клиента
            clientReciever = new ClientReciever(this);
            clientReciever.sendLogin(login);
        } catch (IOException | NumberFormatException ignored) {
        }
    }

    @Override
    public void close() throws Exception {
        clientReciever.close();
    }
}
