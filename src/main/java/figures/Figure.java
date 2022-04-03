package figures;

import java.util.ArrayList;
import java.util.List;

public enum Figure {
    J0(1, 0, 1, 1, 0, 2, 1, 2),
    J1(0, 0, 0, 1, 1, 1, 2, 1),
    J2(1, 0, 2, 0, 1, 1, 1, 2),
    J3(0, 1, 1, 1, 2, 1, 2, 2),
    L0(0, 2, 0, 1, 0, 0, 1, 2),
    L1(0, 0, 1, 0, 1, 1, 2, 1),
    L2,
    L3,
    Z0,
    Z1,
    Z2,
    Z3,
    doubleL0,
    doubleL1,
    doubleL2,
    doubleL3,
    doubleT0,
    doubleT1,
    doubleT2,
    doubleT3,
    smallL0,
    smallL1,
    smallL2,
    smallL3,
    singleton(0, 0),
    I0,
    I1,
    T0,
    T1,
    T2,
    T3;

    public final List<Coord> points;

    Figure(int... coordinates) {
        points = new ArrayList<Coord>();
        for (int i = 0; i < coordinates.length; i += 2) {
            points.add(new Coord(coordinates[i], coordinates[i + 1]));
        }
    }
}

