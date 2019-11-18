package cells;

import base.Appearance;
import base.Enviro;
import baseCells.LowWater;

public class SwampWater extends LowWater {
    static final double speedMod = 0.15;
    static final Appearance code = new Appearance ("", "", "", "", "");

    public SwampWater(String type, Enviro enviro) {
        super (type, enviro, code, speedMod);
    }
}