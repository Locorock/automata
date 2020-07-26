package cells;

import base.Critter;
import base.Enviro;
import baseCells.Bush;

public class BerryBush extends Bush {
    static final double growthRateA = 1;
    static final double growthRateB = 6;
    public BerryBush(String type, Enviro enviro) {
        super (type, enviro, growthRateA);
        foods.addFood (growthRateB, 4, 0, growthRateB * enviro.getHumidity ());
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
