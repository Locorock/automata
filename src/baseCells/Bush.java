package baseCells;

import base.Enviro;
import critters.Critter;

public abstract class Bush extends Living implements Food {
    public Bush(String type, Enviro enviro, double speedMod) {
        super (type, enviro, speedMod, 100);
        this.foodTypes.add ("Leafage");
        this.foodAmounts.add ((2 * enviro.getHumidity ()) / 4);
    }

    @Override
    public void tick() {
        super.tick ();
        this.foodAmounts.set (0, this.foodAmounts.get (0) + (2 * enviro.getHumidity ()) / 40);
    }

    @Override
    public void onEat(Critter critter, int index) {
        //TODO
    }

    @Override
    public void onPassage(Critter critter) {

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
