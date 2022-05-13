package figures;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public enum Figure implements Serializable {
    J0,
    J1,
    J2,
    J3,
    L0,
    L1,
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
    singleton,
    I0,
    I1,
    T0,
    T1,
    T2,
    T3;

    public final List<Coord> points;

    Figure(int... coordinates) {
        points = new ArrayList<>();
        for (int i = 0; i < coordinates.length; i += 2) {
            points.add(new Coord(coordinates[i], coordinates[i + 1]));
        }
    }
}

