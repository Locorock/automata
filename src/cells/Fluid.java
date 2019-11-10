package cells;

import base.Cell;
import base.Enviro;
import critters.Critter;

public abstract class Fluid extends Cell {
    public Fluid(String type, Enviro enviro) {
        super (type, enviro);
    }

    @Override
    public void tick() {

    }

    public abstract void onPassage(Critter critter);
}
