package baseCells;

import base.Appearance;
import base.Enviro;
import critters.Critter;

public abstract class Grass extends Living implements Food {
    double growthRate;

    public Grass(String type, Enviro enviro, Appearance code, double speedMod, double growthRate) {
        super (type, enviro, code, speedMod, -1);
        this.growthRate = growthRate;
        this.foodTypes.add ("Grass");
        this.foodAmounts.add (growthRate * r.nextGaussian ());
    }

    @Override
    public void tick() {
        super.tick ();
    }

    @Override
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
