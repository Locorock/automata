package cells;

import base.Cell;
import base.Enviro;
import baseCells.Solid;
import critters.Critter;

public class RockySoil extends Cell implements Solid {
    static final double speedMod = 1;
    public RockySoil(String type, Enviro enviro) {
        super (type, enviro, speedMod);
    }

    @Override
    public void tick() {

    }

    @Override
    public void onPassage(Critter critter) {

    }
}
