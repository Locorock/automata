package baseCells;

import base.Enviro;
import critters.Critter;

public abstract class Grass extends Living implements Food {
    double growthRate;

    public Grass(String type, Enviro enviro, double speedMod, double growthRate) {
        super (type, enviro, speedMod, -1);
        this.growthRate = growthRate;
        this.foodTypes.add ("Grass");
        this.foodAmounts.add (growthRate * enviro.getHumidity () / 4);
    }

    @Override
    public void tick() {
        super.tick ();
        this.foodAmounts.set (0, this.foodAmounts.get (0) + enviro.getHumidity () * growthRate / 40);
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
