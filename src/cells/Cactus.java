package cells;

import base.Enviro;
import critters.Critter;

public class Cactus extends Tree {
    public Cactus(String type, Enviro enviro) {
        super (type, enviro, 1, false);
    }

    @Override
    public void onEat(Critter critter, int index) {
        super.onEat (critter, index);
        // TODO
    }
}
