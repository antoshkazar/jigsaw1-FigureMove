package userInterface;

import figures.Coord;
import figures.Figure;
import serverAndClient.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;
import java.util.Vector;

public class Form extends JFrame implements DragGestureListener {
    //Массив поля
    private Cell[][] cells;
    //Панель поля
    private JPanel field;
    //Место появления фигур (справа)
    private JPanel spawnPoint;
    int placed;
    private final JTextField hostField;
    private final JTextField portField;
    // Реализую впоследствии.
    // Проверка размещения фигур.
    CheckFigures checkFigures;
    //Вектор номеров уже сгенерированных фигур
    Vector<Integer> generated;
    //Рандомно получившаяся фигура
    public Figure currentFigure;
    Client client;
    // Реализую впоследствии.
    Timer timer;

    public Form() {
        this.setSize(600, 460);
        this.setResizable(false);
        hostField = new JTextField();
        portField = new JTextField();
        hostField.setLocation(400, 50);
        hostField.setSize(100, 40);
        hostField.setText(Params.HOST);
        portField.setText(String.valueOf(Params.PORT));
        hostField.setVisible(true);
        portField.setLocation(400, 100);
        portField.setSize(100, 40);
        portField.setVisible(true);
        this.add(hostField);
        this.add(portField);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        setTitle("Jigsaw");
        newGame();
    }

    public void setClient() {
        client = new Client();
        portField.setText(String.valueOf(Params.PORT));
        hostField.setText(Params.HOST);
        client.form = this;
        this.client.connect(Params.HOST, String.valueOf(Params.PORT));
    }

    /**
     * Создание поля и точка спавна.
     */
    void newGame() {
        cells = new Cell[Params.WIDTH][Params.HEIGHT];
        spawnPoint = new JPanel();
        field = new JPanel();
        checkFigures = new CheckFigures();
        spawnPoint.setBounds(Params.SIZE, Params.SIZE, Params.SIZE, Params.SIZE);
        spawnPoint.setBackground(Color.WHITE);
        spawnPoint.setSize(Params.SIZE * 3, Params.SIZE * 3);
        spawnPoint.setLocation(310, Params.SIZE + 30);
        spawnPoint.setForeground(new Color(25, 50, 250, 0));
        field.setBounds(5 * Params.SIZE, 5 * Params.SIZE, Params.SIZE, Params.SIZE);
        field.setSize(Params.SIZE * 9, Params.SIZE * 9);
        field.setLocation(0, 0);
        //setSize(Params.WIDTH * Params.SIZE + 150, Params.HEIGHT * Params.SIZE + 40);
        generated = new Vector<>(9);
        initCells();
        //generateFigure();
    }

    int total = 1;

    private void initCells() {
        for (int i = 0; i < Params.WIDTH; i++) {
            for (int j = 0; j < Params.HEIGHT; j++) {
                cells[i][j] = new Cell(i, j, false);
                field.add(cells[i][j]);
            }
        }
    }

    /**
     * Показ фигуры (доработаю впоследствии)
     *
     * @param coords координаты
     */
    public void seeCurrentCell(Coord coords) {
        try {

            add(spawnPoint);
            for (var point : currentFigure.points) {
                spawnPoint.add(new Cell((int) (coords.getX() + point.getX()), (int) (coords.getY() + point.getY()), true));
            }
            add(field);
            field.setBackground(Color.GRAY);
            field.setVisible(true);
            spawnPoint.setBackground(Color.WHITE);
            spawnPoint.setVisible(true);
            MyDropTargetListener myDropTargetListener = new MyDropTargetListener(field);
            var ds = new DragSource();
            ds.createDefaultDragGestureRecognizer(spawnPoint,
                    DnDConstants.ACTION_COPY, this);
        } catch (Exception ignored) {

        }
    }

    /***
     * отслеживание курсора.
     */
    public void dragGestureRecognized(DragGestureEvent event) {
        try {
            var cursor = Cursor.getDefaultCursor();
            var movedCell = (JPanel) event.getComponent();
            var color = movedCell.getBackground();
            if (event.getDragAction() != DnDConstants.ACTION_COPY) {
                cursor = DragSource.DefaultCopyDrop;
            }
            event.startDrag(cursor, new TransferableColor(color));
        } catch (Exception ignored) {
        }
    }

    /**
     * Проверка на то, можно ли разместить фигуру на месте, где был отпущен курсор.
     */
    public class CheckFigures {
        public CheckFigures() {
        }

        public boolean checkJ0(int position) {
            if (field.getComponent(position - 1).getBackground() != Params.BACKGROUND ||
                    field.getComponent(position + 1).getBackground() != Params.BACKGROUND ||
                    field.getComponent(position - 8).getBackground() != Params.BACKGROUND ||
                    position % 9 == 0 || (position + 1) % 9 == 0) {
                return false;
            }
            field.getComponent(position).setBackground(Color.ORANGE);
            field.getComponent(position - 1).setBackground(Color.ORANGE);
            field.getComponent(position + 1).setBackground(Color.ORANGE);
            field.getComponent(position - 8).setBackground(Color.ORANGE);
            return true;
        }

