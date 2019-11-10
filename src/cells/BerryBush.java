package cells;

import base.Enviro;

public class BerryBush extends Bush {
    double fruits;

    public BerryBush(String type, Enviro enviro, String[] args) {
        super (type, enviro, args);
        generateStats ();
    }

    public void generateStats() {
        this.fruits = this.enviro.getHumidity () * Math.abs (1 + r.nextGaussian () * 0.3) / 5;
    }

    public void tick() {
        if (this.fruits < this.enviro.getHumidity ()) {
            this.fruits += (this.enviro.getHumidity () * Math.abs (1 + r.nextGaussian () * 0.3)) / 30;
        }
    }
}
