package baseCells;

import base.Enviro;
import base.Foods;
import critters.Critter;

import java.util.ArrayList;

public abstract class Bush extends Living implements Food {
    protected Foods foods;

    public Bush(String type, Enviro enviro, double growthRate) {
        super (type, enviro, 100);
        foods = new Foods (enviro);
        foods.addFood (growthRate, "Leafage", 0, growthRate * enviro.getHumidity ());
    }

    @Override
    public void tick() {
        super.tick ();
        foods.grow ();
    }

    @Override
    public void onEat(Critter critter, int index) {
        critter.setHunger (critter.getHunger () - foods.eatFood (index));
    }

    @Override
    public void onPassage(Critter critter) {
        critter.setSpeed (critter.getBaseSpeed () * 0.8);
    }

    @Override
    public ArrayList<String> getFoodTypes() {
        return foods.getFoodTypes ();
    }

    @Override
    public Double getFoodAmount(int index) {
        return foods.getFoodAmount (index);
    }

    @Override
    public void init() {
        foods.init ();
    }
}
