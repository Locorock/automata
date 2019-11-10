package cells;

import base.Enviro;
import critters.Critter;

public abstract class FreshWater extends Fluid {
    public FreshWater(String type, Enviro enviro, String[] args) {
        super (type, enviro);
    }

    public abstract void onDrink(Critter critter);
}
