package cells;

import base.Enviro;

public class FruitTree extends Tree {
    double fruits;

    public FruitTree(String type, Enviro enviro, String[] args) {
        super (type, enviro, args);
    }

    @Override
    public void generateStats() {
        super.generateStats ();
        this.fruits = this.enviro.getHumidity () * Math.abs (1 + r.nextGaussian () * 0.3) / 5;
    }

    @Override
    public void tick() {
        super.tick ();
        if (this.fruits < this.enviro.getHumidity ()) {
            this.fruits += (this.enviro.getHumidity () * Math.abs (1 + r.nextGaussian () * 0.3)) / 30;
        }
    }
}
