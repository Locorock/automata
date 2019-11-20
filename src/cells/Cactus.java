package cells;

import base.Enviro;
import baseCells.Tree;
import critters.Critter;

public class Cactus extends Tree {
    static final double speedMod = 0.5;
    public Cactus(String type, Enviro enviro) {
        super (type, enviro, speedMod, 1, false);
    }

    @Override
    public void onEat(Critter critter, int index) {
        super.onEat (critter, index);
        // TODO
    }
}
