package baseCells;

import base.Cell;
import base.Enviro;
import critters.Critter;

public abstract class LowWater extends Cell implements FreshWater, Fluid {
    public LowWater(String type, Enviro enviro, double speedMod) {
        super (type, enviro, speedMod);
    }

    @Override
    public void tick() {

    }

    @Override
    public void onDrink(Critter critter) {

    }

    @Override
    public void onPassage(Critter critter) {

    }
}
