package cells;

import base.Appearance;
import base.Cell;
import base.Enviro;
import baseCells.Fluid;
import critters.Critter;

public class SaltWater extends Cell implements Fluid {
    static final double speedMod = 0.2;
    static final Appearance code = new Appearance ("", "", "", "", "");
    public SaltWater(String type, Enviro enviro) {
        super (type, enviro, code, speedMod);
    }

    @Override
    public void tick() {

    }

    @Override
    public void onPassage(Critter critter) {

    }
}
