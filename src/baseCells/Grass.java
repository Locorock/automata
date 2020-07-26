package baseCells;

import base.Critter;
import base.Enviro;
import base.Foods;

public abstract class Grass extends Living {
    public double growthRate;

    public Grass(String type, Enviro enviro, double growthRate) {
        super (type, enviro, -1);
        foods = new Foods (enviro);
        foods.addFood (growthRate, 0, 0, growthRate * enviro.getHumidity ());
    }

    @Override
    public void tick() {
        super.tick ();
    }

    @Override
    public void onPassage(Critter critter) {
        critter.setSpeed (critter.getBaseSpeed () * 1);
    }
}
