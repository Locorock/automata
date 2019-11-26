package cells;

import base.Cell;
import base.Enviro;
import baseCells.FreshWater;
import baseCells.Solid;
import critters.Critter;

public class RiverWater extends Cell implements Solid, FreshWater {
    public RiverWater(String type, Enviro enviro) {
        super (type, enviro);
    }

    @Override
    public void onPassage(Critter critter) {
        critter.setSpeed (critter.getBaseSpeed () * 0.4);
    }

    @Override
    public void onDrink(Critter critter) {
        critter.setThirst (critter.getThirst () - 3);
    }

    @Override
    public void tick() {

    }
}
