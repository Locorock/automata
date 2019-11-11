package cells;

import base.Enviro;
import critters.Critter;

public abstract class Grass extends Living implements Food {
    double growthRate;

    public Grass(String type, Enviro enviro, double growthRate) {
        super (type, enviro, -1);
        this.growthRate = growthRate;
        this.foodTypes.add ("Grass");
        this.foodAmounts.add (growthRate * r.nextGaussian ());
    }

    @Override
    public void tick() {
        super.tick ();
    }

    @Override
    public void onEat(Critter critter, int index) {
        //TODO
    }
}
