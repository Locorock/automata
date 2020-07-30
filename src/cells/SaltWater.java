package cells;

import base.Cell;
import base.Critter;
import base.Enviro;
import baseCells.FreshWater;
import baseCells.Solid;

public class SaltWater extends Cell implements Solid, FreshWater {
    public SaltWater(String type, Enviro enviro) {
        super (type, enviro);
    }

    @Override
    public void onPassage(Critter critter) {
        critter.setSpeed (critter.getBaseSpeed () * 0.6 + critter.getWebbedFeet ());
    }

    @Override
    public void onDrink(Critter critter) {
        critter.setThirst (critter.getThirst () - 0.8 * critter.getSize ());
    }
}
