package serverAndClient;

import figures.Coord;
import userInterface.Form;
import userInterface.Params;

import javax.swing.*;

public class ClientStarter {
    public static void main(String[] args) {
        if (Params.activePlayers < Params.PLAYERS) {
            // System.out.println(Params.PLAYERS);
            Params.activePlayers += 1;
            Form form = new Form();
            form.setClient();
            form.setVisible(true);
            //Form form1 = new Form();
            form.seeCurrentCell(new Coord(0, 0));
        } else {
            JOptionPane.showMessageDialog(null, "Maximum players number reached!");
        }
    }
}