        public boolean checkJ2(int position) {
            if (field.getComponent(position - 1).getBackground() != Params.BACKGROUND ||
                    field.getComponent(position + 1).getBackground() != Params.BACKGROUND ||
                    field.getComponent(position + 8).getBackground() != Params.BACKGROUND ||
                    position % 9 == 0 || (position + 1) % 9 == 0) {
                return false;
            }
            field.getComponent(position).setBackground(Color.ORANGE);
            field.getComponent(position - 1).setBackground(Color.ORANGE);
            field.getComponent(position + 1).setBackground(Color.ORANGE);
            field.getComponent(position + 8).setBackground(Color.ORANGE);
            return true;
        }

        public boolean checkJ1(int position) {
            if (field.getComponent(position - 10).getBackground() != Params.BACKGROUND ||
                    field.getComponent(position - 9).getBackground() != Params.BACKGROUND ||
                    field.getComponent(position + 9).getBackground() != Params.BACKGROUND ||
                    (position - 9) % 9 == 0) {
                return false;
            }
            field.getComponent(position).setBackground(Color.ORANGE);
            field.getComponent(position - 10).setBackground(Color.ORANGE);
            field.getComponent(position - 9).setBackground(Color.ORANGE);
            field.getComponent(position + 9).setBackground(Color.ORANGE);
            return true;
        }

        public boolean checkJ3(int position) {
            if (field.getComponent(position + 10).getBackground() != Params.BACKGROUND ||
                    field.getComponent(position - 9).getBackground() != Params.BACKGROUND ||
                    field.getComponent(position + 9).getBackground() != Params.BACKGROUND ||
                    (position + 9 + 1) % 9 == 0) {
                return false;
            }
            field.getComponent(position).setBackground(Color.ORANGE);
            field.getComponent(position + 10).setBackground(Color.ORANGE);
            field.getComponent(position - 9).setBackground(Color.ORANGE);
            field.getComponent(position + 9).setBackground(Color.ORANGE);
            return true;
        }

        public boolean checkL0(int position) {
            if (field.getComponent(position - 1).getBackground() != Params.BACKGROUND ||
                    field.getComponent(position + 1).getBackground() != Params.BACKGROUND ||
                    field.getComponent(position - 10).getBackground() != Params.BACKGROUND ||
                    position % 9 == 0 || (position + 1) % 9 == 0) {
                return false;
            }
            field.getComponent(position).setBackground(Color.ORANGE);
            field.getComponent(position - 1).setBackground(Color.ORANGE);
            field.getComponent(position + 1).setBackground(Color.ORANGE);
            field.getComponent(position - 10).setBackground(Color.ORANGE);
            return true;
        }

        public boolean checkL2(int position) {
            if (field.getComponent(position - 1).getBackground() != Params.BACKGROUND ||
                    field.getComponent(position + 1).getBackground() != Params.BACKGROUND ||
                    field.getComponent(position + 10).getBackground() != Params.BACKGROUND ||
                    position % 9 == 0 || (position + 1) % 9 == 0) {
                return false;
            }
            field.getComponent(position).setBackground(Color.ORANGE);
            field.getComponent(position - 1).setBackground(Color.ORANGE);
            field.getComponent(position + 1).setBackground(Color.ORANGE);
            field.getComponent(position + 10).setBackground(Color.ORANGE);
            return true;
        }

        public boolean checkL1(int position) {
            if (field.getComponent(position + 8).getBackground() != Params.BACKGROUND ||
                    field.getComponent(position - 9).getBackground() != Params.BACKGROUND ||
                    field.getComponent(position + 9).getBackground() != Params.BACKGROUND ||
                    (position - 9) % 9 == 0) {
                return false;
            }
            field.getComponent(position).setBackground(Color.ORANGE);
            field.getComponent(position + 8).setBackground(Color.ORANGE);
            field.getComponent(position - 9).setBackground(Color.ORANGE);
            field.getComponent(position + 9).setBackground(Color.ORANGE);
            return true;
        }

        public boolean checkL3(int position) {
            if (field.getComponent(position - 8).getBackground() != Params.BACKGROUND ||
                    field.getComponent(position - 9).getBackground() != Params.BACKGROUND ||
                    field.getComponent(position + 9).getBackground() != Params.BACKGROUND ||
                    (position + 9 + 1) % 9 == 0) {
                return false;
            }
            field.getComponent(position).setBackground(Color.ORANGE);
            field.getComponent(position - 8).setBackground(Color.ORANGE);
            field.getComponent(position - 9).setBackground(Color.ORANGE);
            field.getComponent(position + 9).setBackground(Color.ORANGE);
            return true;
        }

