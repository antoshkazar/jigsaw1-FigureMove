package userInterface;

import figures.Coord;
import figures.Figure;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;
import java.awt.event.*;
import java.sql.Array;

public class Form extends JFrame implements DragGestureListener {
    private Cell[][] cells;
    private JPanel field;
    private final JPanel spawnPoint;

    public Form() {
        cells = new Cell[Params.WIDTH][Params.HEIGHT];
        this.setSize(430, 330);
        this.setResizable(false);
        spawnPoint = new JPanel();
        field = new JPanel();
        spawnPoint.setBounds(5 * Params.SIZE, 5 * Params.SIZE, Params.SIZE, Params.SIZE);
        //setBorder(BorderFactory.createLineBorder(Color.black));
        spawnPoint.setSize(Params.SIZE * 2, Params.SIZE * 1);
        spawnPoint.setLocation(310, 1 * Params.SIZE + 90);
        field.setBounds(5 * Params.SIZE, 5 * Params.SIZE, Params.SIZE, Params.SIZE);
        field.setSize(Params.SIZE * 9, Params.SIZE * 9);
        field.setLocation(0, 0);
        setSize(Params.WIDTH * Params.SIZE + 150, Params.HEIGHT * Params.SIZE + 40);
        setDefaultCloseOperation(EXIT_ON_CLOSE); // Исправить потом на предложение начать заново
        setLocationRelativeTo(null);
        setLayout(null);
        setTitle("Jigsaw");
        initCells();
        setVisible(true);

    }


    private void initCells() {

        for (int i = 0; i < Params.WIDTH; i++) {
            for (int j = 0; j < Params.HEIGHT; j++) {
                cells[i][j] = new Cell(i, j, false);
                field.add(cells[i][j]);
            }
        }
        //System.out.println(field.getComponent(0).getLocation());
        // System.out.println(field.getComponent(8).getLocation());
        //System.out.println(field.getComponent(71).getLocation());
        //System.out.println(field.getComponent(80).getLocation());
        // ClickListener clickListener = new ClickListener();
        //  DragListener dragListener = new DragListener();
        //this.addMouseListener(clickListener);
        //this.addMouseMotionListener(dragListener);
    }

    Figure figureField;

    public void seeCurrentCell(Coord coords, Figure figure) {
        int lastX = 0, lastY = 0;

        add(spawnPoint);
        for (var point : figure.points) {
            spawnPoint.add(new Cell((int) (coords.getX() + point.getX()), (int) (coords.getY() + point.getY()), true));
            //System.out.println(new Cell((int) (coords.getX() + point.getX()), (int) (coords.getY() + point.getY()), true));
            lastX = (int) (coords.getX() + point.getX());
            lastY = (int) (coords.getY() + point.getY());

        }
        spawnPoint.setBackground(Color.WHITE);
        add(field);
        field.setBackground(Color.GRAY);
        field.setVisible(true);
        spawnPoint.setVisible(true);
        //System.out.println(spawnPoint);
        MyDropTargetListener myDropTargetListener = new MyDropTargetListener(field);
        //MyDropTargetListener[][] myDropTargetListener = new MyDropTargetListener[Params.WIDTH][Params.HEIGHT];
        // for (int i = 0; i < Params.WIDTH; i++) {
        //  for (int j = 0; j < Params.HEIGHT; j++) {
        //myDropTargetListener = new MyDropTargetListener(field);
        // }
        // }
        var ds = new DragSource();
        ds.createDefaultDragGestureRecognizer(spawnPoint,
                DnDConstants.ACTION_COPY, this);
    }
/*
    public void seeCurrentFigure(Coord coords, Figure figure) {
        for (var point : figure.points) {
            spawnPoint[(int) point.getX()][(int) point.getY()] = new Cell((int) (coords.getX() + point.getX()), (int) (coords.getY() + point.getY()), true);
            add(spawnPoint[(int) point.getX()][(int) point.getY()]);
        }
    }*/

