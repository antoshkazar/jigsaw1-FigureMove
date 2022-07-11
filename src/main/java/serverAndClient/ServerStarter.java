package serverAndClient;

import userInterface.Params;

import javax.swing.*;

public class ServerStarter {
    public static void main(String[] args) {
        Object[] options1 = {"1", "2"};
        int players = 0;
        do {
            players = JOptionPane.showOptionDialog(null, null, "Количество игроков", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                    null, options1, null) + 1;
        } while (players != 1 && players != 2);
        Params.PLAYERS = players;
        try {
            Server server;
            server = new Server(Params.PLAYERS, Params.TIME);
            server.start();
            server.join();
        } catch (Exception ex) {
            System.out.println("Can't create Server socket. Try use another port " + ex.getMessage());
        }
    }
}
