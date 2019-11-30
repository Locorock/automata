package cells;

import base.Critter;
import base.Enviro;
import baseCells.Food;
import baseCells.Grass;

public class HighGrass extends Grass implements Food {
    static final double growthRate = 2;
    public HighGrass(String type, Enviro enviro) {
        super (type, enviro, 1.5);
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
