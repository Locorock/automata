package cells;

import base.Enviro;
import baseCells.FreshWater;
import baseCells.LowWater;
import critters.Critter;

public class SwampWater extends LowWater implements FreshWater {
    public SwampWater(String type, Enviro enviro) {
        super (type, enviro);
    }

    @Override
    public void onDrink(Critter critter) {
        super.onDrink (critter);
        critter.setAge (critter.getAge () + 0.1);
    }

    @Override
    public void onPassage(Critter critter) {
        super.onPassage (critter);
    }

    @Override
    public double getDrinkAmount() {
        return 50;
    }
}