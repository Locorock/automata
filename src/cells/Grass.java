package cells;

import base.Enviro;
import critters.Critter;

public abstract class Grass extends Living implements Food {
    double growthRate;

    public Grass(String type, Enviro enviro, double growthRate) {
        super (type, enviro, -1);
        this.growthRate = growthRate;
        this.foodTypes.add ("Grass");
        this.foodAmounts.add (1, growthRate * r.nextGaussian ());
    }

    @Override
    public void tick() {
        super.tick ();
        this.foodAmounts.set (1, this.foodAmounts.get (1) + r.nextGaussian () * (growthRate / 2));
    }

    @Override
    public void onEat(Critter critter, int index) {
        //TODO
    }
}
