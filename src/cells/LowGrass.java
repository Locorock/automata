package cells;

import base.Enviro;
import baseCells.Grass;
import critters.Critter;

public class LowGrass extends Grass {
    public static double growthRate = 0.10;
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
