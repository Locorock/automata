package cells;

import base.Appearance;
import base.Enviro;
import baseCells.Grass;
import critters.Critter;

public class Lichen extends Grass {
    static final Appearance code = new Appearance ("", "", "", "", "");
    static final double speedMod = 1;
    public Lichen(String type, Enviro enviro) {
        super (type, enviro, code, speedMod, 1);
    }

    @Override
    public void onPassage(Critter critter) {

    }
}
