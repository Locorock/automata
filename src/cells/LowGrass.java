package cells;

import base.Enviro;
import critters.Critter;

public class LowGrass extends Grass {
    public LowGrass(String type, Enviro enviro) {
        super (type, enviro, 2);
    }

    @Override
    public void onPassage(Critter critter) {

    }
}