        public boolean checkSingleton(int position) {
            if (field.getComponent(position).getBackground() != Params.BACKGROUND) {
                return false;
            }
            field.getComponent(position).setBackground(Color.ORANGE);
            return true;
        }

        public boolean checkZ0(int position) {
            if (field.getComponent(position - 1).getBackground() != Params.BACKGROUND ||
                    field.getComponent(position + 10).getBackground() != Params.BACKGROUND ||
                    field.getComponent(position + 9).getBackground() != Params.BACKGROUND ||
                    (position + 9 + 1) % 9 == 0 || position % 9 == 0) {
                return false;
            }
            field.getComponent(position).setBackground(Color.ORANGE);
            field.getComponent(position - 1).setBackground(Color.ORANGE);
            field.getComponent(position + 10).setBackground(Color.ORANGE);
            field.getComponent(position + 9).setBackground(Color.ORANGE);
            return true;
        }

        public boolean checkZ2(int position) {
            if (field.getComponent(position + 1).getBackground() != Params.BACKGROUND ||
                    field.getComponent(position + 8).getBackground() != Params.BACKGROUND ||
                    field.getComponent(position + 9).getBackground() != Params.BACKGROUND ||
                    (position + 1) % 9 == 0 || (position) % 9 == 0) {
                return false;
            }
            field.getComponent(position).setBackground(Color.ORANGE);
            field.getComponent(position + 1).setBackground(Color.ORANGE);
            field.getComponent(position + 8).setBackground(Color.ORANGE);
            field.getComponent(position + 9).setBackground(Color.ORANGE);
            return true;
        }

        public boolean checkZ1(int position) {
            if (field.getComponent(position - 1).getBackground() != Params.BACKGROUND ||
                    field.getComponent(position + 8).getBackground() != Params.BACKGROUND ||
                    field.getComponent(position - 9).getBackground() != Params.BACKGROUND ||
                    (position + 1) % 9 == 0 || (position) % 9 == 0) {
                return false;
            }
            field.getComponent(position).setBackground(Color.ORANGE);
            field.getComponent(position - 1).setBackground(Color.ORANGE);
            field.getComponent(position + 8).setBackground(Color.ORANGE);
            field.getComponent(position - 9).setBackground(Color.ORANGE);
            return true;
        }

        public boolean checkZ3(int position) {
            if (field.getComponent(position - 1).getBackground() != Params.BACKGROUND ||
                    field.getComponent(position + 9).getBackground() != Params.BACKGROUND ||
                    field.getComponent(position - 10).getBackground() != Params.BACKGROUND ||
                    (position + 1) % 9 == 0 || (position) % 9 == 0) {
                return false;
            }
            field.getComponent(position).setBackground(Color.ORANGE);
            field.getComponent(position - 1).setBackground(Color.ORANGE);
            field.getComponent(position + 9).setBackground(Color.ORANGE);
            field.getComponent(position - 10).setBackground(Color.ORANGE);
            return true;
        }

        public boolean checkI0(int position) {
            if (field.getComponent(position + 9).getBackground() != Params.BACKGROUND ||
                    field.getComponent(position - 9).getBackground() != Params.BACKGROUND) {
                return false;
            }
            field.getComponent(position).setBackground(Color.ORANGE);
            field.getComponent(position + 9).setBackground(Color.ORANGE);
            field.getComponent(position - 9).setBackground(Color.ORANGE);
            return true;
        }

        public boolean checkI1(int position) {
            if (field.getComponent(position + 1).getBackground() != Params.BACKGROUND ||
                    field.getComponent(position - 1).getBackground() != Params.BACKGROUND ||
                    position % 9 == 0 || (position + 1) % 9 == 0) {
                return false;
            }
            field.getComponent(position).setBackground(Color.ORANGE);
            field.getComponent(position + 1).setBackground(Color.ORANGE);
            field.getComponent(position - 1).setBackground(Color.ORANGE);
            return true;
        }

        public boolean checkSmallJ0(int position) {
            if (field.getComponent(position + 1).getBackground() != Params.BACKGROUND ||
                    field.getComponent(position + 9).getBackground() != Params.BACKGROUND ||
                    (position + 1) % 9 == 0) {
                return false;
            }
            field.getComponent(position).setBackground(Color.ORANGE);
            field.getComponent(position + 1).setBackground(Color.ORANGE);
            field.getComponent(position + 9).setBackground(Color.ORANGE);
            return true;
        }

