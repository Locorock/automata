package cells;

import base.Enviro;
import baseCells.Bush;
import critters.Critter;

public class BerryBush extends Bush {
    static final double growthRateA = 0.5;
    static final double growthRateB = 1;
    public BerryBush(String type, Enviro enviro) {
        super (type, enviro, growthRateA * enviro.getHumidity ());
        foods.addFood (growthRateB, "Leafage", 0, growthRateB * enviro.getHumidity ());
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
