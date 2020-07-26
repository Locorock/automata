package cells;

import base.Critter;
import base.Enviro;
import baseCells.Grass;

public class HighGrass extends Grass {
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
        critter.setSpeed (critter.getBaseSpeed () * 0.9);
    }
}
