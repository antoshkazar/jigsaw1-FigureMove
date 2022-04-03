import figures.Coord;
import figures.Figure;
import userInterface.Form;

import javax.swing.*;

public class Jigsaw {
    public static void main(String[] args) {
        Form form = new Form();
        form.seeCurrentCell(new Coord(0,0), Figure.J0);
        //form.seeCurrentFigure(new Coord(0, 0), Figure.J0);
    }
}
