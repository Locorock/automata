package cells;

import base.Cell;
import base.Enviro;
import baseCells.Fluid;
import baseCells.FreshWater;
import critters.Critter;

public class RiverWater extends Cell implements Fluid, FreshWater {
    static final double speedMod = 0.2;
    public RiverWater(String type, Enviro enviro) {
        super (type, enviro, speedMod);
    }

    @Override
    public void onPassage(Critter critter) {

    }

    @Override
    public void onDrink(Critter critter) {

    }

    @Override
    public void tick() {

    }
}
