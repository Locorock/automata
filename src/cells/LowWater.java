package cells;

import base.Cell;
import base.Enviro;
import critters.Critter;

public class LowWater extends Cell implements FreshWater, Fluid {
    public LowWater(String type, Enviro enviro) {
        super (type, enviro);
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
