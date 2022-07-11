package figures;

import java.util.Random;
import java.util.Vector;

public class FigureGenerator {
    private static class InstanceHolder {
        private static final FigureGenerator INSTANCE = new FigureGenerator();
    }

    public static FigureGenerator getInstance() {
        return InstanceHolder.INSTANCE;
    }

    // Вектор номеров уже сгенерированных фигур
    private final Vector<Integer> generated = new Vector<>();
    int total = 1;

    /**
     * Генерация фигуры.
     */
    public Figure generateFigure() {
        Random rnd = new Random();
        int rand = rnd.nextInt(19);
        synchronized (generated) {
            if (generated.contains(rand) && total < 19) {
                do {
                    rand = rnd.nextInt(19);
                } while (generated.contains(rand));
            }
            generated.add(rand);
        }
        total++;
        return getFigure(rand);
    }

    /**
     * Получение фигуры по номеру.
     */
    private Figure getFigure(int rand) {
        return switch (rand) {
            case 0 -> Figure.J0;
            case 1 -> Figure.J1;
            case 2 -> Figure.J2;
            case 3 -> Figure.J3;
            case 4 -> Figure.L0;
            case 5 -> Figure.L1;
            case 6 -> Figure.L2;
            case 7 -> Figure.L3;
            case 8 -> Figure.Z0;
            case 9 -> Figure.Z1;
            case 10 -> Figure.Z2;
            case 11 -> Figure.Z3;
            case 12 -> Figure.smallL0;
            case 13 -> Figure.smallL1;
            case 14 -> Figure.smallL2;
            case 15 -> Figure.smallL3;
            case 16 -> Figure.I0;
            case 17 -> Figure.I1;
            case 18 -> Figure.singleton;
            case 19 -> Figure.doubleL1;
            case 20 -> Figure.doubleL2;
            case 21 -> Figure.doubleL3;
            case 22 -> Figure.doubleL0;
            case 23 -> Figure.T1;
            case 24 -> Figure.T2;
            case 25 -> Figure.T3;
            case 26 -> Figure.doubleT0;
            case 27 -> Figure.doubleT1;
            case 28 -> Figure.doubleT2;
            case 29 -> Figure.doubleT3;
            default -> Figure.T0;
        };
    }
}
