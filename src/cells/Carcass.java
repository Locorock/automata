package cells;

import base.Cell;
import base.Enviro;
import critters.Critter;

public class Carcass extends Cell implements Food {
    public Carcass(String type, Enviro enviro, Critter c) {
        super (type, enviro);
        this.foodTypes.add ("Meat");
        //this.foodAmounts.add(c.getBiomass()); TODO
    }

    @Override
    public void tick() {
        //decompose TODO
    }

    public void onEat(Critter critter, int index) {
        //TODO
    }
}
