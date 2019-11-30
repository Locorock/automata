package cells;

import base.Cell;
import base.Critter;
import base.Enviro;
import base.Foods;
import baseCells.Food;
import baseCells.Solid;

import java.util.ArrayList;

public class Carcass extends Cell implements Food, Solid {
    static final double decayRate = -0.5;
    Foods foods;

    public Carcass(String type, Enviro enviro, Critter c) {
        super (type, enviro);
        this.foods = new Foods (enviro);
        foods.addFood (decayRate, 6, 0, 0);
        //this.foodAmounts.add(c.getBiomass()); TODO
    }

    @Override
    public void tick() {
        foods.grow ();
        //decompose TODO
    }

    public double onEat(Critter critter, int index) {
        return foods.eatFood (index);
        //TODO
    }


    @Override
    public Double getFoodAmount(int index) {
        return foods.getFoodAmount (index);
    }

    @Override
    public ArrayList<Integer> getFoodTypes() {
        return foods.getFoodTypes ();
    }

    @Override
    public void onPassage(Critter critter) {
        critter.setSpeed (critter.getBaseSpeed () * 0.9);
    }

    @Override
    public void init() {
    }
}
