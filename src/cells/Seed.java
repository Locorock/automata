package cells;

import base.Enviro;
import baseCells.Living;
import baseCells.Solid;
import critters.Critter;

public class Seed extends Living implements Solid {
    static final double speedMod = 1;
    public Seed(String type, Enviro enviro) {
        super (type, enviro, speedMod, -1);
    }

    @Override
    public void onPassage(Critter critter) {

    }
}
