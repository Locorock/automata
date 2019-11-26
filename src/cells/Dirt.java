package cells;

import base.Cell;
import base.Enviro;
import baseCells.Solid;
import critters.Critter;

public class Dirt extends Cell implements Solid {
    public Dirt(String type, Enviro enviro) {
        super (type, enviro);
    }

    @Override
    public void tick() {
    }

    @Override
    public void onPassage(Critter critter) {
        critter.setSpeed (critter.getBaseSpeed () * 1);
    }
}
