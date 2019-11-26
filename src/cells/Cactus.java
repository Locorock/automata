package cells;

import base.Enviro;
import baseCells.Tree;
import critters.Critter;

public class Cactus extends Tree {
    static final double growthRate = 0.5;
    public Cactus(String type, Enviro enviro) {
        super (type, enviro, growthRate, false);
    }

    public void tick() {
        if (alive) {
            super.tick ();
        }
    }

    @Override
    public void onPassage(Critter critter) {
        super.onPassage (critter);
    }
}
