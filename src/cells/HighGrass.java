package cells;

import base.Enviro;
import critters.Critter;

public class HighGrass extends Grass implements Food {
    public HighGrass(String type, Enviro enviro) {
        super (type, enviro, 4);
    }

    @Override
    public void onPassage(Critter critter) {

    }
}
