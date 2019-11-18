package cells;

import base.Appearance;
import base.Enviro;
import baseCells.Bush;

public class Shrub extends Bush {
    static final double speedMod = 0.7;
    static final Appearance code = new Appearance ("", "", "", "", "");

    public Shrub(String type, Enviro enviro) {
        super (type, enviro, code, speedMod);
    }
}

