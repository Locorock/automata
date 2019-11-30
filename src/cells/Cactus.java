package cells;

import base.Critter;
import base.Enviro;
import baseCells.Tree;

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
