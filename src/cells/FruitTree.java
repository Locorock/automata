package cells;

import base.Enviro;
import baseCells.Tree;
import critters.Critter;

public class FruitTree extends Tree {
    double fruits;
    static final double speedMod = 0.9;
    public FruitTree(String type, Enviro enviro) {
        super (type, enviro, speedMod, 2, true);
        this.fruits = this.enviro.getHumidity () / 5;
    }

    @Override
    public void tick() {
        super.tick ();
        if (this.fruits < this.enviro.getHumidity ()) {
            this.fruits += (this.enviro.getHumidity () / 90);
        }
    }

    @Override
    public void onEat(Critter critter, int index) {
        super.onEat (critter, index);
        //TODO
    }
}