    Point prevPt;

    public void dragGestureRecognized(DragGestureEvent event) {
        var cursor = Cursor.getDefaultCursor();
        var movedCell = (JPanel) event.getComponent();
        //System.out.println(event.getComponent().getClass());
        /*
        for (var pointArr : cells) {
            for (var point : pointArr) {
                if (panel == point) {
                    System.out.println(true);
                }
            }
        }*/
        var color = movedCell.getBackground();
        if (event.getDragAction() != DnDConstants.ACTION_COPY) {
            cursor = DragSource.DefaultCopyDrop;
        }
        event.startDrag(cursor, new TransferableColor(color));
    }


    private class MyDropTargetListener extends DropTargetAdapter {

        private final DropTarget dropTarget;
        private final JPanel panel;
        Cell[][] spawnpoint;

        public MyDropTargetListener(JPanel panel) {
            this.panel = panel;
            dropTarget = new DropTarget(panel, DnDConstants.ACTION_MOVE,
                    this, true, null);
            //System.out.println(dropTarget.getComponent());
            //this.spawnpoint = spawnpoint;
        }

        /*
    java.awt.Point[x=0,y=0]
    java.awt.Point[x=0,y=240]
    java.awt.Point[x=210,y=240]
    java.awt.Point[x=240,y=240]
     */

        

        public void drop(DropTargetDropEvent event) {
            try {
                var cursorDropped = event.getLocation();
                System.out.println(cursorDropped);
                //System.out.println(cursorDropped.x / 30);
                int position = (int) ((cursorDropped.x / 30) * 9 + cursorDropped.y / 30);
                System.out.println(position);
                field.getComponent(position).setBackground(Color.ORANGE);
                field.getComponent(position - 1).setBackground(Color.ORANGE);
                field.getComponent(position + 1).setBackground(Color.ORANGE);
                field.getComponent(position - 8).setBackground(Color.ORANGE);

                event.dropComplete(true);

                return;
                //event.rejectDrop();
            } catch (Exception e) {
                e.printStackTrace();
                event.rejectDrop();
            }
        }
    }


    class TransferableColor implements Transferable {

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
/*
    private void createLayout(JComponent... arg) {
        var pane = getContentPane();
        var gl = new GroupLayout(pane);
        pane.setLayout(gl);
        gl.setAutoCreateContainerGaps(true);
        gl.setAutoCreateGaps(true);

        gl.setHorizontalGroup(gl.createSequentialGroup()
                .addComponent(arg[0])
                .addGap(30)
                .addComponent(arg[1])
                .addGap(30)
                .addComponent(arg[2])
        );

        gl.setVerticalGroup(gl.createParallelGroup()
                .addComponent(arg[0])
                .addComponent(arg[1])
                .addComponent(arg[2])
        );

        pack();
    }
     private class ClickListener extends MouseAdapter {

        public void mousePressed(MouseEvent e) {
            prevPt = e.getPoint();
            //System.out.println("pressed " + prevPt);
            var cell = new Cell(prevPt.x, prevPt.y, true);
            add(cell);
            System.out.println(cell);
            // seeCurrentFigure(new Coord((int) (prevPt.getX()),
            //      (int) (prevPt.getY())), figureField);
            //System.out.println("pressed " + prevPt.x + ' ' +prevPt.y);
        }
    }
    private void seeCell(int x, int y, Color color) {
        if (x < 0 || y < 0 || x > Params.width || y > Params.height) return;
        try {
            spawnPoint[x][y].setColor(color);
        } catch (Exception e) {
            // DO smth later
        }
    }
        private void initSpawn(Figure figure) {
        for (var point : figure.points) {
            spawnPoint[point.getX()][point.getY()] = new Cell(point.getX(), point.getY(), true);
            add(spawnPoint[point.getX()][point.getY()]);
        }/*
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                spawnPoint[i][j] = new Cell(i, j, true);
                add(spawnPoint[i][j]);
            }
        }
 */
