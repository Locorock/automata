package baseCells;

import base.Appearance;
import base.Cell;
import base.Enviro;
import critters.Critter;

public abstract class LowWater extends Cell implements FreshWater, Fluid {
    static final Appearance code = new Appearance ("", "", "", "", "");

    public LowWater(String type, Enviro enviro, Appearance code, double speedMod) {
        super (type, enviro, code, speedMod);
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
