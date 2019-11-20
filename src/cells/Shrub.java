package cells;

import base.Enviro;
import baseCells.Bush;

public class Shrub extends Bush {
    static final double speedMod = 0.7;

    public Shrub(String type, Enviro enviro) {
        super (type, enviro, speedMod);
    }
}

