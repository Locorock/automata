package cells;

import base.Critter;
import base.Enviro;
import baseCells.Tree;

public class FruitTree extends Tree {
    double fruits;
    static final double growthRateA = 1.5;
    static final double growthRateB = 6;
    public FruitTree(String type, Enviro enviro) {
        super (type, enviro, growthRateA, true);
        foods.addFood (growthRateB, 4, 0, growthRateB * enviro.getHumidity ());
    }

    @Override
    public void tick() {
        if (alive) {
            super.tick ();
        }
    }

    @Override
    public void onPassage(Critter critter) {
        super.onPassage (critter);
    }
}
