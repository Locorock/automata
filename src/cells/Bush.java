package cells;

import base.Enviro;
import critters.Critter;

public class Bush extends Living implements Food {
    public Bush(String type, Enviro enviro) {
        super (type, enviro, 100);
        this.foodTypes.add ("Leafage");
        this.foodAmounts.add (Math.abs (r.nextGaussian () * 2));
    }

    @Override
    public void tick() {
        super.tick ();
        this.foodAmounts.set (0, this.foodAmounts.get (0) + Math.abs (r.nextGaussian () * 2));
    }

    @Override
    public void onEat(Critter critter, int index) {
        //TODO
    }

    @Override
    public void onPassage(Critter critter) {

    }
}
