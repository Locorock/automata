package cells;

import base.Enviro;
import baseCells.Bush;
import critters.Critter;

public class BerryBush extends Bush {
    static final double speedMod = 0.7;
    public BerryBush(String type, Enviro enviro) {
        super (type, enviro, speedMod);
        this.foodAmounts.add (this.enviro.getHumidity () / 5);
        this.foodTypes.add ("Fruit");
    }

    public void tick() {
        super.tick ();
        if (this.foodAmounts.get (1) < this.enviro.getHumidity ()) {
            this.foodAmounts.set (1, this.foodAmounts.get (1) + this.enviro.getHumidity () / 90);
        }
    }

    @Override
    public void onEat(Critter critter, int index) {
        super.onEat (critter, index);
        //TODO
    }
}
