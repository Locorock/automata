package cells;

import base.Appearance;
import base.Cell;
import base.Enviro;
import baseCells.Solid;
import critters.Critter;

public class Dirt extends Cell implements Solid {
    static final Appearance code = new Appearance ("", "", "", "", "");
    static final double speedMod = 1;
    public Dirt(String type, Enviro enviro) {
        super (type, enviro, code, speedMod);
    }

    @Override
    public void tick() {
    }

    @Override
    public void onPassage(Critter critter) {

    }
}
