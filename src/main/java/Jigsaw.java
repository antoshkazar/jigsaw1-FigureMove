import figures.Coord;
import figures.Figure;
import userInterface.Form;

public class Jigsaw {
    public static void main(String[] args) {
        Form form = new Form();
        //System.out.println(form.getSize());
        form.seeCurrentCell(new Coord(0, 0), Figure.J0);
        //form.seeCurrentFigure(new Coord(0, 0), Figure.J0);
    }
}
