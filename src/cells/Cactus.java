package cells;

import base.Appearance;
import base.Enviro;
import baseCells.Tree;
import critters.Critter;

public class Cactus extends Tree {
    static final Appearance code = new Appearance ("10101010", null, null, "00001100", "10101010");
    static final double speedMod = 0.5;
    public Cactus(String type, Enviro enviro) {
        super (type, enviro, code, speedMod, 1, false);
    }

    @Override
    public void onEat(Critter critter, int index) {
        super.onEat (critter, index);
        // TODO
    }
}
