package cells;

import base.Enviro;
import base.Foods;
import baseCells.Food;
import baseCells.LowWater;
import critters.Critter;

import java.util.ArrayList;

public class Ambrosia extends LowWater implements Food {
    private Foods foods;

    public Ambrosia(String type, Enviro enviro) {
        super (type, enviro);
        foods = new Foods (enviro);
        foods.addFood (2, "Ambrosia", 0, 100);
    }

    @Override
    public void tick() {
        super.tick ();
        foods.grow ();
    }

    @Override
    public void onDrink(Critter critter) {
        consume (critter);
    }

    @Override
    public double getDrinkAmount() {
        return foods.getFoodAmount (0);
    }

    @Override
    public void onEat(Critter critter, int index) {
        consume (critter);
    }

    public void consume(Critter critter) {
        critter.setHunger (critter.getHunger () - foods.eatFood (0));
        critter.setThirst (-5);
        critter.setAge (critter.getAge () - 5);
    }

    @Override
    public void init() {
        foods.init ();
    }

    @Override
    public ArrayList<String> getFoodTypes() {
        return foods.getFoodTypes ();
    }

    @Override
    public Double getFoodAmount(int index) {
        return foods.getFoodAmount (index);
    }
}
