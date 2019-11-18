package cells;

import base.Appearance;
import base.Cell;
import base.Enviro;
import baseCells.Solid;
import critters.Critter;

public class Ice extends Cell implements Solid {
    static final Appearance code = new Appearance ("", "", "", "", "");
    static final double speedMod = 0.8;
    public Ice(String type, Enviro enviro) {
        super (type, enviro, code, speedMod);
    }

    @Override
    public void tick() {
    }

    @Override
    public void onPassage(Critter critter) {

    }


}
