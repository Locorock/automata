package cells;

import base.Critter;
import base.Enviro;
import base.Foods;
import baseCells.FreshWater;
import baseCells.LowWater;

public class Ambrosia extends LowWater implements FreshWater {
    private final Foods foods;

    public Ambrosia(String type, Enviro enviro) {
        super (type, enviro);
        foods = new Foods (enviro);
        foods.addFood (2, 8, 0, 100);
    }

    @Override
    public void onDrink(Critter critter) {
        critter.setThirst (-5);
    }

    @Override
    public void onEat(Critter critter) {
        critter.setAge (critter.getAge () - 5);
    }
}
