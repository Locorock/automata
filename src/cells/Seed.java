package cells;

import base.Enviro;
import critters.Critter;

public class Seed extends Living implements Solid {
    public Seed(String type, Enviro enviro) {
        super (type, enviro, -1);
    }

    @Override
    public void onPassage(Critter critter) {

    }
}
