package cells;

import base.Enviro;
import critters.Critter;

public class BerryBush extends Bush {
    double fruits;

    public BerryBush(String type, Enviro enviro) {
        super (type, enviro);
        this.foodAmounts.add (this.enviro.getHumidity () * Math.abs (1 + r.nextGaussian () * 0.3) / 5);
        this.foodTypes.add ("Fruit");
    }

    public void tick() {
        super.tick ();
        if (this.foodAmounts.get (1) < this.enviro.getHumidity ()) {
            this.fruits += (this.enviro.getHumidity () * Math.abs (1 + r.nextGaussian () * 0.3)) / 30;
            this.foodAmounts.set (1, this.foodAmounts.get (1) + this.enviro.getHumidity () / 90);
        }
    }

    @Override
    public void onEat(Critter critter, int index) {
        super.onEat (critter, index);
        //TODO
    }
}
