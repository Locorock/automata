package baseCells;

import base.Cell;
import base.Critter;
import base.Enviro;

public abstract class LowWater extends Cell implements FreshWater, Solid {
    public LowWater(String type, Enviro enviro) {
        super (type, enviro);
    }

    @Override
    public void onDrink(Critter critter) {
        critter.setThirst (critter.getThirst () - 2);
    }

    @Override
    public void tick() {
    }

    @Override
    public void onPassage(Critter critter) {
        critter.setSpeed (critter.getBaseSpeed () * 0.5);
    }
}
