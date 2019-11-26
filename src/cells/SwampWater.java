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
    public void tick() {
    }

    @Override
    public void onDrink(Critter critter) {
        super.onDrink (critter);
    }

    @Override
    public void onPassage(Critter critter) {
        super.onPassage (critter);
    }
}