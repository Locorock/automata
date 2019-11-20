package cells;

import base.Enviro;
import baseCells.Food;
import baseCells.Grass;
import critters.Critter;

public class HighGrass extends Grass implements Food {
    static final double speedMod = 0.8;
    public HighGrass(String type, Enviro enviro) {
        super (type, enviro, speedMod, 4);
    }

    @Override
    public void onPassage(Critter critter) {

    }
}
