package cells;

import base.Appearance;
import base.Cell;
import base.Enviro;
import baseCells.Food;
import critters.Critter;

public class Carcass extends Cell implements Food {
    static final Appearance code = new Appearance ("01101100", null, "11111111", "11111111", "00000011");
    static final double speedMod = 1;
    public Carcass(String type, Enviro enviro, Critter c) {
        super (type, enviro, code, speedMod);
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
