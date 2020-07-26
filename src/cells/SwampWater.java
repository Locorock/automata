package cells;

import base.Critter;
import base.Enviro;
import baseCells.FreshWater;
import baseCells.LowWater;

public class SwampWater extends LowWater implements FreshWater {
    public SwampWater(String type, Enviro enviro) {
        super (type, enviro);
    }

    @Override
    public void onDrink(Critter critter) {
        super.tick ();
        super.onDrink (critter);
        critter.setAge (critter.getAge () + 0.05 * critter.getMateRate ());
    }

    @Override
    public void onPassage(Critter critter) {
        super.onPassage (critter);
    }
}