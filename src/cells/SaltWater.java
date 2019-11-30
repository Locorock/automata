package cells;

import base.Cell;
import base.Critter;
import base.Enviro;
import baseCells.Solid;

public class SaltWater extends Cell implements Solid {
    public SaltWater(String type, Enviro enviro) {
        super (type, enviro);
    }

    @Override
    public void tick() {
    }

    @Override
    public void onPassage(Critter critter) {
        critter.setSpeed (critter.getBaseSpeed () * 0.5);
    }
}
