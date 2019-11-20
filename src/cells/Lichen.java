package cells;

import base.Enviro;
import baseCells.Grass;
import critters.Critter;

public class Lichen extends Grass {
    static final double speedMod = 1;
    public Lichen(String type, Enviro enviro) {
        super (type, enviro, speedMod, 1);
    }

    @Override
    public void onPassage(Critter critter) {

    }
}
