package cells;

import base.Critter;
import base.Enviro;
import baseCells.Grass;

public class Lichen extends Grass {
    public static double growthRate = 0.05;
    public Lichen(String type, Enviro enviro) {
        super (type, enviro, growthRate);
    }

    public void tick() {
        if (alive) {
            super.tick ();
        }
    }

    @Override
    public void onPassage(Critter critter) {
        critter.setSpeed (critter.getBaseSpeed () * 0.7);
    }
}
