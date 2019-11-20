package cells;

import base.Cell;
import base.Enviro;
import baseCells.Food;
import critters.Critter;

public class Carcass extends Cell implements Food {
    static final double speedMod = 1;
    public Carcass(String type, Enviro enviro, Critter c) {
        super (type, enviro, speedMod);
        this.foodTypes.add ("Meat");
        //this.foodAmounts.add(c.getBiomass()); TODO
    }

    @Override
    public void tick() {
        //decompose TODO
    }

    public void onEat(Critter critter, int index) {
        //TODO
    }


    @Override
    public String getFoodType(int index) {
        return foodTypes.get (index);
    }

    @Override
    public Double getFoodAmount(int index) {
        return foodAmounts.get (index);
    }

    @Override
    public void setFoodAmount(int index, double amount) {
        foodAmounts.set (index, amount);
    }
}
