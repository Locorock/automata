package cells;

import base.Cell;
import base.Enviro;
import baseCells.Solid;
import critters.Critter;

public class Ice extends Cell implements Solid {
    static final double speedMod = 0.8;
    public Ice(String type, Enviro enviro) {
        super (type, enviro, speedMod);
    }

    @Override
    public void tick() {
    }

    @Override
    public void onPassage(Critter critter) {

    }


}
