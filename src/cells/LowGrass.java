package cells;

import base.Enviro;
import baseCells.Grass;
import critters.Critter;

public class LowGrass extends Grass {
    static final double speedMod = 1;
    public LowGrass(String type, Enviro enviro) {
        super (type, enviro, speedMod, 2);
    }

    @Override
    public void onPassage(Critter critter) {

    }
}
