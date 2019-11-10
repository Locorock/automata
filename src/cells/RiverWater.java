package cells;

import base.Cell;
import base.Enviro;
import critters.Critter;

public class RiverWater extends Cell implements Fluid, FreshWater {
    public RiverWater(String type, Enviro enviro) {
        super (type, enviro);
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