        public boolean checkSmallJ2(int position) {
            if (field.getComponent(position - 1).getBackground() != Params.BACKGROUND ||
                    field.getComponent(position - 9).getBackground() != Params.BACKGROUND ||
                    position % 9 == 0) {
                return false;
            }
            field.getComponent(position).setBackground(Color.ORANGE);
            field.getComponent(position - 1).setBackground(Color.ORANGE);
            field.getComponent(position - 9).setBackground(Color.ORANGE);
            return true;
        }

        public boolean checkSmallJ1(int position) {
            if (
                    field.getComponent(position - 9).getBackground() != Params.BACKGROUND ||
                            field.getComponent(position + 1).getBackground() != Params.BACKGROUND ||
                            (position + 1) % 9 == 0) {
                return false;
            }
            field.getComponent(position).setBackground(Color.ORANGE);
            field.getComponent(position - 9).setBackground(Color.ORANGE);
            field.getComponent(position + 1).setBackground(Color.ORANGE);
            return true;
        }

        public boolean checkSmallJ3(int position) {
            if (field.getComponent(position - 1).getBackground() != Params.BACKGROUND ||
                    field.getComponent(position + 9).getBackground() != Params.BACKGROUND ||
                    position % 9 == 0) {
                return false;
            }
            field.getComponent(position).setBackground(Color.ORANGE);
            field.getComponent(position - 1).setBackground(Color.ORANGE);
            field.getComponent(position + 9).setBackground(Color.ORANGE);
            return true;
        }

    }


    /**
     * Все клетки заполнены.
     */
    public boolean gameFinished() {
        for (int i = 0; i < field.getComponentCount(); i++) {
            if (field.getComponent(i).getBackground() == Params.BACKGROUND) {
                return true;
            }
        }
        return false;
    }

    private class MyDropTargetListener extends DropTargetAdapter {

        public MyDropTargetListener(JPanel panel) {
            DropTarget dropTarget = new DropTarget(panel, DnDConstants.ACTION_MOVE,
                    this, true, null);
        }
        //Координаты краев поля, для удобства.
        /*
        java.awt.Point[x=0,y=0]
        java.awt.Point[x=0,y=240]
        java.awt.Point[x=210,y=240]
        java.awt.Point[x=240,y=240]
        */

        boolean isPossibleToPaste(Figure figure, int position) {
            if (field.getComponent(position).getBackground() != Params.BACKGROUND) {
                return false;
            }
            return switch (figure) {
                case J0 -> checkFigures.checkJ0(position);
                case J1 -> checkFigures.checkJ1(position);
                case J2 -> checkFigures.checkJ2(position);
                case J3 -> checkFigures.checkJ3(position);
                case L0 -> checkFigures.checkL0(position);
                case L1 -> checkFigures.checkL1(position);
                case L2 -> checkFigures.checkL2(position);
                case L3 -> checkFigures.checkL3(position);
                case Z0 -> checkFigures.checkZ0(position);
                case Z1 -> checkFigures.checkZ1(position);
                case Z2 -> checkFigures.checkZ2(position);
                case Z3 -> checkFigures.checkZ3(position);
                case I0 -> checkFigures.checkI0(position);
                case I1 -> checkFigures.checkI1(position);
                case smallL0 -> checkFigures.checkSmallJ0(position);
                case smallL1 -> checkFigures.checkSmallJ1(position);
                case smallL2 -> checkFigures.checkSmallJ2(position);
                case smallL3 -> checkFigures.checkSmallJ3(position);
                default -> checkFigures.checkSingleton(position);

            };
        }

        /**
         * Фигура отпущена
         *
         */
        public void drop(DropTargetDropEvent event) {
            try {
                var cursorDropped = event.getLocation();
                int position = (cursorDropped.x / 30) * 9 + cursorDropped.y / 30;
                if (isPossibleToPaste(currentFigure, position)) {
                    event.dropComplete(true);
                    placed++;
                    return;
                }
                event.rejectDrop();
            } catch (Exception e) {
                event.rejectDrop();
            }
        }


    }


    static class TransferableColor implements Transferable {

        protected static final DataFlavor colorFlavor =
                new DataFlavor(Color.class, "A Color Object");

        protected static final DataFlavor[] supportedFlavors = {
                colorFlavor,
                DataFlavor.stringFlavor,
        };

        private final Color color;

        public TransferableColor(Color color) {

            this.color = color;
        }

        public DataFlavor[] getTransferDataFlavors() {
            return supportedFlavors;
        }

        public boolean isDataFlavorSupported(DataFlavor flavor) {

            return flavor.equals(colorFlavor) ||
                    flavor.equals(DataFlavor.stringFlavor);
        }

        public Object getTransferData(DataFlavor flavor)
                throws UnsupportedFlavorException {

            if (flavor.equals(colorFlavor)) {
                return color;
            } else if (flavor.equals(DataFlavor.stringFlavor)) {
                return color.toString();
            } else {
                throw new UnsupportedFlavorException(flavor);
            }
        }
    }

}
