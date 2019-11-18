package cells;

import base.Appearance;
import base.Cell;
import base.Enviro;
import baseCells.Fluid;
import critters.Critter;

public class Lava extends Cell implements Fluid {
    static final Appearance code = new Appearance ("", "", "", "", "");
    static final double speedMod = 0.1;
    public Lava(String type, Enviro enviro) {
        super (type, enviro, code, speedMod);
    }

    @Override
    public void tick() {
    }

    @Override
    public void onPassage(Critter critter) {
    }
}
