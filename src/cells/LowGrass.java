package cells;

import base.Critter;
import base.Enviro;
import baseCells.Grass;

public class LowGrass extends Grass {
    public static double growthRate = 0.35;
    public LowGrass(String type, Enviro enviro) {
        super (type, enviro, growthRate);
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
