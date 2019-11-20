package cells;

import base.Cell;
import base.Enviro;
import baseCells.Solid;
import critters.Critter;

public class Snow extends Cell implements Solid {
    static final double speedMod = 0.7;
    public Snow(String type, Enviro enviro) {
        super (type, enviro, speedMod);
    }

    @Override
    public void tick() {

    }

    @Override
    public void onPassage(Critter critter) {

    }
}
