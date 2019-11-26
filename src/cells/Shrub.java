package cells;

import base.Enviro;
import baseCells.Bush;
import critters.Critter;

public class Shrub extends Bush {
    static final double growthRate = 0.75;
    public Shrub(String type, Enviro enviro) {
        super (type, enviro, 1);
    }

    @Override
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

