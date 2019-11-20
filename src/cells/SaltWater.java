package cells;

import base.Cell;
import base.Enviro;
import baseCells.Fluid;
import critters.Critter;

public class SaltWater extends Cell implements Fluid {
    static final double speedMod = 0.2;
    public SaltWater(String type, Enviro enviro) {
        super (type, enviro, speedMod);
    }

    @Override
    public void tick() {

    }

    @Override
    public void onPassage(Critter critter) {

    }
}
