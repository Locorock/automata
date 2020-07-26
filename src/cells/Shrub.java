package cells;

import base.Critter;
import base.Enviro;
import baseCells.Bush;

public class Shrub extends Bush {
    static final double growthRate = 1;
    public Shrub(String type, Enviro enviro) {
        super (type, enviro, 1);
    }

    @Override
    public void onPassage(Critter critter) {
        super.onPassage (critter);
    }
}

