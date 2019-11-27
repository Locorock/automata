package baseCells;

import critters.Critter;

public interface FreshWater {
    void onDrink(Critter critter);

    double getDrinkAmount();
}
