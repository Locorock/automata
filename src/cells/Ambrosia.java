package cells;

import base.Critter;
import base.Enviro;
import base.Foods;
import baseCells.Food;
import baseCells.FreshWater;
import baseCells.LowWater;

import java.util.ArrayList;

public class Ambrosia extends LowWater implements Food, FreshWater {
    private Foods foods;

    public Ambrosia(String type, Enviro enviro) {
        super (type, enviro);
        foods = new Foods (enviro);
        foods.addFood (2, 8, 0, 100);
    }

    @Override
    public void tick() {
        super.tick ();
        foods.grow ();
    }

    @Override
    public void onDrink(Critter critter) {
        critter.setThirst (-5);
    }

    @Override
    public double onEat(Critter critter, int index) {
        critter.setAge (critter.getAge () - 5);
        return foods.eatFood (0);
    }

    @Override
    public void init() {
        foods.init ();
    }

    @Override
    public ArrayList<Integer> getFoodTypes() {
        return foods.getFoodTypes ();
    }

    @Override
    public Double getFoodAmount(int index) {
        return foods.getFoodAmount (index);
    }
}
