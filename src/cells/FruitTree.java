package cells;

import base.Enviro;
import critters.Critter;

public class FruitTree extends Tree {
    double fruits;

    public FruitTree(String type, Enviro enviro) {
        super (type, enviro, 2, true);
        this.fruits = this.enviro.getHumidity () * Math.abs (1 + r.nextGaussian () * 0.3) / 5;
    }

    @Override
    public void tick() {
        super.tick ();
        if (this.fruits < this.enviro.getHumidity ()) {
            this.fruits += (this.enviro.getHumidity () / 90);
        }
    }

    @Override
    public void onEat(Critter critter, int index) {
        super.onEat (critter, index);
        //TODO
    }
}
