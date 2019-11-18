package cells;

import base.Appearance;
import base.Enviro;
import baseCells.Living;
import baseCells.Solid;
import critters.Critter;

public class Seed extends Living implements Solid {
    static final double speedMod = 1;
    static final Appearance code = new Appearance ("", "", "", "", "");
    public Seed(String type, Enviro enviro) {
        super (type, enviro, code, speedMod, -1);
    }

    @Override
    public void onPassage(Critter critter) {

    }
}
