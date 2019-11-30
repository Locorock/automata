package cells;

import base.Cell;
import base.Critter;
import base.Enviro;
import baseCells.Solid;

public class Ice extends Cell implements Solid {
    public Ice(String type, Enviro enviro) {
        super (type, enviro);
    }

    @Override
    public void tick() {
    }

    @Override
    public void onPassage(Critter critter) {
        critter.setSpeed (critter.getBaseSpeed () * 0.7);
    }


}
