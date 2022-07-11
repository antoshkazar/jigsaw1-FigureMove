package userInterface;

import javax.swing.*;
import java.awt.*;

public class Cell extends JPanel {
    Color color;

    /**
     * Сущность клетки (поля и фигуры).
     * @param x координата по Х
     * @param y координата по У
     * @param isFigure Указывает на то, является ли клетка частью фигуры или поля
     */
    Cell(int x, int y, boolean isFigure) {
        if (!isFigure) {
            setBounds(x * Params.SIZE, y * Params.SIZE, Params.SIZE, Params.SIZE);
            setBorder(BorderFactory.createLineBorder(Color.black));
            setSize(Params.SIZE, Params.SIZE);
            setLocation(x * Params.SIZE, y * Params.SIZE);
            this.color = Params.BACKGROUND;
            setBackground(Params.BACKGROUND);
        } else {
            setBounds(x * Params.SIZE, y * Params.SIZE, Params.SIZE, Params.SIZE);
            setBorder(BorderFactory.createLineBorder(Color.black));
            setSize(Params.SIZE, Params.SIZE);
            setLocation(x * Params.SIZE + 310, y * Params.SIZE + 90);
            this.color = Params.FOREGROUND;
            setBackground(Params.FOREGROUND);
        }
    }

}
