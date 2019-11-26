package cells;

import base.Enviro;
import baseCells.Food;
import baseCells.Grass;
import critters.Critter;

public class HighGrass extends Grass implements Food {
    static final double growthRate = 2;
    public HighGrass(String type, Enviro enviro) {
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
        critter.setSpeed (critter.getBaseSpeed () * 0.8);
    }
}
