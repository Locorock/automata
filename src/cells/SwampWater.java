package cells;

import base.Enviro;
import baseCells.LowWater;

public class SwampWater extends LowWater {
    static final double speedMod = 0.15;

    public SwampWater(String type, Enviro enviro) {
        super (type, enviro, speedMod);
    }
}