package baseCells;

import base.Appearance;
import base.Enviro;
import critters.Critter;

public abstract class Bush extends Living implements Food {
    public Bush(String type, Enviro enviro, Appearance code, double speedMod) {
        super (type, enviro, code, speedMod, 100);
        this.foodTypes.add ("Leafage");
        this.foodAmounts.add (Math.abs (r.nextGaussian () * 2));
    }

    @Override
    public void tick() {
        super.tick ();
        this.foodAmounts.set (0, this.foodAmounts.get (0) + Math.abs (r.nextGaussian () * 2));
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
