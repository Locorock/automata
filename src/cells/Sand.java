package cells;

import base.Appearance;
import base.Cell;
import base.Enviro;
import baseCells.Solid;
import critters.Critter;

public class Sand extends Cell implements Solid {
    static final double speedMod = 0.8;
    static final Appearance code = new Appearance ("", "", "", "", "");
    public Sand(String type, Enviro enviro) {
        super (type, enviro, code, speedMod);
    }

    @Override
    public void tick() {

    }

    @Override
    public void onPassage(Critter critter) {

    }
}
