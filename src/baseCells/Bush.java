package baseCells;

import base.Critter;
import base.Enviro;
import base.Foods;

public abstract class Bush extends Living {
    public Bush(String type, Enviro enviro, double growthRate) {
        super (type, enviro, 100);
        foods = new Foods (enviro);
        foods.addFood (growthRate, 2, 0, growthRate * enviro.getHumidity ());
    }

    @Override
    public void tick() {
        super.tick ();
    }

    @Override
    public void onPassage(Critter critter) {
        critter.setSpeed (critter.getBaseSpeed () * 0.8);
    }
}
