package baseCells;

import base.Enviro;
import base.Foods;
import critters.Critter;

import java.util.ArrayList;

public abstract class Grass extends Living implements Food {
    public double growthRate;
    protected Foods foods;

    public Grass(String type, Enviro enviro, double growthRate) {
        super (type, enviro, -1);
        foods = new Foods (enviro);
        foods.addFood (growthRate, 0, 0, growthRate * enviro.getHumidity ());
    }

    @Override
    public void tick() {
        super.tick ();
        foods.grow ();
    }

    @Override
    public void onPassage(Critter critter) {
        critter.setSpeed (critter.getBaseSpeed () * 0.9);
    }

    @Override
    public double onEat(Critter critter, int index) {
        return foods.eatFood (index);
    }
    @Override
    public ArrayList<Integer> getFoodTypes() {
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
