package cells;

import base.Enviro;
import critters.Critter;

public class Lichen extends Grass {
    public Lichen(String type, Enviro enviro) {
        super (type, enviro, 1);
    }

    @Override
    public void onPassage(Critter critter) {

    }
}
